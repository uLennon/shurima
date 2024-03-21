package com.api.shurima.models;

import lombok.Data;

import java.util.List;

@Data
public class Perks {
    private List<Long> perkIds;
    private long perkStyle;
    private long perkSubStyle;
}
