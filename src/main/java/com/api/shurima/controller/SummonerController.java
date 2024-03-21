package com.api.shurima.controller;

import com.api.shurima.models.CurrentGameInfo;
import com.api.shurima.modelsDTO.AccountDTO;
import com.api.shurima.modelsDTO.HistoricDTO;
import com.api.shurima.modelsDTO.ProfileDTO;
import com.api.shurima.modelsDTO.SummonerDTO;
import com.api.shurima.service.AccountService;
import com.api.shurima.service.MatchService;
import com.api.shurima.service.SummonerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class SummonerController {

    @Autowired
    SummonerService summonerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MatchService matchService;

    @GetMapping("/{name}/{tagName}")
    public ModelAndView showInfo(@PathVariable String name, @PathVariable String tagName) throws JsonProcessingException {
        ModelAndView mv = new ModelAndView("page");

        try {
            AccountDTO account = accountService.gera(name, tagName);
            SummonerDTO summoner = summonerService.getSummoner(account.getPuuid());
            ProfileDTO profileDTO = summonerService.buildProfile(summoner);
            List<HistoricDTO> historicList = matchService.buildSummoners(account.getPuuid());

            mv.addObject("profiles", profileDTO.getListRank());
            mv.addObject("summoner", profileDTO);
            mv.addObject("historic", historicList);
            return mv;

        }catch (HttpClientErrorException.NotFound e){
            e.printStackTrace();
            mv.addObject("errorMessage", name+"#"+tagName +" não foi encontrado. Por favor, verifique a ortografia e tente novamente!");
            return mv;
            }
        }

    @PostMapping("/{name}/{tagName}")
    public String redirectPageIndex(@RequestParam String name, @RequestParam String tagName) throws JsonProcessingException {
        String nameEncoded = UriUtils.encodeQueryParam(name, StandardCharsets.UTF_8);
        String tagNameEncoded = UriUtils.encodeQueryParam(tagName, StandardCharsets.UTF_8);
        return "redirect:/" + nameEncoded + "/" + tagNameEncoded;
    }

    @GetMapping("/{name}/{tagName}/inLive")
    public ModelAndView inLive(@PathVariable String name, @PathVariable String tagName) throws JsonProcessingException {
        ModelAndView mv = new ModelAndView("inLive");
        try {
            AccountDTO account = accountService.gera(name, tagName);
            SummonerDTO summoner = summonerService.getSummoner(account.getPuuid());
            CurrentGameInfo currentGameInfo = summonerService.getCurrentGameInfo(summoner);
            mv.addObject("game",currentGameInfo);
            return mv;
    }catch (HttpClientErrorException.NotFound e){
            e.printStackTrace();
            mv.addObject("errorMessage","There is no live match.");
            return mv;
        }
    }
}
