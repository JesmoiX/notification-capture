package com.yapemanager.notificationcapture;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private EditText urlInput;
    private Button saveButton;
    private Button openSettingsButton;
    private TextView statusText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPreferences
        prefs = getSharedPreferences("NotificationCapturePrefs", MODE_PRIVATE);

        // Inicializar vistas
        urlInput = findViewById(R.id.urlInput);
        saveButton = findViewById(R.id.saveButton);
        openSettingsButton = findViewById(R.id.openSettingsButton);
        statusText = findViewById(R.id.statusText);

        // Cargar URL guardada
        String savedUrl = prefs.getString("google_sheet_url", "");
        urlInput.setText(savedUrl);

        // Verificar si tiene permisos de notificación
        updateStatus();

        // Guardar URL
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlInput.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa una URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Guardar URL
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("google_sheet_url", url);
                editor.apply();

                Toast.makeText(MainActivity.this, "URL guardada correctamente", Toast.LENGTH_SHORT).show();
                updateStatus();
            }
        });

        // Abrir configuración de notificaciones
        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        boolean hasPermission = isNotificationServiceEnabled();
        String url = prefs.getString("google_sheet_url", "");

        if (hasPermission && !url.isEmpty()) {
            statusText.setText("✅ Estado: ACTIVO\n\nLa app está capturando notificaciones de Gmail y enviándolas a Google Sheets.");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (!hasPermission) {
            statusText.setText("⚠️ Estado: INACTIVO\n\nNecesitas dar permiso de acceso a notificaciones.\n\nPresiona el botón de abajo para abrir la configuración.");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            statusText.setText("⚠️ Estado: CONFIGURACIÓN INCOMPLETA\n\nIngresa la URL de Google Apps Script arriba.");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (flat != null && !flat.isEmpty()) {
            final String[] names = flat.split(":");
            for (String name : names) {
                if (name.contains(pkgName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
