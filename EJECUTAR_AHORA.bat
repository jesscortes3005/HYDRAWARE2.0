@echo off
echo ========================================
echo   PUSH AUTOMATICO A TU FORK
echo ========================================
echo.
echo Ingresa tu usuario de GitHub:
set /p USERNAME="Usuario: "

if "%USERNAME%"=="" (
    echo Error: Debes ingresar un usuario
    pause
    exit /b 1
)

echo.
echo Cambiando remoto a tu fork...
git remote set-url origin https://github.com/%USERNAME%/HYDRAWARE2.0.git

echo.
echo Remoto actualizado:
git remote -v

echo.
echo ========================================
echo Haciendo push ahora...
echo ========================================
git push -u origin main

echo.
echo ========================================
echo Si funciono, tu commit ya esta en tu fork!
echo.
echo Ahora crea un Pull Request en GitHub:
echo https://github.com/%USERNAME%/HYDRAWARE2.0
echo ========================================
pause

