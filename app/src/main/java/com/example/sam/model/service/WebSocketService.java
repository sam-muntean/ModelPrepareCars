package com.example.sam.model.service;

import android.support.annotation.Nullable;

import com.example.sam.model.domain.Car;
import com.example.sam.model.manager.Manager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import timber.log.Timber;

import okhttp3.WebSocketListener;

/**
 * Created by Sam on 31.01.2018.
 */

public class WebSocketService extends WebSocketListener {

    private WebSocket ws;
    private Manager manager;

    public WebSocketService(Manager manager) {
        this.manager = manager;
    }

    public void connect() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://192.168.43.97:4000")
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
        Timber.v("Connected to the websocket.");
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        this.ws = webSocket;
    }

    public void close(String reason) {
        if (ws != null) {
            ws.close(1000, reason);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        String message = "Error while opening the connection: " + t.getMessage();
        Timber.v(message);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Timber.v("Received " + text + " through the websocket.");
        try {
            JSONObject json = new JSONObject(text);
            int id = (Integer) json.get("id");
            String name = (String) json.get("name");
            String type = (String) json.get("type");
            String status = (String) json.get("status");
            int quantity = (Integer) json.get("quantity");
            Car car = new Car(id, name, quantity, type, status);
            System.out.println("New car:" + car);
            manager.addNewCar(car);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Timber.v("onClosing " + code + " " + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Timber.v("onClosed " + code + " " + reason);
    }
}