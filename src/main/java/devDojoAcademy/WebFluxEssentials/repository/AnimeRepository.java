package devDojoAcademy.WebFluxEssentials.repository;




import devDojoAcademy.WebFluxEssentials.domain.Anime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {


    @Query("select * from anime")
    Flux<Anime> getAllAnime();

    Mono<Anime> findAnimeById(int id);


}
