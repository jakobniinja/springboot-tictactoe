package com.javamaster.tictactoe.service;

import com.javamaster.tictactoe.exception.GameNotFoundExeception;
import com.javamaster.tictactoe.exception.InvalidGameException;
import com.javamaster.tictactoe.exception.InvalidParamException;
import com.javamaster.tictactoe.model.*;
import com.javamaster.tictactoe.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.ietf.jgss.GSSContext;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameService {
    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(GameStatus.NEW);
        GameStorage.getInstance().setGames(game);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws InvalidGameException, InvalidParamException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Game id: " + gameId + " does not existing!");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) {
            throw new InvalidGameException("Game With id: " + gameId + " is busy!");
        }
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRSS);
        GameStorage.getInstance().setGames(game);
        return game;
    }

    public Game connectToRandomGame(Player player2) throws GameNotFoundExeception {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(x -> x.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(() -> new GameNotFoundExeception("Game not found"));
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRSS);
        GameStorage.getInstance().setGames(game);
        return game;
    }


    public Game gamePlay(GamePlay gamePlay) throws GameNotFoundExeception, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new GameNotFoundExeception("Game not found!");
        }
        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already Finished");
        }

        int[][] board = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
        Boolean yWinner = checkWinner(game.getBoard(), TicToe.O);


        if (xWinner ) {
            game.setWinner(TicToe.X);
        }
        if (yWinner ) {
            game.setWinner(TicToe.O);
        }
        GameStorage.getInstance().setGames(game);
        return game;
    }

    private Boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArr = new int[9];
        int counterIdx = 0;
        for (int i =0; i< board.length; i++){
            for(int j=0; j< board[i].length; j++){
                boardArr[counterIdx] = board[i][j];
                counterIdx++;
            }
        }

        int[] [] winCombinations  ={{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6} };
        for (int i = 0; i < winCombinations.length; i++) {
            int counter =0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArr[winCombinations[i][j]] == ticToe.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;

                    }
                }
            }
        }
        return false;
    }
}
