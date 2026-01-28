# 📱 GUÍA DE CONFIGURACIÓN DEL CELULAR

## 🎯 OBJETIVO
Configurar tu celular para que la app capture notificaciones **SIEMPRE**, incluso con el celular bloqueado.

---

## ⚙️ CONFIGURACIONES NECESARIAS

### 1️⃣ **DESACTIVAR OPTIMIZACIÓN DE BATERÍA** ⭐ **MUY IMPORTANTE**

#### **Android Stock / Google Pixel:**
```
Configuración → Batería → Optimización de batería
→ Todas las apps → Notification Capture → No optimizar
```

#### **Samsung:**
```
Configuración → Aplicaciones → Notification Capture
→ Batería → Optimizar uso de batería → Desactivar
→ Permitir actividad en segundo plano → Activar
```

#### **Xiaomi / Redmi / POCO:**
```
Configuración → Aplicaciones → Administrar aplicaciones
→ Notification Capture → Ahorro de batería → Sin restricciones

Y TAMBIÉN:
Configuración → Aplicaciones → Administrar aplicaciones
→ Notification Capture → Inicio automático → Activar
```

#### **Huawei:**
```
Configuración → Batería → Inicio de aplicaciones
→ Notification Capture → Administrar manualmente
→ Activar: Inicio automático, Actividad secundaria, Ejecutar en segundo plano
```

#### **OnePlus / Oppo / Realme:**
```
Configuración → Batería → Optimización de batería
→ Todas las apps → Notification Capture → No optimizar

Y TAMBIÉN:
Configuración → Batería → Administración de batería de aplicaciones
→ Notification Capture → Optimizar → Desactivar
```

---

### 2️⃣ **BLOQUEAR APP EN RECIENTES** ⭐ **IMPORTANTE**

Esto evita que Android cierre la app cuando limpias las apps recientes.

#### **Xiaomi / Redmi / POCO:**
1. Abre **Recientes** (botón de apps abiertas)
2. Busca **Notification Capture**
3. Arrastra hacia abajo hasta ver un **candado** 🔒
4. Toca el candado para bloquearlo

#### **Samsung:**
1. Abre **Recientes**
2. Toca el ícono de **Notification Capture**
3. Selecciona **"Bloquear esta app"**

#### **Huawei:**
1. Abre **Recientes**
2. Arrastra la app hacia abajo
3. Aparecerá un **candado** 🔒, tócalo

---

### 3️⃣ **PERMITIR DATOS EN SEGUNDO PLANO**

```
Configuración → Aplicaciones → Notification Capture
→ Uso de datos → Datos en segundo plano → Activar
→ Datos sin restricciones → Activar
```

---

### 4️⃣ **DESACTIVAR LÍMITE DE BATERÍA (Xiaomi/Redmi)**

```
Configuración → Batería y rendimiento
→ Batería → Administrar el uso de batería de las aplicaciones
→ Notification Capture → Sin restricciones
```

---

### 5️⃣ **PERMITIR NOTIFICACIONES PERMANENTES**

La app ahora muestra una notificación permanente que dice:
```
Notification Capture Activo
Capturados: X emails
```

**NO ELIMINES ESTA NOTIFICACIÓN** - Es necesaria para que la app funcione siempre.

Si la ocultas o la eliminas, Android puede cerrar la app.

---

## 🧪 **CÓMO PROBAR QUE FUNCIONA**

### **Prueba 1: Con celular desbloqueado**
1. Envíate un email
2. Espera la notificación de Gmail
3. Verifica que aparezca en Google Sheets

### **Prueba 2: Con celular bloqueado** ⭐
1. **Bloquea el celular**
2. Espera 2-3 minutos (para que entre en reposo)
3. Desde otro dispositivo, envíate un email
4. Espera 1 minuto
5. **Desbloquea el celular**
6. Verifica Google Sheets - debería aparecer el email

### **Prueba 3: Con celular bloqueado toda la noche**
1. Antes de dormir, verifica que la app esté activa
2. Bloquea el celular
3. Por la mañana, revisa si capturó emails nocturnos

---

## ✅ **SEÑALES DE QUE ESTÁ FUNCIONANDO CORRECTAMENTE**

1. ✅ Ves la notificación permanente "Notification Capture Activo"
2. ✅ El contador de emails aumenta cuando llegan notificaciones
3. ✅ Los emails aparecen en Google Sheets inmediatamente
4. ✅ Funciona incluso con celular bloqueado

---

## ❌ **PROBLEMAS COMUNES**

### **Problema: No captura con celular bloqueado**
**Solución:**
- Verifica que desactivaste la optimización de batería
- Bloquea la app en recientes
- Reinicia el celular

### **Problema: La notificación permanente desaparece**
**Solución:**
- Abre la app de nuevo
- Configura los permisos otra vez
- Verifica que no hayas desactivado las notificaciones de la app

### **Problema: Captura "X mensajes nuevos" en lugar de emails**
**Solución:**
- Esto ya está solucionado en la nueva versión
- Actualiza a la última versión del APK

---

## 🔋 **CONSUMO DE BATERÍA**

La app está optimizada para consumir **muy poca batería**:
- Usa un servicio en primer plano de **baja prioridad**
- Solo se activa cuando llega una notificación
- No hace polling constante
- Consumo estimado: **< 1% al día**

---

## 📞 **SOPORTE**

Si después de configurar todo sigue sin funcionar con celular bloqueado:

1. Dime la **marca y modelo** de tu celular
2. Dime la **versión de Android**
3. Te daré instrucciones específicas para tu dispositivo

---

## 🎯 **RESUMEN RÁPIDO**

Para que funcione con celular bloqueado:

1. ✅ Desactivar optimización de batería
2. ✅ Bloquear app en recientes
3. ✅ Permitir datos en segundo plano
4. ✅ NO eliminar la notificación permanente
5. ✅ Probar con celular bloqueado

**¡Listo!** 🎉
