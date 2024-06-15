package br.com.logapi.services;

import br.com.logapi.entities.GameEntity;
import br.com.logapi.models.Game;
import br.com.logapi.models.GameReportDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogParseTest {

    @Mock
    private GameService gameService;

    @Mock KillService killService;

    @InjectMocks
    private LogParse logParse;

    @Captor
    private ArgumentCaptor<GameEntity> gameEntityCaptor;

    @Captor
    private ArgumentCaptor<Game> gameCaptor;

    @Test
    @DisplayName("should parse log and save games correctly")
    public void shouldParseLogAndSaveGames() throws IOException {
        // Cria um arquivo de log de teste
        Path tempFile = Files.createTempFile("game_log", ".log");
        Files.write(tempFile, Arrays.asList(
                "20:34 Kill: 1022 2 22: <world> killed Player1 by MOD_TRIGGER_HURT",
                "20:35 Kill: 2 3 7: Player2 killed Player3 by MOD_ROCKET",
                "20:36 ShutdownGame:"
        ));

        File logFile = tempFile.toFile();

        // Configura os mocks
        Page<GameReportDTO> mockPage = new PageImpl<>(Collections.emptyList());
        when(gameService.findAll(0, 10)).thenReturn(mockPage);

        GameEntity mockGameEntity = new GameEntity(2, LocalDateTime.now(), LocalDateTime.now());
        when(gameService.save(any(GameEntity.class))).thenReturn(mockGameEntity);

        // Chama o método a ser testado
        Page<GameReportDTO> result = logParse.parseLog(logFile);

        // Verifica se os métodos save foram chamados com os argumentos corretos
        verify(gameService, times(2)).save(gameEntityCaptor.capture());
        List<GameEntity> savedGameEntities = gameEntityCaptor.getAllValues();
        verify(killService, times(1)).save(gameCaptor.capture(), gameEntityCaptor.capture());


        // Verifica as propriedades dos GameEntity salvos
        assertEquals(2, savedGameEntities.size());
        assertEquals(2, savedGameEntities.get(0).getTotalKills());

        // Verifica o resultado retornado pelo método parseLog
        assertEquals(mockPage, result);

        // Limpa o arquivo temporário
        Files.deleteIfExists(tempFile);
    }

}