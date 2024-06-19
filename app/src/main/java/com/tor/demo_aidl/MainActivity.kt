package com.tor.demo_aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.tor.demo_aidl.aidl.IProcess
import com.tor.demo_aidl.service.ProcessService
import com.tor.demo_aidl.ui.theme.Demo_aidlTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var processService: IProcess? = null
    private var isConnect = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            processService = IProcess.Stub.asInterface(service)
            isConnect = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            processService = null
            isConnect = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, ProcessService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isConnect) {
            unbindService(connection)
            isConnect = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Demo_aidlTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CountDownText()
                }
            }
        }
    }

    @Composable
    fun CountDownText() {
        var count by remember { mutableStateOf(0) }

        LaunchedEffect(key1 = count) {
            if (count < 60) {
                delay(1000L)
                count = processService?.increment(count) ?: 0
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Center the text
        ) {
            Text(text = "Count = $count", fontSize = 60.sp)
        }
    }

}
