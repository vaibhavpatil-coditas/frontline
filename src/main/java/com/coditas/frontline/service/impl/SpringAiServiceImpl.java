package com.coditas.frontline.service.impl;

import com.coditas.frontline.dto.request.SpringAiRequest;
import com.coditas.frontline.service.SpringAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringAiServiceImpl implements SpringAiService {
    private final ChatClient chatClient;
    @Override
    public String testAi(SpringAiRequest request) {
        String content = chatClient.prompt(request.getTopic())
                .call()
                .content();
        log.info("Ai response: {}", content);
        return content;
    }
}
