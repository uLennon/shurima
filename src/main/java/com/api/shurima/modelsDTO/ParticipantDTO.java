package com.api.shurima.modelsDTO;

import lombok.Data;

@Data
public class ParticipantDTO {
    private ChallengesDTO challenges;
    private int championId;
    private String championName;
    private int assists;
    private int deaths;
    private int kills;
    private String puuid;
    private String summonerName;
    private int totalMinionsKilled;
    private boolean win;
    private int champLevel;
    private int summoner1Id;
    private int summoner2Id;

    public String getResult() {
        if (this.win) {
            return "WIN";
        }
        return "LOSE";
    }
}
