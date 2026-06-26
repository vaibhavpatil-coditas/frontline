package com.coditas.frontline.controller;

import com.coditas.frontline.constants.ApiPaths;
import com.coditas.frontline.dto.request.SpringAiRequest;
import com.coditas.frontline.dto.response.ApplicationResponse;
import com.coditas.frontline.service.SpringAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.BASE_PATH)
@RequiredArgsConstructor
public class SpringAiController {

    private final SpringAiService springAiService;

    @GetMapping(ApiPaths.SpringAi.TEST)
    public ResponseEntity<ApplicationResponse<String>> testAi(@RequestBody SpringAiRequest request) {
        String response = springAiService.testAi(request);
        return ResponseEntity.ok(ApplicationResponse.success(response, "Generated message"));
    }
}
