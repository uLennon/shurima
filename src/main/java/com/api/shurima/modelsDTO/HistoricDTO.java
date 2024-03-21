package com.api.shurima.modelsDTO;

import com.api.shurima.models.SummonerMatch;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class HistoricDTO {
    private String name;
    private String queue;
    private String result;
    private double kda;
    private String time;
    private int farm;
    private String freg;
    private String imageChampion;
    private List<SummonerMatch> summonerMatchList;

    public String getWinOrLose(){
        if("WIN".equals(result))
            return "green";
        return "red";
    }

    public String getDurationMatch(int time) {
        int segundos = time;
        int minutos = segundos / 60;
        segundos %= 60;
        return LocalTime.of(0, minutos, segundos).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public void setKda(ParticipantDTO participant){
        double kda = (double) (participant.getAssists() + participant.getKills()) / participant.getDeaths();
        this.kda = Math.round(kda * 100.0) / 100.0;
    }

}
