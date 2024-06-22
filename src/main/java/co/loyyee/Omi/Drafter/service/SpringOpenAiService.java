package co.loyyee.Omi.Drafter.service;

import co.loyyee.Omi.Drafter.util.exception.ContentFilterException;
import co.loyyee.Omi.Drafter.util.exception.FunctionCallException;
import co.loyyee.Omi.Drafter.util.exception.LengthException;
import co.loyyee.Omi.Drafter.util.exception.NullException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/** SaiOai: Spring AI - OpenAI implementations */
@Service
public class SpringOpenAiService
    implements co.loyyee.Omi.Drafter.service.AIDraftable<SpringOpenAiService, String> {

  private static final String MODEL = "gpt-4o";
  private static final Logger log = LoggerFactory.getLogger(SpringOpenAiService.class);
  private final Message sysMsg =
      new SystemMessage(
          """
						You are a cover letter writer, be creative, unique and confident,
						don't sound like AI
						read through the job requirements then
						create a 5-6 paragraphs letter.
						Show interest, talk about education, GPA,
						praise company culture,
						good fit and able to contribute with my work experience and skills.
						talk about my projects how to be fits the role
						soft skills good fit for the company
						looking forward to the interview and contribute to the company. thanks the manager for taking the time, and including the my contact and ask them to contact.\\n
						""");
  //    private OpenAiChatModel chatClient;
  private final OpenAiApi openAiApi;
  private UserMessage content;

  public SpringOpenAiService() {
    openAiApi = new OpenAiApi(System.getenv("OPENAI_KEY"));
  }


  @Override
  public SpringOpenAiService setContent(String content) {
    this.content = new UserMessage(content + "\n start drafting and start with DRAFT\n");
    return this;
  }

  public String ask() {
    var openAiChatOptions =
        OpenAiChatOptions.builder()
            .withModel(MODEL)
            .withMaxTokens(4000)
            .withTemperature(0.8F)
            .build();
    var chatClient = new OpenAiChatModel(openAiApi, openAiChatOptions);

//    log.info("OPENAI_KEY: {}", System.getenv("OPENAI_KEY"));
    log.info("Preparing draft");
    var resp = chatClient.call(new Prompt(List.of(sysMsg, content)));
    log.info("{}", resp.getMetadata().getUsage().getTotalTokens());

    String finishReason = resp.getResult().getMetadata().getFinishReason();
    if (resp.getResult().getMetadata().getFinishReason().equalsIgnoreCase("STOP")) {
      String content = resp.getResult().getOutput().getContent();
      return content;
    } else {
      if (finishReason.equalsIgnoreCase("length")) {
        throw new LengthException();
      } else if (finishReason.equalsIgnoreCase("content_filter")) {
        throw new ContentFilterException();
      } else if (finishReason.equalsIgnoreCase("tool_calls")) {
        throw new FunctionCallException();
      } else {
        throw new NullException();
      }
    }
  }

  public String update(String requests) {
    
      return null;
  }
  @Hidden
  public Flux<ChatResponse> askFlux() {
    var openAiChatOptions =
        OpenAiChatOptions.builder()
            .withModel(MODEL)
            .withMaxTokens(4000)
            .withTemperature(0.8F)
            .build();
    var chatClient = new OpenAiChatModel(openAiApi, openAiChatOptions);
    StringBuilder sb = new StringBuilder();
    var message =
        sb.append("Generate new cover letter for job application.\n")
            .append("1st paragraph: show interest, education, and GPA \n")
            .append("2nd paragraph: praise company culture,\n")
            .append("3rd paragraph: good fit and able to contribute with my work experience and skills.\n")
            .append("4th paragraph: soft skills good fit for the company\n")
            .append("5th paragraph: looking forward to the interview and contribute to the company.\n")
            .append("last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.\n")
            .append(this.content)
            .toString();
    log.info("Preparing draft");
    var resp = chatClient.stream(new Prompt(message));
    log.info("Responding (finish reason): ");
    return resp;
  }
  
  //  @Deprecated
//  private SpringOpenAiService promptInitialHeader() {
//    var openAiChatOptions = OpenAiChatOptions.builder().withModel(MODEL).withMaxTokens(1).build();
//    var chatClient = new OpenAiChatModel(openAiApi, openAiChatOptions);
//    log.info("Spring AI header go. ");
//    StringBuilder sb = new StringBuilder();
//    var header =
//        sb.append("Generate new cover letter for job application. ")
//            .append("don't output or reply anything, wait for my input")
//            .append("1st paragraph: show interest, education, and GPA, ")
//            .append("2nd: praise company culture, ")
//            .append("3rd: good fit and able to contribute with my work experience and skills.")
//            .append("4th: soft skills good fit for the company ")
//            .append("5th: looking forward to the interview and contribute to the company. ")
//            .append(
//                "last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.")
//            .append("but don't output anything yet\n")
//            .toString();
//    var resp = chatClient.call(new Prompt(header));
//    log.info("Spring Ai header response: {} " , resp);
//    System.out.println();
//    // response will be ignored.
//    return this;
//  }
//
//  @Deprecated
//  private SpringOpenAiService promptHeaderDesc() {
//    var openAiChatOptions =
//        OpenAiChatOptions.builder()
//            .withModel(MODEL)
//            .withMaxTokens(4000)
//            .withTemperature(0.8F)
//            .build();
//    var chatClient = new OpenAiChatModel(openAiApi, openAiChatOptions);
//    log.info("Header desc");
//    StringBuilder sb = new StringBuilder();
//    var header =
//        sb.append("4th: soft skills good fit for the company ")
//            .append("5th: looking forward to the interview and contribute to the company. ")
//            .append(
//                "last paragraph thanks the manager for taking the time, and including the my contact and ask them to contact.")
//            .append("but don't output anything yet\n")
//            .toString();
//    var resp = chatClient.call(new Prompt(header));
//    log.info("Spring Ai header response: {}" , resp);
//    return this;
//  }

}
