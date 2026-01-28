# 📱 GUÍA PASO A PASO - Notification Capture

## 🎯 Resumen
Esta app captura notificaciones de Gmail y las guarda automáticamente en Google Sheets.

---

## 📋 PASO 1: Configurar Google Sheets

### 1.1 Crear la hoja de cálculo
1. Ve a https://sheets.google.com
2. Click en "+" para crear nueva hoja
3. Nómbrala: **"Notificaciones Gmail"**

### 1.2 Configurar Google Apps Script
1. En tu hoja, click en **Extensiones → Apps Script**
2. Se abrirá una nueva pestaña con el editor
3. **Borra todo** el código que aparece
4. Abre el archivo `google-apps-script.js` de este proyecto
5. **Copia TODO** el código
6. **Pega** en el editor de Apps Script
7. Click en **💾 Guardar** (o Ctrl+S)
8. Nombra el proyecto: "Notification Receiver"

### 1.3 Publicar como Web App
1. Click en **Implementar** (arriba a la derecha)
2. Click en **Nueva implementación**
3. Click en el ⚙️ junto a "Selecciona tipo"
4. Selecciona **Aplicación web**
5. Configuración:
   - **Descripción:** "Receptor de notificaciones v1"
   - **Ejecutar como:** Yo (tu email)
   - **Quién tiene acceso:** **Cualquier persona**
6. Click en **Implementar**
7. Aparecerá un diálogo de permisos:
   - Click en **Autorizar acceso**
   - Selecciona tu cuenta de Google
   - Click en **Avanzado**
   - Click en **Ir a Notification Receiver (no seguro)**
   - Click en **Permitir**
8. **¡IMPORTANTE!** Copia la **URL de la aplicación web**
   - Se ve así: `https://script.google.com/macros/s/AKfycby.../exec`
   - **GUÁRDALA** - la necesitarás en el paso 3

### 1.4 Probar el script
1. En el editor de Apps Script
2. Selecciona la función **testNotification** en el menú desplegable
3. Click en **▶️ Ejecutar**
4. Vuelve a tu Google Sheet
5. Deberías ver una fila de prueba agregada ✅

---

## 📱 PASO 2: Obtener el APK de la App

Tienes varias opciones:

### Opción A: Compilar localmente (requiere instalar Java)

#### 2.A.1 Instalar Java JDK
```powershell
# Opción 1: Descargar manualmente
# Ve a: https://www.oracle.com/java/technologies/downloads/
# Descarga: Java 11 o superior para Windows
# Instala normalmente

# Opción 2: Usar Chocolatey (si lo tienes)
choco install openjdk11
```

#### 2.A.2 Verificar instalación
```powershell
java -version
# Debería mostrar: java version "11.x.x" o superior
```

#### 2.A.3 Descargar Gradle
```powershell
# Ve a: https://gradle.org/releases/
# Descarga: gradle-7.6-bin.zip
# Extrae en: C:\Gradle
# Agrega a PATH: C:\Gradle\gradle-7.6\bin
```

#### 2.A.4 Compilar el APK
```powershell
cd C:\Users\Jesus\Desktop\YAPE\NotificationCapture
.\gradlew assembleDebug
```

El APK estará en:
```
app\build\outputs\apk\debug\app-debug.apk
```

---

### Opción B: Usar servicio online (SIN INSTALAR NADA) ⭐

#### 2.B.1 Subir a GitHub
1. Ve a https://github.com
2. Crea cuenta (si no tienes)
3. Click en **New repository**
4. Nombre: `notification-capture`
5. Click en **Create repository**
6. Sube todos los archivos del proyecto

#### 2.B.2 Configurar GitHub Actions
1. En tu repositorio, click en **Actions**
2. Click en **set up a workflow yourself**
3. Pega este código:

```yaml
name: Build APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
          
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
        
      - name: Build with Gradle
        run: ./gradlew assembleDebug
        
      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

4. Click en **Start commit** → **Commit new file**
5. Espera 2-3 minutos
6. Ve a **Actions** → Click en el workflow
7. Descarga el APK en **Artifacts**

---

### Opción C: Usar APK Builder Online

1. Ve a: https://www.apk-builder.com (o similar)
2. Sube los archivos del proyecto
3. Click en "Build"
4. Descarga el APK

---

### Opción D: Te lo compilo yo (si tienes problemas)

Si ninguna opción funciona, puedo:
1. Compilar el APK en mi entorno
2. Subirlo a un servicio de archivos
3. Compartirte el link de descarga

---

## 📲 PASO 3: Instalar la App en tu Celular

### 3.1 Preparar el celular
1. **Habilitar instalación de apps desconocidas:**
   - Ve a **Configuración**
   - Busca **Seguridad** o **Privacidad**
   - Busca **Instalar apps desconocidas** o **Fuentes desconocidas**
   - Activa para **Chrome** o **Archivos**

### 3.2 Transferir el APK
**Opción A: USB**
1. Conecta tu celular a la PC con cable USB
2. Copia `app-debug.apk` a la carpeta **Descargas** del celular

**Opción B: Email**
1. Envíate el APK por email
2. Abre el email en tu celular
3. Descarga el archivo

**Opción C: Google Drive**
1. Sube el APK a Google Drive
2. Descárgalo desde tu celular

### 3.3 Instalar
1. Abre el archivo `app-debug.apk` en tu celular
2. Click en **Instalar**
3. Si aparece advertencia de seguridad:
   - Click en **Más información**
   - Click en **Instalar de todos modos**
4. Espera a que se instale
5. Click en **Abrir**

---

## ⚙️ PASO 4: Configurar la App

### 4.1 Dar permisos de notificación
1. La app se abrirá automáticamente
2. Verás un botón: **⚙️ Configurar Permisos de Notificación**
3. Click en ese botón
4. Se abrirá la configuración de Android
5. Busca **Notification Capture** en la lista
6. Activa el interruptor ✅
7. Confirma en el diálogo que aparece
8. Presiona **Atrás** para volver a la app

### 4.2 Configurar URL de Google Sheets
1. En la app, verás un campo de texto
2. Pega la **URL de Google Apps Script** que copiaste en el Paso 1.3
3. Click en **💾 Guardar URL**
4. Deberías ver: **✅ Estado: ACTIVO**

---

## 🧪 PASO 5: Probar la App

### 5.1 Enviar email de prueba
1. Desde otro dispositivo o computadora
2. Envía un email a tu cuenta de Gmail
3. Asunto: "Prueba de Notification Capture"
4. Contenido: "Este es un mensaje de prueba"

### 5.2 Verificar captura
1. Espera a que llegue la notificación de Gmail en tu celular
2. Deberías ver la notificación normal de Gmail
3. **La app la capturará automáticamente** (invisible)

### 5.3 Verificar en Google Sheets
1. Abre tu Google Sheet en el navegador
2. Deberías ver una nueva fila con:
   - Fecha/Hora
   - Título del email
   - Contenido de la notificación
   - App: com.google.android.gm

### 5.4 Si funciona ✅
¡Perfecto! La app está funcionando correctamente.

### 5.5 Si NO funciona ❌
Ver sección de **Solución de Problemas** abajo.

---

## 🔧 Solución de Problemas

### Problema 1: No aparece en Google Sheets

**Causa posible:** URL incorrecta
- Verifica que copiaste la URL completa
- Debe terminar en `/exec`
- No debe tener espacios

**Causa posible:** Permisos de Google
- Vuelve a publicar el script
- Asegúrate de seleccionar "Cualquier persona"

**Causa posible:** Sin internet
- Verifica que tu celular tenga conexión

### Problema 2: No captura notificaciones

**Causa posible:** Permisos no otorgados
- Ve a: Configuración → Apps → Notification Capture
- Verifica que tenga acceso a notificaciones

**Causa posible:** Optimización de batería
- Ve a: Configuración → Batería → Optimización
- Busca Notification Capture
- Selecciona "No optimizar"

**Causa posible:** Gmail no envía notificaciones
- Verifica configuración de notificaciones de Gmail
- Asegúrate de que Gmail tenga permisos de notificación

### Problema 3: La app se cierra sola

**Solución:**
1. Ve a: Configuración → Apps → Notification Capture
2. Almacenamiento → Borrar caché
3. Permisos → Verifica todos los permisos
4. Batería → No optimizar

---

## 📊 Verificar Logs (Avanzado)

Si quieres ver qué está pasando internamente:

### En el celular:
1. Habilita **Opciones de desarrollador**:
   - Ve a Configuración → Acerca del teléfono
   - Toca 7 veces en "Número de compilación"
2. Ve a Configuración → Sistema → Opciones de desarrollador
3. Activa **Depuración USB**
4. Conecta a la PC
5. Usa `adb logcat` para ver logs

---

## 🎯 Próximos Pasos

Si todo funciona, podemos:

1. ✅ Agregar más apps (Yape, WhatsApp, etc.)
2. ✅ Mejorar el formato de los datos
3. ✅ Agregar filtros (solo ciertos remitentes)
4. ✅ Enviar notificaciones SMS
5. ✅ Crear dashboard web
6. ✅ Agregar análisis de datos

---

## 💬 ¿Necesitas ayuda?

Si tienes problemas en cualquier paso, dime en cuál estás atascado y te ayudo a resolverlo.
