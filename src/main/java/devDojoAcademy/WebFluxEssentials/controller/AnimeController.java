package devDojoAcademy.WebFluxEssentials.controller;


import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import devDojoAcademy.WebFluxEssentials.services.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/anim")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private  final AnimeService animeService;

    @GetMapping("/")
    public Flux<Anime> listAll(){
        return animeService.getAllAnime();
    }

    @GetMapping("/{id}")
    public Mono<Anime> findAnimById(@PathVariable int id){
        return animeService.findById(id);
    }


}
