package com.example.Interfaces;

import com.example.Entity.Song;
import com.example.Enums.Genre;
import com.example.Enums.Types;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SongInterface {

    Song saveSong(Song song);

    Song getSong(String NameOfSong);
    Song getSongById(Integer id);
    List <Song> getSongByYear(Integer year);
    //Page<Song> findByYears(Integer year);
    List<Song> getSongs();
    List<Song> getSongByGenre(Genre genre);

    List<Song> getSongByType(Types type);

    List<Song> getSongFiltredByYear(Integer initial, Integer end);

    Page<Song> getSongByExactName(String NameOfSong, Integer offset, Integer pageSize);

    Page<Song>getSongs(Integer page, Integer pageSize);

    String deleteSong(String id) throws JsonProcessingException;

    // Song updateSong(Song song);
}
