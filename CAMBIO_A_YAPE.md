# 🎉 CAMBIO A NOTIFICACIONES DE YAPE REAL

## 🔄 **CAMBIOS REALIZADOS:**

La app ahora captura notificaciones de **YAPE** en lugar de Gmail.

---

## ✅ **LO QUE CAMBIÓ:**

### **Antes:**
- ✅ Capturaba notificaciones de Gmail
- ✅ Filtraba emails con "YAPE" en el contenido

### **Ahora:**
- ✅ Captura notificaciones de **YAPE directamente**
- ✅ Todas las notificaciones de Yape se procesan
- ✅ Mismo sistema anti-duplicados
- ✅ Mismo sistema de anuncios de voz
- ✅ Mismo sistema de horarios

---

## 📱 **EJEMPLO DE NOTIFICACIÓN CAPTURADA:**

Basado en tu captura:

```
App: Yape
Título: "Confirmación de Pago"
Contenido: "Yanina Man* te envió un pago por S/ 56.7. El cód. de seguridad es: 666"
```

**Se guardará en Google Sheets:**
```
| Fecha/Hora | Título | Contenido | App |
| 28/01 11:19 | Confirmación de Pago | Yanina Man* te envió... | com.yape.app |
```

**Se anunciará en el celular:**
```
"Nuevo pago recibido. Confirmación de Pago. Yanina Man te envió un pago por 56 soles 7"
```

---

## 🔧 **CONFIGURACIÓN:**

### **Package Names detectados:**
La app detecta cualquier package que contenga "yape":
- `com.yape.app` ✅
- `com.yape` ✅
- `pe.com.yape` ✅
- Cualquier variante con "yape" ✅

---

## 🧪 **CÓMO PROBAR:**

### **Paso 1: Instalar nueva versión**
1. Espera a que compile en GitHub Actions
2. Descarga el APK
3. Desinstala versión anterior
4. Instala nueva versión

### **Paso 2: Configurar**
1. Abre la app
2. Verifica que diga: "Captura notificaciones de YAPE"
3. Activa switch de anuncios de voz (opcional)
4. Configura horario (opcional)

### **Paso 3: Dar permisos**
1. Toca "Configurar Permisos de Notificación"
2. Busca "Notification Capture"
3. Activa el permiso

### **Paso 4: Probar con Yape real**
1. Pide a alguien que te yapee
2. O yapéate a ti mismo
3. Espera la notificación de Yape
4. Verifica:
   - ✅ Se guarda en Google Sheets
   - ✅ Se anuncia en el celular (si activaste anuncios)

---

## 📊 **DATOS CAPTURADOS:**

De la notificación de Yape se captura:

**Título:**
- "Confirmación de Pago"
- O el título que Yape envíe

**Contenido:**
- Nombre de quien envió
- Monto
- Código de seguridad
- Todo el texto de la notificación

**Metadata:**
- Fecha y hora
- Package name (com.yape.app)
- Timestamp

---

## ⚙️ **FUNCIONALIDADES ACTIVAS:**

Todas las funcionalidades anteriores siguen funcionando:

1. ✅ **Anti-duplicados:**
   - Verifica ID único
   - Verifica timestamp (< 5 minutos)
   - Cache de IDs procesados

2. ✅ **Anuncios de voz:**
   - TTS local en el celular
   - Horario configurable
   - Activación/desactivación

3. ✅ **Google Sheets:**
   - Envío automático
   - Registro completo
   - Timestamp preciso

4. ✅ **Foreground Service:**
   - Funciona con celular bloqueado
   - Contador de capturas
   - Notificación permanente

---

## 🎯 **FLUJO COMPLETO:**

```
1. Alguien te yapea
   ↓
2. Yape muestra notificación
   ↓
3. App captura (verifica que sea Yape) ✅
   ↓
4. Verifica que no sea duplicado ✅
   ↓
5. Verifica que no sea antiguo (< 5 min) ✅
   ↓
6. Guarda en Google Sheets ✅
   ↓
7. Si anuncios activados:
   - Verifica horario ✅
   - Anuncia en celular 🔊
   ↓
8. Actualiza contador
```

---

## 📝 **NOTAS IMPORTANTES:**

### **Permisos:**
- Necesitas dar permiso de "Acceso a notificaciones"
- Es el mismo permiso que antes
- Solo cambió la app que captura (de Gmail a Yape)

### **Optimización de batería:**
- Sigue las instrucciones de `CONFIGURACION_CELULAR.md`
- Desactiva optimización de batería
- Bloquea app en recientes

### **Anuncios de voz:**
- Se escuchan en el **celular**, no en Google Home
- Sube volumen del celular
- Configura horario según tus necesidades

---

## 🔍 **LOGS:**

En los logs verás:

**Notificación detectada:**
```
Notification from: com.yape.app
✅ Notificación de YAPE detectada: com.yape.app
```

**Notificación válida:**
```
✅ YAPE Notification VÁLIDA
Title: Confirmación de Pago
Text: Yanina Man* te envió...
Edad: 2s
```

**Anuncio enviado:**
```
🔊 Anuncio de voz enviado
```

---

## ⚠️ **SOLUCIÓN DE PROBLEMAS:**

### **No captura notificaciones:**
1. Verifica permisos de notificación
2. Verifica que Yape esté instalado
3. Verifica que la app esté activa
4. Revisa logs

### **No anuncia:**
1. Verifica que switch esté activado
2. Verifica que estés dentro del horario
3. Verifica volumen del celular
4. Revisa logs

### **Se duplican registros:**
1. No debería pasar (anti-duplicados activo)
2. Si pasa, revisa logs
3. Reporta el problema

---

## 🎯 **RESUMEN:**

**Antes:**
```
Gmail → Filtra "YAPE" → Guarda → Anuncia
```

**Ahora:**
```
YAPE → Guarda → Anuncia
```

**Más simple, más directo, más confiable** ✅

---

**¡Listo para probar con Yape real!** 🎉
