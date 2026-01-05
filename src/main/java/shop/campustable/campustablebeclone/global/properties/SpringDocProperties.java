package shop.campustable.campustablebeclone.global.properties;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {

  List<Server> servers;

  @Getter
  public static class Server {
    String url;
    String description;
  }
}
