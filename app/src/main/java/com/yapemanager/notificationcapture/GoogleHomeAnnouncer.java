package com.yapemanager.notificationcapture;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class GoogleHomeAnnouncer {
    
    private static final String TAG = "GoogleHomeAnnouncer";
    private Context context;
    
    public GoogleHomeAnnouncer(Context context) {
        this.context = context;
    }
    
    /**
     * Anuncia un mensaje en Google Home usando Google Assistant Broadcast
     * @param message El mensaje a anunciar
     */
    public void announce(String message) {
        try {
            // Usar Google Assistant para hacer broadcast
            Intent intent = new Intent(Intent.ACTION_VIEW);
            
            // Formato: "broadcast [mensaje]"
            String broadcastCommand = "broadcast " + message;
            
            // URI para Google Assistant
            Uri uri = Uri.parse("https://www.google.com/search?q=" + Uri.encode(broadcastCommand));
            intent.setData(uri);
            intent.setPackage("com.google.android.googlequicksearchbox");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(intent);
            
            Log.d(TAG, "✅ Broadcast enviado a Google Home: " + message);
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al enviar broadcast: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reproduce un sonido antes del anuncio
     * @param soundResId ID del recurso de sonido (R.raw.notification_sound)
     */
    public void playNotificationSound(int soundResId) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResId);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mediaPlayer.start();
                Log.d(TAG, "✅ Sonido de notificación reproducido");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al reproducir sonido: " + e.getMessage());
        }
    }
    
    /**
     * Anuncia un pago de YAPE
     * @param title Título del email (ej: "Juan Pérez")
     * @param content Contenido del email (ej: "Te transfirió S/ 50.00")
     */
    public void announceYapePayment(String title, String content) {
        // Formato: "Nuevo pago recibido. [título]. [contenido]"
        String message = "Nuevo pago recibido. " + title + ". " + content;
        announce(message);
    }
    
    /**
     * Verifica si está dentro del horario configurado
     * @param startHour Hora de inicio (0-23)
     * @param startMinute Minuto de inicio (0-59)
     * @param endHour Hora de fin (0-23)
     * @param endMinute Minuto de fin (0-59)
     * @return true si está dentro del horario
     */
    public static boolean isWithinSchedule(int startHour, int startMinute, int endHour, int endMinute) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentHour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(java.util.Calendar.MINUTE);
        
        int currentTimeInMinutes = currentHour * 60 + currentMinute;
        int startTimeInMinutes = startHour * 60 + startMinute;
        int endTimeInMinutes = endHour * 60 + endMinute;
        
        // Caso normal: 08:00 - 20:00
        if (startTimeInMinutes < endTimeInMinutes) {
            return currentTimeInMinutes >= startTimeInMinutes && currentTimeInMinutes <= endTimeInMinutes;
        }
        // Caso especial: 20:00 - 08:00 (cruza medianoche)
        else {
            return currentTimeInMinutes >= startTimeInMinutes || currentTimeInMinutes <= endTimeInMinutes;
        }
    }
}
