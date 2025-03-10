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
    @Value("${URL_BOOST}")
    private String urlBoost;
    @Value("${URL_EXHAUST}")
    private String urlExhaust;
    @Value("${URL_FLASH}")
    private String urlFlash;
    @Value("${URL_HASTE}")
    private String urlHaste;
    @Value("${URL_HEAL}")
    private String urlHeal;
    @Value("${URL_SMITE}")
    private String urlSmite;
    @Value("${URL_TELEPORT}")
    private String urlTeleport;
    @Value("${URL_MANA}")
    private String urlMana;
    @Value("${URL_DOT}")
    private String urlDot;
    @Value("${URL_BARRIER}")
    private String urlBarrier;
    @Value("${URL_SNOWBALL}")
    private String urlSnowball;
    @Value("${URL_SNOW_URF}")
    private String urlSnowUrf;
    @Value("${URL_ULT_BOOK}")
    private String urlUltBook;


    public AccountDTO getAccoutDTOMapper(String url) {
        return new RestTemplate().getForObject(url, AccountDTO.class);
    }

    public String getSpeel(int option) {
        switch (option) {
            case 1: return urlBoost;
            case 3: return urlExhaust;
            case 4: return urlFlash;
            case 6: return urlHaste;
            case 7: return urlHeal;
            case 11: return urlSmite;
            case 12: return urlTeleport;
            case 13: return urlMana;
            case 14: return urlDot;
            case 21: return urlBarrier;
            case 32: return urlSnowball;
            case 39: return urlSnowUrf;
            default: return urlUltBook;
        }
    }
}
