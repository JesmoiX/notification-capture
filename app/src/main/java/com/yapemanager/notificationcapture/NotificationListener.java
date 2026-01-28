package com.yapemanager.notificationcapture;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("NotificationCapturePrefs", MODE_PRIVATE);
        Log.d(TAG, "NotificationListener Service Created");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        String packageName = sbn.getPackageName();
        Log.d(TAG, "Notification from: " + packageName);

        // Solo capturar notificaciones de Gmail
        if (!packageName.equals("com.google.android.gm")) {
            return;
        }

        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        if (extras == null) {
            return;
        }

        // Extraer información de la notificación
        String title = extras.getString(Notification.EXTRA_TITLE, "");
        String text = extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
        String subText = extras.getString(Notification.EXTRA_SUB_TEXT, "");

        Log.d(TAG, "Gmail Notification - Title: " + title + ", Text: " + text);

        // Enviar a Google Sheets
        sendToGoogleSheets(title, text, packageName);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private void sendToGoogleSheets(final String title, final String content, final String app) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String googleSheetUrl = prefs.getString("google_sheet_url", "");
                    
                    if (googleSheetUrl.isEmpty()) {
                        Log.e(TAG, "Google Sheet URL not configured");
                        return;
                    }

                    // Crear JSON con los datos
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("title", title);
                    jsonData.put("content", content);
                    jsonData.put("app", app);
                    jsonData.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

                    // Enviar POST request
                    URL url = new URL(googleSheetUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Escribir datos
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonData.toString().getBytes("UTF-8"));
                    os.close();

                    // Leer respuesta
                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "Google Sheets Response Code: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                        Log.d(TAG, "Data sent successfully to Google Sheets");
                    } else {
                        Log.e(TAG, "Failed to send data. Response code: " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    Log.e(TAG, "Error sending to Google Sheets: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
