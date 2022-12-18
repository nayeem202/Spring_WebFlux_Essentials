package devDojoAcademy.WebFluxEssentials.integration;

import devDojoAcademy.WebFluxEssentials.Exception.CustomeAttributes;
import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import devDojoAcademy.WebFluxEssentials.services.AnimeService;
import devDojoAcademy.WebFluxEssentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({AnimeService.class, CustomeAttributes.class})
public class AnimeControllerIT {
    @MockBean
    private AnimeRepository animeRepositoryMock;
    @Autowired
    private WebTestClient testClient;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(anime));
        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeTestToBeSaved()))
                .thenReturn(Mono.just(anime));
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
    @Test
    @DisplayName("findById returns a Mono with anime when it exists")
        public void findBy_ReturnMonoAnime_whenSuccessful(){
            testClient
                    .get()
                    .uri("/anime/{id}",1)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Anime.class)
                    .isEqualTo(anime);
        }



    @Test
    @DisplayName("findById returns Mono error when anime does not exist")
    public void findById_ReturnMonoError_whenMonoIsReturned(){
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                        .thenReturn(Mono.empty());
        testClient
                .get()
                .uri("anime/{id}",1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
                //.jsonPath("$.developerMessage").isEqualTo("A Response Status Exception Happened");

    }


    @Test
    @DisplayName("Save Creates an Anime When Successful")
    public void save_CreateAnime_WhenSuccessfull(){
        Anime animeToBeSaved = AnimeCreator.createAnimeTestToBeSaved();
        testClient
                .post()
                .uri("/anime/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Anime.class).isEqualTo(anime);
    }



    @Test
    @DisplayName("Save Return error")
    public void saveReturnError() {
        Anime animeToBeSaved = AnimeCreator.createAnimeTestToBeSaved().withName("");
        testClient
                .post()
                .uri("/anime/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(408);
    }


}

