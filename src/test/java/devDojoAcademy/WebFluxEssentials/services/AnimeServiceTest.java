package devDojoAcademy.WebFluxEssentials.services;

import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import devDojoAcademy.WebFluxEssentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    private  final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
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


//    @BeforeEach
//    public void setUp(){
//        BDDMockito.when(animeRepositoryMock.findAll()
//                .the(Flux.just(anime))
//        )
//    }

    @BeforeEach
    public void setUp(){
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));


        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(anime));


    }

    @Test
    @DisplayName("ListAll returns a flux of anime")
    public void ListAll_ReturnFluxOfAnimee_whenSuccessful(){
        StepVerifier.create(animeService.getAllAnime())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Mono Returns a Mono when a id exists returns")
    public void findByIdReturnMono(){
        StepVerifier.create(animeService.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Id when Mono Returns a Mono when anime doesnot exists returns")
    public void findBy_id_ReturnMonoError_When_empty_Mono_Return(){
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.findById(5))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }


}