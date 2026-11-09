/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.amqhi.shared.core

class NetworkCallback(private val nativeContextPtr: Long) {
    private external fun onResponse(ptr: Long, statusCode: Int, response: String, headers: Map<String, String>)
    private external fun onFailure(ptr: Long, errorCode: Short, data: String)

    fun onResponse(statusCode: Int, response: String, headers: Map<String, String>) {
        onResponse(nativeContextPtr, statusCode, response, headers)
    }

    fun onFailure(errorCode: Short, data: String) {
        onFailure(nativeContextPtr, errorCode, data)
    }
}
