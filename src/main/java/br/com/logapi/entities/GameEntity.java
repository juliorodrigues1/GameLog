package br.com.logapi.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_Kills")
    private int totalKills;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ElementCollection
    @CollectionTable(name = "game_players_kills", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "player_name")
    @Column(name = "kills")
    private Map<String, Integer> playersKills;


    public Long getId() {
        return id;
    }

    public GameEntity(int totalKills, LocalDateTime startTime, LocalDateTime endTime) {
        this.totalKills = totalKills;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public GameEntity() {
    }

    public Map<String, Integer> getPlayersKills() {
        return playersKills;
    }

    public void setPlayersKills(Map<String, Integer> playersKills) {
        this.playersKills = playersKills;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

}
