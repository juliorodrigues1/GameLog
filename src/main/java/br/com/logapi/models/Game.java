package br.com.logapi.models;

import java.time.LocalDateTime;
import java.util.*;

public class Game {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalKills;

    private List<Kill> kills = new ArrayList<>();

    private Set<Player> players = new HashSet<>();

    private Map<String, Integer> playersKills = new HashMap<>();

    public Game() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public List<Kill> getKills() {
        return kills;
    }

    public void setKills(List<Kill> kills) {
        this.kills = kills;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Map<String, Integer> getPlayersKills() {
        return playersKills;
    }


    public void setPlayersKills(Map<String, Integer> playersKills) {
        this.playersKills = playersKills;
    }
}
