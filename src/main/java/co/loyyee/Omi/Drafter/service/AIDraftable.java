package co.loyyee.Omi.Drafter.service;


public interface AIDraftable {
   void promptInitialHeader();
    void promptHeaderDesc();
    String ask();
}
