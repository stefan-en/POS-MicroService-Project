package com.example.Interfaces;

import com.example.Entity.Song;
import com.example.Enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

    Song findByNameOfSong(String nameOfSong);
    Song findByIdSong(Integer idSong);

    @Query(value = "SELECT * FROM song WHERE name_of_song LIKE %?1%", nativeQuery = true)
    Page<Song> findSongByName(String nameOfSong, PageRequest of);

    List<Song> findByYear(Integer year);
    @Query(value = "SELECT * FROM song WHERE genre LIKE %?1%" , nativeQuery = true)
    List<Song> findByGenre(Genre genre);
    @Query(value = "SELECT * FROM song WHERE type LIKE %?1%" , nativeQuery = true)

    List<Song> findByType(Types type);
    @Query(value = "SELECT * FROM song WHERE year BETWEEN ?1 and ?2" , nativeQuery = true)//de verificat ca nu merge

    List<Song> findByFiltredYear(Integer initial, Integer end);
    @Query(value = "SELECT * FROM song WHERE name_of_song = ?1" , nativeQuery = true)
    List<Song> getSongByNameExact(String nameOfSong);
    @Query(value = "SELECT * FROM song WHERE name_of_song = ?1" , nativeQuery = true)
    Page<Song> findAll(String nameOfSong, PageRequest of);

    //Song update(Song song);//nu stiu sa fac update
}
