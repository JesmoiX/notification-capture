package com.yapemanager.notificationcapture;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

    // Device code
    private TextView deviceCodeText;
    private TextView deviceStatusText;
    private Button copyCodeButton;
    private Button refreshStatusButton;
    private DeviceCodeManager deviceCodeManager;

    // Source selection
    private CheckBox yapeCheckbox;
    private CheckBox gmailCheckbox;

    // Google Home controls
    private SwitchCompat googleHomeSwitch;

    private Button testAnnouncementButton;

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

        // Device code views
        deviceCodeText = findViewById(R.id.deviceCodeText);
        deviceStatusText = findViewById(R.id.deviceStatusText);
        copyCodeButton = findViewById(R.id.copyCodeButton);
        refreshStatusButton = findViewById(R.id.refreshStatusButton);

        // Source checkboxes
        yapeCheckbox = findViewById(R.id.yapeCheckbox);
        gmailCheckbox = findViewById(R.id.gmailCheckbox);

        // Google Home controls
        // Google Home controls
        googleHomeSwitch = findViewById(R.id.googleHomeSwitch);
        testAnnouncementButton = findViewById(R.id.testAnnouncementButton);

        // Inicializar DeviceCodeManager
        deviceCodeManager = new DeviceCodeManager(this);
        updateDeviceCodeDisplay();

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

        // Copiar código de dispositivo
        copyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(
                        CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Device Code",
                        deviceCodeManager.getDeviceCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "✅ Código copiado: " + deviceCodeManager.getDeviceCode(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Actualizar estado desde Firebase
        refreshStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "🔄 Actualizando estado...", Toast.LENGTH_SHORT).show();
                deviceCodeManager.updateStatusFromFirebase(new DeviceCodeManager.StatusCallback() {
                    @Override
                    public void onStatusUpdated(String status) {
                        runOnUiThread(() -> {
                            updateDeviceCodeDisplay();
                            Toast.makeText(MainActivity.this, "✅ Estado actualizado: " + status, Toast.LENGTH_SHORT)
                                    .show();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "❌ Error: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });

        // Checkboxes de fuentes
        yapeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("capture_yape", isChecked);
            editor.apply();

            // Validar que al menos una fuente esté activa
            if (!isChecked && !gmailCheckbox.isChecked()) {
                Toast.makeText(this, "⚠️ Debes activar al menos una fuente", Toast.LENGTH_SHORT).show();
                yapeCheckbox.setChecked(true);
                return;
            }

            Toast.makeText(this,
                    isChecked ? "✅ Yape activado" : "❌ Yape desactivado",
                    Toast.LENGTH_SHORT).show();
            updateStatus();
        });

        gmailCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("capture_gmail", isChecked);
            editor.apply();

            // Validar que al menos una fuente esté activa
            if (!isChecked && !yapeCheckbox.isChecked()) {
                Toast.makeText(this, "⚠️ Debes activar al menos una fuente", Toast.LENGTH_SHORT).show();
                gmailCheckbox.setChecked(true);
                return;
            }

            Toast.makeText(this,
                    isChecked ? "✅ Gmail activado" : "❌ Gmail desactivado",
                    Toast.LENGTH_SHORT).show();
            updateStatus();
        });

        // Switch de Google Home
        googleHomeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("google_home_enabled", isChecked);
            editor.apply();

            // Mostrar/ocultar controles de horario y prueba
            // Mostrar/ocultar controles de horario y prueba
            testAnnouncementButton.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            Toast.makeText(MainActivity.this,
                    isChecked ? "🔊 Anuncios activados" : "🔇 Anuncios desactivados",
                    Toast.LENGTH_SHORT).show();
        });

        // Botón de prueba
        testAnnouncementButton.setOnClickListener(v -> {
            GoogleHomeAnnouncer announcer = new GoogleHomeAnnouncer(this);
            announcer.announceYapePayment("Prueba", "Este es un mensaje de prueba");
            Toast.makeText(this, "🔊 Anuncio de prueba enviado", Toast.LENGTH_SHORT).show();
        });

        // ═══════════════════════════════════════════════════════════
        // UI NAVIGATION LOGIC (New Design)
        // ═══════════════════════════════════════════════════════════
        View dashboardScreen = findViewById(R.id.dashboard_screen);
        View settingsScreen = findViewById(R.id.settings_screen);
        View toSettingsBtn = findViewById(R.id.toSettingsBtn);
        View backToDashboardBtn = findViewById(R.id.backToDashboardBtn);

        if (toSettingsBtn != null && settingsScreen != null) {
            toSettingsBtn.setOnClickListener(v -> {
                dashboardScreen.setVisibility(View.GONE);
                settingsScreen.setVisibility(View.VISIBLE);
            });
        }

        if (backToDashboardBtn != null && dashboardScreen != null) {
            backToDashboardBtn.setOnClickListener(v -> {
                settingsScreen.setVisibility(View.GONE);
                dashboardScreen.setVisibility(View.VISIBLE);
            });
        }
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

        // Cargar fuentes de notificaciones
        boolean captureYape = prefs.getBoolean("capture_yape", true);
        boolean captureGmail = prefs.getBoolean("capture_gmail", false);
        yapeCheckbox.setChecked(captureYape);
        gmailCheckbox.setChecked(captureGmail);

        // Mostrar/ocultar controles según estado
        testAnnouncementButton.setVisibility(googleHomeEnabled ? View.VISIBLE : View.GONE);
    }

    private void updateStatus() {
        boolean hasPermission = isNotificationServiceEnabled();
        String url = prefs.getString("google_sheet_url", "");
        boolean googleHomeEnabled = prefs.getBoolean("google_home_enabled", false);
        boolean captureYape = prefs.getBoolean("capture_yape", true);
        boolean captureGmail = prefs.getBoolean("capture_gmail", false);

        StringBuilder status = new StringBuilder();

        if (hasPermission && !url.isEmpty()) {
            status.append("✅ Estado: ACTIVO\n\n");

            // Mostrar fuentes activas
            status.append("Capturando: ");
            if (captureYape && captureGmail) {
                status.append("YAPE y Gmail");
            } else if (captureYape) {
                status.append("YAPE");
            } else if (captureGmail) {
                status.append("Gmail");
            }
            status.append("\nEnviando a Google Sheets.");

            if (googleHomeEnabled) {
                status.append("\n\n🔊 Anuncios de voz: ACTIVADOS");
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

    private void updateDeviceCodeDisplay() {
        String code = deviceCodeManager.getDeviceCode();
        String status = deviceCodeManager.getDeviceStatus();

        deviceCodeText.setText(code);

        // Actualizar texto y color según el estado
        if ("approved".equals(status)) {
            deviceStatusText.setText("✅ Estado: Aprobado - Captura activa");
            deviceStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if ("rejected".equals(status)) {
            deviceStatusText.setText("❌ Estado: Rechazado - Contacta al administrador");
            deviceStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            deviceStatusText.setText("⏳ Estado: Pendiente de aprobación");
            deviceStatusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }
}
