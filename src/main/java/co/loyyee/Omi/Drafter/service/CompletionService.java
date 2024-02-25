package co.loyyee.Omi.Drafter.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompletionService{

	final private static Logger log = LoggerFactory.getLogger(CompletionService.class);
	public static void main(String[] args) {
		String token = System.getenv("OPENAI_API_KEY");

		log.info(token);
		OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
		log.info("OpenAI Service Launching.");
//		CompletionRequest request = CompletionRequest.builder()
//			.model("gpt-3.5-turbo")
//			.prompt("Somebody once told me the world is gonna roll me")
//			.echo(true)
//			.user("testing")
//			.n(3)
//			.build();
		System.out.println("Streaming chat completion...");
		final List<ChatMessage> messages = new ArrayList<>();
		final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
		messages.add(systemMessage);
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
			.builder()
			.model("gpt-3.5-turbo")
			.messages(messages)
			.n(1)
			.maxTokens(50)
			.logitBias(new HashMap<>())
			.build();

		String result = service.streamChatCompletion(chatCompletionRequest)
			.doOnError(Throwable::printStackTrace)
			.filter( el -> el.getChoices().get(0).getMessage().getContent() != null)
			.map(el -> el.getChoices().get(0).getMessage().getContent())
			.reduce( (str1, str2 ) -> str1 + str2)
				.blockingGet();

//			.filter(str -> str != null)
//				.reduce( (str1, str2) -> str1 + str2)
//					.blockingGet();
					log.info(result);

		service.shutdownExecutor();
	}
}
