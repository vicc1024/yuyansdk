package com.yuyan.imemodule.service

import android.content.ClipboardManager.OnPrimaryClipChangedListener
import com.yuyan.imemodule.application.ImeSdkApplication
import com.yuyan.imemodule.application.LauncherModel
import com.yuyan.imemodule.prefs.AppPrefs
import com.yuyan.imemodule.utils.clipboardManager

/**
 * 剪切板监听
 * 移除使用广播监听方式，解决部分手机后台无法启动监听服务异常(API level 31)。
 */
object ClipboardHelper : OnPrimaryClipChangedListener {

    fun init() {
        ImeSdkApplication.context.clipboardManager.addPrimaryClipChangedListener(this)
    }

    override fun onPrimaryClipChanged() {
        val isClipboardListening = AppPrefs.getInstance().clipboard.clipboardListening.getValue()
        if(isClipboardListening) {
           val item = ImeSdkApplication.context.clipboardManager.primaryClip?.getItemAt(0)
            if(item != null) {
                item.takeIf { it.text?.isNotBlank() == true }
                    ?.let { b ->
                        val data = if (b.text.length > 5000) b.text.substring(0, 4999) else b.text.toString()
                        LauncherModel.instance.mClipboardDao?.insertClopboard(data)
                        if (AppPrefs.getInstance().clipboard.clipboardSuggestion.getValue()) {
                            AppPrefs.getInstance().internal.clipboardUpdateTime.setValue(System.currentTimeMillis())
                            AppPrefs.getInstance().internal.clipboardUpdateContent.setValue(data)
                        }
                    }
            }
        }
    }
}
