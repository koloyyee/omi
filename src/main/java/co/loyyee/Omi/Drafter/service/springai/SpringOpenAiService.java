package co.loyyee.Omi.Drafter.service.springai;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/** SaiOai: Spring AI - OpenAI implementations */
@Service
public class SpringOpenAiService
    implements co.loyyee.Omi.Drafter.service.AIDraftable<SpringOpenAiService, ChatResponse> {

  private static final String MODEL = "gpt-3.5-turbo";
  private static final Logger log = LoggerFactory.getLogger(SpringOpenAiService.class);
  private final Message sysMsg =
      new SystemMessage(
          """
						You are a cover letter write,
						create a 5 paragraphs letter.
						1st paragraph: show interest, education, and GPA,
						2nd paragraph: praise company culture,
						3rd paragraph: good fit and able to contribute with my work experience and skills.
						4th paragraph: soft skills good fit for the company
						5th paragraph: looking forward to the interview and contribute to the company.
						last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.\\n
						""");
  //    private OpenAiChatClient chatClient;
  private final OpenAiApi openAiApi;
  private UserMessage content;

  public SpringOpenAiService() {
    openAiApi = new OpenAiApi(System.getenv("OPENAI_KEY"));
  }

  @Deprecated
  private SpringOpenAiService promptInitialHeader() {
    var openAiChatOptions = OpenAiChatOptions.builder().withModel(MODEL).withMaxTokens(1).build();
    var chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    log.info("Spring AI header go. ");
    StringBuilder sb = new StringBuilder();
    var header =
        sb.append("Generate new cover letter for job application. ")
            .append("don't output or reply anything, wait for my input")
            .append("1st paragraph: show interest, education, and GPA, ")
            .append("2nd: praise company culture, ")
            .append("3rd: good fit and able to contribute with my work experience and skills.")
            .append("4th: soft skills good fit for the company ")
            .append("5th: looking forward to the interview and contribute to the company. ")
            .append(
                "last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.")
            .append("but don't output anything yet\n")
            .toString();
    var resp = chatClient.call(new Prompt(header));
    log.info("Spring Ai header response: " + resp);
    System.out.println();
    // response will be ignored.
    return this;
  }

  @Deprecated
  private SpringOpenAiService promptHeaderDesc() {
    var openAiChatOptions =
        OpenAiChatOptions.builder()
            .withModel(MODEL)
            .withMaxTokens(4000)
            .withTemperature(0.8F)
            .build();
    var chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    log.info("Header desc");
    StringBuilder sb = new StringBuilder();
    var header =
        sb.append("4th: soft skills good fit for the company ")
            .append("5th: looking forward to the interview and contribute to the company. ")
            .append(
                "last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.")
            .append("but don't output anything yet\n")
            .toString();
    var resp = chatClient.call(new Prompt(header));
    log.info("Spring Ai header response: " + resp);
    return this;
  }

  @Override
  public SpringOpenAiService setContent(String content) {
    this.content = new UserMessage(content + "\n start drafting and start with DRAFT\n");
    return this;
  }

  public ChatResponse ask() {
    var openAiChatOptions =
        OpenAiChatOptions.builder()
            .withModel(MODEL)
            .withMaxTokens(4000)
            .withTemperature(0.8F)
            .build();
    var chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);

    log.info("OPENAI_KEY: {}", System.getenv("OPENAI_KEY"));
    log.info("Preparing draft");
    var resp = chatClient.call(new Prompt(List.of(sysMsg, content)));
    //    log.info(resp.getResults().toString());
    //    log.info("Responding (finish reason): " +
    // resp.getResult().getMetadata().getFinishReason());
    log.info("{}", resp.getMetadata().getUsage().getTotalTokens());
    return resp;
  }

  public Flux<ChatResponse> askFlux() {
    var openAiChatOptions =
        OpenAiChatOptions.builder()
            .withModel(MODEL)
            .withMaxTokens(4000)
            .withTemperature(0.8F)
            .build();
    var chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    StringBuilder sb = new StringBuilder();
    var message =
        sb.append("Generate new cover letter for job application.\n")
            //                .append("don't output or reply anything, wait for my input\n")
            .append("1st paragraph: show interest, education, and GPA \n")
            .append("2nd paragraph: praise company culture,\n")
            .append(
                "3rd paragraph: good fit and able to contribute with my work experience and skills.\n")
            .append("4th paragraph: soft skills good fit for the company\n")
            .append(
                "5th paragraph: looking forward to the interview and contribute to the company.\n")
            .append(
                "last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.\n")
            .append(this.content)
            .toString();
    log.info("Preparing draft");
    var resp = chatClient.stream(new Prompt(message));
    log.info("Responding (finish reason): ");

    return resp;
  }
}
