package com.chatBot.service;

import org.springframework.http.codec.ServerSentEvent;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import reactor.core.publisher.Flux;

public interface ChatBotservice {

	void generateResponse(String prompt, StreamingResponseHandler<AiMessage> streamingResponseHandler);

	Flux<ServerSentEvent<String>> generateResponseOnScreen(String prompt);

}
