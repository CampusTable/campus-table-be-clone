package shop.campustable.campustablebeclone.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import shop.campustable.campustablebeclone.domain.auth.dto.SejongMemberInfo;

@Service
@Slf4j
@RequiredArgsConstructor
public class SejongPortalLoginService {

  public SejongMemberInfo getMemberAuthInfos(String sejongPortalId, String sejongPortalPw) {
    // 실제 포털 인증
    try {
      // OkHttpClient 생성
      OkHttpClient client = buildClient();

      // 포털 로그인 요청
      doPortalLogin(client, sejongPortalId, sejongPortalPw);

      // SSO 리다이렉트 -> 고전독서인증 사이트
      String ssoUrl = "http://classic.sejong.ac.kr/_custom/sejong/sso/sso-return.jsp?returnUrl=https://classic.sejong.ac.kr/classic/index.do";
      Request ssoReq = new Request.Builder().url(ssoUrl).get().build();
      try (Response ssoResp = client.newCall(ssoReq).execute()) {
        if (!ssoResp.isSuccessful()) {
          throw new RuntimeException("Connection Error (SSO)");
        }
      }

      // 고전독서인증현황 페이지 GET
      String html = fetchReadingStatusHtml(client);

      // JSoup 파싱 -> MemberInfo
      return parseHTMLAndGetMemberInfo(html);

    } catch (IOException e) {
      log.error("세종포털 인증 중 IOException: {}", e.getMessage());
      throw new RuntimeException("세종대 포털 연결 실패", e);
    }
  }

  private void doPortalLogin(OkHttpClient client, String studentId, String password) throws IOException {
    String loginUrl = "https://portal.sejong.ac.kr/jsp/login/login_action.jsp";

    RequestBody formBody = new FormBody.Builder()
        .add("mainLogin", "N")
        .add("rtUrl", "library.sejong.ac.kr")
        .add("id", studentId)
        .add("password", password)
        .build();

    Request request = new Request.Builder()
        .url(loginUrl)
        .post(formBody)
        .header("Host", "portal.sejong.ac.kr")
        .header("Referer", "https://portal.sejong.ac.kr")
        .header("Cookie", "chknos=false")
        .build();

    try (Response response = executeWithRetry(client, request)) {
      // 로그인 결과 확인 등 필요 시 로직 추가
    }
  }

  private String fetchReadingStatusHtml(OkHttpClient client) throws IOException {
    String finalUrl = "https://classic.sejong.ac.kr/classic/reading/status.do";
    Request finalReq = new Request.Builder().url(finalUrl).get().build();

    try (Response finalResp = client.newCall(finalReq).execute()) {
      if (finalResp.body() == null || finalResp.code() != 200) {
        throw new RuntimeException("인증 데이터 가져오기 실패");
      }
      return finalResp.body().string();
    }
  }

  private Response executeWithRetry(OkHttpClient client, Request request) throws IOException {
    int tryCount = 0;
    while (tryCount < 3) {
      try {
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
          return response;
        }
        response.close();
        tryCount++;
        log.warn("[PortalLogin] Timeout 발생 -> 재시도... ({}회)", tryCount);
      } catch (SocketTimeoutException e) {
        tryCount++;
        log.warn("[PortalLogin] Timeout 발생 -> 재시도... ({}회)", tryCount);
      }
    }
    throw new RuntimeException("세종대 API 재시도 횟수 초과");
  }

  private SejongMemberInfo parseHTMLAndGetMemberInfo(String html) {
    Document doc = Jsoup.parse(html);
    // "사용자 정보" 테이블 tr 추출
    String selector = ".b-con-box:has(h4.b-h4-tit01:contains(사용자 정보)) table.b-board-table tbody tr";
    List<String> rowValues = new ArrayList<>();

    doc.select(selector).forEach(tr -> {
      String value = tr.select("td").text().trim();
      rowValues.add(value);
    });

    // 파싱 실패 시 예외 처리 등을 추가할 수 있습니다.
    if (rowValues.isEmpty()) {
      throw new RuntimeException("로그인 실패 또는 정보 파싱 실패: 정보를 찾을 수 없습니다.");
    }

    return SejongMemberInfo.builder()
        .major(getValueFromList(rowValues, 0))
        .studentId(getValueFromList(rowValues, 1))
        .name(getValueFromList(rowValues, 2))
        .grade(getValueFromList(rowValues, 3))
        .status(getValueFromList(rowValues, 4))
        .completedSemester(getValueFromList(rowValues, 5))
        .build();
  }

  private String getValueFromList(List<String> list, int index) {
    return list.size() > index ? list.get(index) : null;
  }

  private OkHttpClient buildClient() {
    try {
      CookieManager cookieManager = new CookieManager();
      cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

      return new OkHttpClient.Builder()
          .cookieJar(new JavaNetCookieJar(cookieManager))
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private X509TrustManager trustAllManager() {
    return new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] chain, String authType) {}
      public void checkServerTrusted(X509Certificate[] chain, String authType) {}
      public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    };
  }
}
