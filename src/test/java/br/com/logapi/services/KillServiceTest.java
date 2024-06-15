package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.entities.KillEntity;
import br.com.logapi.models.Game;
import br.com.logapi.models.Kill;
import br.com.logapi.repositories.KillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class KillServiceTest {

    @Mock
    private KillRepository killRepository;

    @InjectMocks
    private KillService killService;

    @Captor
    private ArgumentCaptor<List<KillEntity>> listKillEntityArgumentCaptor;

    @Nested
    class saveKill{

        @Test
        @DisplayName("should save kill list")
        void shouldSaveKillList(){
            var game = new Game();
            var gameEntity = new GameEntity();
            game.setKills(Arrays.asList(
                    new Kill("Player1", "Player2", "Sword"),
                    new Kill("Player3", "Player4", "Bow")
            ));

            List<KillEntity> expectedKillEntities = Arrays.asList(
                    new KillEntity("Player1", "Player2", "Sword", gameEntity),
                    new KillEntity("Player3", "Player4", "Bow", gameEntity)
            );

            doReturn(expectedKillEntities).when(killRepository).saveAll(listKillEntityArgumentCaptor.capture());

            killService.save(game, gameEntity);

            var kills = listKillEntityArgumentCaptor.getValue();

            assertEquals(expectedKillEntities.get(0).getKiller(), kills.get(0).getKiller());
            assertEquals(expectedKillEntities.get(0).getVictim(), kills.get(0).getVictim());
            assertEquals(expectedKillEntities.get(0).getWeapon(), kills.get(0).getWeapon());
            assertEquals(expectedKillEntities.get(1).getKiller(), kills.get(1).getKiller());
            assertEquals(expectedKillEntities.get(1).getVictim(), kills.get(1).getVictim());
            assertEquals(expectedKillEntities.get(1).getWeapon(), kills.get(1).getWeapon());

        }

    }

}