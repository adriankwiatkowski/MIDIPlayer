package com.example.midiplayer.player;

import com.example.midiplayer.consts.MidiConstants;
import com.example.midiplayer.controllers.GUIController;
import com.example.midiplayer.interfaces.ShowableCard;
import com.example.midiplayer.models.ShowableCardType;
import com.example.midiplayer.utils.FileUtils;
import com.example.midiplayer.utils.MidiUtils;
import com.example.midiplayer.utils.RandomUtils;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;

public class MidiPlayer implements ShowableCard, MidiPlayerGUI.MidiPlayerGUIInterface {

    private static final int INITIAL_TEMPO_IN_BPM = 120;
    private static final float TRACK_TEMPO_UP_MULTIPLIER = 1.03f;
    private static final float TRACK_TEMPO_DOWN_MULTIPLIER = 0.97f;

    private MidiPlayerGUI mMidiPlayerGUI;
    private MidiPlayerServerConnection mMidiPlayerServerConnection;

    private Sequencer mSequencer;
    private Sequence mFileSequence;
    private Sequence mMySequence;
    private Sequence mCurrentSequence;
    private Track mTrack;
    private int mVolume = 100;

    private int mNextNum;

    public MidiPlayer() {
        setupMidi();
        setupGUI();
        setupServerConnection();
    }

    @Override
    public JPanel getPanel() {
        return mMidiPlayerGUI.getMainPanel();
    }

    @Override
    public void clearState() {
        stopTrack();
    }

    private void setupMidi() {
        try {
            mSequencer = MidiSystem.getSequencer();
            mSequencer.open();
            mMySequence = new Sequence(Sequence.PPQ, 4);
            mTrack = mMySequence.createTrack();
            mSequencer.setTempoInBPM(INITIAL_TEMPO_IN_BPM);
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() {
        mMidiPlayerGUI = new MidiPlayerGUI(this);
        mMidiPlayerGUI.buildGUI();
    }

    private void setupServerConnection() {
        mMidiPlayerServerConnection = new MidiPlayerServerConnection((nameToShow, midiCheckboxState) ->
                mMidiPlayerGUI.setOtherSequenceMap(nameToShow, midiCheckboxState));
    }

    @Override
    public void onStartButtonClick(ActionEvent actionEvent) {
        playMidi();
    }

    @Override
    public void onStopButtonClick(ActionEvent actionEvent) {
        stopTrack();
    }

    @Override
    public void onBuildTrackButtonClick(ActionEvent actionEvent) {
        buildTrack();
    }

    @Override
    public void onTempoUpButtonClick(ActionEvent actionEvent) {
        changeTrackTempoUp();
    }

    @Override
    public void onTempoDownButtonClick(ActionEvent actionEvent) {
        changeTrackTempoDown();
    }

    @Override
    public void onGenerateRandomPatterButtonClick(ActionEvent actionEvent) {
        generateRandomPatternAndPlay();
    }

    @Override
    public void onSavePatternButtonClick(ActionEvent actionEvent) {
        savePattern();
    }

    @Override
    public void onLoadPatternButtonClick(ActionEvent actionEvent) {
        loadPattern();
    }

    @Override
    public void onSaveFileButtonClick(ActionEvent actionEvent) {
        saveMidiFile();
    }

    @Override
    public void onLoadFileButtonClick(ActionEvent actionEvent) {
        loadMidiFile();
    }

    @Override
    public void onNavigateToMidiRecordStudioButtonClick(ActionEvent actionEvent) {
        navigateToMidiRecorder();
    }

    @Override
    public void onSendMessageButtonClick(ActionEvent actionEvent) {
        sendMessage();
    }

    @Override
    public void onUserMessagePressedEnter(ActionEvent actionEvent) {
        sendMessage();
    }

    @Override
    public void onIncomingMessageListSelection(ListSelectionEvent listSelectionEvent) {
        incomingListSelection(listSelectionEvent);
    }

    private void buildTrack() {
        stopTrack();
        mMySequence.deleteTrack(mTrack);
        mTrack = mMySequence.createTrack();

        for (int i = 0; i < MidiConstants.ROW_COUNT; i++) {
            int[] trackList = new int[MidiConstants.ROW_COUNT];
            int key = MidiConstants.INSTRUMENT_CODES[i];

            for (int j = 0; j < MidiConstants.COLUMN_COUNT; j++) {
                JCheckBox checkBox = mMidiPlayerGUI.getCheckboxList().get((i * MidiConstants.ROW_COUNT) + j);
                if (checkBox.isSelected()) {
                    trackList[j] = key;
                }
            }

            MidiUtils.makeTracks(mTrack, trackList, mVolume);
            mTrack.add(MidiUtils.makeEvent(ShortMessage.CONTROL_CHANGE, 1, 127, 0, 16));
        }

        mTrack.add(MidiUtils.makeEvent(ShortMessage.PROGRAM_CHANGE, MidiConstants.MIDI_CHANNEL, 1, 0, 15));

        setCurrentSequence(mMySequence);
    }

    private void playMidi() {
        stopTrack();

        if (mCurrentSequence == null)
            return;

        try {
            mSequencer.setSequence(mCurrentSequence);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        mSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        mSequencer.start();
        mSequencer.setTempoInBPM(INITIAL_TEMPO_IN_BPM);
    }

    private void saveMidiFile() {
        if (mFileSequence != null) {
            FileUtils.saveMidiToFile(mFileSequence, getPanel());
        }
    }

    private void loadMidiFile() {
        stopTrack();
        Sequence sequence = FileUtils.loadMidiFromFile(getPanel());
        if (sequence != null) {
            mFileSequence = sequence;
            setCurrentSequence(mFileSequence);
        }
    }

    private void navigateToMidiRecorder() {
        GUIController.getInstance().showLayout(ShowableCardType.MIDI_RECORDER);
    }

    private void savePattern() {
        FileUtils.savePatternToFile(mMidiPlayerGUI.getCheckboxList());
    }

    private void loadPattern() {
        stopTrack();
        FileUtils.loadPatternFromFile(mMidiPlayerGUI.getCheckboxList());
        buildTrack();
    }

    private void stopTrack() {
        mSequencer.stop();
    }

    private void changeTrackTempoUp() {
        changeTrackTempo(TRACK_TEMPO_UP_MULTIPLIER);
    }

    private void changeTrackTempoDown() {
        changeTrackTempo(TRACK_TEMPO_DOWN_MULTIPLIER);
    }

    private void changeTrackTempo(float multiplier) {
        float currentTempoFactor = mSequencer.getTempoFactor();
        mSequencer.setTempoFactor(currentTempoFactor * multiplier);
    }

    private void generateRandomPatternAndPlay() {
        mMidiPlayerGUI.setCheckboxState(RandomUtils.generateRandomBooleanArray(MidiConstants.TOTAL_ROW_AND_COLUMN_COUNT));
        stopTrack();
        buildTrack();
        playMidi();
    }

    private void sendMessage() {
        mMidiPlayerServerConnection.sendIt(
                GUIController.getInstance().getUsername(),
                mNextNum++,
                mMidiPlayerGUI.getUserMessage(),
                mMidiPlayerGUI.getCheckboxState());
        mMidiPlayerGUI.clearUserMessage();
    }

    private void incomingListSelection(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            String selected = mMidiPlayerGUI.getIncomingListSelectedValue();
            if (selected != null) {
                boolean[] selectedState = mMidiPlayerGUI.getOtherSequence(selected);
                stopTrack();
                mMidiPlayerGUI.setCheckboxState(selectedState);
                buildTrack();
                playMidi();
            }
        }
    }

    private void setCurrentSequence(Sequence newSequence) {
        mCurrentSequence = newSequence;
    }
}
