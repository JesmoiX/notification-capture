# 🛡️ SISTEMA ANTI-DUPLICADOS

## 🎯 PROBLEMA RESUELTO

Cuando apagas y enciendes el celular, Android "re-publica" las notificaciones antiguas, causando duplicados en Google Sheets.

**Ejemplo:**
```
Apagaste celular a las 03:03
→ Notificaciones de YAPE 1-7 quedaron en el sistema
→ Encendiste celular a las 10:52
→ Android re-publicó YAPE 7 y 8
→ App las detectó como "nuevas"
→ DUPLICADOS en Google Sheets ❌
```

---

## ✅ SOLUCIÓN IMPLEMENTADA

He agregado **3 mecanismos** de detección de duplicados:

### **MECANISMO 1: ID Único de Notificación** 🔑

Cada notificación tiene un ID único (`notification.getKey()`).

**Cómo funciona:**
1. Cuando llega una notificación, extraemos su ID
2. Verificamos si ya procesamos ese ID antes
3. Si SÍ → **IGNORAMOS** (es duplicado)
4. Si NO → Procesamos y guardamos el ID

**Ejemplo:**
```
Notificación ID: "0|com.google.android.gm|123|null|10001"
→ Primera vez: Procesamos ✅
→ Segunda vez (al encender): IGNORAMOS ⚠️
```

---

### **MECANISMO 2: Timestamp de Notificación** ⏰

Verificamos cuándo se creó la notificación originalmente.

**Cómo funciona:**
1. Obtenemos `sbn.getPostTime()` (hora de creación)
2. Comparamos con la hora actual
3. Si la notificación tiene **más de 5 minutos** → **IGNORAMOS**

**Ejemplo:**
```
Notificación creada: 03:03:00
Hora actual: 10:52:00
Edad: 7 horas 49 minutos
→ IGNORAMOS (es antigua) ⚠️
```

**Por qué 5 minutos:**
- Emails normales llegan en segundos
- 5 minutos es suficiente margen
- Notificaciones más viejas son "re-publicaciones"

---

### **MECANISMO 3: Cache de IDs Procesados** 💾

Mantenemos una lista de los últimos 1000 IDs procesados.

**Cómo funciona:**
1. Guardamos IDs en memoria (HashSet)
2. Cuando llega notificación, verificamos si está en la lista
3. Si la lista crece mucho (>1000), la limpiamos

**Por qué 1000:**
- Suficiente para detectar duplicados del día
- No consume mucha memoria (~50KB)
- Se limpia automáticamente

---

## 📊 LOGS MEJORADOS

Ahora verás en los logs:

### **Notificación Nueva (Válida):**
```
✅ Gmail Notification VÁLIDA
Title: YAPE 9
Text: Transferencia recibida
Edad: 2s
```

### **Duplicado Detectado (ID):**
```
⚠️ DUPLICADO DETECTADO (ID)
Notificación ya procesada
ID: 0|com.google.android.gm|123|null|10001
```

### **Duplicado Detectado (Antiguo):**
```
⚠️ DUPLICADO DETECTADO (ANTIGUO)
Notificación de hace 28140 segundos
Ignorando
```

---

## 🧪 CÓMO PROBAR

### **Prueba 1: Apagar y encender celular**
1. Envíate 3 emails
2. Verifica que se registren en Google Sheets
3. **Apaga el celular**
4. Espera 1 minuto
5. **Enciende el celular**
6. Envíate 1 email nuevo
7. Verifica Google Sheets:
   - ✅ Solo debe aparecer el email nuevo
   - ✅ NO deben duplicarse los 3 anteriores

### **Prueba 2: Notificaciones antiguas**
1. Desactiva WiFi/Datos
2. Envíate un email (no llegará)
3. Espera 10 minutos
4. Activa WiFi/Datos
5. Llegará la notificación (pero es antigua)
6. Verifica Google Sheets:
   - ✅ NO debe registrarse (es antigua)

### **Prueba 3: Notificaciones normales**
1. Envíate un email
2. Debe llegar en segundos
3. Verifica Google Sheets:
   - ✅ Debe registrarse normalmente

---

## ⚙️ CONFIGURACIÓN

### **Tiempo máximo de antigüedad:**
```java
private static final long MAX_NOTIFICATION_AGE_MS = 5 * 60 * 1000; // 5 minutos
```

**Puedes ajustarlo:**
- `1 * 60 * 1000` = 1 minuto (más estricto)
- `10 * 60 * 1000` = 10 minutos (más permisivo)
- `30 * 60 * 1000` = 30 minutos (muy permisivo)

### **Tamaño del cache:**
```java
if (processedNotificationIds.size() > 1000) {
    processedNotificationIds.clear();
}
```

**Puedes ajustarlo:**
- `500` = Cache más pequeño
- `2000` = Cache más grande
- `5000` = Cache muy grande

---

## 📈 RENDIMIENTO

### **Impacto en memoria:**
- HashSet de 1000 IDs ≈ **50KB**
- Muy bajo impacto

### **Impacto en velocidad:**
- Verificación de ID: **< 1ms**
- Verificación de timestamp: **< 1ms**
- Total: **Imperceptible**

---

## ✅ VENTAJAS

1. ✅ **Elimina duplicados** al encender celular
2. ✅ **Ignora notificaciones antiguas** automáticamente
3. ✅ **Muy eficiente** (< 1ms por verificación)
4. ✅ **Bajo consumo de memoria** (~50KB)
5. ✅ **Auto-limpieza** del cache
6. ✅ **Logs claros** para debugging

---

## 🔍 CASOS ESPECIALES

### **¿Qué pasa si reinicio la app?**
- El cache se limpia
- Pero el mecanismo de timestamp sigue funcionando
- Notificaciones antiguas se ignoran igual

### **¿Qué pasa si llegan 2 emails idénticos?**
- Si son notificaciones diferentes → Se procesan ambas ✅
- Si es la misma notificación re-publicada → Se ignora la segunda ⚠️

### **¿Qué pasa si el celular estuvo apagado 1 día?**
- Al encender, todas las notificaciones antiguas se ignoran
- Solo se procesan las nuevas (< 5 minutos)

---

## 🎯 RESUMEN

**Antes:**
```
Apagar celular → Encender → Duplicados ❌
```

**Ahora:**
```
Apagar celular → Encender → Sin duplicados ✅
```

**Mecanismos:**
1. ✅ Verificación de ID único
2. ✅ Verificación de timestamp
3. ✅ Cache de IDs procesados

**Resultado:**
- ✅ Sin duplicados al encender celular
- ✅ Sin notificaciones antiguas
- ✅ Solo emails nuevos y válidos
