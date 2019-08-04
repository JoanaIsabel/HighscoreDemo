package com.joana.demoproject.demo.entitiy;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Score {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer value;

    @JsonFormat(pattern = "dd-MM-YYYY HH:mm")
    @NotNull
    private Date date;

    @ManyToOne
    @NotNull
    private Player player;

    public Score() {
    }

    public Score(Integer value, Date date, Player player) {
        this.value = value;
        this.date = date;
        this.player = player;
    }

    public Long getId() {
        return id;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
