package org.playlist.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.playlist.DTO.PlayListDTO;
import org.playlist.Entity.PlayList;
import org.playlist.Entity.Song;
import org.playlist.Services.PlayListService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlistcollection")
public class PlayListController {
    private final PlayListService playListService;

    @GetMapping("/playlists")
    public ResponseEntity<List<PlayListDTO>> getPlayLists(@RequestParam(value = "port", required = false) Integer port){
        ModelMapper modelMapper = new ModelMapper();

        List<PlayList> playLists = playListService.getPlayLists();
        List<PlayListDTO> playListDTOList = playLists.stream().map(playList -> modelMapper.map(playList, PlayListDTO.class)).collect(Collectors.toList());
        playListDTOList.forEach(playList -> {
            UriComponentsBuilder linkBuilder = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8080)
                    .path("/api/playlistcollection/playlists/{uuid}");
            URI uri = linkBuilder.buildAndExpand(playList.getId()).toUri();
            playList.add(Link.of(String.valueOf(uri)));
            UriComponentsBuilder linkBuilder2 = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(port != null ? port : 8090)
                    .path("/api/songcollection/playlist");
            URI uri2 = linkBuilder2.buildAndExpand(playList.getUserId()).toUri();
            playList.add(Link.of(String.valueOf(uri2)).withRel("parent"));//artist to music


        });
        return ResponseEntity.ok().body(playListDTOList);
    }
    @GetMapping("/playlists/{id}")
    public ResponseEntity<PlayList> findPlayListById(@PathVariable("id") String id){
        return ResponseEntity.ok().body(playListService.getPlayListById(id));
    }
    @GetMapping("/playlists/title")
    public ResponseEntity<List<Song>> findSongsByTitle(@RequestParam(value = "title")String title){
        return ResponseEntity.ok().body(playListService.getSongFromPlayList(title));
    }
    @PostMapping("/playlists/save")
    public ResponseEntity<PlayList>saveSong(@RequestBody PlayList playList) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("playlist/save").toUriString());
        return ResponseEntity.created(uri).body(playListService.savePlayList(playList));

    }
    @DeleteMapping("/playlists/delete")
    public ResponseEntity<?> deletePlayListById(@RequestBody String id) throws JsonProcessingException {
        URI uri  = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("playlist/delete").toUriString());
        return ResponseEntity.created(uri).body(playListService.deleteById(id));
    }


}
