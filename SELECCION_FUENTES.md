# 📲 SELECCIÓN DE FUENTES: YAPE Y/O GMAIL

## 🎉 **NUEVA FUNCIONALIDAD:**

Ahora puedes elegir qué notificaciones capturar:
- ✅ Solo YAPE
- ✅ Solo Gmail  
- ✅ Ambos (YAPE + Gmail)

---

## ✅ **LO QUE SE AGREGÓ:**

### **1. Checkboxes de Selección** ☑️

En la app verás una nueva sección:

```
📲 Fuentes de Notificaciones

Selecciona qué notificaciones capturar:
☑ 💰 Notificaciones de YAPE
☐ 📧 Notificaciones de Gmail

💡 Puedes activar ambas fuentes simultáneamente
```

### **2. Validación Inteligente** 🛡️

- No puedes desactivar ambas fuentes
- Al menos una debe estar activa
- Si intentas desactivar la última, se reactiva automáticamente

### **3. Estado Dinámico** 📊

El estado de la app muestra las fuentes activas:

**Ejemplo 1 (Solo Yape):**
```
✅ Estado: ACTIVO

Capturando: YAPE
Enviando a Google Sheets.
```

**Ejemplo 2 (Ambos):**
```
✅ Estado: ACTIVO

Capturando: YAPE y Gmail
Enviando a Google Sheets.
```

**Ejemplo 3 (Solo Gmail):**
```
✅ Estado: ACTIVO

Capturando: Gmail
Enviando a Google Sheets.
```

---

## 🔧 **CÓMO USAR:**

### **Opción 1: Solo YAPE** 💰
1. Abre la app
2. Activa: ☑ Notificaciones de YAPE
3. Desactiva: ☐ Notificaciones de Gmail
4. Listo - Solo captura Yape

### **Opción 2: Solo Gmail** 📧
1. Abre la app
2. Desactiva: ☐ Notificaciones de YAPE
3. Activa: ☑ Notificaciones de Gmail
4. Listo - Solo captura Gmail

### **Opción 3: Ambos** 🎯 **RECOMENDADO**
1. Abre la app
2. Activa: ☑ Notificaciones de YAPE
3. Activa: ☑ Notificaciones de Gmail
4. Listo - Captura ambos

---

## 📊 **EJEMPLOS DE USO:**

### **Caso 1: Bodega (Solo Yape)**
```
Configuración:
☑ YAPE
☐ Gmail

Resultado:
- Captura pagos de Yape ✅
- Ignora emails ❌
- Perfecto para bodega
```

### **Caso 2: Negocio con emails de Yape (Ambos)**
```
Configuración:
☑ YAPE
☑ Gmail

Resultado:
- Captura pagos de Yape ✅
- Captura emails de confirmación ✅
- Doble registro
```

### **Caso 3: Solo emails (Solo Gmail)**
```
Configuración:
☐ YAPE
☑ Gmail

Resultado:
- Ignora notificaciones de Yape ❌
- Captura emails ✅
- Para quien no usa Yape
```

---

## 🎯 **FLUJO COMPLETO:**

```
Llega notificación
  ↓
¿Es de Yape?
  ↓ Sí
¿Yape activado? → Sí → Captura ✅
                → No → Ignora ❌
  ↓ No
¿Es de Gmail?
  ↓ Sí
¿Gmail activado? → Sí → Captura ✅
                 → No → Ignora ❌
  ↓ No
Ignora (no es Yape ni Gmail) ❌
```

---

## 📝 **LOGS:**

### **Yape activado, Gmail desactivado:**
```
Notification from: com.yape.app
✅ Notificación de YAPE detectada
✅ YAPE Notification VÁLIDA
```

```
Notification from: com.google.android.gm
Gmail desactivado en configuración, ignorando
```

### **Ambos activados:**
```
Notification from: com.yape.app
✅ Notificación de YAPE detectada
✅ YAPE Notification VÁLIDA
```

```
Notification from: com.google.android.gm
✅ Notificación de Gmail detectada
✅ Gmail Notification VÁLIDA
```

---

## ⚙️ **CONFIGURACIÓN GUARDADA:**

Las preferencias se guardan automáticamente:

```
SharedPreferences:
- capture_yape: true/false
- capture_gmail: true/false
```

Al cerrar y abrir la app, se mantienen tus preferencias.

---

## 🔍 **DETALLES TÉCNICOS:**

### **Detección de Yape:**
```java
boolean isYape = packageName.contains("yape");
```

Detecta:
- `com.yape.app` ✅
- `com.yape` ✅
- `pe.com.yape` ✅
- Cualquier variante con "yape" ✅

### **Detección de Gmail:**
```java
boolean isGmail = packageName.equals("com.google.android.gm");
```

Detecta:
- `com.google.android.gm` ✅ (Gmail oficial)

---

## 💡 **RECOMENDACIONES:**

### **Para bodega:**
```
☑ YAPE
☐ Gmail
```
Solo necesitas los pagos directos.

### **Para negocio con contabilidad:**
```
☑ YAPE
☑ Gmail
```
Doble registro para mayor seguridad.

### **Para uso personal:**
```
☑ YAPE
☑ Gmail
```
Captura todo.

---

## 🎯 **VENTAJAS:**

1. ✅ **Flexibilidad:** Elige qué capturar
2. ✅ **Ahorro de datos:** Solo captura lo necesario
3. ✅ **Menos ruido:** Filtra lo que no necesitas
4. ✅ **Fácil de cambiar:** Un toque y listo
5. ✅ **Validación:** No puedes desactivar todo

---

## 📱 **INTERFAZ:**

```
┌─────────────────────────────────┐
│ 📲 Fuentes de Notificaciones   │
├─────────────────────────────────┤
│ Selecciona qué notificaciones   │
│ capturar:                       │
│                                 │
│ ☑ 💰 Notificaciones de YAPE    │
│ ☑ 📧 Notificaciones de Gmail   │
│                                 │
│ 💡 Puedes activar ambas fuentes│
│    simultáneamente              │
└─────────────────────────────────┘
```

---

## 🧪 **PRUEBAS:**

### **Prueba 1: Solo Yape**
1. Activa solo Yape
2. Yapéate
3. Verifica que se capture ✅
4. Envíate email
5. Verifica que NO se capture ❌

### **Prueba 2: Solo Gmail**
1. Activa solo Gmail
2. Envíate email
3. Verifica que se capture ✅
4. Yapéate
5. Verifica que NO se capture ❌

### **Prueba 3: Ambos**
1. Activa ambos
2. Yapéate
3. Verifica que se capture ✅
4. Envíate email
5. Verifica que se capture ✅

---

## 🎯 **RESUMEN:**

**Antes:**
```
Solo YAPE (fijo)
```

**Ahora:**
```
☑ YAPE
☑ Gmail
☑ Ambos
```

**Más flexible, más potente** ✅

---

¡Listo para usar! 🎉
