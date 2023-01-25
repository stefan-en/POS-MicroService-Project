package org.playlist.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.playlist.Entity.PlayList;
import org.playlist.Entity.Song;
import org.playlist.repositories.StreamPlayListRepositories;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlayListService {
    private final StreamPlayListRepositories streamPlayListRepositories;

    public PlayList savePlayList(PlayList playList) {
        PlayList playList1 = new PlayList();
        playList1.setTitle(playList.getTitle());
        playList1.setSongs(playList.getSongs());
        playList1.setUserId(playList.getUserId());
        log.info("Save {} to database.\n", playList1.getTitle());
        return streamPlayListRepositories.save(playList1);
    }


    public void createPlayList(){
        List<Song> songs = List.of(new Song(1,"Colinda 1"),new Song(2,"Manea 1"),new Song(3,"Depresie1"));
        List<Integer> user = List.of(1,4,5);
        PlayList playList = new PlayList("PlayList1",songs,user);
        streamPlayListRepositories.save(playList);
        log.info("Am creat playlist");
    }
    public void showAllPlaylist(){
        streamPlayListRepositories.findAll().forEach(playList -> System.out.println(playList.toString()));

    }
    public void getUsers(){
        streamPlayListRepositories.findAll().forEach(playList -> System.out.println(playList.getUserId()));
    }
    public List<PlayList> getPlayLists(){
        log.info("playlist");
        return streamPlayListRepositories.findAll();
    }
    public PlayList getPlayListByTitle(String title){
        return streamPlayListRepositories.findPlayListByTitle(title);
    }
    public PlayList getPlayListById(String id){
        return streamPlayListRepositories.findPlayListById(id);
    }
    public void updatePlayListByTitle(String title){
        PlayList playList = streamPlayListRepositories.findPlayListByTitle(title);
        playList.setTitle(title);
        streamPlayListRepositories.insert(playList);
    }
    public PlayList updatePlayList(PlayList playList){
        PlayList np = new PlayList();
        np.setId(playList.getId());
        np.setSongs(playList.getSongs());
        np.setTitle(np.getTitle());
        np.setUserId(playList.getUserId());

        return streamPlayListRepositories.save(np);
    }
    public void deleteByTitle(String title){
        PlayList playList = streamPlayListRepositories.findPlayListByTitle(title);
        if(playList == null){
            log.info("Nu exista acest playlist");
        }else{
            streamPlayListRepositories.delete(playList);
        }
    }
    public String deleteById(String id) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(id);
        String uuid = jsonNode.get("id").textValue();
        System.out.println(uuid);
        PlayList playList = streamPlayListRepositories.findPlayListById(uuid);
        if(playList != null) {
            streamPlayListRepositories.delete(playList);
            log.info("Playlist-ul: " + playList.getId() + " a iesit din uz");
            return ("S-a sters playlist-ul");
        }
        else{
            log.info("Nu avem playlist");
            return ("Nu exista acest playlist");

        }

    }
    public List<Song> getSongFromPlayList(String title){
        PlayList playList = streamPlayListRepositories.findPlayListByTitle(title);
        return playList.getSongs();
    }
}
