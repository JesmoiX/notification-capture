package com.yapemanager.notificationcapture;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private static final String CHANNEL_ID = "notification_capture_service";
    private static final int FOREGROUND_ID = 1001;
    private SharedPreferences prefs;
    private ExecutorService executorService;
    private int capturedCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("NotificationCapturePrefs", MODE_PRIVATE);
        executorService = Executors.newFixedThreadPool(3); // Pool de 3 threads para envíos paralelos
        
        // Iniciar como Foreground Service
        startForegroundService();
        
        Log.d(TAG, "NotificationListener Service Created as Foreground");
    }

    private void startForegroundService() {
        createNotificationChannel();
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, 
            notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification Capture Activo")
                .setContentText("Capturando notificaciones en segundo plano")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(FOREGROUND_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Capture Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Mantiene el servicio activo para capturar notificaciones");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
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

        // FILTRAR NOTIFICACIONES DE RESUMEN
        // Ignorar notificaciones que son resúmenes (ej: "6 mensajes nuevos")
        if (isGroupSummary(notification) || isSummaryNotification(title, text)) {
            Log.d(TAG, "Ignorando notificación de resumen: " + title);
            return;
        }

        // Ignorar notificaciones vacías
        if (title.trim().isEmpty() && text.trim().isEmpty()) {
            Log.d(TAG, "Ignorando notificación vacía");
            return;
        }

        Log.d(TAG, "Gmail Notification VÁLIDA - Title: " + title + ", Text: " + text);

        // Enviar a Google Sheets (optimizado con thread pool)
        capturedCount++;
        updateForegroundNotification();
        sendToGoogleSheets(title, text, packageName);
    }

    private boolean isGroupSummary(Notification notification) {
        return (notification.flags & Notification.FLAG_GROUP_SUMMARY) != 0;
    }

    private boolean isSummaryNotification(String title, String text) {
        // Detectar patrones de notificaciones de resumen
        String lowerTitle = title.toLowerCase();
        String lowerText = text.toLowerCase();
        
        // Patrones comunes de resumen
        return lowerTitle.contains("mensajes nuevos") ||
               lowerTitle.contains("new messages") ||
               lowerTitle.contains("mensajes sin leer") ||
               lowerTitle.matches("\\d+\\s+mensaje.*") || // "6 mensajes nuevos"
               lowerText.contains("mensajes nuevos") ||
               lowerText.matches("\\d+\\s+mensaje.*");
    }

    private void updateForegroundNotification() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, 
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Notification Capture Activo")
                    .setContentText("Capturados: " + capturedCount + " emails")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            manager.notify(FOREGROUND_ID, notification);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private void sendToGoogleSheets(final String title, final String content, final String app) {
        // Usar ExecutorService para envíos más rápidos y paralelos
        executorService.execute(new Runnable() {
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

                    // Enviar POST request con timeout optimizado
                    URL url = new URL(googleSheetUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setConnectTimeout(5000); // 5 segundos timeout
                    conn.setReadTimeout(5000);
                    conn.setDoOutput(true);

                    // Escribir datos
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonData.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // Leer respuesta
                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "Google Sheets Response Code: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK || 
                        responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                        responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        responseCode == 302) {
                        Log.d(TAG, "✅ Data sent successfully to Google Sheets");
                    } else {
                        Log.e(TAG, "❌ Failed to send data. Response code: " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    Log.e(TAG, "❌ Error sending to Google Sheets: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
        Log.d(TAG, "NotificationListener Service Destroyed");
    }
}
