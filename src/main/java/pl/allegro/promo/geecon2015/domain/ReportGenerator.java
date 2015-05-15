package pl.allegro.promo.geecon2015.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.allegro.promo.geecon2015.domain.stats.FinancialStatisticsRepository;
import pl.allegro.promo.geecon2015.domain.stats.FinancialStats;
import pl.allegro.promo.geecon2015.domain.transaction.TransactionRepository;
import pl.allegro.promo.geecon2015.domain.transaction.UserTransaction;
import pl.allegro.promo.geecon2015.domain.transaction.UserTransactions;
import pl.allegro.promo.geecon2015.domain.user.User;
import pl.allegro.promo.geecon2015.domain.user.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Spliterator;
import java.util.UUID;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

@Component
public class ReportGenerator {

  private final FinancialStatisticsRepository financialStatisticsRepository;

  private final UserRepository userRepository;

  private final TransactionRepository transactionRepository;

  @Autowired
  public ReportGenerator(FinancialStatisticsRepository financialStatisticsRepository,
                         UserRepository userRepository,
                         TransactionRepository transactionRepository) {
    this.financialStatisticsRepository = financialStatisticsRepository;
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
  }

  public Report generate(ReportRequest request) {
    Report report = new Report();
    FinancialStats uuids = financialStatisticsRepository.listUsersWithMinimalIncome(request.getMinimalIncome(),
                                                                                    request.getUsersToCheck());
    stream(spliteratorUnknownSize(uuids.iterator(), Spliterator.ORDERED), false).forEach(uuid -> {
      report.add(user(uuid));
    });
    return report;
  }

  private ReportedUser user(UUID uuid) {
    Optional<User> maybeUser = userRepository.detailsOf(uuid);
    String userName = maybeUser.map(User::getName).orElse("<failed>");

    BigDecimal sum = transactionsFor(uuid);
    return new ReportedUser(uuid, userName, sum);
  }

  private BigDecimal transactionsFor(UUID uuid) {
    Optional<UserTransactions> maybeTxns = transactionRepository.transactionsOf(uuid);
    Optional<BigDecimal> bigDecimal = maybeTxns.map(txns -> txns.getTransactions().stream().map(UserTransaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

    return bigDecimal.orElse(null);
  }
}
