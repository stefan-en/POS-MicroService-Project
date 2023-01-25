package com.example;

import com.example.Entity.Artist;
import com.example.Entity.Song;
import com.example.Enums.Genre;
import com.example.Enums.Types;
import com.example.Service.SongService;
import com.example.Services.ArtistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ArtistService artistService, SongService songService) {
        return args -> {

            artistService.saveArtist(new Artist("Puya",true));
            artistService.saveArtist(new Artist("BUG Mafia",true));
            artistService.saveArtist(new Artist("Smile",true));
            artistService.saveArtist(new Artist("Fuego",true));
            artistService.saveArtist(new Artist("Ion Paladi",true));
            artistService.saveArtist(new Artist("Metalica",true));
            artistService.saveArtist(new Artist("Florin Salam",true));



            songService.saveSong(new Song("Strazile", Genre.Pop,2007, Types.single));
            songService.saveSong(new Song("Poezia de strada",Genre.Pop,2004,Types.single));
            songService.saveSong(new Song("Acasa",Genre.Pop,20020,Types.single));
            songService.saveSong(new Song("Maidanez",Genre.Pop,2018,Types.single));

            songService.saveSong(new Song("Dorul Basarabiei",Genre.Populara,2005,Types.single));
            songService.saveSong(new Song("Dead Metal",Genre.Rock,2000,Types.album));
            songService.saveSong(new Song("Impodobeste mama bradul",Genre.Classic,2001,Types.album));
            songService.saveSong(new Song("Bag bani",Genre.Pop,2005,Types.albums));

            songService.saveSong(new Song("Saint Tropez",Genre.Manele,2018,Types.single));
            songService.saveSong(new Song("Buna Dimineata",Genre.Manele,2017,Types.single));
            songService.saveSong(new Song("Haven",Genre.Pop,2004,Types.albums));
            songService.saveSong(new Song("Casa parinteasca",Genre.Classic,2002,Types.albums));


            artistService.addSongToArtist("Puya","Maidanez");
            artistService.addSongToArtist("Puya","Bag bani");
            artistService.addSongToArtist("BUG Mafia","Strazile");
            artistService.addSongToArtist("BUG Mafia","Poezia de strada");
            artistService.addSongToArtist("Florin Salam","Saint Tropez");
            artistService.addSongToArtist("Florin Salam","Buna Dimineata");
            artistService.addSongToArtist("Fuego","Impodobeste mama bradul");
            artistService.addSongToArtist("Fuego","Casa parinteasca");
            artistService.addSongToArtist("Smile","Acasa");
            artistService.addSongToArtist("Ion Paladi","Dorul Basarabiei");
            artistService.addSongToArtist("Ion Paladi","Casa parinteasca");
            artistService.addSongToArtist("Metalica","Haven");
            artistService.addSongToArtist("Metalica","Dead Metal");


        };
    }
}
