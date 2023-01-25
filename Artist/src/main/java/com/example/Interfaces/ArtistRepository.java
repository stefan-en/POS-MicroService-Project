package com.example.Interfaces;

import com.example.Entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Integer> {

    Artist findByName(String name);
    Artist findByUUID(String UUID);

    @Query(value = "SELECT * FROM artist WHERE name LIKE %?1%", nativeQuery = true)
    List<Artist> findArtistByName(String name);
}
