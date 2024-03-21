package com.api.shurima.service;

import com.api.shurima.modelsDTO.AccountDTO;
import com.api.shurima.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    @Autowired
    private ApiUtil apiUtil;

    @Value(value = "${API_KEY}")
    private String API_KEY;

    @Value("${URL_ACCOUNT}")
    private String URL_ACCOUNT;

    public AccountDTO gera(String name, String tagName) {
        return apiUtil.getAccoutDTOMapper( URL_ACCOUNT + name + "/" + tagName + "?api_key=" + API_KEY);
    }

}
