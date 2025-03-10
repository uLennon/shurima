package com.api.shurima.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummonerMatch {
    private String name;
    private int level;
    private String imageIconSpeel1;
    private String imageIconSpeel2;
    private String kda;
    private int farm;
    private String imageIconProfile;
    private String resultMatch;
    private String linkProfile;
    private String item0;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;
}
