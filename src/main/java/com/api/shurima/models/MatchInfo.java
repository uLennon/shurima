package com.api.shurima.models;

import lombok.Data;

import java.util.List;

@Data
public class MatchInfo {
    private List<SummonerMatch> summonerMatchList;
    private int team;

}
