package com.capstone.mind.backend.controller;

import com.capstone.mind.backend.services.ExternalApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/external-data")
    public String getExternalData(@RequestParam(defaultValue = "false") boolean fail) {
        return externalApiService.callExternalApi(fail);
    }
}
