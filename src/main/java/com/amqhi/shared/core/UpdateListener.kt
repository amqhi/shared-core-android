package com.amqhi.shared.core

import org.json.JSONObject

interface UpdateListener {
    fun onNotify(code: Short)
    fun onNotify(code: Short, data: String)
    fun onNotify(code: Short, data: JSONObject)
}