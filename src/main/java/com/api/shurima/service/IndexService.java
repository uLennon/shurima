package com.api.shurima.service;

import com.api.shurima.modelsDTO.ChampionInfoDTO;
import com.api.shurima.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {
    @Autowired
    private ApiUtil apiUtil;
    @Value("${URL_ICON_CHAMP}")
    private String URL_ICON_CHAMP;
    @Value("${URL_FREEWEEK}")
    private String URL_FREEWEEK ;

    public List<Integer> getFreeWeek() {
        return new RestTemplate().getForObject(URL_FREEWEEK + apiUtil.getAPI_KEY(), ChampionInfoDTO.class).getFreeChampionIds();
    }
    public List<String> getUrlListIcon() {
        return getFreeWeek().stream().map(string -> URL_ICON_CHAMP + string + ".png").collect(Collectors.toList());
    }
}
