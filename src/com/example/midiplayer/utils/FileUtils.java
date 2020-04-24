package com.example.midiplayer.utils;

import com.example.midiplayer.consts.MidiConstants;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.List;

public class FileUtils {

    public static void savePatternToFile(List<JCheckBox> checkBoxList) {
        boolean[] checkboxState = new boolean[MidiConstants.TOTAL_ROW_AND_COLUMN_COUNT];

        for (int i = 0; i < checkboxState.length; i++) {
            JCheckBox checkbox = checkBoxList.get(i);
            if (checkbox.isSelected())
                checkboxState[i] = true;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(MidiConstants.PATTERN_FILE_PATH));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(checkboxState);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPatternFromFile(List<JCheckBox> checkBoxList) {
        boolean[] checkboxState = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(MidiConstants.PATTERN_FILE_PATH));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            checkboxState = (boolean[]) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (checkboxState == null || checkboxState.length <= 0)
            return;

        for (int i = 0; i < MidiConstants.TOTAL_ROW_AND_COLUMN_COUNT; i++) {
            checkBoxList.get(i).setSelected(checkboxState[i]);
        }
    }

    public static void saveMidiToFile(Sequence sequence, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("MIDI", "mid");
        fileChooser.setFileFilter(fileFilter);
        int returnValue = fileChooser.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getPath();
            if (!filePath.contains(".mid")) {
                file = new File(fileChooser.getSelectedFile().getPath() + ".mid");
            }
            try {
                MidiSystem.write(sequence, 1, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Sequence loadMidiFromFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("MIDI", "mid");
        fileChooser.setFileFilter(fileFilter);
        int returnValue = fileChooser.showOpenDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.getPath().endsWith(".mid")) {
                try {
                    return MidiSystem.getSequence(file);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
