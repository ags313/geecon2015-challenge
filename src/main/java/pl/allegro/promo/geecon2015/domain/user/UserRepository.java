package pl.allegro.promo.geecon2015.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    
    Optional<User> detailsOf(UUID userId);
    
}
