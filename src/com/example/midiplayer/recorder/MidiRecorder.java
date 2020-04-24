package com.example.midiplayer.recorder;

import com.example.midiplayer.consts.MidiConstants;
import com.example.midiplayer.controllers.GUIController;
import com.example.midiplayer.interfaces.ShowableCard;
import com.example.midiplayer.models.Recorder;
import com.example.midiplayer.models.ShowableCardType;
import com.example.midiplayer.utils.FileUtils;
import com.example.midiplayer.utils.MidiUtils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MidiRecorder implements ShowableCard, KeyListener {

    private static final int TOP_BORDER = 10, LEFT_BORDER = 10, BOTTOM_BORDER = 10, RIGHT_BORDER = 10;
    private static final int VGAP = 1, HGAP = 2;

    private JPanel mMainPanel;

    private Recorder[] mRecorders = new Recorder[MidiConstants.RECORDER_INSTRUMENT_CODES.length];
    private JButton[] mRecorderButtons = new JButton[mRecorders.length];
    private boolean mIsRecording = false;

    public MidiRecorder() {
        FlowLayout flowLayout = new FlowLayout();
        mMainPanel = new JPanel(flowLayout);
        mMainPanel.setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, LEFT_BORDER, BOTTOM_BORDER, RIGHT_BORDER));

        mMainPanel.addKeyListener(this);
        mMainPanel.setFocusable(true);
        mMainPanel.requestFocusInWindow();

        assert mRecorders.length == MidiConstants.RECORDER_INSTRUMENT_CODES.length : "Invalid length.";

        int columns = 4;
        int rows = MidiConstants.RECORDER_INSTRUMENT_CODES.length;
        rows = (rows % columns == 0) ? rows / columns : (rows / columns) + 1;

        GridLayout gridLayout = new GridLayout(rows, columns, HGAP, VGAP);
        JPanel gridPanel = new JPanel(gridLayout);
        mMainPanel.add(gridPanel);

        // Create buttons and recorders.
        for (int i = 0; i < mRecorders.length; i++) {
            // Add buttons.
            String buttonText = String.format("%s (%s)",
                    MidiConstants.RECORDER_INSTRUMENT_NAMES[i],
                    MidiConstants.RECORDER_KEY_CODES[i].getChar().toUpperCase());
            JButton button = new JButton(buttonText);
            button.addActionListener(this::clearFocus);
            mRecorderButtons[i] = button;
            gridPanel.add(button);

            // Create recorder.
            mRecorders[i] = new Recorder(MidiConstants.RECORDER_KEY_CODES[i],
                    MidiConstants.RECORDER_INSTRUMENT_NAMES[i],
                    MidiConstants.RECORDER_INSTRUMENT_CODES[i]);
        }

        JButton startRecordingButton = new JButton("START RECORDING");
        startRecordingButton.addActionListener(this::startRecording);
        mMainPanel.add(startRecordingButton);

        JButton stopRecordingButton = new JButton("STOP RECORDING");
        stopRecordingButton.addActionListener(this::stopRecording);
        mMainPanel.add(stopRecordingButton);

        JButton playerButton = new JButton("PLAYER STUDIO");
        playerButton.addActionListener(this::navigateToMidiPlayer);
        mMainPanel.add(playerButton);
    }

    @Override
    public JPanel getPanel() {
        return mMainPanel;
    }

    @Override
    public void clearState() {
        cleanAllRecordersData();
        setIsNotRecording();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not implemented.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isRecording())
            return;

        findRecorderWithKeyCode(e.getKeyCode()).ifPresent(Recorder::registerKey);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!isRecording())
            return;

        findRecorderWithKeyCode(e.getKeyCode()).ifPresent(Recorder::unregisterKey);
    }

    private void startRecording(ActionEvent actionEvent) {
        clearFocus(actionEvent);

        if (isRecording())
            return;

        cleanAllRecordersData();
        prepareAllRecorders();
        setIsRecording();
    }

    private void stopRecording(ActionEvent actionEvent) {
        clearFocus(actionEvent);

        if (!isRecording())
            return;

        setIsNotRecording();
        buildMidiAndSave();
    }

    private void navigateToMidiPlayer(ActionEvent actionEvent) {
        setIsNotRecording();
        cleanAllRecordersData();
        GUIController.getInstance().showLayout(ShowableCardType.MIDI_PLAYER);
    }

    private void clearFocus(ActionEvent actionEvent) {
        mMainPanel.requestFocus();
    }

    private Optional<Recorder> findRecorderWithKeyCode(int keyCode) {
        return Stream.of(mRecorders)
                .filter(Objects::nonNull)
                .filter(recorder -> keyCode == recorder.getRegisterKeyCode().getCode())
                .findAny();
    }

    private void cleanAllRecordersData() {
        for (Recorder recorder : mRecorders) {
            recorder.cleanRecordingData();
        }
    }

    private void prepareAllRecorders() {
        for (Recorder recorder : mRecorders) {
            recorder.prepareRecordingData();
        }
    }

    private void buildMidiAndSave() {
        try {
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();

            int volume = 100;

            for (Recorder recorder : mRecorders) {
                List<Long> registerTimeList = recorder.getRegisterTimeList();
                for (int i = 0; i < registerTimeList.size(); i++) {
                    int instrumentCode = recorder.getInstrumentCode();
                    long registerTime = registerTimeList.get(i);
                    long duration = recorder.getRegisterTimerDuration(i);
                    MidiUtils.addEventToTrack(track, instrumentCode, volume, registerTime, duration);
                }
            }

            FileUtils.saveMidiToFile(sequence, mMainPanel);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private boolean isRecording() {
        return mIsRecording;
    }

    private void setIsRecording() {
        mIsRecording = true;
    }

    private void setIsNotRecording() {
        mIsRecording = false;
    }
}
