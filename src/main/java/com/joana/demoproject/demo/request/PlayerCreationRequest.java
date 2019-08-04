package com.joana.demoproject.demo.request;
import javax.validation.constraints.NotBlank;

public class PlayerCreationRequest {

    @NotBlank
    private String nickname;

    public PlayerCreationRequest(){}

    public PlayerCreationRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
