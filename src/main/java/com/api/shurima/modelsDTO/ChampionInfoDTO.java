package com.api.shurima.modelsDTO;

import lombok.Data;

import java.util.List;

@Data
public class ChampionInfoDTO {
    private int maxNewPlayerLevel;
    private List<Integer> freeChampionIdsForNewPlayers;
    private List<Integer> freeChampionIds;

}
