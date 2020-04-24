package com.example.midiplayer.models;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Recorder {

    private KeyCode mRegisterKeyCode;
    private String mInstrumentName;
    private int mInstrumentCode;

    private List<Long> mRegisterTimeList = new ArrayList<>();
    private long mCurrentRegisterTimeMillis;
    private long mStartRegisterTimeMillis;
    private boolean mIsRegistered = false;

    public Recorder(KeyCode registerKeyCode, String instrumentName, int instrumentCode) {
        this.mRegisterKeyCode = registerKeyCode;
        this.mInstrumentName = instrumentName;
        this.mInstrumentCode = instrumentCode;
    }

    public void cleanRecordingData() {
        mRegisterTimeList.clear();
        mCurrentRegisterTimeMillis = 0;
        mIsRegistered = false;
    }

    public void prepareRecordingData() {
        mStartRegisterTimeMillis = System.currentTimeMillis();
    }

    public void registerKey() {
        if (!mIsRegistered) {
            mIsRegistered = true;
            mCurrentRegisterTimeMillis = System.currentTimeMillis();
            System.out.println(String.format(
                    "Registering key. Char: %s Key: %d",
                    mRegisterKeyCode.getChar(),
                    mRegisterKeyCode.getCode()));
        }
    }

    public void unregisterKey() {
        if (mIsRegistered) {
            mIsRegistered = false;
            long registeredTimeMillis = System.currentTimeMillis() - mCurrentRegisterTimeMillis;
            long registeredTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(registeredTimeMillis);
            mRegisterTimeList.add(registeredTimeSeconds);
            System.out.println(String.format(
                    "Unregistering key. Char: %s Key: %d Total time: %d",
                    mRegisterKeyCode.getChar(),
                    mRegisterKeyCode.getCode(),
                    registeredTimeSeconds));
        }
    }

    public long getRegisterTimerDuration(int index) {
        return TimeUnit.MILLISECONDS.toSeconds(mRegisterTimeList.get(index) - mStartRegisterTimeMillis);
    }

    public KeyCode getRegisterKeyCode() {
        return mRegisterKeyCode;
    }

    public String getInstrumentName() {
        return mInstrumentName;
    }

    public int getInstrumentCode() {
        return mInstrumentCode;
    }

    public List<Long> getRegisterTimeList() {
        return mRegisterTimeList;
    }
}
