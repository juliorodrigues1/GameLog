package br.com.logapi.models;


import java.util.HashSet;
import java.util.Set;

public class Player {


    private String name;

    private Set<Game> games = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
