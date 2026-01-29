# 🔥 CONFIGURACIÓN RÁPIDA DE FIREBASE

## ⚡ **PASOS RÁPIDOS:**

### **1. Crear Proyecto Firebase**
```
1. Ve a: https://console.firebase.google.com/
2. Click "Agregar proyecto"
3. Nombre: NotificationCapture
4. Crear proyecto
```

### **2. Agregar App Android**
```
1. Click "Agregar app" → Android
2. Package name: com.yapemanager.notificationcapture
3. Registrar app
```

### **3. Descargar google-services.json**
```
1. Descargar el archivo
2. Reemplazar: app/google-services.json
```

### **4. Habilitar Realtime Database**
```
1. Ir a "Realtime Database"
2. Crear base de datos
3. Ubicación: United States
4. Modo: Prueba
5. Habilitar
```

### **5. Configurar Reglas**
```json
{
  "rules": {
    "payments": {
      ".read": true,
      ".write": true
    }
  }
}
```

### **6. Compilar**
```bash
# Subir cambios
git add .
git commit -m "Add Firebase integration"
git push

# Esperar GitHub Actions
# Descargar APK
# Instalar y probar
```

---

## 📊 **VER DATOS:**

### **Consola Web:**
```
https://console.firebase.google.com/
→ Tu Proyecto
→ Realtime Database
→ Ver datos en tiempo real
```

### **API REST:**
```bash
curl https://TU-PROYECTO.firebaseio.com/payments.json
```

---

## ✅ **VERIFICAR:**

1. ✅ Archivo `google-services.json` reemplazado
2. ✅ Realtime Database habilitado
3. ✅ Reglas configuradas
4. ✅ App compilada e instalada
5. ✅ Recibir un pago de prueba
6. ✅ Verificar en Firebase Console

---

## 📄 **DOCUMENTACIÓN COMPLETA:**

Ver: `FIREBASE_INTEGRATION.md`

---

¡Listo! 🎉
