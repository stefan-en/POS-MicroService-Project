package org.playlist.repositories;

import org.playlist.Entity.PlayList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StreamPlayListRepositories extends MongoRepository<PlayList, Integer> {

    @Query("{title:'?0'")
    PlayList findPlayListByTitle(String title);
    @Query("{id:'?0'}")
    PlayList findPlayListById(String id);

}
