package com.techfortyone.servicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techfortyone.servicedemo.boundservice.MyBoundService
import com.techfortyone.servicedemo.ui.theme.ServiceDemoTheme

class MainActivity : ComponentActivity() {

    private lateinit var myBoundServiceBinder: MyBoundService.MyServiceBinder
    private lateinit var myService: MyBoundService
    private var isServiceBound: Boolean? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startServiceIntent = Intent(this, MyBoundService::class.java)
        setContent {
            ServiceDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.d("TAGSS", "onCreate: with thread id ${Thread.currentThread().id}")

                    ServiceScreen(
                        modifier = Modifier.fillMaxSize(), this,
                        serviceIntent = startServiceIntent,
                        onBindServiceClicked = { serviceIntent ->
                            mBindService(serviceIntent)
                        },
                        unBindServiceClicked = {
                            mUnbindService()
                        }
                    ) {
                        getRandomNumber()
                    }

                }
            }
        }
    }

    private fun mBindService(serviceIntent: Intent) {
        if (serviceConnection == null) {
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
                    myBoundServiceBinder =
                        iBinder as MyBoundService.MyServiceBinder
                    myService = myBoundServiceBinder.getService()
                    isServiceBound = true
                }

                override fun onServiceDisconnected(componentName: ComponentName?) {
                    isServiceBound = false
                }
            }
        }
        bindService(serviceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)

    }


    private fun mUnbindService() {
        if (isServiceBound == true) {
            serviceConnection?.let { unbindService(it) }
        }
    }

    private fun getRandomNumber(): Int? {
        if (isServiceBound == true) {
            if(this::myService.isInitialized) {
                Log.d("TAGSS", "myservice is  initizli: ")
                return myService.getRandomNumber()

            }
            else {
                Log.d("TAGSS", "myservice is  not initizli: ")
            }
        }
        return 0
    }
}


@Composable
fun ServiceScreen(
    modifier: Modifier,
    context: Context,
    serviceIntent: Intent,
    onBindServiceClicked: (Intent) -> Unit,
    unBindServiceClicked: () -> Unit,
    getRandomNumber: () -> Int?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "The random number is")

        Button(onClick = {
            context.apply {
                //  val startServiceIntent = Intent(this, MyService::class.java)
                startService(serviceIntent)
            }
        }) {
            Text(text = "Start Service")
        }
        Button(onClick = {
            context.apply {
                //   val stopServiceIntent = Intent(this, MyService::class.java)
                stopService(serviceIntent)
            }
        }) {
            Text(text = "Stop Service")
        }
        Button(onClick = {
            context.apply {
                //   val stopServiceIntent = Intent(this, MyService::class.java)
                onBindServiceClicked.invoke(serviceIntent)

            }
        }) {
            Text(text = "Bind Service")
        }

        Button(onClick = {
            context.apply {
                //   val stopServiceIntent = Intent(this, MyService::class.java)
                unBindServiceClicked.invoke()

            }
        }) {
            Text(text = "Unbind Service")
        }

        Button(onClick = {
            context.apply {
                //   val stopServiceIntent = Intent(this, MyService::class.java)
                val res = getRandomNumber.invoke()

                Log.d("TAGSS", "random number is $res")
            }
        }) {

            Text(text = "Get random number")
        }
    }

}



