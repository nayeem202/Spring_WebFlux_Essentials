package devDojoAcademy.WebFluxEssentials.services;


import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;

    public Flux<Anime> getAllAnime() {
        return animeRepository.findAll();
    }



    public Mono<Anime> findById(int id){
        return animeRepository.findAnimeById(id)
                .switchIfEmpty(monoResponseStatusException())
                .log() ;
    }

    public <T> Mono<T> monoResponseStatusException(){
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime Not Found"));
    }

}
