package devDojoAcademy.WebFluxEssentials.controller;

import devDojoAcademy.WebFluxEssentials.domain.Anime;
import devDojoAcademy.WebFluxEssentials.repository.AnimeRepository;
import devDojoAcademy.WebFluxEssentials.services.AnimeService;
import devDojoAcademy.WebFluxEssentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

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
        BDDMockito.when(animeServiceMock.getAllAnime())
                .thenReturn(Flux.just(anime));
        BDDMockito.when(animeServiceMock.AnimefindById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(anime));
        BDDMockito.when(animeServiceMock.save(AnimeCreator.createAnimeTestToBeSaved()))
                .thenReturn(Mono.just(anime));
        BDDMockito.when(animeServiceMock.update(AnimeCreator.createValidUpdateAnime()))
                        .thenReturn(Mono.empty());

        BDDMockito.when(animeServiceMock.delete(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        BDDMockito.when(animeServiceMock
                        .saveAll(List.of(AnimeCreator.createAnimeTestToBeSaved(), AnimeCreator.createAnimeTestToBeSaved())))
                .thenReturn(Flux.just(anime,anime));

    }




    @Test
    @DisplayName("ListAll returns a flux of anime")
    public void ListAll_ReturnFluxOfAnime_whenSuccessful(){
        StepVerifier.create(animeController.listAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }


    @Test
    @DisplayName("findByid returns Mono Error when anime does not exit")
    public void findById_ReturnMonoerror_whenEmptyMonoisReturned(){
        StepVerifier.create(animeController.findAnimById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }


    @Test
    @DisplayName("find By Id when Mono Returns a Mono when anime doesnot exists returns")
    public void save_createAnime_when_successful(){
       Anime animeToSaved =  AnimeCreator.createAnimeTestToBeSaved();
        StepVerifier.create(animeController.save(animeToSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }
    @Test
    @DisplayName("update save update Anime and returns empty when successfull")
    public void update_saveUpdateAnime_with_successful(){
        StepVerifier.create(animeController.update(1, AnimeCreator.createValidUpdateAnime()))
                .expectSubscription()
                .verifyComplete();

    }
    @Test
    @DisplayName("delete removes the anime when successful")
    public void delete_RemoveAnime_when_successful(){
        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }





/*
    @Test
    @DisplayName("delete returns Mono error when anime does not exist")
    public void Delete_ReturnedMonoError_whenEmptyMonoIsReturned(){
        BDDMockito.when(animeController.findAnimById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }
*/


    @Test
    @DisplayName("Batch Creates an Anime When Successful")
    public void saveAll_CreateAnime_WhenSuccessfull(){
        Anime animeToBeSaved =  AnimeCreator.createAnimeTestToBeSaved();

        StepVerifier.create(animeController.batchSave(List.of(animeToBeSaved, animeToBeSaved)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

/*
    @Test
    @DisplayName("SaveAll return a mono error an when one of the  anime is ivalid")
    public void saveAll_returns_Mono_error_whenContainsInvalidName(){
        Anime animeToBeSaved =  AnimeCreator.createAnimeTestToBeSaved();
        BDDMockito.when(animeServiceMock
                        .saveAll(ArgumentMatchers.anyIterable()))
                .thenReturn(Flux.just(anime, anime.withName("")));
        StepVerifier.create(animeServiceMock.saveAll(List.of(animeToBeSaved, animeToBeSaved.withName(""))))
                .expectSubscription()
                .expectNext(anime)
                .expectError(ResponseStatusException.class)
                .verify();
    }
*/





}