package co.loyyee.Omi.Drafter.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

/**
 * SaiOai: Spring AI - OpenAI implementations
 * */
public class SpringOpenAiService implements AIDraftable {
    private OpenAiChatClient chatClient;
    private OpenAiApi openAiApi;

    public SpringOpenAiService () {
        openAiApi = new OpenAiApi(System.getenv("OPENAI_KEY"));
        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo")
                .withMaxTokens(200)
                .withTemperature(0.4F)
                .build();
        this.chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    }
    @Override
    public void promptInitialHeader() {
        StringBuilder sb = new StringBuilder();
        var header = sb.append("Generate new cover letter for job application.\n")
                .append("1st paragraph: show interest, education, and GPA\n")
                .append("2nd: praise company culture\n")
                .append("3rd: good fit and able to contribute with my work experience and skills\n")
                .append("but don't output or reply anything, wait for my input")
                .toString();
        var resp = chatClient.call(new Prompt(header));
//        if(resp.getMetadata().)
    }

    @Override
    public void promptHeaderDesc() {

    }

    @Override
    public String ask() {
            return null;
    }
}
