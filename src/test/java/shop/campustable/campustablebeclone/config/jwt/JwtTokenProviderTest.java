package shop.campustable.campustablebeclone.config.jwt;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.campustable.campustablebeclone.domain.auth.provider.JwtTokenProvider;

public class JwtTokenProviderTest {

  private static final String TEST_SECRET = "testSecretKeyForCampusTableProject1234567890";

  private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(TEST_SECRET);

  @Test
  @DisplayName("토큰 정상 생성 후 Id 꺼내기")
  void tokenCreationAndExtractionTest(){

    //given
    Long studentId = 22011052L;
    String role = "USER";

    //when
    String token = jwtTokenProvider.createToken(studentId, role);

    //then
    Long extractedId = jwtTokenProvider.getStudentId(token);
    assertThat(extractedId).isEqualTo(studentId);
    assertThat(jwtTokenProvider.validateToken(token)).isTrue();
  }

}
