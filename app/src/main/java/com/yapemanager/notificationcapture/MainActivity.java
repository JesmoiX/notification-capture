package com.yapemanager.notificationcapture;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private EditText urlInput;
    private Button saveButton;
    private Button openSettingsButton;
    private TextView statusText;
    private SharedPreferences prefs;
    
    // Google Home controls
    private SwitchCompat googleHomeSwitch;
    private LinearLayout scheduleLayout;
    private Button startTimeButton;
    private Button endTimeButton;
    private Button testAnnouncementButton;
    
    private int startHour = 8;
    private int startMinute = 0;
    private int endHour = 20;
    private int endMinute = 0;

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
        
        // Google Home controls
        googleHomeSwitch = findViewById(R.id.googleHomeSwitch);
        scheduleLayout = findViewById(R.id.scheduleLayout);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        testAnnouncementButton = findViewById(R.id.testAnnouncementButton);

        // Cargar configuración guardada
        loadSavedSettings();

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
        
        // Switch de Google Home
        googleHomeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("google_home_enabled", isChecked);
            editor.apply();
            
            // Mostrar/ocultar controles de horario y prueba
            scheduleLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            testAnnouncementButton.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            
            Toast.makeText(MainActivity.this, 
                isChecked ? "🔊 Anuncios activados" : "🔇 Anuncios desactivados", 
                Toast.LENGTH_SHORT).show();
        });
        
        // Botón de hora de inicio
        startTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, hourOfDay, minute) -> {
                    startHour = hourOfDay;
                    startMinute = minute;
                    startTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                    saveSchedule();
                },
                startHour,
                startMinute,
                true
            );
            timePickerDialog.show();
        });
        
        // Botón de hora de fin
        endTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, hourOfDay, minute) -> {
                    endHour = hourOfDay;
                    endMinute = minute;
                    endTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                    saveSchedule();
                },
                endHour,
                endMinute,
                true
            );
            timePickerDialog.show();
        });
        
        // Botón de prueba
        testAnnouncementButton.setOnClickListener(v -> {
            GoogleHomeAnnouncer announcer = new GoogleHomeAnnouncer(this);
            announcer.announceYapePayment("Prueba", "Este es un mensaje de prueba");
            Toast.makeText(this, "🔊 Anuncio de prueba enviado", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }
    
    private void loadSavedSettings() {
        // Cargar URL
        String savedUrl = prefs.getString("google_sheet_url", "");
        urlInput.setText(savedUrl);
        
        // Cargar configuración de Google Home
        boolean googleHomeEnabled = prefs.getBoolean("google_home_enabled", false);
        googleHomeSwitch.setChecked(googleHomeEnabled);
        
        // Cargar horario
        startHour = prefs.getInt("announce_start_hour", 8);
        startMinute = prefs.getInt("announce_start_minute", 0);
        endHour = prefs.getInt("announce_end_hour", 20);
        endMinute = prefs.getInt("announce_end_minute", 0);
        
        startTimeButton.setText(String.format("%02d:%02d", startHour, startMinute));
        endTimeButton.setText(String.format("%02d:%02d", endHour, endMinute));
        
        // Mostrar/ocultar controles según estado
        scheduleLayout.setVisibility(googleHomeEnabled ? View.VISIBLE : View.GONE);
        testAnnouncementButton.setVisibility(googleHomeEnabled ? View.VISIBLE : View.GONE);
    }
    
    private void saveSchedule() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("announce_start_hour", startHour);
        editor.putInt("announce_start_minute", startMinute);
        editor.putInt("announce_end_hour", endHour);
        editor.putInt("announce_end_minute", endMinute);
        editor.apply();
        
        Toast.makeText(this, "⏰ Horario guardado: " + 
            String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute), 
            Toast.LENGTH_SHORT).show();
    }

    private void updateStatus() {
        boolean hasPermission = isNotificationServiceEnabled();
        String url = prefs.getString("google_sheet_url", "");
        boolean googleHomeEnabled = prefs.getBoolean("google_home_enabled", false);

        StringBuilder status = new StringBuilder();
        
        if (hasPermission && !url.isEmpty()) {
            status.append("✅ Estado: ACTIVO\n\n");
            status.append("La app está capturando notificaciones de Gmail y enviándolas a Google Sheets.");
            
            if (googleHomeEnabled) {
                status.append("\n\n🔊 Anuncios de Google Home: ACTIVADOS");
                status.append("\n⏰ Horario: ");
                status.append(String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute));
            }
            
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (!hasPermission) {
            status.append("⚠️ Estado: INACTIVO\n\n");
            status.append("Necesitas dar permiso de acceso a notificaciones.\n\n");
            status.append("Presiona el botón de abajo para abrir la configuración.");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            status.append("⚠️ Estado: CONFIGURACIÓN INCOMPLETA\n\n");
            status.append("Ingresa la URL de Google Apps Script arriba.");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
        
        statusText.setText(status.toString());
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
