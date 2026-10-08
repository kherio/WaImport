<p align="center"><img src="docs/icon.png" width="96" alt="WaImport"></p>

# WaImport

Módulo LSPosed independiente que añade **Importar chat** al menú de la pantalla principal de
WhatsApp y WhatsApp Business. Carga una exportación de chat de WhatsApp ("Exportar chat") en un
chat que ya existe en el dispositivo:

- Acepta el `.txt` o el `.zip` con adjuntos (imágenes, vídeo, audio, notas de voz, documentos, stickers).
- Formatos de Android y de iOS; detecta el orden de la fecha (día/mes, mes/día, año/mes/día).
- Solo importa mensajes anteriores al primero del chat, así que no toca los que ya hay. Si el chat está vacío, importa todo.
- Es idempotente (importar dos veces el mismo archivo no duplica) y deshace los cambios si falla o se cancela.
- Copia de seguridad opcional de `msgstore.db` antes de importar.

- Destino: un chat existente, o un chat nuevo con un contacto que aún no tiene conversación (también por número de teléfono).
- Eliges cuál de los participantes del archivo eres tú, para que sus mensajes salgan como enviados.

Los mensajes importados existen solo en este dispositivo.

## Uso

1. Instala el APK y actívalo en LSPosed con ámbito `com.whatsapp` y/o `com.whatsapp.w4b`.
2. Reinicia WhatsApp.
3. Menú de la pantalla principal → **Importar chat** → elige el archivo.
4. Elige el destino (chat existente o chat nuevo con un contacto) y quién eres tú.
5. Al terminar, pulsa **Reiniciar ahora** para que WhatsApp relea la base de datos.

Un chat importado se ordena en la lista por la fecha de su último mensaje; si no lo ves, búscalo por el nombre.

## Estado

El parser, la lectura del ZIP y el importador tienen tests (unitarios y de integración contra SQLite).
Probado en un dispositivo real con exportaciones de Android en español (texto, fotos y audios). Otros
idiomas y formatos (iOS) están cubiertos por tests pero no se han probado en un móvil. Haz una copia de
seguridad antes de usarlo; la casilla de backup del diálogo la hace por ti.

## Compilar

```
./gradlew assembleDebug
```

Licencia: GPL-3.0.
