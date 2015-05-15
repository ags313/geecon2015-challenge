package pl.allegro.promo.geecon2015.domain.transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    
    Optional<UserTransactions> transactionsOf(UUID userId);
    
}
