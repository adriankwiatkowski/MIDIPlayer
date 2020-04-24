package com.example.midiplayer.controllers;

import com.example.midiplayer.factories.ShowableCardFactory;
import com.example.midiplayer.interfaces.ShowableCard;
import com.example.midiplayer.models.ShowableCardType;

import javax.swing.*;
import java.awt.*;

public class GUIController {

    private static final Object LOCK = new Object();
    private static GUIController sInstance;

    private static final String MAIN_FRAME_NAME = "Music App";
    private static final int BOUND_X = 50, BOUND_Y = 50, BOUND_WIDTH = 300, BOUND_HEIGHT = 300;

    private boolean mIsInitialized = false;

    private JFrame mFrame;
    private JPanel mCards;

    private String mUsername = "";

    private GUIController() {
        initializeGUI();
    }

    public synchronized static GUIController getInstance() {
        System.out.println("Getting the midi.controllers.GUIController.");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new GUIController();
                System.out.println("Made new midi.controllers.GUIController.");
            }
        }
        return sInstance;
    }

    private void initializeGUI() {
        if (mIsInitialized) return;
        System.out.println("Initializing GUI.");
        mIsInitialized = true;

        try {
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIManager.put("swing.boldMetal", false);

        mFrame = new JFrame(MAIN_FRAME_NAME);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(mFrame.getContentPane());

        mFrame.setBounds(BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT);
        mFrame.pack();
        mFrame.setVisible(true);
    }

    private void addComponentsToPane(Container pane) {
        mCards = new JPanel(new CardLayout());

        ShowableCardType[] showableCardTypes = ShowableCardType.values();
        for (ShowableCardType showableCardType : showableCardTypes) {
            ShowableCard showableCard = ShowableCardFactory.getShowableCard(showableCardType);
            mCards.add(showableCard.getPanel(), showableCardType.toString());
        }

        pane.add(mCards, BorderLayout.CENTER);
    }

    public void showLayout(ShowableCardType showableCardType) {
        System.out.println(String.format("Showing layout: %s.", showableCardType));
        CardLayout cardLayout = (CardLayout) mCards.getLayout();
        cardLayout.show(mCards, showableCardType.toString());
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }
}
