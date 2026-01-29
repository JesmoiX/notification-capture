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
            
            // Inicializar FirebaseApp
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);
            
            if (firebaseApp != null) {
                Log.d(TAG, "✅ FirebaseApp inicializado: " + firebaseApp.getName());
                
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
                Log.e(TAG, "❌ FirebaseApp.initializeApp() retornó null");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al inicializar Firebase: " + e.getMessage());
            Log.e(TAG, "   Tipo de error: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
