package com.api.shurima.service;

import com.api.shurima.models.CurrentGameInfo;
import com.api.shurima.modelsDTO.LeagueEntryDTO;
import com.api.shurima.modelsDTO.ProfileDTO;
import com.api.shurima.modelsDTO.RankDTO;
import com.api.shurima.modelsDTO.SummonerDTO;
import com.api.shurima.utils.ApiUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummonerService {
    @Autowired
    private ApiUtil apiUtil;
    @Value("${URL_SUMMONER_ACCOUNT}")
    private String URL_SUMMONER_ACCOUNT;
    @Value("${URL_ICON_PROFILE}")
    private String URL_ICON_PROFILE;
    @Value("${URL_RANK}")
    private String URL_RANK;
    @Value("${URL_GAMECURRENT}")
    private String URL_GAMECURRENT;

    @Value("${URL_ICON_RANK}")
    private String URL_ICON_RANK;

    public SummonerDTO getSummoner(String puuid) {
        return new RestTemplate().getForObject(URL_SUMMONER_ACCOUNT + puuid + "?api_key=" + apiUtil.getAPI_KEY(), SummonerDTO.class);
    }

    public List<LeagueEntryDTO> getRankSummoner(SummonerDTO summoner) {
        String URL = URL_RANK + summoner.getId() + "?api_key=" + apiUtil.getAPI_KEY();
        return new RestTemplate().exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<LeagueEntryDTO>>(){}).getBody();
    }

    public ProfileDTO buildProfile(SummonerDTO summoner)  {
        List<LeagueEntryDTO> league = getRankSummoner(summoner);
        ProfileDTO profile = new ProfileDTO();
        List<RankDTO> listRank = league.stream().map(list -> new ModelMapper().map(list, RankDTO.class)).map(rank -> setWinrateRankUrl(rank)).collect(Collectors.toList());

        profile.setName(summoner.getName());
        profile.setIconeProfile(URL_ICON_PROFILE + summoner.getProfileIconId() + ".jpg");
        profile.setLevel(summoner.getSummonerLevel());
        profile.setListRank(listRank);

        return profile;
    }
    public RankDTO setWinrateRankUrl(RankDTO rank){
        rank.convertRank(rank.getRank());
        rank.setWinrate();
        return rank.toBuilder().iconeRank(URL_ICON_RANK + rank.getTier().toLowerCase() + ".png").build();
    }
    public CurrentGameInfo getCurrentGameInfo(SummonerDTO summoner) {
        return new RestTemplate().getForObject(URL_GAMECURRENT+summoner.getId() +"?api_key=" + apiUtil.getAPI_KEY(), CurrentGameInfo.class);
    }
}
