package com.example.midiplayer.player;

import com.example.midiplayer.consts.MidiConstants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class MidiPlayerGUI implements ActionListener, ListSelectionListener {

    public interface MidiPlayerGUIInterface {

        void onStartButtonClick(ActionEvent actionEvent);

        void onStopButtonClick(ActionEvent actionEvent);

        void onBuildTrackButtonClick(ActionEvent actionEvent);

        void onTempoUpButtonClick(ActionEvent actionEvent);

        void onTempoDownButtonClick(ActionEvent actionEvent);

        void onGenerateRandomPatterButtonClick(ActionEvent actionEvent);

        void onSavePatternButtonClick(ActionEvent actionEvent);

        void onLoadPatternButtonClick(ActionEvent actionEvent);

        void onSaveFileButtonClick(ActionEvent actionEvent);

        void onLoadFileButtonClick(ActionEvent actionEvent);

        void onNavigateToMidiRecordStudioButtonClick(ActionEvent actionEvent);

        void onSendMessageButtonClick(ActionEvent actionEvent);

        void onUserMessagePressedEnter(ActionEvent actionEvent);

        void onIncomingMessageListSelection(ListSelectionEvent listSelectionEvent);

    }

    private static final int TOP_BORDER = 10, LEFT_BORDER = 10, BOTTOM_BORDER = 10, RIGHT_BORDER = 10;
    private static final int VGAP = 1, HGAP = 2;

    private MidiPlayerGUIInterface mMidiPlayerGUIInterface;

    private JPanel mMainPanel;

    private JList<String> mIncomingMessageList;
    private JTextField mUserMessage;
    private List<JCheckBox> mCheckboxList = new ArrayList<>();
    private Vector<String> mListVector = new Vector<>();
    private HashMap<String, boolean[]> mOtherSeqsMap = new HashMap<>();
    private JButton mStartButton, mStopButton, mBuildTrackButton,
            mTempoUpButton, mTempoDownButton, mRandomPatterButton,
            mSavePatternButton, mLoadPatternButton, mSaveFileButton,
            mLoadFileButton, mSendMessageButton, mNaigateToMidiRecordStudioButton;

    public MidiPlayerGUI(MidiPlayerGUIInterface midiPlayerGUIInterface) {
        if (midiPlayerGUIInterface == null) {
            throw new IllegalArgumentException("MidiPlayerGUIInterface listener cannot be null.");
        }
        mMidiPlayerGUIInterface = midiPlayerGUIInterface;
    }

    public void buildGUI() {
        BorderLayout borderLayout = new BorderLayout();
        mMainPanel = new JPanel(borderLayout);
        mMainPanel.setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, LEFT_BORDER, BOTTOM_BORDER, RIGHT_BORDER));

        mCheckboxList = new ArrayList<>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        Box startStopBuildBox = new Box(BoxLayout.X_AXIS);
        mStartButton = new JButton("START");
        mStartButton.addActionListener(this);
        startStopBuildBox.add(mStartButton);
        mStopButton = new JButton("STOP");
        mStopButton.addActionListener(this);
        startStopBuildBox.add(mStopButton);
        mBuildTrackButton = new JButton("BUILD TRACK");
        mBuildTrackButton.addActionListener(this);
        startStopBuildBox.add(mBuildTrackButton);

        Box tempoBox = new Box(BoxLayout.X_AXIS);
        mTempoUpButton = new JButton("TEMPO UP");
        mTempoUpButton.addActionListener(this);
        tempoBox.add(mTempoUpButton);
        mTempoDownButton = new JButton("TEMP DOWN");
        mTempoDownButton.addActionListener(this);
        tempoBox.add(mTempoDownButton);

        Box randomPatternBox = new Box(BoxLayout.X_AXIS);
        mRandomPatterButton = new JButton("RANDOM PATTER");
        mRandomPatterButton.addActionListener(this);
        randomPatternBox.add(mRandomPatterButton);

        Box saveLoadPatternBox = new Box(BoxLayout.X_AXIS);
        mSavePatternButton = new JButton("SAVE PATTERN");
        mSavePatternButton.addActionListener(this);
        saveLoadPatternBox.add(mSavePatternButton);
        mLoadPatternButton = new JButton("LOAD PATTERN");
        mLoadPatternButton.addActionListener(this);
        saveLoadPatternBox.add(mLoadPatternButton);

        Box saveLoadFileBox = new Box(BoxLayout.X_AXIS);
        mSaveFileButton = new JButton("SAVE FILE");
        mSaveFileButton.addActionListener(this);
        saveLoadFileBox.add(mSaveFileButton);
        mLoadFileButton = new JButton("LOAD FILE");
        mLoadFileButton.addActionListener(this);
        saveLoadFileBox.add(mLoadFileButton);

        Box recordStudioBox = new Box(BoxLayout.X_AXIS);
        mNaigateToMidiRecordStudioButton = new JButton("RECORD STUDIO");
        mNaigateToMidiRecordStudioButton.addActionListener(this);
        recordStudioBox.add(mNaigateToMidiRecordStudioButton);

        Box sendItBox = new Box(BoxLayout.X_AXIS);
        mSendMessageButton = new JButton("SEND Message");
        mSendMessageButton.addActionListener(this);
        sendItBox.add(mSendMessageButton);

        buttonBox.add(startStopBuildBox);
        buttonBox.add(randomPatternBox);
        buttonBox.add(saveLoadPatternBox);
        buttonBox.add(saveLoadFileBox);
        buttonBox.add(recordStudioBox);
        buttonBox.add(sendItBox);

        mUserMessage = new JTextField();
        mUserMessage.addActionListener(this);
        buttonBox.add(mUserMessage);

        mIncomingMessageList = new JList<>();
        mIncomingMessageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mIncomingMessageList.addListSelectionListener(this);

        JScrollPane scrollIncomingList = new JScrollPane(mIncomingMessageList);
        buttonBox.add(scrollIncomingList);

        mIncomingMessageList.setListData(mListVector);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < MidiConstants.INSTRUMENT_NAMES.length; i++) {
            nameBox.add(new Label(MidiConstants.INSTRUMENT_NAMES[i]));
        }

        mMainPanel.add(BorderLayout.EAST, buttonBox);
        mMainPanel.add(BorderLayout.WEST, nameBox);

        GridLayout gridLayout = new GridLayout(MidiConstants.ROW_COUNT, MidiConstants.COLUMN_COUNT);
        gridLayout.setVgap(VGAP);
        gridLayout.setHgap(HGAP);
        JPanel gridPanel = new JPanel(gridLayout);
        mMainPanel.add(BorderLayout.CENTER, gridPanel);

        for (int i = 0; i < MidiConstants.TOTAL_ROW_AND_COLUMN_COUNT; i++) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            mCheckboxList.add(checkBox);
            gridPanel.add(checkBox);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedObject = e.getSource();
        if (clickedObject == mStartButton) mMidiPlayerGUIInterface.onStartButtonClick(e);
        else if (clickedObject == mStopButton) mMidiPlayerGUIInterface.onStopButtonClick(e);
        else if (clickedObject == mBuildTrackButton) mMidiPlayerGUIInterface.onBuildTrackButtonClick(e);
        else if (clickedObject == mTempoUpButton) mMidiPlayerGUIInterface.onTempoUpButtonClick(e);
        else if (clickedObject == mTempoDownButton) mMidiPlayerGUIInterface.onTempoDownButtonClick(e);
        else if (clickedObject == mRandomPatterButton) mMidiPlayerGUIInterface.onGenerateRandomPatterButtonClick(e);
        else if (clickedObject == mSavePatternButton) mMidiPlayerGUIInterface.onSavePatternButtonClick(e);
        else if (clickedObject == mLoadPatternButton) mMidiPlayerGUIInterface.onLoadPatternButtonClick(e);
        else if (clickedObject == mSaveFileButton) mMidiPlayerGUIInterface.onSaveFileButtonClick(e);
        else if (clickedObject == mLoadFileButton) mMidiPlayerGUIInterface.onLoadFileButtonClick(e);
        else if (clickedObject == mSendMessageButton) mMidiPlayerGUIInterface.onSendMessageButtonClick(e);
        else if (clickedObject == mUserMessage) mMidiPlayerGUIInterface.onUserMessagePressedEnter(e);
        else if (clickedObject == mNaigateToMidiRecordStudioButton)
            mMidiPlayerGUIInterface.onNavigateToMidiRecordStudioButtonClick(e);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Object selectedObject = e.getSource();
        if (selectedObject == mIncomingMessageList) mMidiPlayerGUIInterface.onIncomingMessageListSelection(e);
    }

    public JPanel getMainPanel() {
        return mMainPanel;
    }

    public List<JCheckBox> getCheckboxList() {
        return mCheckboxList;
    }

    public boolean[] getCheckboxState() {
        boolean[] checkboxState = new boolean[mCheckboxList.size()];
        for (int i = 0; i < mCheckboxList.size(); i++) {
            if (mCheckboxList.get(i).isSelected()) {
                checkboxState[i] = true;
            }
        }
        return checkboxState;
    }

    public void setCheckboxState(boolean[] checkboxState) {
        for (int i = 0; i < checkboxState.length; i++) {
            mCheckboxList.get(i).setSelected(checkboxState[i]);
        }
    }

    public String getUserMessage() {
        return mUserMessage.getText();
    }

    public void clearUserMessage() {
        mUserMessage.setText("");
    }

    public String getIncomingListSelectedValue() {
        return mIncomingMessageList.getSelectedValue();
    }

    public boolean[] getOtherSequence(String selection) {
        return mOtherSeqsMap.get(selection);
    }

    public void setOtherSequenceMap(String nameToShow, boolean[] checkboxState) {
        mOtherSeqsMap.put(nameToShow, checkboxState);
        mListVector.add(nameToShow);
        mIncomingMessageList.setListData(mListVector);
    }
}
