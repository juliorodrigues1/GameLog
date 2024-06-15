package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.exceptions.GameNotFound;
import br.com.logapi.models.GameReportDTO;
import br.com.logapi.repositories.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Captor
    private ArgumentCaptor<Long> longCaptor;

    @Captor
    private ArgumentCaptor<GameEntity> gameEntityCaptor;


    @Nested
    class getGameById{

        @Test
        @DisplayName("should get game by id with success")
        void shouldGetGameByIdWithSuccess(){
            Map<String, Integer> players = new HashMap<>();
            players.put("player1", 1);
            players.put("player2", 2);

            var gameEntity = new GameEntity();
            gameEntity.setId(1L);
            gameEntity.setPlayersKills(players);
            gameEntity.setTotalKills(10);

            doReturn(Optional.of(gameEntity)).when(gameRepository).findById(longCaptor.capture());

            gameService.findGameReportById(gameEntity.getId());

            assertEquals(gameEntity.getId(), longCaptor.getValue());
        }

        @Test
        @DisplayName("should throw GameNotFound exception")
        void shouldThrowGameNotFoundException() {
            Long gameId = 1L;

            doReturn(Optional.empty()).when(gameRepository).findById(longCaptor.capture());

            GameNotFound exception = assertThrows(GameNotFound.class, () -> {
                gameService.findGameReportById(gameId);
            });

            assertEquals("Game not found", exception.getMessage());
            assertEquals(gameId, longCaptor.getValue());
        }
    }

    @Nested
    class getAllGames{

        @Test
        @DisplayName("should return all games")
        void shouldReturnAllGames(){
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);

            GameEntity gameEntity1 = new GameEntity();
            gameEntity1.setId(1L);
            gameEntity1.setTotalKills(10);
            gameEntity1.setPlayersKills(Map.of("player1", 1, "player2", 2));

            GameEntity gameEntity2 = new GameEntity();
            gameEntity2.setId(2L);
            gameEntity2.setTotalKills(5);
            gameEntity2.setPlayersKills(Map.of("player3", 3, "player4", 4));

            List<GameEntity> gameEntities = Arrays.asList(gameEntity1, gameEntity2);
            Page<GameEntity> gameEntityPage = new PageImpl<>(gameEntities, pageable, gameEntities.size());

            doReturn(gameEntityPage).when(gameRepository).findAll(pageable);

            Page<GameReportDTO> result = gameService.findAll(page, size);

            assertEquals(2, result.getTotalElements());
            assertEquals("game_"+1L, result.getContent().get(0).getGameId());
            assertEquals("game_"+2L, result.getContent().get(1).getGameId());
            assertEquals(10, result.getContent().get(0).getTotalKills());
            assertEquals(5, result.getContent().get(1).getTotalKills());

        }
    }

    @Nested
    class SaveGame {

        @Test
        @DisplayName("should save game entity")
        void shouldSaveGameEntity() {
            GameEntity gameEntity = new GameEntity();
            gameEntity.setId(1L);
            gameEntity.setTotalKills(10);
            gameEntity.setPlayersKills(Map.of("player1", 1, "player2", 2));

            doReturn(gameEntity).when(gameRepository).save(gameEntityCaptor.capture());

            GameEntity savedGameEntity = gameService.save(gameEntity);

            assertNotNull(savedGameEntity);
            assertEquals(1L, savedGameEntity.getId());
            assertEquals(10, savedGameEntity.getTotalKills());
            assertEquals(Map.of("player1", 1, "player2", 2), savedGameEntity.getPlayersKills());
            verify(gameRepository, times(1)).save(gameEntity);
        }
    }
}