package com.teamlens.nativeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Section(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    Team("Team", Icons.Outlined.Groups),
    Attendance("Time", Icons.Outlined.Schedule),
    Screenshots("Shots", Icons.Outlined.Image),
    Live("Live", Icons.Outlined.PlayCircle),
    Activities("Activity", Icons.Outlined.DonutLarge),
    Calendar("Calendar", Icons.Outlined.CalendarMonth),
    Settings("Settings", Icons.Outlined.Settings)
}
