package pl.allegro.promo.geecon2015.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.allegro.promo.geecon2015.domain.transaction.TransactionRepository;
import pl.allegro.promo.geecon2015.domain.transaction.UserTransactions;

import java.util.Optional;
import java.util.UUID;

@Component
public class RestTransactionRepository implements TransactionRepository {

  private final String baseUri;

  private final RestTemplate restTemplate;

  @Autowired
  public RestTransactionRepository(@Value("${transaction.uri}") String uri, RestTemplate restTemplate) {
    this.baseUri = uri;
    this.restTemplate = restTemplate;
  }

  @Override
  public Optional<UserTransactions> transactionsOf(UUID userId) {
    try {
      UserTransactions txns = restTemplate.getForEntity(baseUri + "/transactions/" + userId.toString(), UserTransactions.class).getBody();
      return Optional.of(txns);
    } catch (RestClientException e) {
      return Optional.empty();
    }
  }
}
