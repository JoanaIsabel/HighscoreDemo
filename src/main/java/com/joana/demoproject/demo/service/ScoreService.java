package com.joana.demoproject.demo.service;

import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.entitiy.Score;
import com.joana.demoproject.demo.repository.ScoreRepository;
import com.joana.demoproject.demo.request.ScoreCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerService playerService;

    public ScoreService(ScoreRepository scoreRepository, PlayerService playerService) {
        this.scoreRepository = scoreRepository;
        this.playerService = playerService;
    }

    public Score saveFromRequest(ScoreCreationRequest request) {
        Player player = playerService.findById(request.getPlayerId()).orElse(playerService.createGuestPlayer());
        Score score = new Score(request.getValue(), request.getDate(), player);
        this.scoreRepository.save(score);
        return score;
    }

    public List<Score> findTopScoresLimitedBy(Integer limit) {
        return this.scoreRepository.findTopScores(limit);
    }

    public List<Score> findTopScoresForPlayerLimitedBy(Integer limit, Long playerId) {
        return this.scoreRepository.findTopScoresForPlayer(limit, playerId);
    }
}
