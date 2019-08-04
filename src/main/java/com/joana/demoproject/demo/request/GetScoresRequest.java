package com.joana.demoproject.demo.request;

public class GetScoresRequest {

    private Integer limit;

    public  GetScoresRequest () {}

    public GetScoresRequest(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
