package co.loyyee.Omi.Drafter.service;


import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

public interface AIDraftable<T>{
   T promptInitialHeader();
    T promptHeaderDesc();
    T setContent(String content);
//    String ask();
    Flux<ChatResponse> ask();
}
