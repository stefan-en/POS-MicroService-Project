package com.example.DTO;

import com.example.Entity.Song;
import lombok.Data;
import org.springframework.hateoas.EntityModel;

import java.util.Set;

@Data
public class ArtistDTO extends EntityModel {
    private String UUID;
    private String name;
    private boolean active;
    private Set<Song> listOfSongs;

}
