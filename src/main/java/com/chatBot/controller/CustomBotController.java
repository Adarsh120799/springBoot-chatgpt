package com.chatBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chatBot.dto.ChatGPTRequest;
import com.chatBot.dto.ChatGptResponse;
import com.chatBot.dto.ChatbotRequestDTO;

@CrossOrigin
@RestController
@RequestMapping("/api/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/chatAssistant")
    public String chat(@RequestBody ChatbotRequestDTO chatbotRequestDTO){
        ChatGPTRequest request=new ChatGPTRequest(model, chatbotRequestDTO.getPrompt());
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}