package br.com.logapi.models;


public class Kill {

    private String killer;
    private String victim;
    private String weapon;

    public Kill() {
    }
    public Kill(String killer, String victim, String weapon) {
        this.killer = killer;
        this.victim = victim;
        this.weapon = weapon;
    }

    private Game game;


    public String getKiller() {
        return killer;
    }

    public void setKiller(String killer) {
        this.killer = killer;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
