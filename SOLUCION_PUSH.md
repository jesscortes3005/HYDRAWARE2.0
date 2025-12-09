# Solución para hacer Push al Repositorio

## Situación Actual:
- Tu usuario Git: Emmanuelmg19
- Repositorio remoto: jesscortes3005/HYDRAWARE2.0
- Error: Permisos denegados (403)

## Soluciones posibles:

### Opción A: Si eres colaborador del repositorio

1. **Crea un Token Personal de GitHub:**
   - Ve a: https://github.com/settings/tokens
   - Click en "Generate new token (classic)"
   - Nombre: "HYDRAWARE2.0"
   - Scope: marca "repo"
   - Click en "Generate token"
   - **Copia el token** (solo se muestra una vez)

2. **Actualiza las credenciales:**
   - Ve al Administrador de Credenciales de Windows
   - Busca: git:https://github.com
   - Elimínalas
   - O ejecuta el siguiente comando en PowerShell (como Administrador):
     ```powershell
     cmdkey /list | findstr git
     cmdkey /delete:git:https://github.com
     ```

3. **Haz push de nuevo:**
   - Ejecuta: `git push`
   - Cuando pida usuario: `jesscortes3005` (o el usuario que tenga permisos)
   - Cuando pida contraseña: **pega el token personal** (NO tu contraseña)

### Opción B: Si NO tienes permisos - Usa tu Fork

Si no tienes acceso al repositorio original, trabaja con tu fork:

1. Ve a GitHub y haz fork del repositorio
2. Cambia el remoto a tu fork:
   ```bash
   git remote set-url origin https://github.com/Emmanuelmg19/HYDRAWARE2.0.git
   ```
3. Luego haz push normalmente

### Opción C: Solicita permisos

Contacta a jesscortes3005 para que te agregue como colaborador al repositorio.

## ¿Cuál es tu caso?
- ¿Tienes acceso al repositorio de jesscortes3005?
- ¿O prefieres trabajar en tu propio fork?

