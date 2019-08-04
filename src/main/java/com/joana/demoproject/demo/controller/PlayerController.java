package com.joana.demoproject.demo.controller;

import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.request.PlayerCreationRequest;
import com.joana.demoproject.demo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/{playerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getPlayerForId(@PathVariable Long playerId) {
        Optional<Player> player = this.playerService.findById(playerId);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> createPlayer(
            @RequestBody(required = true) @Valid PlayerCreationRequest playerCreationRequest) {
        Player player = this.playerService.createPlayer(playerCreationRequest.getNickname());
        return new ResponseEntity<>(player, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/{playerId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> changeNickname(
            @PathVariable Long playerId,
            @RequestBody(required = true) @Valid PlayerCreationRequest playerCreationRequest) {
        Optional<Player> optionalPlayer = this.playerService.updateNickname(playerId, playerCreationRequest.getNickname());
        return optionalPlayer.map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
