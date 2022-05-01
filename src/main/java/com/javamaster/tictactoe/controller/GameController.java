package com.javamaster.tictactoe.controller;

import com.javamaster.tictactoe.controller.dto.ConnectRequest;
import com.javamaster.tictactoe.exception.FieldIsOccupiedException;
import com.javamaster.tictactoe.exception.GameNotFoundExeception;
import com.javamaster.tictactoe.exception.InvalidGameException;
import com.javamaster.tictactoe.exception.InvalidParamException;
import com.javamaster.tictactoe.model.Game;
import com.javamaster.tictactoe.model.GamePlay;
import com.javamaster.tictactoe.model.Player;
import com.javamaster.tictactoe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request(");
        return ResponseEntity.ok(gameService.createGame(player));
    }


    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("connect request: {} ", request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(),request.getGameId() ));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestParam Player player) throws GameNotFoundExeception {
        log.info("connect random: {}", player);

        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws InvalidGameException, GameNotFoundExeception, FieldIsOccupiedException {
        log.info("game play: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progess" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
