# 📤 GUÍA: Subir a GitHub y Compilar APK

## ✅ Tu URL de Google Sheets:
```
https://script.google.com/macros/s/AKfycbx_LtrD3tiq9qGxE9LI907Z4yXXn0eGQgrRVy7uViLa_ANktAKBcOpBT_YHOslrO8Xl/exec
```

---

## 🚀 PASO A PASO

### **Opción 1: Subir con GitHub Desktop (MÁS FÁCIL)** ⭐

#### 1. Descargar GitHub Desktop
1. Ve a: https://desktop.github.com/
2. Descarga e instala
3. Abre GitHub Desktop
4. Haz login con tu cuenta de GitHub

#### 2. Crear repositorio
1. En GitHub Desktop, click en **File → New Repository**
2. Configuración:
   - **Name:** `notification-capture`
   - **Local Path:** `C:\Users\Jesus\Desktop\YAPE`
   - **Initialize with README:** NO (ya lo tenemos)
3. Click en **Create Repository**

#### 3. Agregar archivos
1. GitHub Desktop detectará automáticamente los archivos
2. En la parte izquierda verás todos los archivos
3. En el campo de abajo escribe: "Initial commit - Notification Capture App"
4. Click en **Commit to main**

#### 4. Publicar en GitHub
1. Click en **Publish repository** (arriba)
2. Desmarca "Keep this code private" (para que sea público)
3. Click en **Publish repository**

#### 5. Ver compilación
1. Ve a tu repositorio en GitHub.com
2. Click en **Actions** (arriba)
3. Verás el workflow "Build Android APK" ejecutándose
4. Espera 3-5 minutos
5. Cuando termine (✅ verde), click en el workflow
6. Descarga el APK en **Artifacts**

---

### **Opción 2: Subir desde el navegador (SIN INSTALAR NADA)**

#### 1. Crear repositorio en GitHub.com
1. Ve a: https://github.com/new
2. **Repository name:** `notification-capture`
3. **Public** (seleccionado)
4. **NO** marques "Add a README file"
5. Click en **Create repository**

#### 2. Subir archivos
1. En la página del repositorio, click en **uploading an existing file**
2. Arrastra TODA la carpeta `NotificationCapture` al navegador
3. O click en **choose your files** y selecciona todos los archivos
4. Espera a que se suban todos
5. En el campo de abajo escribe: "Initial commit"
6. Click en **Commit changes**

#### 3. Activar GitHub Actions
1. Click en **Actions** (arriba)
2. Click en **I understand my workflows, go ahead and enable them**
3. El workflow se ejecutará automáticamente

#### 4. Descargar APK
1. Espera 3-5 minutos
2. Refresca la página
3. Click en el workflow que se ejecutó
4. Baja hasta **Artifacts**
5. Click en **notification-capture-apk** para descargar

---

### **Opción 3: Usar Git desde la terminal** (Avanzado)

```powershell
# Navegar a la carpeta
cd C:\Users\Jesus\Desktop\YAPE\NotificationCapture

# Inicializar repositorio
git init

# Agregar todos los archivos
git add .

# Hacer commit
git commit -m "Initial commit - Notification Capture App"

# Conectar con GitHub (reemplaza TU_USUARIO)
git remote add origin https://github.com/TU_USUARIO/notification-capture.git

# Subir
git push -u origin main
```

---

## 📥 DESPUÉS DE COMPILAR

### 1. Descargar el APK
- El archivo se descargará como: `notification-capture-apk.zip`
- Descomprímelo
- Dentro encontrarás: `app-debug.apk`

### 2. Transferir a tu celular

**Opción A: Cable USB**
1. Conecta tu celular a la PC
2. Copia `app-debug.apk` a la carpeta **Descargas** del celular

**Opción B: Email**
1. Envíate el APK por email
2. Abre el email en tu celular
3. Descarga el archivo

**Opción C: Google Drive**
1. Sube el APK a Google Drive
2. Descárgalo desde tu celular

### 3. Instalar en el celular
1. En tu celular, ve a **Configuración → Seguridad**
2. Activa **Instalar apps desconocidas** para Chrome o Archivos
3. Abre el archivo `app-debug.apk`
4. Click en **Instalar**
5. Si aparece advertencia, click en **Instalar de todos modos**
6. Click en **Abrir**

### 4. Configurar la app
1. La app se abrirá
2. Click en **⚙️ Configurar Permisos de Notificación**
3. Busca **Notification Capture** y activa el interruptor
4. Vuelve a la app
5. Pega tu URL de Google Sheets:
   ```
   https://script.google.com/macros/s/AKfycbx_LtrD3tiq9qGxE9LI907Z4yXXn0eGQgrRVy7uViLa_ANktAKBcOpBT_YHOslrO8Xl/exec
   ```
6. Click en **💾 Guardar URL**
7. Deberías ver: **✅ Estado: ACTIVO**

### 5. Probar
1. Envíate un email a ti mismo
2. Espera la notificación de Gmail
3. Abre tu Google Sheet
4. ¡Debería aparecer la notificación capturada! 🎉

---

## 🤔 ¿Cuál opción prefieres?

**Opción 1** (GitHub Desktop) - Más fácil, interfaz gráfica
**Opción 2** (Navegador) - Sin instalar nada
**Opción 3** (Terminal) - Más rápido si sabes usar Git

**Dime cuál prefieres y te guío paso a paso** 😊

---

## ⏱️ Tiempo estimado:
- Subir a GitHub: 5 minutos
- Compilación automática: 3-5 minutos
- Descargar e instalar: 5 minutos
- **Total: ~15 minutos**
