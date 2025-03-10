package com.api.shurima.service;

import com.api.shurima.models.SummonerMatch;
import com.api.shurima.modelsDTO.GameModeDTO;
import com.api.shurima.modelsDTO.HistoricDTO;
import com.api.shurima.modelsDTO.MatchDTO;
import com.api.shurima.modelsDTO.ParticipantDTO;
import com.api.shurima.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Value("${URL_MATCHS}")
    private String URL_MATCHS;
    @Value("${URL_ID_MATCH}")
    private String URL_ID_MATCH;
    @Value("${URL_SPLASH_ART}")
    private String URL_SPLASH_ART;
    @Value("${URL_ICON_CHAMPION}")
    private String URL_ICON_CHAMPION;
    @Value("${URL_API_MAP}")
    private String URL_API_MAP;
    @Value("${URL_ITEMS}")
    private String URL_ITEMS;

    private final RestTemplate restTemplate;
    private final ApiUtil apiUtil;

    public MatchService(ApiUtil apiUtil) {
        this.restTemplate = new RestTemplate();
        this.apiUtil = apiUtil;
    }

    private List<String> getIdsMatch(String puuid) {
        String param = "/ids?start=0&count=10&api_key=";
        return restTemplate.getForEntity(URL_MATCHS + puuid + param + apiUtil.getAPI_KEY(), List.class).getBody();
    }

    public List<MatchDTO> listMatchs(String puuid) {
        List<String> idsMatch = getIdsMatch(puuid);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            List<CompletableFuture<MatchDTO>> futures = idsMatch.stream()
                    .map(matchId -> CompletableFuture.supplyAsync(() -> makeApiCall(matchId), executorService))
                    .collect(Collectors.toList());

            return futures.stream()
                    .map(this::getFutureResult)
                    .collect(Collectors.toList());

        } finally {
            executorService.shutdown();
        }
    }

    private MatchDTO makeApiCall(String matchId) {
        try {
            return restTemplate.getForObject(URL_ID_MATCH + matchId + "?api_key=" + apiUtil.getAPI_KEY(), MatchDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API para o Match ID: " + matchId, e);
        }
    }

    private MatchDTO getFutureResult(CompletableFuture<MatchDTO> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao obter resultado da execução assíncrona", e);
        }
    }

    public List<HistoricDTO> buildSummoners(String puuid) throws InterruptedException, ExecutionException {
        List<MatchDTO> matches = listMatchs(puuid);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Future<HistoricDTO>> futures = matches.stream()
                .map(match -> executorService.submit(() -> {
                    HistoricDTO historicDTO = new HistoricDTO();
                    List<SummonerMatch> summonerMatchList = match.getInfo().getParticipants().stream()
                            .map(participant -> buildParticipant(puuid, participant, match, historicDTO))
                            .collect(Collectors.toList());
                    historicDTO.setSummonerMatchList(summonerMatchList);
                    return historicDTO;
                }))
                .toList();

        List<HistoricDTO> historicDTOs = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        executorService.shutdown();
        return historicDTOs;
    }

    private SummonerMatch buildParticipant(String puuid, ParticipantDTO participant, MatchDTO matchDTO, HistoricDTO historic) {
        int totalFarm = getTotalFarm(participant);
        String score = getScoreFormat(participant);
        if (puuid.equals(participant.getPuuid())) {
            buildHistoricParticipant(historic,participant,matchDTO,score,totalFarm);
        }
        setProfileUrl(participant);

        return SummonerMatch.builder().linkProfile(participant.getUrlForProfile())
                .name(participant.getRiotIdGameName())
                .level(participant.getChampLevel())
                .resultMatch(participant.getResult())
                .farm(totalFarm)
                .item0(getUrlItem(participant.getItem0()))
                .item1(getUrlItem(participant.getItem1()))
                .item2(getUrlItem(participant.getItem2()))
                .item3(getUrlItem(participant.getItem3()))
                .item4(getUrlItem(participant.getItem4()))
                .item5(getUrlItem(participant.getItem5()))
                .item6(getUrlItem(participant.getItem6()))
                .imageIconProfile(URL_ICON_CHAMPION + participant.getChampionName() + "_0.jpg")
                .imageIconSpeel1(apiUtil.getSpeel(participant.getSummoner1Id()))
                .imageIconSpeel2(apiUtil.getSpeel(participant.getSummoner2Id()))
                .kda(score).build();
    }

    private void setProfileUrl(ParticipantDTO participant) {
        participant.setUrlForProfile(participant.getRiotIdGameName()+"-"+ participant.getRiotIdTagline());
    }

    private String getScoreFormat(ParticipantDTO participant) {
        return participant.getKills() + "/" + participant.getDeaths() + "/" + participant.getAssists();
    }

    private  int getTotalFarm(ParticipantDTO participant) {
        return participant.getNeutralMinionsKilled()+ participant.getTotalMinionsKilled();
    }

    private void buildHistoricParticipant(HistoricDTO historicDTO, ParticipantDTO participant,MatchDTO matchDTO, String score, int totalFarm) {
        historicDTO.setName(participant.getChampionName());
        historicDTO.setQueue(gameMode(matchDTO.getInfo().getQueueId()));
        historicDTO.setResult(participant.getResult());
        historicDTO.setFarm(totalFarm);
        historicDTO.setFreg(score);
        historicDTO.setImageChampion(URL_ICON_CHAMPION + participant.getChampionName() + "_0.jpg");
        historicDTO.setTime(historicDTO.getDurationMatch(matchDTO.getInfo().getGameDuration()));
    }


    private String getUrlItem(int id){
        return (id == 0) ? URL_ITEMS + "9168.png.webp" : URL_ITEMS+id+".png.webp";
    }

    @Cacheable("mapas")
    public String gameMode(int queueId){
        ResponseEntity<List<GameModeDTO>> listMaps = restTemplate.exchange(
                URL_API_MAP,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<GameModeDTO> listGameMap = listMaps.getBody();
        Map<Integer, GameModeDTO> hashMap = listGameMap != null ?
                listGameMap.stream().collect(Collectors.toMap(
                        GameModeDTO::getQueueId,
                        gameMode -> gameMode
                )):
                Map.of();

        return nameQueue(hashMap.get(queueId));
    }

    private String nameQueue(GameModeDTO gameModeDTO){
        if(gameModeDTO == null){
            return "default";
        }
        if(!"Summoner's Rift".equals(gameModeDTO.getMap())){
            return gameModeDTO.getMap();
        }

        String ranked = "Ranked";
        String flex = "Flex";
        String solo = "Solo";

        if(gameModeDTO.getDescription().contains(ranked)){
            if(gameModeDTO.getDescription().contains(flex)){
                return "Flex";
            }
            if(gameModeDTO.getDescription().contains(solo)){
                return "Solo";
            }
        }
        return gameModeDTO.getDescription().split(" ")[0];
    }
}
