package shop.campustable.campustablebeclone.global.properties;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {

  List<Server> servers;

  @Getter
  @AllArgsConstructor
  public static class Server {

    String url;
    String description;
  }
}
