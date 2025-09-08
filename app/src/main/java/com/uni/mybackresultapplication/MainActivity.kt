package com.uni.mybackresultapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uni.mybackresultapplication.ui.DateRangePickerScreen
import com.uni.mybackresultapplication.ui.theme.MyBackResultApplicationTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBackResultApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    var showDatePicker by remember { mutableStateOf(false) }

    //if (showDatePicker) {
    DateRangePickerScreen(
        onDismiss = { showDatePicker = false },
        onConfirm = { fromDate, toDate ->
            android.util.Log.d("DatePicker", "Selected dates: $fromDate to $toDate")
            showDatePicker = false
        }
    )
    // }
} 