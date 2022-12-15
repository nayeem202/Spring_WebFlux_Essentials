package devDojoAcademy.WebFluxEssentials.controller;


import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.services.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/anime")
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
        return animeService.AnimefindById(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Anime> save(@Valid @RequestBody Anime anime){
        return animeService.save(anime);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable long id ,@Valid @RequestBody Anime anime){
        return animeService.update(anime.withId(id));
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable long id){
        return animeService.delete(id);
    }




}
