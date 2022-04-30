package com.javamaster.tictactoe.controller.dto;

import com.javamaster.tictactoe.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
