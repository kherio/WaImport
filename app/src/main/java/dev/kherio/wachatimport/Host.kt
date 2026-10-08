package dev.kherio.wachatimport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.util.TypedValue
import android.widget.Toast
import de.robv.android.xposed.XposedBridge
import java.io.File

/** Utilidades para trabajar dentro del proceso de WhatsApp (o WhatsApp Business). */
object Host {

    fun dp(context: Context, v: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics
        ).toInt()

    /** Carpeta de datos de la cuenta activa (con varias cuentas está en accounts/<id>). */
    fun accountDataDir(context: Context): File {
        val dataDir = context.applicationContext.filesDir.parentFile!!
        val sw = File(dataDir, "app_account_switching/active_account")
        if (sw.exists()) {
            val id = try { sw.readText().trim() } catch (_: Exception) { "" }
            if (id.isNotEmpty()) {
                val accountDir = File(dataDir, "accounts/$id")
                if (accountDir.exists()) return accountDir
            }
        }
        return dataDir
    }

    /** Carpeta raíz de medios de WhatsApp ("WhatsApp" o "WhatsApp Business"). */
    fun rootWhatsAppDir(context: Context): File {
        val app = context.applicationContext
        val appName = app.packageManager.getApplicationLabel(app.applicationInfo).toString()
        val mediaDirs = app.externalMediaDirs
        if (mediaDirs.isNotEmpty() && mediaDirs[0] != null) {
            val root = File(mediaDirs[0], appName)
            if (root.exists()) return root
        }
        return File(Environment.getExternalStorageDirectory(), appName)
    }

    fun restart(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName) ?: return
        val main = Intent.makeRestartActivityTask(intent.component)
        main.setPackage(activity.packageName)
        activity.startActivity(main)
        Runtime.getRuntime().exit(0)
    }

    fun toast(context: Context, text: String?, length: Int = Toast.LENGTH_SHORT) {
        if (text.isNullOrEmpty()) return
        val act = context as? Activity
        val show = { Toast.makeText(context, text, length).show() }
        if (act != null) act.runOnUiThread(show) else show()
    }

    fun log(t: Throwable) {
        XposedBridge.log("[WaChatImport] " + android.util.Log.getStackTraceString(t))
    }

    fun log(msg: String) {
        XposedBridge.log("[WaChatImport] $msg")
    }

    /** Color de texto del tema actual, con una alternativa según modo claro/oscuro. */
    fun textColor(context: Context): Int {
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.textColorPrimary, tv, true)) {
            if (tv.type in TypedValue.TYPE_FIRST_COLOR_INT..TypedValue.TYPE_LAST_COLOR_INT) return tv.data
            try {
                return context.getColor(tv.resourceId)
            } catch (_: Exception) {
            }
        }
        val night = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES
        return if (night) -0x1 else -0x1000000
    }

    /** Nombre del contacto del teléfono para un número, si WhatsApp tiene permiso de contactos. */
    fun contactName(context: Context, phoneDigits: String): String? = try {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("+$phoneDigits")
        )
        context.contentResolver.query(
            uri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null
        )?.use { c -> if (c.moveToFirst()) c.getString(0)?.takeIf { it.isNotBlank() } else null }
    } catch (_: Throwable) {
        null
    }
}
