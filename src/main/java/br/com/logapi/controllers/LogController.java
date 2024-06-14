package br.com.logapi.controllers;

import br.com.logapi.models.GameReportDTO;
import br.com.logapi.services.GameService;
import br.com.logapi.services.LogParse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@RestController
@RequestMapping("api/log")
@Tag(name = "LogController", description = "API para gerenciamento de logs")
public class LogController {

    private final LogParse logParse;
    private final GameService gameService;

    public LogController(LogParse logParse, GameService gameService) {
        this.logParse = logParse;
        this.gameService = gameService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recebe arquivo de log", description = "Processa o arquivo de log")
    public ResponseEntity<Page<GameReportDTO>> getLog(@RequestParam(name = "file") MultipartFile file)  {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            var games = logParse.parseLog(convFile);

            return ResponseEntity.ok(games);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um jogos", description = "Retorna um jogo por id")
    public ResponseEntity<GameReportDTO> getGameById(@PathVariable Long id) {
        GameReportDTO gameReportDTO = gameService.findGameReportById(id);
        if (gameReportDTO != null) {
            return ResponseEntity.ok(gameReportDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Retorna todos os jogos paginados", description = "Retorna uma lista paginada de jogos")
    public ResponseEntity<Page<GameReportDTO>> getAllGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GameReportDTO> games = gameService.findAll(page, size);
        return ResponseEntity.ok(games);
    }


}
