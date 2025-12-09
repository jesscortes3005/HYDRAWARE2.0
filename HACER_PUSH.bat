@echo off
echo ============================================
echo   HACER PUSH AL REPOSITORIO
echo ============================================
echo.
echo Cuando ejecutes git push, te pedira credenciales:
echo.
echo   Usuario: jesscortes3005
echo   Contrasena: [PEGA AQUI TU TOKEN]
echo.
echo Presiona cualquier tecla para continuar con git push...
pause >nul
echo.
echo Ejecutando git push...
git push
echo.
echo Si funciono, ya esta! Si no, verifica que el token sea correcto.
pause

