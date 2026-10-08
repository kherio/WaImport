package dev.kherio.waimport

/** Textos del módulo (inglés y español). Se usan en vez de recursos porque el código corre dentro de WhatsApp. */
object S {
    const val import_chat = 0
    const val import_chat_reading = 1
    const val import_chat_summary = 2
    const val import_chat_destination = 3
    const val import_chat_pick = 4
    const val import_chat_search_hint = 5
    const val import_chat_group_suffix = 6
    const val import_chat_me = 7
    const val import_chat_date_order = 8
    const val import_chat_order_auto = 9
    const val import_chat_order_dmy = 10
    const val import_chat_order_mdy = 11
    const val import_chat_order_ymd = 12
    const val import_chat_media = 13
    const val import_chat_backup = 14
    const val import_chat_note = 15
    const val import_chat_action = 16
    const val import_chat_need_chat = 17
    const val import_chat_progress_backup = 18
    const val import_chat_progress_media = 19
    const val import_chat_progress_messages = 20
    const val import_chat_done_title = 21
    const val import_chat_done_message = 22
    const val import_chat_done_media_failed = 23
    const val import_chat_done_skipped = 24
    const val import_chat_done_backup = 25
    const val import_chat_restart_now = 26
    const val import_chat_later = 27
    const val import_chat_cancelled = 28
    const val import_chat_error_title = 29
    const val import_chat_error_empty = 30
    const val import_chat_error_db_missing = 31
    const val import_chat_error_chat_missing = 32
    const val import_chat_error_nothing = 33
    const val import_chat_error_space_db = 34
    const val import_chat_error_space_media = 35
    const val import_chat_error_open = 36

    private val en = arrayOf(
        "Import chat",
        "Reading file…",
        "%1\$d messages and %2\$d attachments found.\nFrom %3\$s to %4\$s.\nParticipants: %5\$s",
        "Destination chat",
        "Choose chat…",
        "Search chats",
        "%1\$s (group)",
        "Which participant are you?",
        "Date order in the file",
        "Automatic (%1\$s)",
        "Day/Month",
        "Month/Day",
        "Year/Month/Day",
        "Import attachments (%1\$d files)",
        "Back up the database first (recommended)",
        "Only messages older than the first message of the chat are imported, so nothing already there changes. Imported messages exist only on this device. WhatsApp must restart when finished.",
        "Import",
        "Choose the destination chat first",
        "Backing up the database… %1\$d%%",
        "Copying attachments… %1\$d / %2\$d",
        "Importing messages… %1\$d / %2\$d",
        "Import finished",
        "Imported %1\$d messages (%2\$d with attachments).%3\$s",
        "\n%1\$d attachments could not be copied and were imported as text.",
        "\n%1\$d messages were skipped because they are not older than the first message of the chat.",
        "\nBackup: %1\$s",
        "Restart now",
        "Later",
        "Import cancelled. Nothing was changed.",
        "Could not import",
        "No messages were found. Make sure the file is a chat exported from WhatsApp (.txt or .zip).",
        "WhatsApp database not found.",
        "The chosen chat no longer exists.",
        "All the messages are newer than the first message of the chat, so there is nothing to import.",
        "Not enough free space for the database backup. Free some space or turn the backup off.",
        "Not enough free space for the attachments.",
        "The file could not be read: %1\$s",
    )

    private val es = arrayOf(
        "Importar chat",
        "Leyendo archivo…",
        "%1\$d mensajes y %2\$d adjuntos encontrados.\nDel %3\$s al %4\$s.\nParticipantes: %5\$s",
        "Chat de destino",
        "Elegir chat…",
        "Buscar chats",
        "%1\$s (grupo)",
        "¿Cuál de los participantes eres tú?",
        "Orden de las fechas del archivo",
        "Automático (%1\$s)",
        "Día/Mes",
        "Mes/Día",
        "Año/Mes/Día",
        "Importar adjuntos (%1\$d archivos)",
        "Hacer antes una copia de la base de datos (recomendado)",
        "Solo se importan los mensajes anteriores al primero del chat, así que lo que ya hay no cambia. Los mensajes importados solo existen en este dispositivo. Al terminar hay que reiniciar WhatsApp.",
        "Importar",
        "Elige primero el chat de destino",
        "Copiando la base de datos… %1\$d%%",
        "Copiando adjuntos… %1\$d / %2\$d",
        "Importando mensajes… %1\$d / %2\$d",
        "Importación terminada",
        "Se importaron %1\$d mensajes (%2\$d con adjunto).%3\$s",
        "\n%1\$d adjuntos no se pudieron copiar y se importaron como texto.",
        "\n%1\$d mensajes se omitieron por no ser anteriores al primero del chat.",
        "\nCopia de seguridad: %1\$s",
        "Reiniciar ahora",
        "Más tarde",
        "Importación cancelada. No se ha cambiado nada.",
        "No se pudo importar",
        "No se encontró ningún mensaje. Comprueba que el archivo es un chat exportado desde WhatsApp (.txt o .zip).",
        "No se encontró la base de datos de WhatsApp.",
        "El chat elegido ya no existe.",
        "Todos los mensajes son posteriores al primero del chat, así que no hay nada que importar.",
        "No hay espacio libre para la copia de la base de datos. Libera espacio o desactiva la copia.",
        "No hay espacio libre para los adjuntos.",
        "No se pudo leer el archivo: %1\$s",
    )

    /** Texto en el idioma del dispositivo; los argumentos usan el formato de String.format. */
    fun get(id: Int, vararg args: Any): String {
        val table = if (java.util.Locale.getDefault().language == "es") es else en
        val raw = table[id]
        return if (args.isEmpty()) raw else String.format(java.util.Locale.getDefault(), raw, *args)
    }
}
