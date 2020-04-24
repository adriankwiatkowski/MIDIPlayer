package com.example.midiplayer.consts;

import javafx.scene.input.KeyCode;

public final class MidiConstants {

    public static final String[] INSTRUMENT_NAMES = {
            "Acoustic Grand Piano",
            "Xylophone",
            "Percussive Organ",
            "Acoustic Guitar (steel)",
            "Acoustic Bass",
            "Violin",
            "Synth Voice",
            "Trumpet",
            "Baritone Sax",
            "Flute",
            "Lead 4 (chiff)",
            "Pad 5 (bowed)",
            "FX 8 (sci-fi)",
            "Kalimba",
            "Woodblock",
            "Bird Tweet"
    };

    public static final int[] INSTRUMENT_CODES = new int[]{
            1,
            14,
            18,
            26,
            33,
            41,
            55,
            57,
            68,
            74,
            84,
            93,
            104,
            109,
            116,
            124
    };

    public static final String[] RECORDER_INSTRUMENT_NAMES = new String[]{
            "Acoustic Grand Piano",
            "Xylophone",
            "Percussive Organ",
            "Acoustic Guitar (steel)",
            "Acoustic Bass",
            "Violin",
            "Synth Voice",
            "Trumpet",
            "Baritone Sax",
            "Flute",
            "Lead 4 (chiff)",
            "Pad 5 (bowed)",
            "FX 8 (sci-fi)",
            "Kalimba",
            "Woodblock",
            "Bird Tweet"
    };

    public static final int[] RECORDER_INSTRUMENT_CODES = new int[]{
            1,
            14,
            18,
            26,
            33,
            41,
            55,
            57,
            68,
            74,
            84,
            93,
            104,
            109,
            116,
            124
    };

    public static final KeyCode[] RECORDER_KEY_CODES = new KeyCode[]{
            KeyCode.DIGIT1,
            KeyCode.DIGIT2,
            KeyCode.DIGIT3,
            KeyCode.DIGIT4,
            KeyCode.Q,
            KeyCode.W,
            KeyCode.E,
            KeyCode.R,
            KeyCode.A,
            KeyCode.S,
            KeyCode.D,
            KeyCode.F,
            KeyCode.Z,
            KeyCode.X,
            KeyCode.C,
            KeyCode.V
    };

    public static final int MIDI_CHANNEL = 1;

    public static final String PATTERN_FILE_PATH = "pattern.turbo";

    public static final int ROW_COUNT = 16;
    public static final int COLUMN_COUNT = 16;
    public static final int TOTAL_ROW_AND_COLUMN_COUNT = ROW_COUNT * COLUMN_COUNT;

    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 5000;
}