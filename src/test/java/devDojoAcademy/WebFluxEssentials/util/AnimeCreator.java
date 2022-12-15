package devDojoAcademy.WebFluxEssentials.util;

import devDojoAcademy.WebFluxEssentials.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeTestToBeSaved(){
       return Anime.builder()
               .name("Nayeem Ahmed")
               .build();
    }
    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1)
                .name("Nayeem Ahmed")
                .build();
    }


    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .id(1)
                .name("Ahmed Nayeem")
                .build();
    }


}
