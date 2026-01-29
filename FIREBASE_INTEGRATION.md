# 🔥 INTEGRACIÓN CON FIREBASE

## 🎉 **NUEVA FUNCIONALIDAD:**

Ahora la app registra los pagos en **DOS lugares**:
1. ✅ **Google Sheets** (como antes)
2. ✅ **Firebase Realtime Database** (NUEVO)

---

## 📊 **VENTAJAS DE FIREBASE:**

### **1. Tiempo Real** ⚡
```
✅ Los datos se sincronizan instantáneamente
✅ Puedes ver los pagos en tiempo real
✅ Actualizaciones automáticas
```

### **2. Offline First** 📴
```
✅ Funciona sin internet
✅ Sincroniza cuando hay conexión
✅ No pierdes datos
```

### **3. Estructura de Datos** 🗂️
```
✅ Datos organizados en JSON
✅ Fácil de consultar
✅ Búsquedas rápidas
```

### **4. Escalabilidad** 📈
```
✅ Soporta millones de registros
✅ Consultas eficientes
✅ Sin límites de filas (como Sheets)
```

---

## 🔧 **CONFIGURACIÓN DE FIREBASE:**

### **Paso 1: Crear Proyecto en Firebase**

1. Ve a: https://console.firebase.google.com/
2. Click en **"Agregar proyecto"**
3. Nombre del proyecto: `NotificationCapture` (o el que prefieras)
4. Acepta los términos y crea el proyecto

### **Paso 2: Agregar App Android**

1. En la consola de Firebase, click en **"Agregar app"**
2. Selecciona **Android** (ícono de Android)
3. **Package name:** `com.yapemanager.notificationcapture`
4. **App nickname:** `Notification Capture` (opcional)
5. Click en **"Registrar app"**

### **Paso 3: Descargar google-services.json**

1. Firebase te mostrará un botón **"Descargar google-services.json"**
2. Descarga el archivo
3. **IMPORTANTE:** Reemplaza el archivo `app/google-services.json` con el que descargaste
4. El archivo debe estar en: `NotificationCapture/app/google-services.json`

### **Paso 4: Habilitar Realtime Database**

1. En la consola de Firebase, ve a **"Realtime Database"**
2. Click en **"Crear base de datos"**
3. Selecciona ubicación: **United States** (us-central1)
4. Modo de seguridad: **"Modo de prueba"** (por ahora)
5. Click en **"Habilitar"**

### **Paso 5: Configurar Reglas de Seguridad**

En la pestaña **"Reglas"**, reemplaza con:

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

**Nota:** Estas reglas son para desarrollo. Para producción, debes restringir el acceso.

---

## 📊 **ESTRUCTURA DE DATOS EN FIREBASE:**

### **Ubicación:**
```
firebase-database/
└── payments/
    ├── payment_1738123456789/
    │   ├── timestamp: 1738123456789
    │   ├── date: "2026-01-29 14:30:56"
    │   ├── title: "Confirmación de Pago"
    │   ├── content: "Yape! María González te envió un pago por S/ 50.00"
    │   ├── source: "YAPE"
    │   ├── sender: "María González"
    │   ├── amount: "50.00"
    │   └── currency: "PEN"
    ├── payment_1738123457890/
    │   ├── timestamp: 1738123457890
    │   ├── date: "2026-01-29 14:31:00"
    │   ├── title: "Confirmación de Pago"
    │   ├── content: "Yape! Pedro Sánchez te envió un pago por S/ 100.50"
    │   ├── source: "Gmail"
    │   ├── sender: "Pedro Sánchez"
    │   ├── amount: "100.50"
    │   └── currency: "PEN"
    └── ...
```

### **Campos de cada pago:**

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `timestamp` | Number | Timestamp en milisegundos | `1738123456789` |
| `date` | String | Fecha formateada | `"2026-01-29 14:30:56"` |
| `title` | String | Título de la notificación | `"Confirmación de Pago"` |
| `content` | String | Contenido completo | `"Yape! María te envió..."` |
| `source` | String | Fuente (YAPE o Gmail) | `"YAPE"` |
| `sender` | String | Nombre del remitente | `"María González"` |
| `amount` | String | Monto del pago | `"50.00"` |
| `currency` | String | Moneda (siempre PEN) | `"PEN"` |

---

## 🔍 **CONSULTAR DATOS EN FIREBASE:**

### **Opción 1: Consola Web**

1. Ve a: https://console.firebase.google.com/
2. Selecciona tu proyecto
3. Click en **"Realtime Database"**
4. Verás todos los pagos en tiempo real

### **Opción 2: API REST**

```bash
# Obtener todos los pagos
curl https://TU-PROYECTO.firebaseio.com/payments.json

# Obtener un pago específico
curl https://TU-PROYECTO.firebaseio.com/payments/payment_1738123456789.json

# Filtrar por fecha (últimos 10)
curl https://TU-PROYECTO.firebaseio.com/payments.json?orderBy="timestamp"&limitToLast=10
```

### **Opción 3: SDK de Firebase (Web)**

```javascript
import { getDatabase, ref, onValue } from "firebase/database";

const db = getDatabase();
const paymentsRef = ref(db, 'payments');

onValue(paymentsRef, (snapshot) => {
  const data = snapshot.val();
  console.log(data);
});
```

---

## 📝 **LOGS:**

### **Firebase habilitado:**
```
✅ Firebase inicializado correctamente
✅ YAPE Notification VÁLIDA - Title: Confirmación de Pago...
📊 Pago enviado a Firebase
✅ Pago registrado en Firebase: payment_1738123456789
   Remitente: María González, Monto: 50.00
```

### **Firebase deshabilitado:**
```
❌ Error al inicializar Firebase: [error]
⚠️ Firebase no está habilitado, saltando registro
```

---

## 🎯 **COMPARACIÓN: SHEETS VS FIREBASE:**

| Característica | Google Sheets | Firebase |
|----------------|---------------|----------|
| **Tiempo real** | ❌ No | ✅ Sí |
| **Offline** | ❌ No | ✅ Sí |
| **Límite de filas** | ⚠️ ~5 millones | ✅ Ilimitado |
| **Velocidad de escritura** | ⚠️ Media | ✅ Muy rápida |
| **Consultas** | ⚠️ Limitadas | ✅ Avanzadas |
| **Visualización** | ✅ Fácil (Excel-like) | ⚠️ Requiere código |
| **Costo** | ✅ Gratis | ✅ Gratis (hasta 1GB) |

---

## 💡 **CASOS DE USO:**

### **Caso 1: Dashboard en Tiempo Real**

Puedes crear un dashboard web que muestre los pagos en tiempo real:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard de Pagos</title>
    <script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js"></script>
    <script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-database-compat.js"></script>
</head>
<body>
    <h1>Pagos en Tiempo Real</h1>
    <div id="payments"></div>
    
    <script>
        // Configurar Firebase
        const firebaseConfig = {
            databaseURL: "https://TU-PROYECTO.firebaseio.com"
        };
        firebase.initializeApp(firebaseConfig);
        
        // Escuchar pagos
        const paymentsRef = firebase.database().ref('payments');
        paymentsRef.on('child_added', (snapshot) => {
            const payment = snapshot.val();
            document.getElementById('payments').innerHTML += 
                `<p>${payment.date} - ${payment.sender}: S/ ${payment.amount}</p>`;
        });
    </script>
</body>
</html>
```

### **Caso 2: Notificaciones Push**

Puedes usar Firebase Cloud Functions para enviar notificaciones cuando llegue un pago:

```javascript
exports.onNewPayment = functions.database.ref('/payments/{paymentId}')
    .onCreate((snapshot, context) => {
        const payment = snapshot.val();
        // Enviar notificación push
        return admin.messaging().send({
            notification: {
                title: 'Nuevo Pago',
                body: `${payment.sender} te pagó S/ ${payment.amount}`
            }
        });
    });
```

### **Caso 3: Análisis de Datos**

Puedes exportar los datos de Firebase a BigQuery para análisis avanzados.

---

## 🔒 **SEGURIDAD (PRODUCCIÓN):**

Para producción, cambia las reglas de Firebase:

```json
{
  "rules": {
    "payments": {
      ".read": "auth != null",
      ".write": "auth != null",
      "$paymentId": {
        ".validate": "newData.hasChildren(['timestamp', 'date', 'title', 'content', 'source', 'sender', 'amount', 'currency'])"
      }
    }
  }
}
```

Esto requiere autenticación para leer/escribir.

---

## 🧪 **PRUEBAS:**

### **Prueba 1: Verificar Firebase**

1. Instala la app
2. Recibe un pago de Yape
3. Ve a la consola de Firebase
4. Verifica que aparezca en **Realtime Database > payments**

### **Prueba 2: Verificar Datos**

1. Expande el pago en Firebase
2. Verifica que tenga todos los campos:
   - timestamp
   - date
   - title
   - content
   - source
   - sender
   - amount
   - currency

### **Prueba 3: Tiempo Real**

1. Abre la consola de Firebase
2. Recibe un pago
3. Verifica que aparezca instantáneamente en Firebase

---

## 📄 **ARCHIVOS MODIFICADOS:**

1. ✅ `build.gradle` (proyecto) - Agregado classpath de Google Services
2. ✅ `app/build.gradle` - Agregadas dependencias de Firebase
3. ✅ `app/google-services.json` - Archivo de configuración (debes reemplazarlo)
4. ✅ `FirebaseManager.java` - Nueva clase para gestionar Firebase
5. ✅ `NotificationListener.java` - Integrado Firebase

---

## 🎯 **RESUMEN:**

**Antes:**
```
Notificación → Google Sheets
```

**Ahora:**
```
Notificación → Google Sheets + Firebase
```

**Ventajas:**
```
✅ Doble respaldo
✅ Tiempo real
✅ Offline first
✅ Consultas avanzadas
✅ Escalable
```

---

## ⚠️ **IMPORTANTE:**

1. **Debes reemplazar** `app/google-services.json` con tu propio archivo de Firebase
2. **Habilita Realtime Database** en la consola de Firebase
3. **Configura las reglas** de seguridad
4. **Compila la app** después de configurar Firebase

---

## 🔄 **PRÓXIMOS PASOS:**

1. Crear proyecto en Firebase
2. Descargar `google-services.json`
3. Reemplazar el archivo de ejemplo
4. Habilitar Realtime Database
5. Configurar reglas
6. Compilar y probar

---

¡Listo para usar! 🎉

**Ahora tienes registro dual: Sheets + Firebase** ✅
