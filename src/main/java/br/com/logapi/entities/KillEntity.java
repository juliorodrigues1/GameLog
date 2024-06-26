package br.com.logapi.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "kills")
public class KillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String killer;
    private String victim;
    private String weapon;

    @JoinColumn(name = "game_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;

    public KillEntity(String killer, String victim, String weapon, GameEntity game) {
        this.killer = killer;
        this.victim = victim;
        this.weapon = weapon;
        this.game = game;
    }

    public KillEntity() {}

    public long getId() {
        return id;
    }

    public String getKiller() {
        return killer;
    }

    public String getVictim() {
        return victim;
    }

    public String getWeapon() {
        return weapon;
    }

    public GameEntity getGame() {
        return game;
    }
}
