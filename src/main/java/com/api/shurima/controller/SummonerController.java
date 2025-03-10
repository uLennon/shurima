package com.api.shurima.controller;

import com.api.shurima.modelsDTO.AccountDTO;
import com.api.shurima.modelsDTO.HistoricDTO;
import com.api.shurima.modelsDTO.ProfileDTO;
import com.api.shurima.modelsDTO.SummonerDTO;
import com.api.shurima.service.AccountService;
import com.api.shurima.service.MatchService;
import com.api.shurima.service.SummonerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class SummonerController {

    @Autowired
    SummonerService summonerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MatchService matchService;

    @GetMapping("/lol/{nameTag}")
    @Cacheable("invocador")
    public ModelAndView showInfo(@PathVariable String nameTag) {
        ModelAndView mv = new ModelAndView("histor");
        try {
            String[] split = nameTag.split("-");
            String name = split[0];
            String tagName =  split[1];

            AccountDTO account = accountService.gera(name, tagName);
            SummonerDTO summoner = summonerService.getSummoner(account.getPuuid());
            ProfileDTO profileDTO = summonerService.buildProfile(summoner,account.getGameName());
            List<HistoricDTO> historicList = matchService.buildSummoners(account.getPuuid());

            mv.addObject("profiles", profileDTO.getListRank());
            mv.addObject("summoner", profileDTO);
            mv.addObject("historic", historicList);
            return mv;

        }catch (ArrayIndexOutOfBoundsException e){
            mv.addObject("errorMessage", "Invocador não foi encontrado. Por favor, verifique a ortografia e tente novamente!");
            return mv;
        }

        catch (HttpClientErrorException.NotFound e){
            e.printStackTrace();
            mv.addObject("errorMessage", "Invocador não foi encontrado. Por favor, verifique a ortografia e tente novamente!");
            return mv;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/lol/{nameTag}")
    public String redirectPageIndex(@RequestParam String nameTag){
        return "redirect:/lol/" + nameTag.replaceFirst("#","-");
    }

}
