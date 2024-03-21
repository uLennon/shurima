package com.api.shurima.models;

import lombok.Data;

@Data
public class BannedChampion {

    private int pickTurn;
    private long championId;
    private long teamId;
}
