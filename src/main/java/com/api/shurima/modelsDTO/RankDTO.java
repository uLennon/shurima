package com.api.shurima.modelsDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RankDTO {


    private int wins;
    private int losses;
    private int leaguePoints;
    private String queue;
    private String rank;
    private String tier;
    private String iconeRank;
    private int winrate;

    public void setWinrate(){
        this.winrate = (int) (100 * (this.wins / ((double) this.wins + this.losses)));
    }

    public void convertRank(String rank){
        if(rank.equals("RANKED_FLEX_SR")){
            this.queue = "RANKED FLEX";
        }
        if (rank.equals("RANKED_SOLO_5x5")){
            this.queue = "RANKED SOLO/DUO";
        }
    }
}
