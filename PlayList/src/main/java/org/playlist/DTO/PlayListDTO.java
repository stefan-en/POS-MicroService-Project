package org.playlist.DTO;

import lombok.Data;
import org.playlist.Entity.Song;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
public class PlayListDTO  extends EntityModel {
    private String id;
    private String title;
    private List<Song> songs;
    private List<Integer> userId;
    //TODO SONG DTO

}
