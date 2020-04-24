package com.example.midiplayer.factories;

import com.example.midiplayer.interfaces.ShowableCard;
import com.example.midiplayer.login.MidiLogin;
import com.example.midiplayer.models.ShowableCardType;
import com.example.midiplayer.player.MidiPlayer;
import com.example.midiplayer.recorder.MidiRecorder;

public class ShowableCardFactory {

    public static ShowableCard getShowableCard(ShowableCardType showableCardType) {
        switch (showableCardType) {
            case MIDI_PLAYER:
                return new MidiPlayer();
            case MIDI_LOGIN:
                return new MidiLogin();
            case MIDI_RECORDER:
                return new MidiRecorder();
            default:
                System.out.println(String.format("Unknown type: %s. Returning %s.", showableCardType, MidiLogin.class.getSimpleName()));
                return new MidiLogin();
        }
    }

}
