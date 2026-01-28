# 🔊 INTEGRACIÓN CON GOOGLE HOME

## 🎯 FUNCIONALIDAD

La app ahora puede anunciar automáticamente los pagos de YAPE en tu Google Home/Nest Mini.

---

## ✅ CARACTERÍSTICAS IMPLEMENTADAS

### **1. Detección Automática de YAPE**
- Solo anuncia emails que contengan "YAPE" en título o contenido
- Filtra automáticamente otros emails

### **2. Formato de Anuncio**
```
"Nuevo pago recibido. [título]. [contenido]"
```

**Ejemplo:**
```
Email recibido:
- Título: "Juan Pérez"
- Contenido: "Te transfirió S/ 50.00"

Google Home anuncia:
"Nuevo pago recibido. Juan Pérez. Te transfirió 50 soles"
```

### **3. Horario Configurable**
- Puedes configurar desde qué hora hasta qué hora anunciar
- Por defecto: 08:00 - 20:00
- Fuera de ese horario, NO anuncia (pero sigue guardando en Google Sheets)

### **4. Activación/Desactivación**
- Switch para activar/desactivar anuncios
- Cuando está desactivado, solo guarda en Google Sheets

### **5. Botón de Prueba**
- Prueba el anuncio sin necesidad de recibir un email real

---

## ⚙️ CONFIGURACIÓN

### **Paso 1: Activar Anuncios**
1. Abre la app "Notification Capture"
2. Baja hasta "🔊 Anuncios en Google Home"
3. Activa el switch "Activar anuncios de YAPE"

### **Paso 2: Configurar Horario**
1. Aparecerá la sección "⏰ Horario de anuncios"
2. Toca "Desde:" para elegir hora de inicio (ej: 08:00)
3. Toca "Hasta:" para elegir hora de fin (ej: 20:00)
4. El horario se guarda automáticamente

### **Paso 3: Probar**
1. Toca el botón "🔊 Probar Anuncio"
2. Tu Google Home debería anunciar: "Nuevo pago recibido. Prueba. Este es un mensaje de prueba"

---

## 📱 REQUISITOS

Para que funcione correctamente:

1. ✅ **Google Home/Nest Mini** configurado
2. ✅ **Misma red WiFi** (celular y Google Home)
3. ✅ **App Google Home** instalada
4. ✅ **Google Assistant** activado
5. ✅ **Cuenta Google** vinculada

---

## 🔄 FLUJO COMPLETO

```
1. Llega email de YAPE
   ↓
2. App captura notificación
   ↓
3. Verifica si contiene "YAPE" ✅
   ↓
4. Verifica si anuncios están activados ✅
   ↓
5. Verifica si está dentro del horario ✅
   ↓
6. Envía a Google Sheets ✅
   ↓
7. Envía broadcast a Google Home 🔊
   ↓
8. Google Home anuncia el pago
```

---

## 🧪 CÓMO PROBAR

### **Prueba 1: Botón de prueba**
1. Activa el switch de Google Home
2. Toca "🔊 Probar Anuncio"
3. Escucha el anuncio en Google Home

### **Prueba 2: Email real**
1. Envíate un email con "YAPE" en el asunto
2. Ejemplo: Asunto "YAPE - Prueba"
3. Contenido: "Transferencia de S/ 10"
4. Espera el anuncio

### **Prueba 3: Horario**
1. Configura horario: 15:00 - 16:00
2. Envíate email a las 15:30 → Debe anunciar ✅
3. Envíate email a las 17:00 → NO debe anunciar ❌

---

## ⚠️ SOLUCIÓN DE PROBLEMAS

### **Problema: No anuncia nada**

**Solución:**
1. Verifica que el switch esté activado
2. Verifica que estés dentro del horario
3. Verifica que el email contenga "YAPE"
4. Verifica que Google Home esté encendido
5. Verifica que estén en la misma red WiFi

### **Problema: Anuncia pero no se escucha**

**Solución:**
1. Verifica volumen de Google Home
2. Di "Ok Google, sube el volumen"
3. Prueba con el botón de prueba

### **Problema: Dice "No entiendo" o no responde**

**Solución:**
1. Verifica que Google Assistant esté activado
2. Abre la app Google Home
3. Verifica que el dispositivo esté online
4. Reinicia Google Home

---

## 🎛️ CONFIGURACIÓN AVANZADA

### **Cambiar horario:**
- Toca los botones de hora
- Selecciona nueva hora
- Se guarda automáticamente

### **Desactivar temporalmente:**
- Desactiva el switch
- Los emails se seguirán guardando en Google Sheets
- Pero NO se anunciarán

### **Activar 24/7:**
- Configura: 00:00 - 23:59
- Anunciará a cualquier hora

---

## 📊 LOGS

En los logs verás:

### **Anuncio enviado:**
```
🔊 Anuncio enviado a Google Home
```

### **Anuncios desactivados:**
```
Google Home anuncios desactivados
```

### **No contiene YAPE:**
```
No contiene YAPE, no se anuncia
```

### **Fuera de horario:**
```
Fuera de horario configurado, no se anuncia
```

---

## 💡 CONSEJOS

1. **Horario de trabajo:** Configura 08:00 - 20:00 para no molestar de noche
2. **Prueba primero:** Usa el botón de prueba antes de esperar emails reales
3. **Volumen:** Ajusta el volumen de Google Home según tu preferencia
4. **Red WiFi:** Asegúrate de estar en la misma red

---

## 🔮 FUTURAS MEJORAS (Opcional)

Si quieres, puedo agregar:

1. **Sonido previo:** Reproducir "ding" antes del anuncio
2. **Filtro de monto:** Solo anunciar pagos mayores a X soles
3. **Voz personalizada:** Cambiar voz de Google Home
4. **Múltiples dispositivos:** Elegir en qué Google Home anunciar

---

## 🎯 RESUMEN

**Configuración:**
1. ✅ Activar switch de Google Home
2. ✅ Configurar horario (ej: 08:00 - 20:00)
3. ✅ Probar con botón de prueba

**Resultado:**
- Emails con "YAPE" → Se anuncian en Google Home 🔊
- Emails sin "YAPE" → Solo se guardan en Google Sheets
- Fuera de horario → Solo se guardan en Google Sheets

**Formato:**
```
"Nuevo pago recibido. [título]. [contenido]"
```

¡Listo! 🎉
