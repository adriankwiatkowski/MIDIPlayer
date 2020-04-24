package com.example.midiplayer.utils;

import com.example.midiplayer.consts.MidiConstants;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiUtils {

    public static MidiEvent makeEvent(int command, int channel, int data1, int data2, long tick) {
        MidiEvent midiEvent = null;

        try {
            ShortMessage shortMessage = new ShortMessage();
            shortMessage.setMessage(command, channel, data1, data2);
            midiEvent = new MidiEvent(shortMessage, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return midiEvent;
    }

    public static void makeTracks(Track track, int[] list, int volume) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(ShortMessage.NOTE_ON, MidiConstants.MIDI_CHANNEL, key, volume, i));
                track.add(makeEvent(ShortMessage.NOTE_OFF, MidiConstants.MIDI_CHANNEL, key, volume, i + 1));
            }
        }
    }

    public static void addEventToTrack(Track track, int instrumentCode, int volume, long registerTime, long duration) {
        track.add(MidiUtils.makeEvent(
                ShortMessage.NOTE_ON,
                MidiConstants.MIDI_CHANNEL,
                instrumentCode,
                volume,
                registerTime));

        track.add(MidiUtils.makeEvent(
                ShortMessage.NOTE_OFF,
                MidiConstants.MIDI_CHANNEL,
                instrumentCode,
                volume,
                registerTime + duration));
    }
}
