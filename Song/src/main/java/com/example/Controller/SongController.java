package com.example.Controller;


import com.example.DTO.SongDTO;
import com.example.Entity.Song;
import com.example.Enums.Genre;
import com.example.Enums.Types;
import com.example.Service.SongService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/songcollection")
public class SongController {

    private final SongService songService;

    @GetMapping("/songs")
    public ResponseEntity<List<SongDTO>> getSongs(
            @RequestParam (value="nameOfSong", required = false) String nameOfSong,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value="items_per_page", required = false) Integer pageSize,
            @RequestParam(value = "match", required = false,defaultValue = "partial"
            ) String match,
            @RequestParam(value = "port", required = false) Integer port){
        ModelMapper modelMapper = new ModelMapper();
        List<SongDTO> dtoList = new ArrayList<>();
        Page<Song> songList = null;
        if(page != null){
            if(pageSize == null){
                pageSize = Integer.MAX_VALUE;
            }
        }else{
            page = 0;
            pageSize = Integer.MAX_VALUE;
        }
        if (nameOfSong != null) {
            if (match.equals("exact")) {
                songList = songService.getSongByExactName(nameOfSong,page,pageSize);

            } else {
                songList = songService.getSongByPartialName(nameOfSong,page,pageSize);

            }
        }else {
            songList = songService.getSongs(page,pageSize);
        }
        List<SongDTO> songDTOS = songList.stream().map(song -> modelMapper.map(song, SongDTO.class)).collect(Collectors.toList());
        songDTOS.forEach(song -> {
            UriComponentsBuilder linkBuilder = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8090)
                    .path("/api/songcollection/songs/{uuid}");
            URI uri = linkBuilder.buildAndExpand(song.getIdSong()).toUri();
            song.add(Link.of(String.valueOf(uri)));
            UriComponentsBuilder linkBuilder2 = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8090)
                    .path("/api/songcollection/songs");
            URI uri2 = linkBuilder2.buildAndExpand(song.getIdSong()).toUri();
            song.add(Link.of(String.valueOf(uri2)).withRel("parent"));
        });
        return ResponseEntity.ok().body(songDTOS);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> findSongById(@PathVariable("id")Integer id){
        return ResponseEntity.ok().body(songService.getSongById(id));
    }
    @GetMapping("/songs/year")
    public ResponseEntity<List<Song>> findSongByYear(@RequestParam("year") Integer year) {
        return ResponseEntity.ok().body(songService.getSongByYear(year));
    }
    @GetMapping("/songs/genre")
    public ResponseEntity<List<Song>> findSongByGenre(@RequestParam("genre") Genre genre) {
        return ResponseEntity.ok().body(songService.getSongByGenre(genre));
    }
    @GetMapping("/songs/type")
    public ResponseEntity<List<Song>> findSongByType(@RequestParam("type") Types type) {
        return ResponseEntity.ok().body(songService.getSongByType(type));
    }
    @GetMapping("/songs/filter")
    public ResponseEntity<List<Song>> findSongByYearFilter(@RequestParam("initialYear") Integer initial , @RequestParam("endYear") Integer end) {
        return ResponseEntity.ok().body(songService.getSongFiltredByYear(initial,end));
    }


    @PostMapping("/songs/save")
    public ResponseEntity<Song>saveSong(@RequestBody Song song) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("song/save").toUriString());
        return ResponseEntity.created(uri).body(songService.saveSong(song));

    }
    @DeleteMapping("/songs/delete")
    public ResponseEntity<String>deleteSong(@RequestBody String id) throws JsonProcessingException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("song/delete").toUriString());

        return ResponseEntity.created(uri).body(songService.deleteSong(id));

    }


}
