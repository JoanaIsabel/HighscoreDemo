package com.joana.demoproject.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.entitiy.Score;
import com.joana.demoproject.demo.repository.PlayerRepository;
import com.joana.demoproject.demo.repository.ScoreRepository;
import com.joana.demoproject.demo.request.GetScoresRequest;
import com.joana.demoproject.demo.request.ScoreCreationRequest;
import com.joana.demoproject.demo.service.PlayerService;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    private Player setUpPlayer(String nickname) {
        Player player = new Player(nickname);
        this.playerRepository.save(player);
        return player;
    }

    private Score setUpScore(int value, Player player) {
        Date date = new Date();
        Score scorePlayer = new Score(value, date, player);
        this.scoreRepository.save(scorePlayer);
        return scorePlayer;
    }

    private String getJsonBodyForGetScoreRequest(int limit) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        GetScoresRequest getScoresRequest = new GetScoresRequest(limit);
        return objectMapper.writeValueAsString(getScoresRequest);
    }

    private String getJsonBodyForScoreCreationRequest(int value, long playerId, boolean setPlayerId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ScoreCreationRequest scoreCreationRequest = new ScoreCreationRequest();
        scoreCreationRequest.setDate(new Date());
        scoreCreationRequest.setValue(value);
        if(setPlayerId) {
            scoreCreationRequest.setPlayerId(playerId);
        }
        return objectMapper.writeValueAsString(scoreCreationRequest);
    }

    @Test
    public void testGetGlobalScoresWithNoLimitExpects200() throws Exception {
        int lowerScore = 100;
        int higherScore = 111;

        Score scorePlayer = this.setUpScore(lowerScore, this.setUpPlayer("Bjoern"));
        Score guestPlayerScore = this.setUpScore(higherScore, this.setUpPlayer("Guest"));

        String jsonRequest = "{}";

        mockMvc.perform(get("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(guestPlayerScore.getId()))
                .andExpect(jsonPath("$[0].value").value(guestPlayerScore.getValue()))
                .andExpect(jsonPath("$[0].player.id").value(guestPlayerScore.getPlayer().getId()))
                .andExpect(jsonPath("$[0].player.nickname").value(guestPlayerScore.getPlayer().getNickname()))
                .andExpect(jsonPath("$[1].id").value(scorePlayer.getId()))
                .andExpect(jsonPath("$[1].value").value(scorePlayer.getValue()))
                .andExpect(jsonPath("$[1].player.id").value(scorePlayer.getPlayer().getId()))
                .andExpect(jsonPath("$[1].player.nickname").value(scorePlayer.getPlayer().getNickname()));
    }

    @Test
    public void testGetPlayerScoresWithNoLimitExpects200() throws Exception {
        int lowerScore = 100;
        int higherScore = 111;

        Player player = this.setUpPlayer("Joana");
        Player guest = this.setUpPlayer("guest");

        Score scorePlayer = this.setUpScore(lowerScore, player);
        Score guestPlayerScore = this.setUpScore(higherScore, guest);

        String jsonRequest = "{}";

        mockMvc.perform(get("/score/{playerId}", player.getId())
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(scorePlayer.getId()))
                .andExpect(jsonPath("$[0].value").value(scorePlayer.getValue()))
                .andExpect(jsonPath("$[0].player.id").value(scorePlayer.getPlayer().getId()))
                .andExpect(jsonPath("$[0].player.nickname").value(scorePlayer.getPlayer().getNickname()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    public void testGetPlayerScoresWithLimit1Expects200() throws Exception {
        int lowerScore = 100;
        int higherScore = 111;
        int limit = 1;
        Player player = this.setUpPlayer("Joana");
        Player guest = this.setUpPlayer("guest");

        Score scorePlayer = this.setUpScore(higherScore, player);
        Score score2Player = this.setUpScore(lowerScore, player);
        Score guestPlayerScore = this.setUpScore(higherScore, guest);

        String jsonRequest = this.getJsonBodyForGetScoreRequest(limit);

        mockMvc.perform(get("/score/{playerId}", player.getId())
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(limit)))
                .andExpect(jsonPath("$[0].id").value(scorePlayer.getId()))
                .andExpect(jsonPath("$[0].value").value(scorePlayer.getValue()))
                .andExpect(jsonPath("$[0].player.id").value(scorePlayer.getPlayer().getId()))
                .andExpect(jsonPath("$[0].player.nickname").value(scorePlayer.getPlayer().getNickname()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    public void testGetGlobalScoresWithLimit2Expects200() throws Exception {
        int lowerScore = 100;
        int midScore = 105;
        int higherScore = 111;
        int limit = 2;
        Player player = this.setUpPlayer("Joana");
        Player guest = this.setUpPlayer("guest");

        Score scorePlayer = this.setUpScore(midScore, player);
        Score score2Player = this.setUpScore(lowerScore, player);
        Score guestPlayerScore = this.setUpScore(higherScore, guest);

        String jsonRequest = this.getJsonBodyForGetScoreRequest(limit);

        mockMvc.perform(get("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(limit)))
                .andExpect(jsonPath("$[0].id").value(guestPlayerScore.getId()))
                .andExpect(jsonPath("$[0].value").value(guestPlayerScore.getValue()))
                .andExpect(jsonPath("$[0].player.id").value(guestPlayerScore.getPlayer().getId()))
                .andExpect(jsonPath("$[0].player.nickname").value(guestPlayerScore.getPlayer().getNickname()))
                .andExpect(jsonPath("$[1].id").value(scorePlayer.getId()))
                .andExpect(jsonPath("$[1].value").value(scorePlayer.getValue()))
                .andExpect(jsonPath("$[1].player.id").value(scorePlayer.getPlayer().getId()))
                .andExpect(jsonPath("$[1].player.nickname").value(scorePlayer.getPlayer().getNickname()));
    }

    @Test
    public void testPutScoreWithPlayerIdExpects201() throws Exception {
        int highScore = 100;
        Player player = this.setUpPlayer("Joana");

        String jsonRequest = this.getJsonBodyForScoreCreationRequest(highScore, player.getId(), true);

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.value").value(highScore))
                .andExpect(jsonPath("$.player.id").value(player.getId()))
                .andExpect(jsonPath("$.player.nickname").value(player.getNickname()));
    }

    @Test
    public void testPutScoreWithoutPlayerIdExpects201() throws Exception {
        int highScore = 100;
        String jsonRequest = this.getJsonBodyForScoreCreationRequest(highScore, 0, false);

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.value").value(highScore))
                .andExpect(jsonPath("$.player.id").exists())
                .andExpect(jsonPath("$.player.nickname").value(PlayerService.GUEST_NICKNAME));
    }

    @Test
    public void testPutScoreWithoutValueOrDateExpects400() throws Exception {
        String jsonRequest = "{}";

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutScoreWithoutValueExpects400() throws Exception {
        String jsonRequest = "{\"date\": \"08.06.2019 15:11\"}";

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutScoreWithoutDateExpects400() throws Exception {
        String jsonRequest = "{\"value\": 100}";

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutScoreWithNonExistentPlayerExpects404() throws Exception {
        int highScore = 100;
        String jsonRequest = this.getJsonBodyForScoreCreationRequest(highScore, 20, true);

        mockMvc.perform(put("/score")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPlayerScoreWithNonExistentPlayerExpects404() throws Exception {
        int playerId = 20;
        String jsonRequest = this.getJsonBodyForGetScoreRequest(1);

        mockMvc.perform(get("/score/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isNotFound());
    }
}


