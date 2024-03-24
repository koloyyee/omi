package co.loyyee.Omi.Chat;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 *  A sample code from Spring AI documentation
 *  Autoconfiguration.
 * */
@RestController
public class AutoChatController {
    private final OpenAiChatClient chatClient;

    @Autowired
    public AutoChatController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

 /**
  *  2 kinds of generation:
  *  1. return in Map
  *  2. return in Stream with Flux
  * */
    @GetMapping("/ai/auto/generate")
    public Map generate(@RequestParam(value = "message") String message ) {

        System.out.println(message);
       var val = Map.of("generation", chatClient.call(message));
        System.out.println(val);
       return val;
    }@PostMapping("/ai/generate/letter")
    public Map generateLetter(
            @RequestParam(value = "message") String message

    ) {

        System.out.println(message);
        var val = Map.of("generation", chatClient.call(message));
        System.out.println(val);
        return val;
    }
    @GetMapping("/ai/auto/generateStream")
    public Flux<ChatResponse> generateStream( @RequestParam(value = "message") String message) {
        Prompt prompt = new Prompt( new UserMessage(message ));
        Flux<ChatResponse> val  = chatClient.stream(prompt);
        val.collectList().map( list -> {
            System.out.println(list);
            return list;
        });
        return val;
    }
}
