package com.example.filip.finalproject;
import android.content.Intent;
import android.support.design.circularreveal.CircularRevealHelper;
import android.util.Log;

import com.google.android.gms.nearby.*;
import com.google.android.gms.nearby.connection.*;

import java.util.ArrayList;

import static android.util.Xml.Encoding.UTF_8;


public class MultiplayerConnection {

    public static MultiplayerConnection connection;
    public static ConnectionsClient connectionsClient;
    private String opponentEndpointId;
    public static boolean hasConnection = false;


    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    int[] receivedData = new int[3];
                    if (payload.asBytes().length != 5) {
                        GameEngine.message = "Received invalid data!";
                    } else {
                        receivedData[0] = payload.asBytes()[0];
                        receivedData[1] = payload.asBytes()[1] * 100 + payload.asBytes()[2];
                        receivedData[2] = payload.asBytes()[3] * 100 + payload.asBytes()[4];

                        receivedData[1] *= FullscreenActivity.scaleFactor;
                        receivedData[2] *= FullscreenActivity.scaleFactor;

                        GameEngine.tapProcessor(receivedData[1],receivedData[2],receivedData[0]);
                        if (receivedData[0] == 1) {
                            FullscreenActivity.memory.add(new Integer(receivedData[1] * FullscreenActivity.widthfullscreen + receivedData[2]));
                        } else {
                            FullscreenActivity.memory.add(new Integer((receivedData[1] * FullscreenActivity.widthfullscreen + receivedData[2]) * -1));
                        }
                    }
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    //not needed?
                }
            };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i("TAG", "onConnectionInitiated: accepting connection");
                    MultiplayerMenu.changeText("opponent found, connecting");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    hasConnection = true;
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i("TAG", "onConnectionResult: connection success");
                        hasConnection = true;
                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();
                        opponentEndpointId = endpointId;

                        MultiplayerMenu.launchGame();
                    } else {
                        Log.i("TAG", "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i("TAG", "onConnectionInitiated: onDisconnected");
                }
            };

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i("Tag", "onEndpointFound: endpoint found, connecting");
                    MultiplayerMenu.changeText("host found, connecting");
                    connectionsClient.requestConnection("Device A", endpointId, connectionLifecycleCallback);
                    hasConnection = true;
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    Log.i("Tag", "onEndpointFound: endpoint lost");
                }
            };

    private void startAdvertising() {
        connectionsClient.startAdvertising(
                        /* endpointName= */ "Device A",
                        /* serviceId= */ "com.landsOfBattle",
                        connectionLifecycleCallback,
                        new AdvertisingOptions(Strategy.P2P_STAR));
    }

    private void startDiscovery() {
        connectionsClient.startDiscovery(
                "com.landsOfBattle",endpointDiscoveryCallback,
                new DiscoveryOptions(Strategy.P2P_STAR));
    }

    private void hostGameInternal() {
        startAdvertising();
        Log.i("Tag", "search started as host");
        MultiplayerMenu.changeText("search started as host");
    }

    private void joinGameInternal() {
        startDiscovery();
        Log.i("Tag", "search started as client");
        MultiplayerMenu.changeText("search started as client");
    }

    public static void hostGame() {
        connection.hostGameInternal();
        GameEngine.gameIsMultiplayer = true;
        GameEngine.isHostPhone = true;
        //TechTree.techIsEnabled = false;
    }

    public static void joinGame() {
        connection.joinGameInternal();
        GameEngine.gameIsMultiplayer = true;
        GameEngine.isHostPhone = false;
        //TechTree.techIsEnabled = false;
    }


    private void sendGameDataInternal(int mode, int x, int y) {
        x /= FullscreenActivity.scaleFactor;
        y /= FullscreenActivity.scaleFactor;


        byte[] toSend = new byte[5];
        toSend[0] = (byte) mode;
        toSend[1] = (byte) (x / 100);
        toSend[2] = (byte) (x % 100);
        toSend[3] = (byte) (y / 100);
        toSend[4] = (byte) (y % 100);
        connectionsClient.sendPayload(
                opponentEndpointId, Payload.fromBytes(toSend));
    }

    public static void sendGameData(int mode, int x, int y) {
        connection.sendGameDataInternal(mode,x,y);
    }


}
