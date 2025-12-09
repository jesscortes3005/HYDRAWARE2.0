# Script para hacer push a tu fork
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CONFIGURAR Y HACER PUSH A TU FORK" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$username = Read-Host "Ingresa tu usuario de GitHub (ej: Emmanuelmg19)"

if ($username) {
    Write-Host ""
    Write-Host "Cambiando remoto a tu fork..." -ForegroundColor Yellow
    git remote set-url origin "https://github.com/$username/HYDRAWARE2.0.git"
    
    Write-Host ""
    Write-Host "Remoto actualizado. Verificando..." -ForegroundColor Green
    git remote -v
    
    Write-Host ""
    Write-Host "Haciendo push..." -ForegroundColor Yellow
    git push -u origin main
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  ¡LISTO! Ahora puedes crear un Pull Request" -ForegroundColor Green
    Write-Host "  Ve a: https://github.com/$username/HYDRAWARE2.0" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
} else {
    Write-Host "Usuario no ingresado. Cancelando..." -ForegroundColor Red
}

