package com.example.DTO;

import com.example.Enums.Genre;
import lombok.Data;
import org.springframework.hateoas.EntityModel;

@Data
public class SongDTO extends EntityModel {
    private Integer idSong;
    private String nameOfSong;
    private Genre genre;
    private int year;
}
