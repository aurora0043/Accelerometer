package com.example.accelerometer

import android.content.Context.SENSOR_SERVICE
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.accelerometer.ui.theme.AccelerometerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AccelerometerTheme {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var showSecond by remember { mutableStateOf(false) }

    if (showSecond) {
        SecondScreen(onBackClick = { showSecond = false })
    } else {
        FirstScreen(onNavigateToSecond = { showSecond = true })
    }
}

@Composable
fun FirstScreen(onNavigateToSecond: () -> Unit) {
    var msg by remember { mutableStateOf("加速感應器實例") }
    var msg2 by remember { mutableStateOf("") }
    var xTilt by remember { mutableStateOf(0f) }
    var yTilt by remember { mutableStateOf(0f) }
    var zTilt by remember { mutableStateOf(0f) }
    val context = LocalContext.current
    val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                xTilt = event.values[0]
                yTilt = event.values[1]
                zTilt = event.values[2]
                msg = "加速感應器實例\n" + String.format("x軸: %1.2f \n"
                        + "y軸: %1.2f \n" +
                        "z軸: %1.2f", xTilt, yTilt, zTilt)
                if (Math.abs(xTilt) < 1 && Math.abs(yTilt) < 1 && zTilt < -9) {
                    msg2 = "朝下平放"
                } else if (Math.abs(xTilt) + Math.abs(yTilt) + Math.abs(zTilt) > 32) {
                    msg2 = "手機搖晃"
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            TODO("Not yet implemented")
        }

    }
    LaunchedEffect(Unit) {
        // composable首次載入時，註冊監聽事件
        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Green),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(msg)
        Text(msg2)
        Button(onClick = { onNavigateToSecond() }) {
            Text(text = "跳轉畫面2")
        }
    }
}

@Composable
fun SecondScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onBackClick() }) {
            Text(text = "返回畫面1")
        }
    }
}

@Composable
fun SecondScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Button(onClick = {  })
        {
            Text(text = "返回畫面1")
        }
    }
}