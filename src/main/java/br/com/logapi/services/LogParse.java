package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.models.Game;
import br.com.logapi.models.GameReportDTO;
import br.com.logapi.models.Kill;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogParse {

    private final GameService gameService;
    private final KillService killService;

    public LogParse(GameService gameService, KillService killService) {
        this.gameService = gameService;
        this.killService = killService;
    }

    private static final Pattern KILL_PATTERN = Pattern.compile(
            "(\\d{1,2}:\\d{2}) Kill: \\d+ \\d+ \\d+: (.+) killed (.+) by (.+)"
    );

    private static final Pattern SHUTDOWN_PATTERN = Pattern.compile(
            "(\\d{1,2}:\\d{2}) ShutdownGame:"
    );

    private static final Pattern INIT_PATTERN = Pattern.compile(
            "(\\d{1,2}:\\d{2}) InitGame:"
    );

    public Page<GameReportDTO> parseLog(File logFile) throws IOException {
        List<Game> games = new ArrayList<>();
        Game currentGame = null;
        List<String> lines = Files.readAllLines(logFile.toPath());

        for (String line : lines) {
            Matcher killMatcher = KILL_PATTERN.matcher(line);
            Matcher shutdownMatcher = SHUTDOWN_PATTERN.matcher(line);
            Matcher initMatcher = INIT_PATTERN.matcher(line);

            if (shutdownMatcher.find()) {
                if (currentGame != null) {
                    finalizeGame(currentGame);
                    games.add(currentGame);
                    currentGame = null;
                }
            } else if (killMatcher.find()) {
                if (currentGame == null) {
                    currentGame = new Game();
                    currentGame.setStartTime(LocalDateTime.now());
                }
                processKill(currentGame, killMatcher);
            }
        }

        return this.gameService.findAll(0, 10);
    }

    private void finalizeGame(Game currentGame) {
        currentGame.setEndTime(LocalDateTime.now());
        GameEntity gameEntity = new GameEntity(currentGame.getTotalKills(), currentGame.getStartTime(), currentGame.getEndTime());
        GameEntity savedGame = this.gameService.save(gameEntity);
        currentGame.setId(savedGame.getId());

        Map<String, Integer> playersKills = currentGame.getPlayersKills();
        savedGame.setPlayersKills(playersKills);
        this.gameService.save(savedGame);
        this.killService.save(currentGame, savedGame);
    }

    public void processKill(Game currentGame, Matcher killMatcher) {
        String killer = killMatcher.group(2);
        String victim = killMatcher.group(3);
        String weapon = killMatcher.group(4);

        Kill kill = new Kill();
        kill.setKiller(killer);
        kill.setVictim(victim);
        kill.setWeapon(weapon);
        kill.setGame(currentGame);

        if ("<world>".equals(killer)) {
            currentGame.setTotalKills(currentGame.getTotalKills() + 1);
            updatePlayerKillsForWorldKill(currentGame, victim);
        } else {
            currentGame.getKills().add(kill);
            currentGame.setTotalKills(currentGame.getTotalKills() + 1);
            updatePlayerKills(currentGame, killer, victim);
        }
    }

    private void updatePlayerKills(Game currentGame, String killer, String victim) {
        Map<String, Integer> playersKills = currentGame.getPlayersKills();
        playersKills.put(killer, playersKills.getOrDefault(killer, 0) + 1);
        playersKills.put(victim, playersKills.getOrDefault(victim, 0));
    }

    private void updatePlayerKillsForWorldKill(Game currentGame, String victim) {
        Map<String, Integer> playersKills = currentGame.getPlayersKills();
        playersKills.put(victim, playersKills.getOrDefault(victim, 0) - 1);
    }

}
