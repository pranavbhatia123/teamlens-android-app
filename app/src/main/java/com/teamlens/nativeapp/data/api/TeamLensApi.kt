package com.teamlens.nativeapp.data.api

import com.teamlens.nativeapp.data.model.ApiResponse
import com.teamlens.nativeapp.data.model.ActivityTimeline
import com.teamlens.nativeapp.data.model.AttendanceOverview
import com.teamlens.nativeapp.data.model.AuthPayload
import com.teamlens.nativeapp.data.model.CalendarDay
import com.teamlens.nativeapp.data.model.CreateManualTimeRequest
import com.teamlens.nativeapp.data.model.DashboardAnalytics
import com.teamlens.nativeapp.data.model.LoginRequest
import com.teamlens.nativeapp.data.model.ManualTimeRequest
import com.teamlens.nativeapp.data.model.MePayload
import com.teamlens.nativeapp.data.model.ReviewManualTimeRequest
import com.teamlens.nativeapp.data.model.ScreenshotItem
import com.teamlens.nativeapp.data.model.SignupManagerRequest
import com.teamlens.nativeapp.data.model.Team
import com.teamlens.nativeapp.data.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamLensApi {
    @POST("/api/web/auth/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<AuthPayload>

    @POST("/api/web/auth/signup-manager")
    suspend fun signupManager(@Body body: SignupManagerRequest): ApiResponse<AuthPayload>

    @GET("/api/web/auth/me")
    suspend fun me(@Header("Authorization") authorization: String): ApiResponse<MePayload>

    @GET("/api/web/dashboard/analytics")
    suspend fun dashboardAnalytics(
        @Header("Authorization") authorization: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: String? = null
    ): ApiResponse<DashboardAnalytics>

    @GET("/api/web/dashboard/calendar")
    suspend fun calendarHeatmap(
        @Header("Authorization") authorization: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("userId") userId: String? = null
    ): ApiResponse<List<CalendarDay>>

    @GET("/api/web/dashboard/attendance")
    suspend fun attendance(
        @Header("Authorization") authorization: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: String? = null
    ): ApiResponse<AttendanceOverview>

    @GET("/api/web/dashboard/activity-timeline")
    suspend fun activityTimeline(
        @Header("Authorization") authorization: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: String? = null
    ): ApiResponse<ActivityTimeline>

    @GET("/api/web/dashboard/manual-time-requests")
    suspend fun manualTimeRequests(
        @Header("Authorization") authorization: String,
        @Query("status") status: String? = null
    ): ApiResponse<List<ManualTimeRequest>>

    @POST("/api/web/dashboard/manual-time-requests")
    suspend fun createManualTimeRequest(
        @Header("Authorization") authorization: String,
        @Body body: CreateManualTimeRequest
    ): ApiResponse<ManualTimeRequest>

    @PATCH("/api/web/dashboard/manual-time-requests/{id}/review")
    suspend fun reviewManualTimeRequest(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("id") id: String,
        @Body body: ReviewManualTimeRequest
    ): ApiResponse<ManualTimeRequest>

    @GET("/api/web/users")
    suspend fun users(@Header("Authorization") authorization: String): ApiResponse<List<UserProfile>>

    @GET("/api/web/teams")
    suspend fun teams(@Header("Authorization") authorization: String): ApiResponse<List<Team>>

    @GET("/api/agent/screenshots")
    suspend fun screenshots(
        @Header("Authorization") authorization: String,
        @Query("userId") userId: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("limit") limit: String = "50"
    ): ApiResponse<List<ScreenshotItem>>
}
