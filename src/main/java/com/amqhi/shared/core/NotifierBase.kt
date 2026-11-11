package com.amqhi.shared.core

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.util.concurrent.CopyOnWriteArrayList

open class NotifierBase {
    protected val listeners = CopyOnWriteArrayList<UpdateListener>()
    protected val mainHandler = Handler(Looper.getMainLooper())

    open fun notify(code: Short) {
        mainHandler.post {
            listeners.forEach { it.onNotify(code) }
        }
    }

    open fun notify(code: Short, data: String) {
        mainHandler.post {
            listeners.forEach { it.onNotify(code, data) }
        }
    }

    open fun notifyJson(code: Short, data: String) {
        mainHandler.post {
            listeners.forEach { it.onNotify(code, JSONObject(data)) }
        }
    }

    open fun registerListener(listener: UpdateListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    open fun unregisterListener(listener: UpdateListener) {
        listeners.remove(listener)
    }
}
