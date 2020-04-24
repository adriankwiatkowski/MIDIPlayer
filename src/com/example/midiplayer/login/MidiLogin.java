package com.example.midiplayer.login;

import com.example.midiplayer.controllers.GUIController;
import com.example.midiplayer.interfaces.ShowableCard;
import com.example.midiplayer.models.ShowableCardType;

import javax.swing.*;
import java.awt.*;

public class MidiLogin implements ShowableCard {

    private static final int TOP_BORDER = 10, LEFT_BORDER = 10, BOTTOM_BORDER = 10, RIGHT_BORDER = 10;

    private JPanel mMainPanel;
    private JTextField mUsernameField;
    private JButton mSubmitButton;

    public MidiLogin() {
        FlowLayout flowLayout = new FlowLayout();
        mMainPanel = new JPanel(flowLayout);
        mMainPanel.setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, LEFT_BORDER, BOTTOM_BORDER, RIGHT_BORDER));

        Box box = new Box(BoxLayout.Y_AXIS);

        JLabel usernameLabel = new JLabel("Username: ");
        box.add(usernameLabel);

        mUsernameField = new JTextField(20);
        mUsernameField.addActionListener(e -> login());
        box.add(mUsernameField);

        mSubmitButton = new JButton("SUBMIT");
        mSubmitButton.addActionListener(e -> login());
        box.add(mSubmitButton);

        mMainPanel.add(box);
    }

    @Override
    public JPanel getPanel() {
        return mMainPanel;
    }

    @Override
    public void clearState() {
        // Not implemented.
    }

    private void login() {
        String username = mUsernameField.getText().trim();
        if (username.length() > 0) {
            GUIController guiController = GUIController.getInstance();
            guiController.setUsername(username);
            guiController.showLayout(ShowableCardType.MIDI_PLAYER);
        }
    }
}
