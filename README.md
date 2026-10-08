# WaImport

Módulo LSPosed independiente que añade **Importar chat** al menú de la pantalla principal de
WhatsApp y WhatsApp Business. Carga una exportación de chat de WhatsApp ("Exportar chat") en un
chat que ya existe en el dispositivo:

- Acepta el `.txt` o el `.zip` con adjuntos (imágenes, vídeo, audio, notas de voz, documentos, stickers).
- Formatos de Android y de iOS; detecta el orden de la fecha (día/mes, mes/día, año/mes/día).
- Solo importa mensajes anteriores al primero del chat, así que no toca los que ya hay. Si el chat está vacío, importa todo.
- Es idempotente (importar dos veces el mismo archivo no duplica) y deshace los cambios si falla o se cancela.
- Copia de seguridad opcional de `msgstore.db` antes de importar.

Los mensajes importados existen solo en este dispositivo.

## Uso

1. Instala el APK y actívalo en LSPosed con ámbito `com.whatsapp` y/o `com.whatsapp.w4b`.
2. Reinicia WhatsApp.
3. Menú de la pantalla principal → **Importar chat** → elige el archivo, el chat de destino y quién eres tú.

## Estado

El parser, la lectura del ZIP y el importador tienen tests (unitarios y de integración contra SQLite).
Aún no se ha probado en un dispositivo con una `msgstore.db` real: haz una copia antes de usarlo.

## Compilar

```
./gradlew assembleDebug
```

Licencia: GPL-3.0.
