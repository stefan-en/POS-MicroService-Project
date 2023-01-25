package com.example;
import com.example.Entity.Song;
import com.example.Enums.Genre;
import com.example.Enums.Types;
import com.example.Service.SongService;
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
    CommandLineRunner commandLineRunner( SongService songService) {
        return args -> {




            songService.saveSong(new Song("Strazile",Genre.Pop,2007,Types.single));
            songService.saveSong(new Song("Poezia de strada",Genre.Pop,2004,Types.single));
            songService.saveSong(new Song("Acasa",Genre.Pop,20020,Types.single));
            songService.saveSong(new Song("Maidanez",Genre.Pop,2018,Types.single));

            songService.saveSong(new Song("Dorul Basarabiei", Genre.Populara,2005, Types.single));
            songService.saveSong(new Song("Dead Metal",Genre.Rock,2000,Types.album));
            songService.saveSong(new Song("Impodobeste mama bradul",Genre.Classic,2001,Types.album));
            songService.saveSong(new Song("Bag bani",Genre.Pop,2005,Types.albums));

            songService.saveSong(new Song("Saint Tropez",Genre.Manele,2018,Types.single));
            songService.saveSong(new Song("Buna Dimineata",Genre.Manele,2017,Types.single));
            songService.saveSong(new Song("Haven",Genre.Pop,2004,Types.albums));
            songService.saveSong(new Song("Casa parinteasca",Genre.Classic,2002,Types.albums));




        };
    }
}
