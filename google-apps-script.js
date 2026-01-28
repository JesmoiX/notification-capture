/**
 * Google Apps Script para recibir notificaciones de la app
 * 
 * INSTRUCCIONES:
 * 1. Abre tu Google Sheet
 * 2. Ve a Extensiones → Apps Script
 * 3. Borra todo y pega este código
 * 4. Guarda (Ctrl+S)
 * 5. Click en "Implementar" → "Nueva implementación"
 * 6. Tipo: "Aplicación web"
 * 7. Ejecutar como: "Yo"
 * 8. Quién tiene acceso: "Cualquier persona"
 * 9. Click en "Implementar"
 * 10. COPIA LA URL que te da
 * 11. Pega esa URL en la app Android
 */

function doPost(e) {
  try {
    // Obtener la hoja activa
    var sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
    
    // Si es la primera vez, crear encabezados
    if (sheet.getLastRow() === 0) {
      sheet.appendRow(['Fecha/Hora', 'Título', 'Contenido', 'App', 'Timestamp']);
      sheet.getRange(1, 1, 1, 5).setFontWeight('bold');
      sheet.getRange(1, 1, 1, 5).setBackground('#4CAF50');
      sheet.getRange(1, 1, 1, 5).setFontColor('#FFFFFF');
    }
    
    // Parsear los datos recibidos
    var data = JSON.parse(e.postData.contents);
    
    // Extraer información
    var title = data.title || '';
    var content = data.content || '';
    var app = data.app || '';
    var timestamp = data.timestamp || new Date().toISOString();
    
    // Formatear fecha legible
    var now = new Date();
    var dateStr = Utilities.formatDate(now, Session.getScriptTimeZone(), 'dd/MM/yyyy HH:mm:ss');
    
    // Agregar fila con los datos
    sheet.appendRow([
      dateStr,
      title,
      content,
      app,
      timestamp
    ]);
    
    // Auto-ajustar columnas
    sheet.autoResizeColumns(1, 5);
    
    // Respuesta exitosa
    return ContentService.createTextOutput(JSON.stringify({
      status: 'success',
      message: 'Notificación guardada correctamente',
      row: sheet.getLastRow()
    })).setMimeType(ContentService.MimeType.JSON);
    
  } catch (error) {
    // Respuesta de error
    return ContentService.createTextOutput(JSON.stringify({
      status: 'error',
      message: error.toString()
    })).setMimeType(ContentService.MimeType.JSON);
  }
}

// Función de prueba GET (para verificar que funciona)
function doGet(e) {
  return ContentService.createTextOutput(JSON.stringify({
    status: 'active',
    message: 'Google Apps Script está funcionando correctamente',
    timestamp: new Date().toISOString()
  })).setMimeType(ContentService.MimeType.JSON);
}

// Función de prueba manual (ejecuta esto para probar)
function testNotification() {
  var testData = {
    postData: {
      contents: JSON.stringify({
        title: 'Prueba de Notificación',
        content: 'Este es un mensaje de prueba desde Google Apps Script',
        app: 'com.google.android.gm',
        timestamp: new Date().toISOString()
      })
    }
  };
  
  var result = doPost(testData);
  Logger.log(result.getContent());
}
