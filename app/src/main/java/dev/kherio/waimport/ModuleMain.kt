package dev.kherio.waimport

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import dev.kherio.waimport.importer.ImportChatFlow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method

/**
 * Punto de entrada del módulo: añade "Importar chat" al menú de la pantalla principal de
 * WhatsApp / WhatsApp Business y recibe el archivo elegido en el selector.
 */
class ModuleMain : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.whatsapp" && lpparam.packageName != "com.whatsapp.w4b") return
        if (lpparam.processName != lpparam.packageName) return
        Host.log("cargado en ${lpparam.packageName}")
        // La clase de la pantalla principal se localiza por el manifiesto cuando la app ya existe.
        XposedHelpers.findAndHookMethod(
            Instrumentation::class.java, "callApplicationOnCreate", Application::class.java,
            object : XC_MethodHook() {
                private var done = false
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (done) return
                    done = true
                    try {
                        val app = param.args[0] as Application
                        hookHome(app, lpparam.classLoader)
                    } catch (t: Throwable) {
                        Host.log(t)
                    }
                }
            })
    }

    @Suppress("DEPRECATION")
    private fun findHomeClass(app: Application, cl: ClassLoader): Class<*> {
        val names = app.packageManager
            .getPackageInfo(app.packageName, PackageManager.GET_ACTIVITIES)
            .activities?.map { it.name }.orEmpty()
        val name = names.firstOrNull { it.endsWith(".HomeActivity") }
            ?: names.firstOrNull { it.endsWith("HomeActivity") }
            ?: throw ClassNotFoundException("HomeActivity no está en el manifiesto (${names.size} actividades)")
        Host.log("pantalla principal: $name")
        return cl.loadClass(name)
    }

    private fun hookHome(app: Application, cl: ClassLoader) {
        val home = findHomeClass(app, cl)

        val onCreateMenu = findDeclared(home, "onCreateOptionsMenu", Menu::class.java)
        Host.log("hook de menú en ${onCreateMenu.declaringClass.name}")

        XposedBridge.hookMethod(onCreateMenu, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val menu = param.args[0] as? Menu ?: return
                val activity = param.thisObject as? Activity ?: return
                try {
                    menu.add(0, MENU_ID, 0, S.get(S.import_chat)).setOnMenuItemClickListener {
                        openPicker(activity)
                        true
                    }
                } catch (t: Throwable) {
                    Host.log(t)
                }
            }
        })

        val onResult = findDeclared(
            home, "onActivityResult", Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Intent::class.java
        )
        XposedBridge.hookMethod(onResult, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val requestCode = param.args[0] as Int
                val resultCode = param.args[1] as Int
                if (requestCode != REQUEST_PICK_FILE || resultCode != Activity.RESULT_OK) return
                val uri = (param.args[2] as? Intent)?.data ?: return
                val activity = param.thisObject as? Activity ?: return
                try {
                    ImportChatFlow(activity, uri).start()
                } catch (t: Throwable) {
                    Host.log(t)
                    Host.toast(activity, t.message)
                }
            }
        })
    }

    /** Busca el método subiendo por la jerarquía hasta la clase que lo declara. */
    private fun findDeclared(start: Class<*>, name: String, vararg params: Class<*>): Method {
        var c: Class<*>? = start
        while (c != null) {
            try {
                return c.getDeclaredMethod(name, *params)
            } catch (_: NoSuchMethodException) {
                c = c.superclass
            }
        }
        throw NoSuchMethodException("${start.name}.$name")
    }

    private fun openPicker(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("text/plain", "application/zip", "application/x-zip-compressed", "application/octet-stream")
            )
        }
        try {
            activity.startActivityForResult(intent, REQUEST_PICK_FILE)
        } catch (e: Exception) {
            Host.toast(activity, e.message)
        }
    }

    private companion object {
        const val REQUEST_PICK_FILE = 0x5A17
        const val MENU_ID = 0x5A18
    }
}
