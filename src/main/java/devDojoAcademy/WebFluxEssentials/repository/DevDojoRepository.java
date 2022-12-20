package devDojoAcademy.WebFluxEssentials.repository;
import devDojoAcademy.WebFluxEssentials.domain.DevDojoUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DevDojoRepository extends ReactiveCrudRepository<DevDojoUser,Integer> {

    Mono<DevDojoUser> findDevDojoUserByUsername(String username);
}
