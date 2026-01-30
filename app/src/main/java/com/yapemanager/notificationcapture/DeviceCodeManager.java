package com.yapemanager.notificationcapture;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.security.SecureRandom;

/**
 * Gestor de código único del dispositivo
 * Genera y almacena un código alfanumérico de 8 caracteres
 */
public class DeviceCodeManager {
    
    private static final String TAG = "DeviceCodeManager";
    private static final String PREFS_NAME = "DevicePrefs";
    private static final String KEY_DEVICE_CODE = "device_code";
    private static final String KEY_DEVICE_STATUS = "device_status";
    private static final int CODE_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    private SharedPreferences prefs;
    private String deviceCode;
    private String deviceStatus;
    
    public DeviceCodeManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadOrGenerateCode();
    }
    
    /**
     * Carga el código existente o genera uno nuevo
     */
    private void loadOrGenerateCode() {
        deviceCode = prefs.getString(KEY_DEVICE_CODE, null);
        deviceStatus = prefs.getString(KEY_DEVICE_STATUS, "pending");
        
        if (deviceCode == null) {
            deviceCode = generateUniqueCode();
            saveCode();
            registerDeviceInFirebase();
            Log.d(TAG, "✅ Nuevo código generado: " + deviceCode);
        } else {
            Log.d(TAG, "📱 Código existente: " + deviceCode + " (Status: " + deviceStatus + ")");
            // Sincronizar estado desde Firebase
            syncStatusFromFirebase();
        }
    }
    
    /**
     * Sincroniza el estado desde Firebase (sin callback)
     */
    private void syncStatusFromFirebase() {
        try {
            com.google.firebase.FirebaseApp firebaseApp = com.google.firebase.FirebaseApp.getInstance();
            com.google.firebase.database.FirebaseDatabase database = 
                com.google.firebase.database.FirebaseDatabase.getInstance(
                    firebaseApp,
                    "https://notificationcapture-b4935-default-rtdb.firebaseio.com"
                );
            
            database.getReference("devices").child(deviceCode).child("status")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        String newStatus = snapshot.getValue(String.class);
                        if (newStatus != null && !newStatus.equals(deviceStatus)) {
                            deviceStatus = newStatus;
                            prefs.edit().putString(KEY_DEVICE_STATUS, deviceStatus).apply();
                            Log.d(TAG, "✅ Estado sincronizado desde Firebase: " + deviceStatus);
                        } else {
                            Log.d(TAG, "ℹ️ Estado sin cambios: " + deviceStatus);
                        }
                    }
                    
                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        Log.e(TAG, "❌ Error al sincronizar estado: " + error.getMessage());
                    }
                });
                
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al sincronizar desde Firebase: " + e.getMessage());
        }
    }
    
    /**
     * Genera un código alfanumérico único de 8 caracteres
     */
    private String generateUniqueCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        
        return code.toString();
    }
    
    /**
     * Guarda el código en SharedPreferences
     */
    private void saveCode() {
        prefs.edit()
            .putString(KEY_DEVICE_CODE, deviceCode)
            .putString(KEY_DEVICE_STATUS, deviceStatus)
            .apply();
    }
    
    /**
     * Registra el dispositivo en Firebase
     */
    private void registerDeviceInFirebase() {
        try {
            com.google.firebase.FirebaseApp firebaseApp = com.google.firebase.FirebaseApp.getInstance();
            com.google.firebase.database.FirebaseDatabase database = 
                com.google.firebase.database.FirebaseDatabase.getInstance(
                    firebaseApp,
                    "https://notificationcapture-b4935-default-rtdb.firebaseio.com"
                );
            
            com.google.firebase.database.DatabaseReference devicesRef = 
                database.getReference("devices").child(deviceCode);
            
            // Información del dispositivo
            java.util.Map<String, Object> deviceInfo = new java.util.HashMap<>();
            deviceInfo.put("code", deviceCode);
            deviceInfo.put("status", "pending");
            deviceInfo.put("createdAt", System.currentTimeMillis());
            deviceInfo.put("model", android.os.Build.MODEL);
            deviceInfo.put("manufacturer", android.os.Build.MANUFACTURER);
            deviceInfo.put("androidVersion", android.os.Build.VERSION.RELEASE);
            deviceInfo.put("appVersion", "1.0");
            
            devicesRef.setValue(deviceInfo)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "✅ Dispositivo registrado en Firebase: " + deviceCode);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Error al registrar dispositivo: " + e.getMessage());
                });
                
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al registrar en Firebase: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene el código del dispositivo
     */
    public String getDeviceCode() {
        return deviceCode;
    }
    
    /**
     * Obtiene el estado de aprobación
     */
    public String getDeviceStatus() {
        return deviceStatus;
    }
    
    /**
     * Verifica si el dispositivo está aprobado
     */
    public boolean isApproved() {
        return "approved".equals(deviceStatus);
    }
    
    /**
     * Actualiza el estado del dispositivo desde Firebase
     */
    public void updateStatusFromFirebase(StatusCallback callback) {
        try {
            com.google.firebase.FirebaseApp firebaseApp = com.google.firebase.FirebaseApp.getInstance();
            com.google.firebase.database.FirebaseDatabase database = 
                com.google.firebase.database.FirebaseDatabase.getInstance(
                    firebaseApp,
                    "https://notificationcapture-b4935-default-rtdb.firebaseio.com"
                );
            
            database.getReference("devices").child(deviceCode).child("status")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        String newStatus = snapshot.getValue(String.class);
                        Log.d(TAG, "🔄 Estado leído desde Firebase: " + newStatus);
                        Log.d(TAG, "   Estado actual en app: " + deviceStatus);
                        
                        if (newStatus != null) {
                            if (!newStatus.equals(deviceStatus)) {
                                Log.d(TAG, "✅ Estado cambió de '" + deviceStatus + "' a '" + newStatus + "'");
                                deviceStatus = newStatus;
                                prefs.edit().putString(KEY_DEVICE_STATUS, deviceStatus).apply();
                            } else {
                                Log.d(TAG, "ℹ️ Estado sin cambios: " + deviceStatus);
                            }
                            
                            // Siempre llamar al callback con el estado actual
                            if (callback != null) {
                                callback.onStatusUpdated(newStatus);
                            }
                        } else {
                            Log.e(TAG, "❌ Estado es null en Firebase");
                            if (callback != null) {
                                callback.onError("Estado no encontrado en Firebase");
                            }
                        }
                    }
                    
                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        Log.e(TAG, "❌ Error al actualizar estado: " + error.getMessage());
                        if (callback != null) {
                            callback.onError(error.getMessage());
                        }
                    }
                });
                
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al verificar estado: " + e.getMessage());
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }
    
    /**
     * Callback para actualización de estado
     */
    public interface StatusCallback {
        void onStatusUpdated(String status);
        void onError(String error);
    }
}
