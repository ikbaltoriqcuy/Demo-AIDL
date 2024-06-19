// ProcessService.kt
package com.tor.demo_aidl.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tor.demo_aidl.aidl.IProcess

class ProcessService : Service() {

    private val binder = object : IProcess.Stub() {

        override fun increment(value: Int): Int {
            return value + 1
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}
