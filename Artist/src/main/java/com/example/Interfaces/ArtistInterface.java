package com.example.Interfaces;

import com.example.Entity.Artist;
import com.example.Entity.Song;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.List;
import java.util.Set;

public interface ArtistInterface {

    Artist saveArtist(Artist artist);

    void addSongToArtist(String artist, String song);

    Artist getArtist(String name);

    Artist getArtistByUUID(String UUID);

    List<Artist> getArtists();

    Set<Song> getArtistSong(String name);

    List<Artist> getArtistByName(String name);

    void deleteArtist(String id) throws JsonProcessingException;
}
