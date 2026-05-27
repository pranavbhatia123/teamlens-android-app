package com.teamlens.nativeapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean = false,
    val message: String? = null,
    val data: T? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignupManagerRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val organizationName: String
)

@Serializable
data class AuthPayload(
    val accessToken: String? = null,
    val user: UserProfile? = null,
    val organization: Organization? = null
)

@Serializable
data class MePayload(
    val id: String,
    val fullName: String,
    val email: String,
    val role: String,
    val organization: Organization
)

@Serializable
data class UserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val role: String,
    val organizationId: String? = null
)

@Serializable
data class Organization(
    val id: String,
    val name: String,
    val slug: String? = null
)

@Serializable
data class DashboardAnalytics(
    val workSeconds: Double = 0.0,
    val activeSeconds: Double = 0.0,
    val idleSeconds: Double = 0.0,
    val productivityPercent: Double = 0.0,
    val totalTrackedSeconds: Double = 0.0,
    val totalActiveSeconds: Double = 0.0,
    val totalIdleSeconds: Double = 0.0,
    val avgActivityPercent: Double = 0.0
)

@Serializable
data class Team(
    val id: String,
    val name: String,
    val description: String? = null,
    val members: List<UserProfile> = emptyList()
)

@Serializable
data class ScreenshotItem(
    val id: String,
    val sessionId: String? = null,
    val filePath: String? = null,
    val activeApplication: String? = null,
    val windowTitle: String? = null,
    val domain: String? = null,
    val url: String? = null,
    val employeeName: String? = null,
    val projectName: String? = null,
    val capturedAt: String? = null,
    val createdAt: String? = null,
    val userId: String? = null,
    @SerialName("user")
    val user: UserProfile? = null
)

@Serializable
data class CalendarDay(
    val date: String,
    val workSeconds: Double = 0.0,
    val activeSeconds: Double = 0.0,
    val manualSeconds: Double = 0.0
)

@Serializable
data class AttendanceOverview(
    val month: String = "",
    val thresholdMinutes: Int = 0,
    val daysInMonth: Int = 0,
    val stats: AttendanceStats = AttendanceStats(),
    val employees: List<AttendanceEmployee> = emptyList(),
    val timesheets: List<TimesheetEntry> = emptyList()
)

@Serializable
data class AttendanceStats(
    val attendedDays: Int = 0,
    val currentlyWorking: Int = 0,
    val belowThreshold: Int = 0,
    val employees: Int = 0,
    val officeDays: Int = 0,
    val remoteDays: Int = 0
)

@Serializable
data class AttendanceEmployee(
    val userId: String,
    val employeeName: String,
    val email: String,
    val initials: String = "",
    val attendedDays: Int = 0,
    val belowThresholdDays: Int = 0,
    val absentDays: Int = 0,
    val workingDays: Int = 0,
    val officeDays: Int = 0,
    val remoteDays: Int = 0,
    val shiftSummary: String = "",
    val cells: List<AttendanceCell> = emptyList()
)

@Serializable
data class AttendanceCell(
    val date: String,
    val day: Int,
    val status: String,
    val workSeconds: Double = 0.0,
    val shiftName: String? = null,
    val locationStatus: String? = null,
    val clockInAt: String? = null,
    val clockOutAt: String? = null,
    val sessions: List<AttendanceSession> = emptyList()
)

@Serializable
data class AttendanceSession(
    val id: String,
    val clockInAt: String,
    val clockOutAt: String? = null,
    val workSeconds: Double = 0.0,
    val shiftName: String = "",
    val locationType: String? = null,
    val isCurrentlyWorking: Boolean = false
)

@Serializable
data class TimesheetEntry(
    val id: String,
    val userId: String,
    val employeeName: String,
    val teamName: String? = null,
    val locationStatus: String? = null,
    val shiftName: String = "",
    val date: String,
    val clockInAt: String,
    val clockOutAt: String? = null,
    val workSeconds: Double = 0.0,
    val activeSeconds: Double = 0.0,
    val isCurrentlyWorking: Boolean = false
)

@Serializable
data class ManualTimeRequest(
    val id: String,
    val userId: String,
    val employeeName: String,
    val employeeEmail: String = "",
    val requestedByName: String = "",
    val reviewedByName: String? = null,
    val startAt: String,
    val endAt: String,
    val durationSeconds: Double = 0.0,
    val reason: String,
    val status: String,
    val reviewNote: String? = null,
    val createdAt: String
)

@Serializable
data class CreateManualTimeRequest(
    val userId: String? = null,
    val startAt: String,
    val endAt: String,
    val reason: String
)

@Serializable
data class ReviewManualTimeRequest(
    val status: String,
    val reviewNote: String? = null
)

@Serializable
data class ActivityTimeline(
    val start: String = "",
    val end: String = "",
    val employees: List<ActivityTimelineEmployee> = emptyList()
)

@Serializable
data class ActivityTimelineEmployee(
    val userId: String,
    val employeeName: String = "",
    val email: String = "",
    val activeSeconds: Double = 0.0,
    val idleSeconds: Double = 0.0,
    val workSeconds: Double = 0.0,
    val utilizationPercent: Int = 0,
    val mousePercent: Int? = null,
    val keyboardPercent: Int? = null,
    val firstActiveAt: String? = null,
    val lastActiveAt: String? = null,
    val topApps: List<ActivityTimelineApp> = emptyList(),
    val segments: List<ActivityTimelineSegment> = emptyList(),
    val mouseMoves: Int = 0,
    val keyPresses: Int = 0
)

@Serializable
data class ActivityTimelineApp(
    val name: String,
    val seconds: Double = 0.0
)

@Serializable
data class ActivityTimelineSegment(
    val start: String,
    val end: String,
    val kind: String = "active",
    val mouseMoves: Int = 0,
    val keyPresses: Int = 0
)
