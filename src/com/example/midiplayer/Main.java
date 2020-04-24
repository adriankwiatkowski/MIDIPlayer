package com.example.midiplayer;

import com.example.midiplayer.controllers.GUIController;
import com.example.midiplayer.models.ShowableCardType;

public class Main {

    public static void main(String[] args) {
        GUIController.getInstance().showLayout(ShowableCardType.MIDI_LOGIN);
    }

}