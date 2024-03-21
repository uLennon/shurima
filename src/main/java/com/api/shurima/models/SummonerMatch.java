package com.api.shurima.models;

import lombok.Data;

@Data
public class SummonerMatch {
    private String name;
    private int level;
    private String imageIconSpeel1;
    private String imageIconSpeel2;
    private String kda;
    private int farm;
    private String imageIconProfile;
    private String resultMatch;
}
