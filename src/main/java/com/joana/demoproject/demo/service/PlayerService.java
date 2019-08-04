package com.joana.demoproject.demo.service;

import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    public static final String GUEST_NICKNAME = "guest";

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> findById (Long playerId) {
        return this.playerRepository.findById(playerId);
    }

    public Player createPlayer(String nickname) {
        Player player = new Player(nickname);
        this.playerRepository.save(player);
        return player;
    }

    public Player createGuestPlayer(){
        return createPlayer(GUEST_NICKNAME);
    }

    public Optional<Player> updateNickname(Long playerId, String newNickname) {
        Optional<Player> optionalPlayer = this.playerRepository.findById(playerId);
        optionalPlayer.ifPresent(player -> {
            player.setNickname(newNickname);
            this.playerRepository.save(player);
        });
        return optionalPlayer;
    }
}
