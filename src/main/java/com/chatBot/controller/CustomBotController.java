package com.chatBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.chatBot.dto.ChatGPTRequest;
import com.chatBot.dto.ChatGptResponse;
import com.chatBot.dto.ChatbotRequestDTO;
import com.chatBot.service.ChatBotservice;
import reactor.core.publisher.Flux;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response; 

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

    @Autowired
    private ChatBotservice chatBotservice;
    
    @PostMapping("/chatAssistant")
    public String chat(@RequestBody ChatbotRequestDTO chatbotRequestDTO){
        ChatGPTRequest request=new ChatGPTRequest(model, chatbotRequestDTO.getPrompt());
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
    
@PostMapping("/chatAssistantStream")
public void getChatStreaming(@RequestBody ChatbotRequestDTO chatbotRequestDTO) {
    String prompt = chatbotRequestDTO.getPrompt();
    chatBotservice.generateResponse(prompt, new StreamingResponseHandler<AiMessage>() {

        @Override
        public void onNext(String token) {
            System.out.print(token); // This could be sent to the client as a Server-Sent Event
        }

        @Override
        public void onComplete(Response<AiMessage> response) {
            System.out.println("\n\nDone streaming");
            // Optionally, you can send a response to the client here
        }

        @Override
        public void onError(Throwable error) {
            System.out.println("Something went wrong: " + error.getMessage());
            // Optionally, you can send an error response to the client here
        }
    });
}

@PostMapping("/chatBotStreaming")
public Flux<ServerSentEvent<String>> chatBotStreaming(@RequestBody ChatbotRequestDTO chatbotRequestDTO) {
    
    return chatBotservice.generateResponseOnScreen(chatbotRequestDTO.getPrompt());
}

}