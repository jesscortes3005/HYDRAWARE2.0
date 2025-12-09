@echo off
echo ========================================
echo SOLUCION PARA PUSH AL REPOSITORIO
echo ========================================
echo.
echo Opcion 1: Eliminar credenciales guardadas
echo Opcion 2: Verificar usuario actual
echo Opcion 3: Cambiar remoto a tu fork
echo.
echo Por favor, ejecuta estos comandos manualmente segun tu caso:
echo.
echo.
echo === OPCION 1: Eliminar credenciales ===
echo cmdkey /delete:LegacyGeneric:target=git:https://github.com
echo cmdkey /delete:LegacyGeneric:target=GitHub - https://api.github.com/Emmanuelmg19
echo.
echo === OPCION 2: Si tienes un fork, cambia el remoto ===
echo git remote set-url origin https://github.com/Emmanuelmg19/HYDRAWARE2.0.git
echo.
echo === OPCION 3: Si eres colaborador, usa el usuario correcto ===
echo Configura Git para usar el usuario con permisos:
echo git config user.name "jesscortes3005"
echo git config user.email "tu-email@ejemplo.com"
echo.
pause

