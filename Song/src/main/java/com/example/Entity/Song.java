package com.example.Entity;

import com.example.Enums.Genre;
import com.example.Enums.Types;
import lombok.Data;


import javax.persistence.*;


@Table(name = "Song")
@Entity(name = "SongDTO")
@Data
public class Song {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Integer idSong;
    private String nameOfSong;
    private int year;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Enumerated(EnumType.STRING)
    private Types type;

    public Song(){

    }
    public Song( String nameOfSong, Genre genre, int year, Types type) {
        this.nameOfSong = nameOfSong;
        this.genre = genre;
        this.year = year;
        this.type = type;
    }
    @ManyToMany(mappedBy = "listOfSongs")
    //private Set<Artist> listOfArtists;


    public Integer getIdSong() {
        return idSong;
    }

    public void setIdSong(Integer idSong) {
        this.idSong = idSong;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public void setNameOfSong(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }
}
