package com.uni.mybackresultapplication.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePickerScreen(
    onDismiss: () -> Unit = {},
    onConfirm: (LocalDate?, LocalDate?) -> Unit = { _, _ -> }
) {
    var selectedFromDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedToDate by remember { mutableStateOf<LocalDate?>(null) }
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Date input fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // From date field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "From",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showFromDatePicker = true }
                    ) {
                        Text(
                            text = selectedFromDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                ?: "Select a date",
                            fontSize = 16.sp,
                            color = if (selectedFromDate != null) Color.Black else Color(0xFF999999),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                        // Green underline
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color(0xFF4CAF50))
                                .align(Alignment.BottomCenter)
                        )
                    }
                }

                // To date field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "To",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showToDatePicker = true }
                    ) {
                        Text(
                            text = selectedToDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                ?: "Select a date",
                            fontSize = 16.sp,
                            color = if (selectedToDate != null) Color.Black else Color(0xFF999999),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                        // Green underline
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color(0xFF4CAF50))
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm button
            Button(
                onClick = {
                    onConfirm(selectedFromDate, selectedToDate)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CONFIRM",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // From date picker bottom sheet
        if (showFromDatePicker) {
            DatePickerBottomSheet(
                selectedDate = selectedFromDate,
                onDateSelected = { date ->
                    selectedFromDate = date
                    showFromDatePicker = false
                },
                onDismiss = { showFromDatePicker = false }
            )
        }

        // To date picker bottom sheet
        if (showToDatePicker) {
            DatePickerBottomSheet(
                selectedDate = selectedToDate,
                onDateSelected = { date ->
                    selectedToDate = date
                    showToDatePicker = false
                },
                onDismiss = { showToDatePicker = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerBottomSheet(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(selectedDate ?: LocalDate.now()) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    // Bottom sheet overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(0.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with close button only
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Month navigation header - month on left, arrows on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${
                            currentMonth.month.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            )
                        } ${currentMonth.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentMonth = currentMonth.minusMonths(1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous month",
                                tint = Color.Black
                            )
                        }

                        IconButton(
                            onClick = { currentMonth = currentMonth.plusMonths(1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next month",
                                tint = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                        Text(
                            text = day,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Calendar grid - simplified approach
                val firstDayOfMonth = currentMonth.withDayOfMonth(1)
                val firstDayOfWeek =
                    firstDayOfMonth.dayOfWeek.value % 7 // Convert to 0=Sunday format
                val daysInMonth = currentMonth.lengthOfMonth()
                val previousMonth = currentMonth.minusMonths(1)
                val daysInPreviousMonth = previousMonth.lengthOfMonth()

                // Create a 6x7 grid (42 cells total)
                val calendarDays = mutableListOf<Pair<Int, String>>() // (day, type)

                // Add previous month days
                for (i in 0 until firstDayOfWeek) {
                    val day = daysInPreviousMonth - firstDayOfWeek + i + 1
                    calendarDays.add(Pair(day, "previous"))
                }

                // Add current month days
                for (day in 1..daysInMonth) {
                    calendarDays.add(Pair(day, "current"))
                }

                // Add next month days to fill remaining cells
                val remainingCells = 42 - calendarDays.size
                for (day in 1..remainingCells) {
                    calendarDays.add(Pair(day, "next"))
                }

                // Create calendar rows
                val calendarRows = calendarDays.chunked(7)

                calendarRows.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { (day, type) ->
                            val isCurrentMonth = type == "current"
                            val isPreviousMonth = type == "previous"

                            val date = when (type) {
                                "current" -> currentMonth.withDayOfMonth(day)
                                "previous" -> previousMonth.withDayOfMonth(day)
                                "next" -> currentMonth.plusMonths(1).withDayOfMonth(day)
                                else -> currentMonth.withDayOfMonth(day)
                            }

                            val isSelected =
                                tempSelectedDate != null && date == tempSelectedDate && isCurrentMonth

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) Color(0xFF4CAF50) else Color.Transparent
                                    )
                                    .clickable {
                                        if (isCurrentMonth) {
                                            tempSelectedDate = date
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 16.sp,
                                    color = when {
                                        isSelected -> Color.White
                                        isCurrentMonth -> Color.Black
                                        else -> Color(0xFFCCCCCC)
                                    },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm button
                Button(
                    onClick = {
                        tempSelectedDate?.let { onDateSelected(it) }
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "CONFIRM",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
