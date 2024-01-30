package com.chatBot.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.chatBot.service.ChatBotservice;

import dev.langchain4j.model.output.Response;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import reactor.core.publisher.Flux;

@Service
public class ChatBotServiceImpl implements ChatBotservice {

	@Value("${openai.api.key}")
    String openaiApiKey;
	
	@Override
	public void generateResponse(String prompt, StreamingResponseHandler<AiMessage> streamingResponseHandler) {
		OpenAiStreamingChatModel model = OpenAiStreamingChatModel.withApiKey(openaiApiKey);
        model.generate(prompt, streamingResponseHandler);
		
	}

	@Override
	public Flux<ServerSentEvent<String>> generateResponseOnScreen(String prompt) {
        return Flux.create(sink -> {
            OpenAiStreamingChatModel model = OpenAiStreamingChatModel.withApiKey(openaiApiKey);
            model.generate(prompt, new StreamingResponseHandler<AiMessage>() {

                @Override
                public void onNext(String token) {
                    sink.next(ServerSentEvent.builder(token).build());
                    System.out.print(token);
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    sink.complete();
                    System.out.println("\n\nDone streaming");
                }

                @Override
                public void onError(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }

}
