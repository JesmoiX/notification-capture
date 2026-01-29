# Script para descargar e instalar APK desde GitHub Actions
Write-Host "🔄 Esperando a que termine la compilación..." -ForegroundColor Yellow

# Esperar 2 minutos para que compile
Start-Sleep -Seconds 120

Write-Host "📥 Descargando APK desde GitHub..." -ForegroundColor Cyan

# Crear directorio temporal
$tempDir = "$env:TEMP\yape-apk"
New-Item -ItemType Directory -Force -Path $tempDir | Out-Null

# URL del último artifact (necesitarás un token de GitHub para esto)
# Por ahora, asumimos que ya está descargado manualmente

# Buscar el APK en Downloads
$apkPath = Get-ChildItem -Path "$env:USERPROFILE\Downloads" -Filter "app-debug.apk" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1

if ($apkPath) {
    Write-Host "✅ APK encontrado: $($apkPath.FullName)" -ForegroundColor Green
    
    # Desinstalar versión anterior
    Write-Host "🗑️ Desinstalando versión anterior..." -ForegroundColor Yellow
    & "C:\Users\Jesus\Downloads\platform-tools-latest-windows\platform-tools\adb.exe" uninstall com.yapemanager.notificationcapture
    
    # Instalar nueva versión
    Write-Host "📲 Instalando nueva versión..." -ForegroundColor Cyan
    & "C:\Users\Jesus\Downloads\platform-tools-latest-windows\platform-tools\adb.exe" install -r $apkPath.FullName
    
    Write-Host "✅ ¡Instalación completada!" -ForegroundColor Green
} else {
    Write-Host "❌ No se encontró el APK en Downloads" -ForegroundColor Red
    Write-Host "Por favor descarga manualmente desde:" -ForegroundColor Yellow
    Write-Host "https://github.com/JesmoiX/notification-capture/actions" -ForegroundColor Cyan
}

Read-Host "Presiona Enter para continuar"
