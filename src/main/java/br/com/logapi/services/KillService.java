package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.entities.KillEntity;
import br.com.logapi.models.Game;
import br.com.logapi.repositories.KillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KillService {

    private KillRepository killRepository;

    public KillService(KillRepository killRepository) {
        this.killRepository = killRepository;
    }

    public List<KillEntity> save(Game currentGame, GameEntity gameSave) {
        List<KillEntity> killEntities = currentGame.getKills().stream()
                .map(kill -> new KillEntity(kill.getKiller(), kill.getVictim(), kill.getWeapon(), gameSave))
                .collect(Collectors.toList());
        return killRepository.saveAll(killEntities);
    }
}
