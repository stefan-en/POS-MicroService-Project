package com.example.controller;

import com.example.DTO.ArtistDTO;
import com.example.Entity.Artist;
import com.example.Services.ArtistService;
import com.example.Entity.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/songcollection")
@Slf4j
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping("/artists")
    public ResponseEntity<List<ArtistDTO>>getArtists(@RequestParam (value="name", required = false) String name, @RequestParam(value = "port", required = false) Integer port) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        List<Artist> artists = new ArrayList<>();
        if(name != null)
            artists = artistService.getArtistByName(name);
        else artists = artistService.getArtists();

        List<ArtistDTO> artistDTOList = artists.stream().map(artist -> modelMapper.map(artist, ArtistDTO.class)).collect(Collectors.toList());
        artistDTOList.forEach(artist -> {
            UriComponentsBuilder linkBuilder = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8090)
                    .path("/api/songcollection/artists/{uuid}");
            URI uri = linkBuilder.buildAndExpand(artist.getUUID()).toUri();
            artist.add(Link.of(String.valueOf(uri)));
            UriComponentsBuilder linkBuilder2 = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8090)
                    .path("/api/songcollection/artists");
            URI uri2 = linkBuilder2.buildAndExpand(artist.getUUID()).toUri();
            artist.add(Link.of(String.valueOf(uri2)).withRel("parent"));
            //artist.add(linkTo(ArtistController.class).slash("/artists").withRel("parent"));
        });
        return ResponseEntity.ok(artistDTOList);
    }
    @GetMapping("/artists/{uuid}")//artistByUuid
    public ResponseEntity<Artist>findArtistById(@PathVariable ("uuid") String uuid) {
        System.out.println(artistService.getArtistByUUID(uuid));
        if (artistService.getArtistByUUID(uuid) != null){
            return ResponseEntity.ok().body(artistService.getArtistByUUID(uuid));
        }
        else
        {
            return ResponseEntity.notFound().build();
    }

    }

    @GetMapping("/artists/{uuid}/songs")//song of artist with uuid
    public ResponseEntity<Set<Song>>findSongsOfTheArtist(@PathVariable ("uuid") String uuid){
        return ResponseEntity.ok().body(artistService.getArtistSong(uuid));
    }
    @PostMapping("/artists/save")
    public ResponseEntity<Artist>saveArtist(@RequestBody Artist artist){
        URI uri  = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("artist/save").toUriString());
        return ResponseEntity.created(uri).body(artistService.saveArtist(artist));
    }
    @DeleteMapping("/artists/delete")
    public ResponseEntity<String>DeleteArtist(@RequestBody String id) throws JsonProcessingException {
        URI uri  = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("artist/delete").toUriString());
        artistService.deleteArtist(id);
        return ResponseEntity.created(uri).body("artistul cu id-ul " + id + " a fost sters");
    }


}
