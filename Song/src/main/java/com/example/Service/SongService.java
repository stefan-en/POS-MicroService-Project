package com.example.Service;

import com.example.Entity.Song;
import com.example.Enums.Genre;
import com.example.Enums.Types;
import com.example.Interfaces.SongInterface;
import com.example.Interfaces.SongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SongService implements SongInterface {
    private final SongRepository songRepository;

    @Override
    public Song saveSong(Song song) {
        Song tSong = new Song();
        tSong.setNameOfSong(song.getNameOfSong());
        tSong.setGenre(song.getGenre());
        tSong.setYear(song.getYear());
        tSong.setType(song.getType());
        log.info("Melodia: " + song.getNameOfSong() + " a fost adaugata la database");
        return songRepository.save(tSong);
    }

    @Override
    public Song getSong(String NameOfSong) {

        return songRepository.findByNameOfSong(NameOfSong);
    }

    @Override
    public Song getSongById(Integer id) {
        return songRepository.findByIdSong(id);
    }

    @Override
    public List<Song> getSongByYear(Integer year) {
        return songRepository.findByYear(year);
    }


    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> getSongByGenre(Genre genre) {
        return songRepository.findByGenre(genre);
    }

    @Override
    public List<Song> getSongByType(Types type) {
        return songRepository.findByType(type);
    }

    @Override
    public List<Song> getSongFiltredByYear(Integer initial, Integer end) {
        return songRepository.findByFiltredYear(initial,end);
    }
    @Override
    public Page<Song> getSongByExactName(String NameOfSong, Integer offset, Integer pageSize) {
        return songRepository.findAll(NameOfSong,PageRequest.of(offset,pageSize));
    }

    @Override
    public Page<Song> getSongs(Integer page, Integer pageSize) {
        return  songRepository.findAll(PageRequest.of(page,pageSize));
    }

    public Page<Song> getSongByPartialName(String name, Integer page, Integer pageSize) {
        return songRepository.findSongByName(name,PageRequest.of(page,pageSize));
    }
    @Override
    public String deleteSong(String id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(id);
        Integer uuid = jsonNode.get("id").asInt();
        System.out.println(uuid);
        Song song = songRepository.findByIdSong(uuid);
        if(song != null) {
            songRepository.delete(song);
            log.info("Melodia: " + song.getNameOfSong() + " a trecut din trend");
            return  ("Melodia: " + song.getNameOfSong() + " a trecut din trend");
        }
        else{
            return  ("Nu avem melodia pe YouTube");
        }

    }

}
