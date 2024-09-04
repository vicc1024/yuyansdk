package com.yuyan.imemodule.service

import android.content.ClipboardManager.OnPrimaryClipChangedListener
import com.yuyan.imemodule.application.LauncherModel
import com.yuyan.imemodule.prefs.AppPrefs
import splitties.systemservices.clipboardManager

/**
 * 剪切板监听
 * 移除使用广播监听方式，解决部分手机后台无法启动监听服务异常(API level 31)。
 * @see android.app.Exceptions.BackgroundServiceStartNotAllowedException
 */
object ClipboardHelper : OnPrimaryClipChangedListener {

    fun init() {
        clipboardManager.addPrimaryClipChangedListener(this)
    }

    override fun onPrimaryClipChanged() {
        val isClipboardListening = AppPrefs.getInstance().clipboard.clipboardListening.getValue()
        if(isClipboardListening) {
            clipboardManager.primaryClip?.getItemAt(0)
                ?.takeIf { it.text!!.isNotBlank() }
                ?.let { b ->
                    val data =
                        if (b.text.length > 5000) b.text.substring(0, 4999) else b.text.toString()
                    if (AppPrefs.getInstance().clipboard.clipboardSuggestion.getValue()) {
                        LauncherModel.instance.mLastClipboardContent = data
                        LauncherModel.instance.mLastClipboardTime = System.currentTimeMillis()
                    }
                    LauncherModel.instance.mClipboardDao?.insertClopboard(data)

                }
        }
    }
}