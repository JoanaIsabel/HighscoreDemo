package com.joana.demoproject.demo.controller;

import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.entitiy.Score;
import com.joana.demoproject.demo.request.GetScoresRequest;
import com.joana.demoproject.demo.request.ScoreCreationRequest;
import com.joana.demoproject.demo.service.PlayerService;
import com.joana.demoproject.demo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private PlayerService playerService;

    public ScoreController(ScoreService scoreService,
                           PlayerService playerService) {
        this.scoreService = scoreService;
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Score>> getGlobalScores(
            @RequestBody GetScoresRequest getScoresRequest) {
        List<Score> scores = this.scoreService.findTopScoresLimitedBy(getScoresRequest.getLimit());
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/{playerId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Score>> getScoresforPlayer(
            @PathVariable Long playerId,
            @RequestBody GetScoresRequest getScoresRequest) {
        Optional<Player> optinalPlayer = this.playerService.findById(playerId);
        if (optinalPlayer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Score> scores = this.scoreService.findTopScoresForPlayerLimitedBy(getScoresRequest.getLimit(), playerId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Score> putScore(
            @RequestBody(required = true) @Valid ScoreCreationRequest scoreCreationRequest) {
        if(scoreCreationRequest.getPlayerId() != null) {
            Optional<Player> optinalPlayer = this.playerService.findById(scoreCreationRequest.getPlayerId());
            if (optinalPlayer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        Score score = this.scoreService.saveFromRequest(scoreCreationRequest);
        return new ResponseEntity<>(score, HttpStatus.CREATED);
    }
}
