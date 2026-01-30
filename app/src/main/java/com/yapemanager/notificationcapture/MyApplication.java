package com.yapemanager.notificationcapture;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Clase Application personalizada para inicializar Firebase
 * al inicio de la aplicación
 */
public class MyApplication extends Application {
    
    private static final String TAG = "MyApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d(TAG, "🚀 Iniciando aplicación...");
        
        // Inicializar Firebase
        initializeFirebase();
    }
    
    private void initializeFirebase() {
        try {
            Log.d(TAG, "🔄 Inicializando Firebase en Application...");
            
            // Crear FirebaseOptions manualmente (sin depender de google-services.json)
            com.google.firebase.FirebaseOptions options = new com.google.firebase.FirebaseOptions.Builder()
                .setProjectId("notificationcapture-b4935")
                .setApplicationId("1:576269581874:android:6dc581812fa06c6db5c62b")
                .setApiKey("AIzaSyA5O4gFFL4WuAVVEjk6PuvVRg1bOL_n_Jk")
                .setDatabaseUrl("https://notificationcapture-b4935-default-rtdb.firebaseio.com")
                .build();
            
            Log.d(TAG, "   FirebaseOptions creado manualmente");
            
            // Inicializar FirebaseApp con las opciones
            FirebaseApp firebaseApp;
            try {
                firebaseApp = FirebaseApp.initializeApp(this, options);
                Log.d(TAG, "✅ FirebaseApp inicializado: " + firebaseApp.getName());
            } catch (IllegalStateException e) {
                // Ya existe, obtener la instancia
                firebaseApp = FirebaseApp.getInstance();
                Log.d(TAG, "✅ FirebaseApp ya existía: " + firebaseApp.getName());
            }
            
            if (firebaseApp != null) {
                // Obtener instancia de Firebase Database
                FirebaseDatabase database = FirebaseDatabase.getInstance(
                    firebaseApp,
                    "https://notificationcapture-b4935-default-rtdb.firebaseio.com"
                );
                
                // Habilitar persistencia offline
                try {
                    database.setPersistenceEnabled(true);
                    Log.d(TAG, "✅ Persistencia offline habilitada");
                } catch (Exception e) {
                    Log.w(TAG, "⚠️ Persistencia ya estaba habilitada");
                }
                
                Log.d(TAG, "✅ Firebase Database configurado correctamente");
                Log.d(TAG, "   URL: https://notificationcapture-b4935-default-rtdb.firebaseio.com");
            } else {
                Log.e(TAG, "❌ FirebaseApp es null después de inicialización");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al inicializar Firebase: " + e.getMessage());
            Log.e(TAG, "   Tipo de error: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
