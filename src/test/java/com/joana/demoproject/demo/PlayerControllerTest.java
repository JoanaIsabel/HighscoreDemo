package com.joana.demoproject.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joana.demoproject.demo.entitiy.Player;
import com.joana.demoproject.demo.repository.PlayerRepository;
import com.joana.demoproject.demo.request.PlayerCreationRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void testCreatePlayerExpects201()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerCreationRequest playerCreationRequest = new PlayerCreationRequest("Joe");
        String jsonRequest = objectMapper.writeValueAsString(playerCreationRequest);

        mockMvc.perform(put("/player")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nickname").value("Joe"));
    }

    @Test
    public void testCreatePlayerWithEmptyJsonExpects400()
            throws Exception {
        String emptyJson = "{}";

        mockMvc.perform(put("/player")
                .contentType(MediaType.APPLICATION_JSON).content(emptyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePlayerWithNoNicknameInJsonExpects400()
            throws Exception {
        String emptyJson = "{\"nickname\":\"\"}";

        mockMvc.perform(put("/player")
                .contentType(MediaType.APPLICATION_JSON).content(emptyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPlayerWithValidIdExpects200()
            throws Exception {

        Player player = new Player("Bob");
        this.playerRepository.save(player);

        mockMvc.perform(get("/player/{playerId}", player.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.nickname").value(player.getNickname()));
    }

    @Test
    public void testGetPlayerWithNoValidIdExpects404()
            throws Exception {

        long playerId = 10;

        mockMvc.perform(get("/player/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPlayerWithNonNumberIdExpects400()
            throws Exception {

        String playerId = "NaN";

        mockMvc.perform(get("/player/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeNicknameExpects200()
            throws Exception {

        Player player = new Player("Hugo");
        this.playerRepository.save(player);

        ObjectMapper objectMapper = new ObjectMapper();
        String newNickname = "Thorsten";
        PlayerCreationRequest playerCreationRequest = new PlayerCreationRequest(newNickname);
        String jsonRequest = objectMapper.writeValueAsString(playerCreationRequest);

        mockMvc.perform(post("/player/{playerId}", player.getId())
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.nickname").value(newNickname));
    }

    @Test
    public void testChangeNicknameNoValidPlayerIdExpects404()
            throws Exception {

        long notExistingPlayerId = 1202;
        ObjectMapper objectMapper = new ObjectMapper();
        String newNickname = "Thorsten";
        PlayerCreationRequest playerCreationRequest = new PlayerCreationRequest(newNickname);
        String jsonRequest = objectMapper.writeValueAsString(playerCreationRequest);

        mockMvc.perform(post("/player/{playerId}", notExistingPlayerId)
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangeNicknameWithMissingJsonBodyExpects400()
            throws Exception {

        long playerId = 1202;
        mockMvc.perform(post("/player/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeNicknameWithEmptyNicknameInJsonExpects400()
            throws Exception {

        Player player = new Player("Jorg");
        this.playerRepository.save(player);
        String emptyJson = "{\"nickname\":\"\"}";

        mockMvc.perform(post("/player/{playerId}", player.getId())
                .contentType(MediaType.APPLICATION_JSON).content(emptyJson))
                .andExpect(status().isBadRequest());
    }
}
