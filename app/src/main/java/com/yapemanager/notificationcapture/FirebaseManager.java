package com.yapemanager.notificationcapture;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Gestor de Firebase Realtime Database
 * Registra las notificaciones de pago en Firebase
 */
public class FirebaseManager {
    
    private static final String TAG = "FirebaseManager";
    private DatabaseReference databaseRef;
    private boolean isEnabled = false;
    
    public FirebaseManager(Context context) {
        try {
            // Inicializar Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference("payments");
            isEnabled = true;
            Log.d(TAG, "✅ Firebase inicializado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al inicializar Firebase: " + e.getMessage());
            isEnabled = false;
        }
    }
    
    /**
     * Registra un pago en Firebase
     * 
     * @param title Título de la notificación
     * @param content Contenido de la notificación
     * @param source Fuente (YAPE o Gmail)
     */
    public void registerPayment(String title, String content, String source) {
        if (!isEnabled) {
            Log.w(TAG, "⚠️ Firebase no está habilitado, saltando registro");
            return;
        }
        
        try {
            // Crear timestamp
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = sdf.format(new Date(timestamp));
            
            // Extraer información del pago
            PaymentInfo info = extractPaymentInfo(content);
            
            // Crear objeto de pago
            Map<String, Object> payment = new HashMap<>();
            payment.put("timestamp", timestamp);
            payment.put("date", formattedDate);
            payment.put("title", title);
            payment.put("content", content);
            payment.put("source", source);
            payment.put("sender", info.sender);
            payment.put("amount", info.amount);
            payment.put("currency", "PEN"); // Soles peruanos
            
            // Generar ID único basado en timestamp
            String paymentId = "payment_" + timestamp;
            
            // Guardar en Firebase
            databaseRef.child(paymentId).setValue(payment)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "✅ Pago registrado en Firebase: " + paymentId);
                    Log.d(TAG, "   Remitente: " + info.sender + ", Monto: " + info.amount);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Error al registrar en Firebase: " + e.getMessage());
                });
                
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al procesar pago para Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Extrae información del pago del contenido
     */
    private PaymentInfo extractPaymentInfo(String content) {
        PaymentInfo info = new PaymentInfo();
        
        try {
            // Extraer nombre (entre "Yape!" o "!" y "te envió")
            if (content.contains("te envió")) {
                String[] parts = content.split("te envió");
                if (parts.length > 0) {
                    String senderPart = parts[0];
                    senderPart = senderPart.replace("Yape!", "").replace("!", "").trim();
                    if (!senderPart.isEmpty()) {
                        info.sender = senderPart;
                    }
                }
            }
            
            // Extraer monto (después de "S/")
            if (content.contains("S/")) {
                String[] parts = content.split("S/");
                if (parts.length > 1) {
                    String amountPart = parts[1].trim();
                    
                    // Extraer solo números y punto decimal
                    StringBuilder amountBuilder = new StringBuilder();
                    for (char c : amountPart.toCharArray()) {
                        if (Character.isDigit(c) || c == '.') {
                            amountBuilder.append(c);
                        } else if (amountBuilder.length() > 0) {
                            break;
                        }
                    }
                    
                    if (amountBuilder.length() > 0) {
                        info.amount = amountBuilder.toString();
                    }
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al extraer información: " + e.getMessage());
        }
        
        return info;
    }
    
    /**
     * Clase interna para almacenar información del pago
     */
    private static class PaymentInfo {
        String sender = "Desconocido";
        String amount = "0.00";
    }
    
    /**
     * Verifica si Firebase está habilitado
     */
    public boolean isEnabled() {
        return isEnabled;
    }
    
    /**
     * Obtiene una referencia a un nodo específico
     */
    public DatabaseReference getReference(String path) {
        if (databaseRef != null) {
            return databaseRef.child(path);
        }
        return null;
    }
}
