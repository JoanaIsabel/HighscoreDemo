package com.joana.demoproject.demo.repository;

import com.joana.demoproject.demo.entitiy.Score;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ScoreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save (Score score){
        entityManager.persist(score);
        entityManager.flush();
    }

    public List<Score> findAllByPlayerId (Long playerId) {
        return entityManager.createQuery("SELECT s FROM Score s WHERE s.player = :playerId")
                        .setParameter("playerId",playerId)
                        .getResultList();
    }

    public List<Score> findTopScores (@Param("limit")Integer limit) {
        Query query = entityManager.createQuery("SELECT s FROM Score s ORDER BY value DESC");
        if (limit != null){
            query.setMaxResults(limit);
        }
        return query.getResultList();

    }

    public List<Score> findTopScoresForPlayer(@Param("limit") Integer limit, @Param("playerId") Long playerId) {
        Query query = entityManager.createQuery("SELECT s FROM Score s WHERE s.player.id = :playerId ORDER BY value DESC");
        if (limit != null){
            query.setMaxResults(limit);
        }
        return query.setParameter("playerId", playerId).getResultList();
    }
}
