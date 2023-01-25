package org.playlist.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "playlists")

public class PlayList {
    @Id
    private String id;
    private String title;
    private List<Song> songs;
    private List<Integer> userId;

    public PlayList(String title, List<Song> songList, List<Integer> integerList){
        super();
        this.title = title;
        this.songs = songList;
        this.userId = integerList;
    }
}
