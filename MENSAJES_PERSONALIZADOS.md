# 🔊 MENSAJES DE VOZ PERSONALIZADOS

## 🎉 **NUEVA FUNCIONALIDAD:**

Ahora los anuncios de voz son **personalizados y más claros**:

### **ANTES:**
```
"Nuevo pago recibido. Confirmación de Pago. Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00"
```

### **AHORA:**
```
"CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Jesús Moisés Gallegos Parlona DE 1.00 SOLES"
```

---

## ✅ **LO QUE SE AGREGÓ:**

### **1. Extracción Inteligente de Datos** 🧠

La app ahora **extrae automáticamente**:
- ✅ **Nombre de la persona** que envió el pago
- ✅ **Monto exacto** del pago

### **2. Mensaje Personalizado** 📢

**Formato fijo:**
```
CONFIRMACIÓN DE PAGO
RECIBIÓ UN YAPE DE [nombre]
DE [monto] SOLES
```

### **3. Volumen Máximo Automático** 🔊

- ✅ **Aumenta el volumen al máximo** automáticamente
- ✅ **Se escucha incluso en TikTok, YouTube, etc.**
- ✅ **Restaura el volumen** después de 10 segundos
- ✅ **Funciona con celular bloqueado**

---

## 📊 **EJEMPLOS:**

### **Ejemplo 1: Pago de Yape**

**Notificación recibida:**
```
Título: Confirmación de Pago
Contenido: Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00
```

**Mensaje de voz:**
```
CONFIRMACIÓN DE PAGO
RECIBIÓ UN YAPE DE Jesús Moisés Gallegos Parlona
DE 1.00 SOLES
```

### **Ejemplo 2: Pago de S/ 50.00**

**Notificación recibida:**
```
Título: Confirmación de Pago
Contenido: Yape! María González te envió un pago por S/ 50.00
```

**Mensaje de voz:**
```
CONFIRMACIÓN DE PAGO
RECIBIÓ UN YAPE DE María González
DE 50.00 SOLES
```

### **Ejemplo 3: Gmail (para pruebas)**

**Notificación recibida:**
```
Título: Confirmación de Pago
Contenido: Yape! Pedro Sánchez te envió un pago por S/ 100.50
```

**Mensaje de voz:**
```
CONFIRMACIÓN DE PAGO
RECIBIÓ UN YAPE DE Pedro Sánchez
DE 100.50 SOLES
```

---

## 🔧 **CÓMO FUNCIONA:**

### **Paso 1: Extracción de Nombre**

```
Contenido: "Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00"
           ↓
Busca: "te envió"
           ↓
Extrae: "Yape! Jesús Moisés Gallegos Parlona"
           ↓
Limpia: "Jesús Moisés Gallegos Parlona"
```

### **Paso 2: Extracción de Monto**

```
Contenido: "Yape! Jesús Moisés Gallegos Parlona te envió un pago por S/ 1.00"
           ↓
Busca: "S/"
           ↓
Extrae: "1.00"
```

### **Paso 3: Construcción del Mensaje**

```
"CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE " + nombre + " DE " + monto + " SOLES"
```

### **Paso 4: Volumen Máximo**

```
1. Guarda volumen actual: 50%
2. Aumenta a máximo: 100%
3. Reproduce mensaje
4. Espera 10 segundos
5. Restaura volumen: 50%
```

---

## 🎯 **VENTAJAS:**

### **1. Más Claro y Profesional** 💼
```
❌ ANTES: "Nuevo pago recibido. Confirmación de Pago. Yape! Jesús..."
✅ AHORA: "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Jesús DE 1.00 SOLES"
```

### **2. Información Esencial** 📊
```
✅ Nombre de quien paga
✅ Monto exacto
✅ Sin información redundante
```

### **3. Se Escucha Siempre** 🔊
```
✅ En TikTok
✅ En YouTube
✅ En WhatsApp
✅ En cualquier app
✅ Con celular bloqueado
```

### **4. Volumen Inteligente** 🎚️
```
✅ Aumenta automáticamente
✅ Se escucha fuerte y claro
✅ Restaura después de 10 segundos
✅ No afecta tu música/videos
```

---

## 🧪 **PRUEBAS:**

### **Prueba 1: Yape Real**
1. Pide a alguien que te yapee S/ 1.00
2. Escucha el mensaje:
   ```
   "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE [nombre] DE 1.00 SOLES"
   ```
3. Verifica que se escuche fuerte y claro

### **Prueba 2: Con TikTok Abierto**
1. Abre TikTok y reproduce un video
2. Pide que te yapeen
3. El anuncio debería interrumpir el video
4. Se escucha fuerte y claro
5. Después de 10 segundos, el volumen vuelve a la normalidad

### **Prueba 3: Con Celular Bloqueado**
1. Bloquea el celular
2. Pide que te yapeen
3. El anuncio se escucha incluso con celular bloqueado
4. Verifica en Google Sheets que se registró

### **Prueba 4: Gmail (para pruebas)**
1. Envíate un email con:
   ```
   Asunto: Confirmación de Pago
   Contenido: Yape! Pedro Sánchez te envió un pago por S/ 100.50
   ```
2. Escucha el mensaje:
   ```
   "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Pedro Sánchez DE 100.50 SOLES"
   ```

---

## 📝 **LOGS:**

### **Extracción Exitosa:**
```
📝 Mensaje extraído - Nombre: Jesús Moisés Gallegos Parlona, Monto: 1.00
🔊 Mensaje personalizado: CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Jesús Moisés Gallegos Parlona DE 1.00 SOLES
🔊 Volumen aumentado: 7 → 15
🔊 Anuncio local reproducido con volumen máximo
🔊 Volumen restaurado: 15 → 7
```

### **Fallback (si falla la extracción):**
```
Error al extraer datos: [error]
🔊 Mensaje personalizado: CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE desconocido DE 0 SOLES
```

---

## 🔍 **DETALLES TÉCNICOS:**

### **Patrones de Extracción:**

**Nombre:**
```java
content.split("te envió")
→ "Yape! Jesús Moisés Gallegos Parlona"
→ .replace("Yape!", "").replace("!", "").trim()
→ "Jesús Moisés Gallegos Parlona"
```

**Monto:**
```java
content.split("S/")
→ " 1.00"
→ Extraer solo dígitos y punto decimal
→ "1.00"
```

### **Control de Volumen:**

```java
AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

// Guardar volumen actual
int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

// Aumentar al máximo
audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

// Reproducir TTS
tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "announcement");

// Restaurar después de 10 segundos
new Handler().postDelayed(() -> {
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
}, 10000);
```

---

## 💡 **CASOS DE USO:**

### **Bodega:**
```
Cliente yapea S/ 5.00
↓
Escuchas: "CONFIRMACIÓN DE PAGO. RECIBIÓ UN YAPE DE Juan Pérez DE 5.00 SOLES"
↓
Sabes inmediatamente quién pagó y cuánto
↓
Entregas el producto
```

### **Negocio:**
```
Cliente yapea S/ 150.00
↓
Estás atendiendo a otro cliente
↓
Escuchas el anuncio fuerte y claro
↓
Sabes que María González pagó S/ 150.00
↓
Confirmas el pago sin revisar el celular
```

---

## 🎯 **RESUMEN:**

**Mensaje Personalizado:**
```
✅ "CONFIRMACIÓN DE PAGO"
✅ "RECIBIÓ UN YAPE DE [nombre]"
✅ "DE [monto] SOLES"
```

**Volumen Inteligente:**
```
✅ Aumenta automáticamente
✅ Se escucha en cualquier app
✅ Restaura después de 10 segundos
✅ Funciona con celular bloqueado
```

**Extracción Automática:**
```
✅ Nombre de quien paga
✅ Monto exacto
✅ Fallback si falla
```

---

## 🔧 **COMPATIBILIDAD:**

### **Funciona con:**
- ✅ Yape (notificaciones reales)
- ✅ Gmail (para pruebas)
- ✅ Celular bloqueado
- ✅ TikTok abierto
- ✅ YouTube abierto
- ✅ WhatsApp abierto
- ✅ Cualquier app en primer plano

### **Requiere:**
- ✅ Anuncios de voz activados
- ✅ Horario configurado (opcional)
- ✅ Fuente activada (Yape o Gmail)

---

¡Listo para usar! 🎉

**Ahora los anuncios son más claros, profesionales y se escuchan siempre** ✅
