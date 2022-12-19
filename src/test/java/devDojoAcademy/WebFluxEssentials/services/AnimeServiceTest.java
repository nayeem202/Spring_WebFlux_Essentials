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

import java.util.List;
import java.util.concurrent.*;

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

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeTestToBeSaved()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeTestToBeSaved()))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepositoryMock
                        .saveAll(List.of(AnimeCreator.createAnimeTestToBeSaved(), AnimeCreator.createAnimeTestToBeSaved())))
                .thenReturn(Flux.just(anime,anime));

        BDDMockito.when(animeRepositoryMock.delete(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());


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
        StepVerifier.create(animeService.AnimefindById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Id when Mono Returns a Mono when anime doesnot exists returns")
    public void findBy_id_ReturnMonoError_When_empty_Mono_Return(){
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.AnimefindById(5))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Save Creates an Anime When Successful")
    public void save_CreateAnime_WhenSuccessfull(){
        Anime animeToBeSaved =  AnimeCreator.createAnimeTestToBeSaved();

        StepVerifier.create(animeService.save(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("SaveAll Creates an Anime When Successful")
    public void saveAll_CreateAnime_WhenSuccessfull(){
        Anime animeToBeSaved =  AnimeCreator.createAnimeTestToBeSaved();

        StepVerifier.create(animeService.saveAll(List.of(animeToBeSaved, animeToBeSaved)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("SaveAll return a mono error an when one of the  anime is ivalid")
    public void saveAll_returns_Mono_error_whenContainsInvalidName(){
        Anime animeToBeSaved =  AnimeCreator.createAnimeTestToBeSaved();
        BDDMockito.when(animeRepositoryMock
                .saveAll(ArgumentMatchers.anyIterable()))
                        .thenReturn(Flux.just(anime, anime.withName("")));
        StepVerifier.create(animeService.saveAll(List.of(animeToBeSaved, animeToBeSaved.withName(""))))
                .expectSubscription()
                .expectNext(anime)
                .expectError(ResponseStatusException.class)
                .verify();
    }


    @Test
    @DisplayName("update returns empty Mono when successful")
    public void Update_SaveUpdateAnime_whenSuccessful(){

        StepVerifier.create(animeService.update(AnimeCreator.createValidAnime()))
                .expectSubscription()
                .verifyComplete();
    }
    @Test
    @DisplayName("update returns Mono error when anime does not exist")
    public void update_ReturnedMonoError_whenEmptyMonoIsReturned(){
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.update(AnimeCreator.createValidAnime()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete returns Mono error when anime does not exist")
    public void Delete_ReturnedMonoError_whenEmptyMonoIsReturned(){
        BDDMockito.when(animeRepositoryMock.findAnimeById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemoveAnime_when_successful(){
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }
}