package com.uni.mybackresultapplication.viewmodel

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CameraState(
    val isConnected: Boolean = false,
    val isPreviewActive: Boolean = false,
    val connectedDevice: UsbDevice? = null,
    val errorMessage: String? = null,
    val availableDevices: List<UsbDevice> = emptyList()
)

class CameraViewModel(private val usbManager: UsbManager) : ViewModel() {
    
    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()
    
    init {
        scanForUsbDevices()
    }
    
    fun scanForUsbDevices() {
        viewModelScope.launch {
            try {
                val deviceList = usbManager.deviceList
                val cameraDevices = deviceList.values.filter { device ->
                    isCameraDevice(device)
                }
                
                _cameraState.value = _cameraState.value.copy(
                    availableDevices = cameraDevices,
                    errorMessage = null
                )
                
                // Auto-connect to first available camera if none connected
                if (!_cameraState.value.isConnected && cameraDevices.isNotEmpty()) {
                    val firstDevice = cameraDevices.first()
                    if (usbManager.hasPermission(firstDevice)) {
                        connectToCamera(firstDevice)
                    }
                }
            } catch (e: Exception) {
                _cameraState.value = _cameraState.value.copy(
                    errorMessage = "Error scanning for devices: ${e.message}"
                )
            }
        }
    }
    
    fun onUsbDeviceAttached(device: UsbDevice) {
        viewModelScope.launch {
            if (isCameraDevice(device)) {
                val currentDevices = _cameraState.value.availableDevices.toMutableList()
                if (!currentDevices.contains(device)) {
                    currentDevices.add(device)
                }
                
                _cameraState.value = _cameraState.value.copy(
                    availableDevices = currentDevices,
                    errorMessage = null
                )
                
                // Auto-connect if no camera is currently connected
                if (!_cameraState.value.isConnected && usbManager.hasPermission(device)) {
                    connectToCamera(device)
                }
            }
        }
    }
    
    fun onUsbDeviceDetached(device: UsbDevice) {
        viewModelScope.launch {
            val currentDevices = _cameraState.value.availableDevices.toMutableList()
            currentDevices.remove(device)
            
            val isCurrentDevice = _cameraState.value.connectedDevice == device
            
            _cameraState.value = _cameraState.value.copy(
                availableDevices = currentDevices,
                isConnected = if (isCurrentDevice) false else _cameraState.value.isConnected,
                isPreviewActive = if (isCurrentDevice) false else _cameraState.value.isPreviewActive,
                connectedDevice = if (isCurrentDevice) null else _cameraState.value.connectedDevice,
                errorMessage = if (isCurrentDevice) "Camera disconnected" else null
            )
        }
    }
    
    fun connectToCamera(device: UsbDevice) {
        viewModelScope.launch {
            try {
                if (usbManager.hasPermission(device)) {
                    _cameraState.value = _cameraState.value.copy(
                        isConnected = true,
                        connectedDevice = device,
                        isPreviewActive = true,
                        errorMessage = null
                    )
                } else {
                    _cameraState.value = _cameraState.value.copy(
                        errorMessage = "Permission required for device: ${device.deviceName}"
                    )
                }
            } catch (e: Exception) {
                _cameraState.value = _cameraState.value.copy(
                    errorMessage = "Error connecting to camera: ${e.message}"
                )
            }
        }
    }
    
    fun disconnectCamera() {
        viewModelScope.launch {
            _cameraState.value = _cameraState.value.copy(
                isConnected = false,
                isPreviewActive = false,
                connectedDevice = null,
                errorMessage = null
            )
        }
    }
    
    fun clearError() {
        viewModelScope.launch {
            _cameraState.value = _cameraState.value.copy(errorMessage = null)
        }
    }
    
    private fun isCameraDevice(device: UsbDevice): Boolean {
        // Check for UVC (USB Video Class) devices
        for (i in 0 until device.interfaceCount) {
            val usbInterface = device.getInterface(i)
            if (usbInterface.interfaceClass == 14) { // USB_CLASS_VIDEO
                return true
            }
        }
        
        // Also check for common camera vendor IDs and product IDs
        val vendorId = device.vendorId
        val productId = device.productId
        
        // Common camera manufacturers
        val cameraVendors = setOf(
            0x046d, // Logitech
            0x0bda, // Realtek
            0x1bcf, // Sunplus Innovation Technology
            0x0c45, // Sonix Technology
            0x1e4e, // Cubeternet
            0x0ac8, // Z-Star Microelectronics
            0x1b3b, // IMC Networks
            0x0bda, // Realtek Semiconductor
            0x1e4e, // Cubeternet
            0x0ac8  // Z-Star Microelectronics
        )
        
        return cameraVendors.contains(vendorId)
    }
    
    fun cleanup() {
        disconnectCamera()
    }
}
