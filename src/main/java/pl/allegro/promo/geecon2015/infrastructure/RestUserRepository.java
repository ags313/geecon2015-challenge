package pl.allegro.promo.geecon2015.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.allegro.promo.geecon2015.domain.user.User;
import pl.allegro.promo.geecon2015.domain.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class RestUserRepository implements UserRepository {

  private final String baseUri;

  private final RestTemplate restTemplate;

  @Autowired
  public RestUserRepository(@Value("${user.uri}") String uri, RestTemplate restTemplate) {
    this.baseUri = uri;
    this.restTemplate = restTemplate;
  }

  @Override
  public Optional<User> detailsOf(UUID userId) {
    try {
      User body = restTemplate.getForEntity(baseUri + "/users/" + userId.toString(), User.class).getBody();
      return Optional.of(body);
    } catch (RestClientException e) {
      return Optional.empty();
    }
  }
}
