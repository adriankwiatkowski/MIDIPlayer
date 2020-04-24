package com.example.midiplayer.player;

import com.example.midiplayer.consts.MidiConstants;
import com.example.midiplayer.interfaces.RemoteMidiResponseListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MidiPlayerServerConnection {

    private Socket mSocket;
    private ObjectInputStream mIn;
    private ObjectOutputStream mOut;

    private RemoteMidiResponseListener mRemoteMidiResponseListener;

    public MidiPlayerServerConnection(RemoteMidiResponseListener remoteMidiResponseListener) {
        mRemoteMidiResponseListener = remoteMidiResponseListener;

        try {
            mSocket = new Socket(MidiConstants.SERVER_HOST, MidiConstants.SERVER_PORT);
            mIn = new ObjectInputStream(mSocket.getInputStream());
            mOut = new ObjectOutputStream(mSocket.getOutputStream());

            new Thread(new RemoteReader()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendIt(String userName, int i, String userMessage, boolean[] checkboxState) {
        try {
            mOut.writeObject(userName + i + ": " + userMessage);
            mOut.writeObject(checkboxState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RemoteReader implements Runnable {
        private Object object = null;
        private String nameToShow;
        private boolean[] checkboxState;

        @Override
        public void run() {
            while (true) {
                try {
                    if ((object = mIn.readObject()) == null)
                        continue;

                    nameToShow = (String) object;
                    checkboxState = (boolean[]) mIn.readObject();

                    mRemoteMidiResponseListener.onRemoteMidiResponse(nameToShow, checkboxState);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}