package com.api.shurima.modelsDTO;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InfoDTO {
    private String gameMode;
    private ArrayList<ParticipantDTO> participants;
    private int gameDuration;
}
