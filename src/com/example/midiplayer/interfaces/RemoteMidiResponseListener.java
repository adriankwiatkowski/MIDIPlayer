package com.example.midiplayer.interfaces;

public interface RemoteMidiResponseListener {
    void onRemoteMidiResponse(String nameToShow, boolean[] midiCheckboxState);
}