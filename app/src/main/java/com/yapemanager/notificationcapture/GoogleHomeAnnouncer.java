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
     * Aumenta el volumen al máximo para que se escuche incluso en otras apps
     */
    private void announceLocally(String message) {
        try {
            // Obtener AudioManager para controlar el volumen
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            
            // Guardar volumen actual
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            
            // Aumentar volumen al máximo temporalmente
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            
            Log.d(TAG, "🔊 Volumen aumentado: " + currentVolume + " → " + maxVolume);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Usar QUEUE_FLUSH para interrumpir cualquier anuncio anterior
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "announcement");
            } else {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
            
            Log.d(TAG, "🔊 Anuncio local reproducido con volumen máximo");
            
            // Restaurar volumen después de 10 segundos
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                    Log.d(TAG, "🔊 Volumen restaurado: " + maxVolume + " → " + currentVolume);
                }
            }, 10000); // 10 segundos
            
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
     * Anuncia un pago de YAPE o Gmail con formato personalizado
     * Extrae el nombre y monto del contenido
     * 
     * Ejemplo de contenido:
     * "Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00"
     * 
     * Mensaje de salida:
     * "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Jesús Moisés Gallegos Parlona DE 1.00 SOLES"
     * 
     * @param title Título de la notificación
     * @param content Contenido de la notificación
     */
    public void announceYapePayment(String title, String content) {
        try {
            String customMessage = buildCustomMessage(content);
            announce(customMessage);
        } catch (Exception e) {
            Log.e(TAG, "Error al construir mensaje personalizado: " + e.getMessage());
            // Fallback al mensaje original
            String message = "Nuevo pago recibido. " + title + ". " + content;
            announce(message);
        }
    }
    
    /**
     * Construye un mensaje personalizado extrayendo nombre y monto
     * 
     * @param content Contenido de la notificación
     * @return Mensaje personalizado
     */
    private String buildCustomMessage(String content) {
        String nombre = "desconocido";
        String monto = "0";
        
        try {
            // Extraer nombre (entre "Yape!" o "!" y "te envió")
            // Ejemplo: "Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00"
            
            // Buscar el patrón de nombre
            if (content.contains("te envió")) {
                String[] parts = content.split("te envió");
                if (parts.length > 0) {
                    String nombrePart = parts[0];
                    
                    // Limpiar "Yape!" o "!" del inicio
                    nombrePart = nombrePart.replace("Yape!", "").replace("!", "").trim();
                    
                    if (!nombrePart.isEmpty()) {
                        nombre = nombrePart;
                    }
                }
            }
            
            // Extraer monto (después de "S/" o "S/.")
            // Ejemplo: "S/ 1.00" o "S/1.00"
            if (content.contains("S/")) {
                String[] parts = content.split("S/");
                if (parts.length > 1) {
                    String montoPart = parts[1].trim();
                    
                    // Extraer solo números y punto decimal
                    StringBuilder montoBuilder = new StringBuilder();
                    for (char c : montoPart.toCharArray()) {
                        if (Character.isDigit(c) || c == '.') {
                            montoBuilder.append(c);
                        } else if (montoBuilder.length() > 0) {
                            // Ya encontramos el monto completo
                            break;
                        }
                    }
                    
                    if (montoBuilder.length() > 0) {
                        monto = montoBuilder.toString();
                    }
                }
            }
            
            Log.d(TAG, "📝 Mensaje extraído - Nombre: " + nombre + ", Monto: " + monto);
            
        } catch (Exception e) {
            Log.e(TAG, "Error al extraer datos: " + e.getMessage());
        }
        
        // Construir mensaje personalizado
        String customMessage = "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE " + nombre + " DE " + monto + " SOLES";
        
        Log.d(TAG, "🔊 Mensaje personalizado: " + customMessage);
        
        return customMessage;
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
