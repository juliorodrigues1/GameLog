package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.exceptions.GameNotFound;
import br.com.logapi.models.GameReportDTO;
import br.com.logapi.repositories.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameEntity save(GameEntity game) {
        return this.gameRepository.save(game);
    }

    public GameReportDTO findGameReportById(Long id) {
        GameEntity gameEntity = gameRepository.findById(id).orElseThrow(() -> new GameNotFound("Game not found"));

        return convertToGameReportDTO(gameEntity);

    }

    public Page<GameReportDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GameEntity> gameEntities = gameRepository.findAll(pageable);
        return gameEntities.map(this::convertToGameReportDTO);
    }

    private GameReportDTO convertToGameReportDTO(GameEntity gameEntity) {
        GameReportDTO report = new GameReportDTO();
        report.setGameId("game_" + gameEntity.getId());
        report.setTotalKills(gameEntity.getTotalKills());
        report.setKills(gameEntity.getPlayersKills());
        List<String> playes = new ArrayList<>();
        gameEntity.getPlayersKills().forEach((s, integer) -> playes.add(s));

        report.setPlayers(playes);
        return report;
    }


}
