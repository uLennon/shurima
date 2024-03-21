package com.api.shurima.modelsDTO;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDTO {

    private String name;
    private int level;
    private String iconeProfile;

    private List<RankDTO> listRank;

}


