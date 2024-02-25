package co.loyyee.Omi.Drafter.service;

import co.loyyee.Omi.config.DrafterConfigurationProperties;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionService;

/**
 * <h4>DrafterService is the bridge between Drafter and OpenAI</h4>
 * <hr>
 * The service is using Theo Kanning's
 *
 * see {@link<a href=" https://github.com/TheoKanning/openai-jav"> OpenAI Java library</a>}
 * */
@Component
public class DrafterService {
	final private static Logger log = LoggerFactory.getLogger(CompletionService.class);
	private String promptContent;
//	@Value("${OPENAI_KEY}") // for environment variables
	@Value("${openai.api-key}") // for application.yml variable
	private String apiKey;
	private int durationSecs;
	private OpenAiService service;
	private List<ChatMessage> messages;
	private ChatMessage systemMessages;
	private String messageHeader;
//	final private DrafterConfigurationProperties properties; // for configuration properties
	public DrafterService(DrafterConfigurationProperties properties) {
		this.messageHeader = "You will help me to draft a cover letter and help me to land an interview.";
		this.durationSecs = 30;
		this.messages = new ArrayList<>();
		/* different ways of getting the environment variables */
//		this.properties = properties;
//		this.apiKey = properties.apiKey();
//		this.apiKey = System.getenv("OPENAI_KEY"); // the default way to get environment variables
	}

	public DrafterService setContent(String promptContent) {
		StringBuilder strB = new StringBuilder();
		strB.append(this.messageHeader);
		strB.append(promptContent);
		this.promptContent = strB.toString();
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
	public DrafterService setDurationSecs(int durationSecs) {
		this.durationSecs = durationSecs;
		return this;
	}

	public String ask() {
		this.service = new OpenAiService(this.apiKey, Duration.ofSeconds(this.durationSecs));
		this.systemMessages = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.promptContent);
		this.messages.add(systemMessages);
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
			.model("gpt-3.5-turbo")
			.messages(messages)
			.n(1)
//			.maxTokens()
			.logitBias(new HashMap<>())
			.build();

		log.info("OpenAI Service Launching.");
		/** joining the message. */
		try {
			return this.service.streamChatCompletion(chatCompletionRequest)
				.doOnError(Throwable::printStackTrace)
				.filter(el -> el.getChoices().get(0).getMessage().getContent() != null)
				.map(el -> el.getChoices().get(0).getMessage().getContent())
				.reduce((str1, str2) -> str1 + str2)
				.blockingGet();

		} catch (Exception e) {
			log.error(e.getMessage());
			return e.getMessage();
		} finally {
			this.service.shutdownExecutor();
		}
	}

	public static void main(String[] args) {

	}
}
