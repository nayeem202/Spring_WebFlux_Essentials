package devDojoAcademy.WebFluxEssentials.integration;

import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import devDojoAcademy.WebFluxEssentials.services.AnimeService;
import devDojoAcademy.WebFluxEssentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(AnimeService.class)
public class AnimeControllerT {
    @MockBean
    private AnimeRepository animeRepository;
    @Autowired
    private WebTestClient testClient;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeRepository.findAll())
                .thenReturn(Flux.just(anime));
    }

    @Test
    public void blockHoundWorks(){
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);
            task.get(10, TimeUnit.SECONDS);
            //Assertions.fail("Should fail");

        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }


    @Test
    @DisplayName("ListAll returns a flux of anime")
    public void listAll_ReturnFluxOfAnime_whenSuccessful(){
        testClient
                .get()
                .uri("/anime/")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(anime.getId())
                .jsonPath("$.[0].name").isEqualTo(anime.getName());
    }

}

