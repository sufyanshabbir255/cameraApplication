package com.uni.mybackresultapplication

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uni.mybackresultapplication.ui.theme.MyBackResultApplicationTheme
import com.uni.mybackresultapplication.ui.CameraPreviewScreen
import com.uni.mybackresultapplication.viewmodel.CameraViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var usbManager: UsbManager
    private lateinit var cameraViewModel: CameraViewModel
    
    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    device?.let { cameraViewModel.onUsbDeviceAttached(it) }
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    device?.let { cameraViewModel.onUsbDeviceDetached(it) }
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        cameraViewModel = CameraViewModel(usbManager)
        
        // Register USB receiver
        val filter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        registerReceiver(usbReceiver, filter)
        
        setContent {
            MyBackResultApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraPreviewScreen(
                        viewModel = cameraViewModel,
                        onRequestPermission = { device ->
                            requestUsbPermission(device)
                        }
                    )
                }
            }
        }
    }
    
    private fun requestUsbPermission(device: UsbDevice) {
        val permissionIntent = PendingIntent.getBroadcast(
            this, 0, Intent(ACTION_USB_PERMISSION), 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        usbManager.requestPermission(device, permissionIntent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbReceiver)
        cameraViewModel.cleanup()
    }
    
    companion object {
        private const val ACTION_USB_PERMISSION = "com.uni.mybackresultapplication.USB_PERMISSION"
    }
} 