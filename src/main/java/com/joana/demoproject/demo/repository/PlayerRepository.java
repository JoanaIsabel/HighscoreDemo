package com.joana.demoproject.demo.repository;

import com.joana.demoproject.demo.entitiy.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, String> {
    public Optional<Player> findById (Long playerId);
}
