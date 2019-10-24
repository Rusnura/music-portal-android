package org.tumasov.rmusicplayer.api;

import org.tumasov.rmusicplayer.helpers.WebRequest;

public class APIController {
    private final String API_URL_ADDRESS;
    private WebRequest webRequest = new WebRequest();

    public APIController(String apiAddress) {
        this.API_URL_ADDRESS = apiAddress;
    }


}
