package com.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Entity(name = "ArtistDTO")
@Table(name = "Artist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @Column(
            name = "idArtist",
            updatable = false,
            length = 128
    )
    private String UUID;
    @Column(
            name = "name",
            updatable = true
    )
    private String name;
    @Column(name = "activity")
    private boolean active;

    @ManyToMany
    @JoinTable(name="artistsSongs", joinColumns = @JoinColumn(name="IDArtist"),inverseJoinColumns = @JoinColumn(name="IDSong"))
    private Set<Song> listOfSongs;

    public Artist(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public Set<Song> getListOfSongs() {
        return listOfSongs;
    }
}
