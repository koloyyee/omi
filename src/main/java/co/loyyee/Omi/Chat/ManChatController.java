package co.loyyee.Omi.Chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  A sample code from Spring AI documentation
 *   Manual Configuration
 *
 * Rate Limit: {@link ChatResponse#getMetadata getRateLimit() }
 * Usage: {@link ChatResponse#getMetadata() getUsage() }
 * Results: {@link ChatResponse#getResults()}
 * First Result: {@link ChatResponse#getResult()}
 * */
@RestController
public class ManChatController {
    private final static Logger log = LoggerFactory.getLogger(ManChatController.class);
    private OpenAiChatClient chatClient;
    private OpenAiApi openAiApi;
    private final static String MODEL = "gpt-3.5-turbo";

    @Autowired
    public ManChatController() {
        openAiApi = new OpenAiApi(System.getenv("OPENAI_KEY")) ;
        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel(MODEL)
                .withTemperature(0.4F)
                .withMaxTokens(200)
                .build();
        this.chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    }

 /**
  *  2 kinds of generation:
  *  1. return in Map
  *  2. return in Stream with Flux
  *
  * */
    @GetMapping("/ai/man/generate/full")
    public ChatResponse generateFull(@RequestParam(value="message") String  message) {
        var val = chatClient.call(new Prompt( message));
        return val;
    }
    @GetMapping("/ai/man/generate")
    public ResponseEntity<String> generate(@RequestParam(value="message") String  message) {
        var val = chatClient.call(new Prompt( message));
        var reason = val.getResult()
                .getOutput()
                .getProperties()
                .entrySet()
                .stream().filter( prop -> prop.getKey().equals("finishReason"))
                .findFirst()
                .orElseThrow()
                .getValue();

            return reason.equals(OpenAiApi.ChatCompletionFinishReason.STOP) ?
                    ResponseEntity.ok(val.getResult().getOutput().getContent())
                    :
                    ResponseEntity.badRequest().body("No Responses");
    }
    @GetMapping("/ai/man/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value="message") String  message) {
        return chatClient.stream(new Prompt(message));
    }
    @GetMapping("/ai/sync/generate")
    public ResponseEntity<OpenAiApi.ChatCompletion> generateSyncRequest() {
        OpenAiApi.ChatCompletionMessage chatCompletionMessage =
                new OpenAiApi.ChatCompletionMessage("Hello world", OpenAiApi.ChatCompletionMessage.Role.USER);
        return openAiApi.chatCompletionEntity(
                new OpenAiApi.ChatCompletionRequest(List.of(chatCompletionMessage), MODEL, 0.8f, false));
    }
    @GetMapping("/ai/stream/generateStream")
    public Flux<OpenAiApi.ChatCompletionChunk> generateStreamRequest() {
        OpenAiApi.ChatCompletionMessage chatCompletionMessage =
                new OpenAiApi.ChatCompletionMessage("Hello world", OpenAiApi.ChatCompletionMessage.Role.USER);
        return openAiApi.chatCompletionStream(
                new OpenAiApi.ChatCompletionRequest(List.of(chatCompletionMessage), MODEL, 0.8f, true));
    }
}
