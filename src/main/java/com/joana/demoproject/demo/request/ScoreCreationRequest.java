package com.joana.demoproject.demo.request;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ScoreCreationRequest {

    @NotNull
    private Integer value;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @NotNull
    private Date date;

    private Long playerId;

    public ScoreCreationRequest() {
    }

    public ScoreCreationRequest(Integer value, Date date, Long playerId) {
        this.value = value;
        this.date = date;
        this.playerId = playerId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

}
