package com.example.Services;

import com.example.Entity.Artist;
import com.example.Interfaces.ArtistInterface;
import com.example.Interfaces.ArtistRepository;
import com.example.Interfaces.SongRepository;
import com.example.Entity.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArtistService implements ArtistInterface {
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;

    @Override
    public Artist saveArtist(Artist artist) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        artist.setName(artist.getName());
        artist.setUUID(uuidAsString);
        artist.setActive(true);
        log.info("Save {} to database.\n", artist.getName());
        return artistRepository.save(artist);
    }

    @Override
    public void addSongToArtist(String artist, String song) {
        Artist artists = artistRepository.findByName(artist);
        Song songs = songRepository.findByNameOfSong(song);
        artists.getListOfSongs().add(songs);
        log.info("Added {} to {}\n",songs,artists);

    }

    @Override
    public Artist getArtist(String name) {
        log.info("See you artists:{}\n",name);
        return artistRepository.findByName(name);
    }

    @Override
    public Artist getArtistByUUID(String UUID) {
        return  artistRepository.findByUUID(UUID);
    }

    @Override
    public List<Artist> getArtists() {
        log.info("See all artists\n");
        return  artistRepository.findAll();
    }

    @Override
    public Set<Song> getArtistSong(String uuid) {
        Artist artist = artistRepository.findByUUID(uuid);
        if(artist == null){
            log.info("No it's ok");
        }
        assert artist != null;
        return artist.getListOfSongs();

    }

    @Override
    public List<Artist> getArtistByName(String name) {

        return artistRepository.findArtistByName(name);
    }
    @Override
    public void deleteArtist(String id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(id);
        String uuid = jsonNode.get("id").textValue();
        System.out.println(uuid);
        Artist artist = artistRepository.findByUUID(uuid);
        if(artist != null) {
            artistRepository.delete(artist);
            log.info("Artistul: " + artist.getName() + " a trecut la Domnul");
        }
        else{
            log.info("Nu avem artist");
        }

    }
}
