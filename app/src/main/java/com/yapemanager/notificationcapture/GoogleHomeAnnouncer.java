package com.yapemanager.notificationcapture;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class GoogleHomeAnnouncer {
    
    private static final String TAG = "GoogleHomeAnnouncer";
    private Context context;
    private static TextToSpeech tts;
    
    public GoogleHomeAnnouncer(Context context) {
        this.context = context;
        initializeTTS();
    }
    
    /**
     * Inicializa el motor de Text-to-Speech
     */
    private void initializeTTS() {
        if (tts == null) {
            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(new Locale("es", "ES"));
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e(TAG, "Idioma español no soportado, usando idioma por defecto");
                            tts.setLanguage(Locale.getDefault());
                        }
                        
                        // Configurar velocidad y tono
                        tts.setPitch(1.0f);
                        tts.setSpeechRate(1.0f);
                        
                        Log.d(TAG, "✅ TTS inicializado correctamente");
                    } else {
                        Log.e(TAG, "❌ Error al inicializar TTS");
                    }
                }
            });
        }
    }
    
    /**
     * Anuncia un mensaje usando Text-to-Speech del celular
     * NOTA: Este método reproduce el audio en el CELULAR, no en Google Home
     * Para Google Home se necesita Google Cast API (requiere configuración adicional)
     * 
     * @param message El mensaje a anunciar
     */
    public void announce(String message) {
        try {
            if (tts != null) {
                // Método 1: TTS local (funciona siempre)
                announceLocally(message);
                
                // Método 2: Intentar broadcast a Google Home (puede no funcionar)
                tryGoogleAssistantBroadcast(message);
                
                Log.d(TAG, "✅ Anuncio enviado: " + message);
            } else {
                Log.e(TAG, "❌ TTS no inicializado");
                initializeTTS();
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al anunciar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Anuncia localmente en el celular usando TTS
     */
    private void announceLocally(String message) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "announcement");
            } else {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
            Log.d(TAG, "🔊 Anuncio local reproducido");
        } catch (Exception e) {
            Log.e(TAG, "Error en anuncio local: " + e.getMessage());
        }
    }
    
    /**
     * Intenta enviar broadcast a Google Home
     * NOTA: Este método requiere interacción manual y puede no funcionar
     */
    private void tryGoogleAssistantBroadcast(String message) {
        try {
            // Método 1: Intent directo a Google Assistant
            Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
            intent.putExtra("android.intent.extra.TEXT", "broadcast " + message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                Log.d(TAG, "📢 Broadcast intent enviado");
            } else {
                Log.w(TAG, "⚠️ No se pudo enviar broadcast (Google Assistant no disponible)");
            }
        } catch (Exception e) {
            Log.w(TAG, "⚠️ Broadcast no disponible: " + e.getMessage());
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
    
    /**
     * Libera recursos de TTS
     */
    public static void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
