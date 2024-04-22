package co.loyyee.Omi.Drafter.service;


public interface AIDraftable<T, R>{
//   T promptInitialHeader();
//    T promptHeaderDesc();
    T setContent(String content);
//    String ask();
    R ask();
//    Flux<ChatResponse> askFlux();
}
