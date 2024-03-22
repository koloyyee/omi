package co.loyyee.Omi.Chat;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 *  A sample code from Spring AI documentation
 *  Part 1:  Autoconfiguration.
 *  Part 2:  Manual
 * */
@RestController
public class ChatController {
    private final OpenAiChatClient chatClient;
    private OpenAiApi openAiApi;
    @Autowired
    public ChatController(OpenAiChatClient chatClient) {
        this.openAiApi = new OpenAiApi(System.getenv("SPRING_AI_OPENAI_API_KEY"));
        this.chatClient = chatClient;
    }

 /**
  *  2 kinds of generation:
  *  1. return in Map
  *  2. return in Stream with Flux
  * */
    @GetMapping("/ai/auto/generate")
    public Map generate(@RequestParam(value = "message ", defaultValue = "Tell me a joke") String message ) {
        return Map.of("generation", chatClient.call(message));
    }
    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream( @RequestParam(value = "message ", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt( new UserMessage(message ));
        return chatClient.stream(prompt);
    }

    @GetMapping("/ai/man/generate")
    public Map generate( ) {
        return Map.of("

    }
    @GetMapping("/ai/man/generateStream")
    public Flux<ChatResponse> generateStream( @RequestParam(value = "message ", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt( new UserMessage(message ));
        return chatClient.stream(prompt);
    }
}
