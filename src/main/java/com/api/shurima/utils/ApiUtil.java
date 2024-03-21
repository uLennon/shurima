package com.api.shurima.utils;

import com.api.shurima.modelsDTO.AccountDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
public class ApiUtil {

    @Value("${API_KEY}")
    private String API_KEY;


    public AccountDTO getAccoutDTOMapper(String url) {
        return new RestTemplate().getForObject(url, AccountDTO.class);
    }

    public String getSpeel(int option){

        if(option == 1){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_boost.png";
        }
        if(option == 3){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_exhaust.png";
        }
        if(option == 4){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_flash.png";
        }
        if(option == 6){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_haste.png";
        }
        if(option == 7){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_heal.png";
        }
        if(option == 11){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_smite.png";
        }
        if(option == 12){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_teleport.png";
        }
        if(option == 13){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summonermana.png";
        }
        if(option == 14){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summonerignite.png";
        }
        if(option == 21){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summonerbarrier.png";
        }
        if(option == 32){
            return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_mark.png";
        }
        return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/summoner_empty.png";
    }

}
