package com.api.shurima.service;

import com.api.shurima.models.SummonerMatch;
import com.api.shurima.modelsDTO.HistoricDTO;
import com.api.shurima.modelsDTO.MatchDTO;
import com.api.shurima.modelsDTO.ParticipantDTO;
import com.api.shurima.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private ApiUtil apiUtil;

    @Value("${URL_MATCHS}")
    private String URL_MATCHS;
    @Value("${URL_ID_MATCH}")
    private String URL_ID_MATCH;
    @Value("${URL_SPLASH_ART}")
    private String URL_SPLASH_ART;
    @Value("${URL_ICON_CHAMPION}")
    private String URL_ICON_CHAMPION;


    private List<String> getIdsMatch(String puuid) {
        String param = "/ids?start=0&count=10&api_key=";
        return new RestTemplate().getForEntity(URL_MATCHS + puuid + param + apiUtil.getAPI_KEY(), List.class).getBody();
    }

    private List<MatchDTO> listMatchs(String puuid){
        return getIdsMatch(puuid).stream().map(matchDTO -> new RestTemplate().getForObject(URL_ID_MATCH + matchDTO + "?api_key=" + apiUtil.getAPI_KEY(), MatchDTO.class)).collect(Collectors.toList());
    }

    public List<HistoricDTO> buildSummoners(String puuid){
        return listMatchs(puuid).stream().map(match -> {
            HistoricDTO historicDTO = new HistoricDTO();
            List<SummonerMatch> summonerMatchList = match.getInfo().getParticipants().stream()
                    .map(participant -> buildParticipant(puuid, participant, match, historicDTO)).collect(Collectors.toList());
            historicDTO.setSummonerMatchList(summonerMatchList);
            return historicDTO;
        }).collect(Collectors.toList());
    }
    private SummonerMatch buildParticipant(String puuid, ParticipantDTO participant, MatchDTO matchDTO, HistoricDTO historic) {
        SummonerMatch summonerMatch = new SummonerMatch();
        if (puuid.equals(participant.getPuuid())) {
            historic.setName(participant.getChampionName());
            historic.setQueue(matchDTO.getInfo().getGameMode());
            historic.setResult(participant.getResult());
            historic.setKda(participant);
            historic.setFarm(participant.getTotalMinionsKilled());
            historic.setImageChampion(URL_SPLASH_ART + participant.getChampionId() + "/" + participant.getChampionId() + "000.jpg");
            historic.setTime(historic.getDurationMatch(matchDTO.getInfo().getGameDuration()));
        }
        summonerMatch.setName(participant.getSummonerName());
        summonerMatch.setLevel(participant.getChampLevel());
        summonerMatch.setResultMatch(participant.getResult());
        summonerMatch.setFarm(participant.getTotalMinionsKilled());
        summonerMatch.setImageIconProfile(URL_ICON_CHAMPION + "/" + participant.getChampionId() + ".png");
        summonerMatch.setImageIconSpeel1(apiUtil.getSpeel(participant.getSummoner1Id()));
        summonerMatch.setImageIconSpeel2(apiUtil.getSpeel(participant.getSummoner2Id()));
        summonerMatch.setKda(participant.getKills() + "/" + participant.getDeaths() + "/" + participant.getAssists());
        return  summonerMatch;
    }

}
