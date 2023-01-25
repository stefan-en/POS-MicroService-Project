package org.playlist;

import lombok.RequiredArgsConstructor;
import org.playlist.Services.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoRepositories
@RequiredArgsConstructor
public class Main  implements CommandLineRunner {
    @Autowired
    private final PlayListService playListService;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        playListService.createPlayList();
        playListService.showAllPlaylist();

    }
}