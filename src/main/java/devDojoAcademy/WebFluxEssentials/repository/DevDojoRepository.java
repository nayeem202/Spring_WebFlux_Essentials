package devDojoAcademy.WebFluxEssentials.repository;

import devDojoAcademy.WebFluxEssentials.domain.DevDojoUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface DevDojoRepository extends ReactiveCrudRepository<DevDojoUser,Integer> {
    Mono<DevDojoUser> findDevDojoUserByUsername(String username);
}
