package co.loyyee.Omi.Drafter.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionService;

/**
 * <h2>Oai: OpenAI</h2>
 * <h4>OaiDrafterService is the bridge between Drafter and OpenAI</h4>
 * <hr>
 * The service is using Theo Kanning's
 * <p>
 * see {@link<a href=" https://github.com/TheoKanning/openai-java"> OpenAI Java library</a>}
 */
@Service
public class OaiDrafterService implements AIDraftable{
    final private static Logger log = LoggerFactory.getLogger(CompletionService.class);
    private String promptContent;
    private int durationSecs;
    private final List<ChatMessage> messages;
//    private final String messageHeader;

    /* different ways of getting the environment variables */

    //	final private DrafterConfigurationProperties properties; // for configuration properties
    //	@Value("${OPENAI_KEY}") // for environment variables
//	@Value("${api.openai}") // for application.yml variable
    private String apiKey;

    public OaiDrafterService() {


//        this.messageHeader = header.toString();
        this.durationSecs = 30;
        this.messages = new ArrayList<>();
        /* different ways of getting the environment variables */
        this.apiKey = System.getenv("OPENAI_KEY"); // the default way to get environment variables
        System.out.println(apiKey);
    }

    public void promptInitialHeader() {
        StringBuilder header = new StringBuilder();
        header.append("You will help me to draft a NEW cover letter and help me to land an interview. ")
                .append("first paragraph about me, and why I am interested in the position, and education background and GPA. ")
                .append("second paragraph is about how good the company culture is ")
                .append("third paragraph work experience and skill set will contribute to the company ")
                .append("but don't output anything, wait for my input.");

        OpenAiService service = new OpenAiService(this.apiKey, Duration.ofSeconds(this.durationSecs));
        ChatMessage systemMessages = new ChatMessage(ChatMessageRole.SYSTEM.value(), header.toString());
        this.messages.add(systemMessages);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
//				.maxTokens(10)
                .logitBias(new HashMap<>())
                .build();
        System.out.println("OpenAI Service setting header.");
        try {
            var value = service.streamChatCompletion(chatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .filter(el -> el.getChoices().get(0).getMessage().getContent() != null)
                    .map(el -> el.getChoices().get(0).getMessage().getContent())
                    .reduce((str1, str2) -> str1 + str2)
                    .blockingGet();

            log.info("Initial Header: " + value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            service.shutdownExecutor();
        }

    }
    public void promptHeaderDesc() {
        StringBuilder header = new StringBuilder();
        header.append("fourth paragraph soft skills good fit for the company ")
                .append("fifth paragraph looking forward to the interview and opportunity to work with the company. ")
                .append("last paragraph thanks the hiring manager for taking the time, and including the my contact information and as them to contact me. ")
                .append("but don't output anything, wait for my input.");

        OpenAiService service = new OpenAiService(this.apiKey, Duration.ofSeconds(this.durationSecs));
        ChatMessage systemMessages = new ChatMessage(ChatMessageRole.SYSTEM.value(), header.toString());
        this.messages.add(systemMessages);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
//				.maxTokens(10)
                .logitBias(new HashMap<>())
                .build();
        System.out.println("OpenAI Service setting header description.");
        try {
            var value = service.streamChatCompletion(chatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .filter(el -> el.getChoices().get(0).getMessage().getContent() != null)
                    .map(el -> el.getChoices().get(0).getMessage().getContent())
                    .reduce((str1, str2) -> str1 + str2)
                    .blockingGet();

            log.info("Initial Header: " + value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            service.shutdownExecutor();
        }

    }



    public OaiDrafterService setContent(String promptContent) {
        this.promptContent = promptContent + "\n start drafting. And start with the word 'DRAFT\n' ";
        return this;
    }

    public int getDurationSecs() {
        return durationSecs;
    }

    /**
     * Duration.ofSeconds default is 30
     *
     * @param durationSecs input for custom seconds.
     */
    public OaiDrafterService setDurationSecs(int durationSecs) {
        this.durationSecs = durationSecs;
        return this;
    }

    public String ask() {

        OpenAiService service = new OpenAiService(this.apiKey, Duration.ofSeconds(this.durationSecs));
        this.promptInitialHeader();
        this.promptHeaderDesc();
        ChatMessage systemMessages = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.promptContent);
        this.messages.add(systemMessages);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(4000)
                .logitBias(new HashMap<>())
                .build();

        log.info("OpenAI Service Launching.");
        /** joining the message. */
        try {
            var result = service.streamChatCompletion(chatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .filter(el -> el.getChoices().get(0).getMessage().getContent() != null)
                    .map(el -> el.getChoices().get(0).getMessage().getContent())
                    .reduce((str1, str2) -> str1 + str2)
                    .blockingGet();
            if (!result.contains("DRAFT")) {
                throw new RuntimeException("This is an invalid response. Please try again.");
            } else {
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        } finally {
            service.shutdownExecutor();
        }
    }

    public static void main(String[] args) {

    }
}
