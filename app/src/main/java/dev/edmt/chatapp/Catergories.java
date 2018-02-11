package dev.edmt.chatapp;

/**
 * Created by song on 2/11/18.
 */

public class Catergories {
    String [] phrases = {   "Check my finances.",
                            "I bought a thing.",
                            "Create a budget.",
                        };
    String [] responses = {
                            "Check your finances?",
                            "Input spending amount?",
                            "Start a budget?"
                        };
    Catergories() {

    }
    String getResponse(String message) {
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        return "HELLO WORLD";
    };
}
