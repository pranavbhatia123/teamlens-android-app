package com.teamlens.nativeapp

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.ConsoleMessage
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teamlens.nativeapp.data.api.ApiClient
import com.teamlens.nativeapp.data.model.ActivityTimeline
import com.teamlens.nativeapp.data.model.ActivityTimelineEmployee
import com.teamlens.nativeapp.data.api.BackendConfig
import com.teamlens.nativeapp.data.model.AttendanceOverview
import com.teamlens.nativeapp.data.model.CalendarDay
import com.teamlens.nativeapp.data.model.CreateManualTimeRequest
import com.teamlens.nativeapp.data.model.DashboardAnalytics
import com.teamlens.nativeapp.data.model.LoginRequest
import com.teamlens.nativeapp.data.model.ManualTimeRequest
import com.teamlens.nativeapp.data.model.ReviewManualTimeRequest
import com.teamlens.nativeapp.data.model.ScreenshotItem
import com.teamlens.nativeapp.data.model.SignupManagerRequest
import com.teamlens.nativeapp.data.model.Team
import com.teamlens.nativeapp.data.model.TimesheetEntry
import com.teamlens.nativeapp.data.model.UserProfile
import com.teamlens.nativeapp.data.session.SessionStore
import com.teamlens.nativeapp.ui.components.ActivityTimelineAxis
import com.teamlens.nativeapp.ui.components.ActivityTimelineLegend
import com.teamlens.nativeapp.ui.components.EmployeeActivityCard
import com.teamlens.nativeapp.ui.theme.Background
import com.teamlens.nativeapp.ui.theme.Border
import com.teamlens.nativeapp.ui.theme.Brand
import com.teamlens.nativeapp.ui.theme.BrandLight
import com.teamlens.nativeapp.ui.theme.Danger
import com.teamlens.nativeapp.ui.theme.DangerTint
import com.teamlens.nativeapp.ui.theme.Info
import com.teamlens.nativeapp.ui.theme.Ink
import com.teamlens.nativeapp.ui.theme.Muted
import com.teamlens.nativeapp.ui.theme.MutedLight
import com.teamlens.nativeapp.ui.theme.Surface
import com.teamlens.nativeapp.ui.theme.Surface2
import com.teamlens.nativeapp.ui.theme.Success
import com.teamlens.nativeapp.ui.theme.Warning
import com.teamlens.nativeapp.ui.theme.TeamLensTheme
import com.teamlens.nativeapp.ui.navigation.Section
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.rgb(248, 245, 241)))
        window.statusBarColor = Color.rgb(255, 253, 251)
        window.navigationBarColor = Color.rgb(255, 253, 251)
        window.decorView.setBackgroundColor(Color.rgb(248, 245, 241))
        setContent {
            TeamLensTheme {
                TeamLensApp()
            }
        }
    }
}

private enum class AuthMode { Login, Signup }
private data class HomeModule(
    val label: String,
    val subtitle: String,
    val color: androidx.compose.ui.graphics.Color,
    val section: Section
)

data class UiState(
    val loading: Boolean = true,
    val token: String = "",
    val user: UserProfile? = null,
    val organizationName: String = "",
    val analytics: DashboardAnalytics = DashboardAnalytics(),
    val attendance: AttendanceOverview = AttendanceOverview(),
    val manualTimeRequests: List<ManualTimeRequest> = emptyList(),
    val employees: List<UserProfile> = emptyList(),
    val teams: List<Team> = emptyList(),
    val screenshotCount: Int = 0,
    val calendarDays: List<CalendarDay> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedUserId: String = "",
    val selectedDayAnalytics: DashboardAnalytics = DashboardAnalytics(),
    val dayScreenshots: List<ScreenshotItem> = emptyList(),
    val screenshotLoading: Boolean = false,
    val allScreenshots: List<ScreenshotItem> = emptyList(),
    val liveRows: List<LiveEmployeeUi> = emptyList(),
    val teamActivity: List<ActivityTimelineEmployee> = emptyList(),
    val selectedEmployeeId: String? = null,
    val teamLoading: Boolean = false,
    val liveLoading: Boolean = false,
    val timeLoading: Boolean = false,
    val error: String = ""
)

data class LiveEmployeeUi(
    val user: UserProfile,
    val screenshot: ScreenshotItem? = null,
    val lastActiveAt: String? = null,
    val activeApp: String = "No active app"
)

class TeamLensViewModel(application: Application) : AndroidViewModel(application) {
    private val store = SessionStore(application)
    private val api = ApiClient.api

    var state by mutableStateOf(UiState())
        private set

    init {
        restoreSession()
    }

    private fun bearer(token: String = state.token) = "Bearer $token"

    private fun authErrorMessage(error: Throwable): String {
        if (error is HttpException && error.code() == 401) {
            return "Hosted TeamLens rejected these credentials. Try the hosted account/password or create a new manager account."
        }
        return error.message ?: "Login failed"
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val token = store.token.first()
            if (token.isBlank()) {
                state = state.copy(loading = false)
                return@launch
            }
            state = state.copy(token = token)
            runCatching {
                val me = api.me(bearer(token))
                if (!me.success || me.data == null) error(me.message ?: "Session expired")
                val data = me.data
                state = state.copy(
                    user = UserProfile(data.id, data.fullName, data.email, data.role, data.organization.id),
                    organizationName = data.organization.name
                )
                loadHome()
            }.onFailure {
                store.clear()
                state = UiState(loading = false, error = it.message ?: "Unable to restore session")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(loading = true, error = "")
            runCatching {
                val res = api.login(LoginRequest(email.trim(), password.trim()))
                if (!res.success || res.data?.accessToken.isNullOrBlank()) error(res.message ?: "Login failed")
                val token = res.data!!.accessToken!!
                store.saveToken(token)
                state = state.copy(
                    token = token,
                    user = res.data.user,
                    organizationName = res.data.organization?.name.orEmpty()
                )
                loadHome()
            }.onFailure {
                state = state.copy(loading = false, error = authErrorMessage(it))
            }
        }
    }

    fun signup(name: String, email: String, password: String, org: String) {
        viewModelScope.launch {
            state = state.copy(loading = true, error = "")
            runCatching {
                val res = api.signupManager(SignupManagerRequest(name.trim(), email.trim(), password.trim(), org.trim()))
                if (!res.success || res.data?.accessToken.isNullOrBlank()) error(res.message ?: "Signup failed")
                val token = res.data!!.accessToken!!
                store.saveToken(token)
                state = state.copy(
                    token = token,
                    user = res.data.user,
                    organizationName = res.data.organization?.name.orEmpty()
                )
                loadHome()
            }.onFailure {
                state = state.copy(loading = false, error = it.message ?: "Signup failed")
            }
        }
    }

    fun loadHome() {
        viewModelScope.launch {
            state = state.copy(loading = true, error = "")
            runCatching {
                val today = LocalDate.now()
                val start = today.atStartOfDay().toInstant(ZoneOffset.UTC).toString()
                val end = Instant.now().toString()
                val analytics = api.dashboardAnalytics(bearer(), start, end)
                val users = if (state.user?.role == "MANAGER") api.users(bearer()) else null
                val teams = if (state.user?.role == "MANAGER") api.teams(bearer()) else null
                val shots = api.screenshots(bearer(), limit = "200")
                val timeline = api.activityTimeline(bearer(), start, end)
                state = state.copy(
                    loading = false,
                    analytics = analytics.data ?: DashboardAnalytics(),
                    employees = users?.data ?: state.user?.let { listOf(it) } ?: emptyList(),
                    teams = teams?.data ?: emptyList(),
                    screenshotCount = shots.data?.size ?: 0,
                    teamActivity = timeline.data?.employees.orEmpty()
                )
            }.onFailure {
                state = state.copy(loading = false, error = it.message ?: "Unable to load dashboard")
            }
        }
    }

    fun loadTime() {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            state = state.copy(timeLoading = true, error = "")
            runCatching {
                val today = LocalDate.now()
                val start = today.withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC).toString()
                val end = Instant.now().toString()
                val attendance = api.attendance(bearer(), start, end)
                val manual = api.manualTimeRequests(bearer())
                state = state.copy(
                    timeLoading = false,
                    attendance = attendance.data ?: AttendanceOverview(),
                    manualTimeRequests = manual.data ?: emptyList()
                )
            }.onFailure {
                state = state.copy(timeLoading = false, error = it.message ?: "Unable to load time data")
            }
        }
    }

    fun createManualTime(startAt: String, endAt: String, reason: String, userId: String? = null) {
        viewModelScope.launch {
            state = state.copy(timeLoading = true, error = "")
            runCatching {
                val res = api.createManualTimeRequest(
                    bearer(),
                    CreateManualTimeRequest(userId = userId?.ifBlank { null }, startAt = startAt, endAt = endAt, reason = reason)
                )
                if (!res.success) error(res.message ?: "Unable to create manual time request")
                loadTime()
            }.onFailure {
                state = state.copy(timeLoading = false, error = it.message ?: "Unable to create manual time request")
            }
        }
    }

    fun reviewManualTime(id: String, status: String) {
        viewModelScope.launch {
            state = state.copy(timeLoading = true, error = "")
            runCatching {
                val res = api.reviewManualTimeRequest(bearer(), id, ReviewManualTimeRequest(status = status))
                if (!res.success) error(res.message ?: "Unable to review manual time request")
                loadTime()
            }.onFailure {
                state = state.copy(timeLoading = false, error = it.message ?: "Unable to review manual time request")
            }
        }
    }

    fun loadCalendarAndScreenshots(date: LocalDate = state.selectedDate, userId: String = state.selectedUserId) {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            val cleanUserId = userId.ifBlank { "" }
            state = state.copy(
                selectedDate = date,
                selectedUserId = cleanUserId,
                screenshotLoading = true,
                error = ""
            )
            runCatching {
                val (start, end) = dayRangeIso(date)
                val managerUsers = if (state.user?.role == "MANAGER" && state.employees.isEmpty()) {
                    api.users(bearer()).data.orEmpty()
                } else {
                    state.employees
                }
                val calendar = api.calendarHeatmap(
                    bearer(),
                    date.year,
                    date.monthValue,
                    cleanUserId.ifBlank { null }
                )
                val analytics = api.dashboardAnalytics(
                    bearer(),
                    start,
                    end,
                    cleanUserId.ifBlank { null }
                )
                val shots = if (cleanUserId.isNotBlank() || state.user?.role != "MANAGER") {
                    api.screenshots(
                        bearer(),
                        userId = cleanUserId.ifBlank { null },
                        startDate = start,
                        endDate = end,
                        limit = "80"
                    ).data.orEmpty()
                } else {
                    managerUsers.map { employee ->
                        api.screenshots(
                            bearer(),
                            userId = employee.id,
                            startDate = start,
                            endDate = end,
                            limit = "40"
                        ).data.orEmpty()
                    }.flatten()
                }
                state = state.copy(
                    screenshotLoading = false,
                    employees = if (managerUsers.isNotEmpty()) managerUsers else state.employees,
                    calendarDays = calendar.data.orEmpty(),
                    selectedDayAnalytics = analytics.data ?: DashboardAnalytics(),
                    dayScreenshots = shots.sortedByDescending { it.capturedAt ?: it.createdAt.orEmpty() },
                    screenshotCount = shots.size
                )
            }.onFailure {
                state = state.copy(screenshotLoading = false, error = it.message ?: "Unable to load calendar")
            }
        }
    }

    fun loadScreenshotsPage(date: LocalDate = state.selectedDate, userId: String = state.selectedUserId) {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            val cleanUserId = userId.ifBlank { "" }
            state = state.copy(selectedDate = date, selectedUserId = cleanUserId, screenshotLoading = true, error = "")
            runCatching {
                val users = ensureEmployees()
                val (start, end) = dayRangeIso(date)
                val shots = fetchScreenshotsForUsers(users, cleanUserId, start, end, selectedLimit = "120", allLimit = "50")
                state = state.copy(
                    screenshotLoading = false,
                    employees = users.ifEmpty { state.employees },
                    allScreenshots = shots,
                    screenshotCount = shots.size
                )
            }.onFailure {
                state = state.copy(screenshotLoading = false, error = it.message ?: "Unable to load screenshots")
            }
        }
    }

    fun loadScreenshotsRange(startDate: LocalDate, endDate: LocalDate, userId: String = state.selectedUserId) {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            val cleanUserId = userId.ifBlank { "" }
            val orderedStart = minOf(startDate, endDate)
            val orderedEnd = maxOf(startDate, endDate)
            state = state.copy(selectedDate = orderedStart, selectedUserId = cleanUserId, screenshotLoading = true, error = "")
            runCatching {
                val users = ensureEmployees()
                val (start, _) = dayRangeIso(orderedStart)
                val (_, end) = dayRangeIso(orderedEnd)
                val shots = fetchScreenshotsForUsers(users, cleanUserId, start, end, selectedLimit = "200", allLimit = "200")
                state = state.copy(
                    screenshotLoading = false,
                    employees = users.ifEmpty { state.employees },
                    allScreenshots = shots,
                    screenshotCount = shots.size
                )
            }.onFailure {
                state = state.copy(screenshotLoading = false, error = it.message ?: "Unable to load screenshots")
            }
        }
    }

    fun loadLiveView(userId: String = state.selectedUserId) {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            val cleanUserId = userId.ifBlank { "" }
            state = state.copy(selectedUserId = cleanUserId, liveLoading = true, error = "")
            runCatching {
                val users = ensureEmployees()
                val (start, end) = dayRangeIso(LocalDate.now())
                val timeline = api.activityTimeline(bearer(), start, end, cleanUserId.ifBlank { null }).data ?: ActivityTimeline()
                val activityByUser = timeline.employees.associateBy { it.userId }
                val targetUsers = if (cleanUserId.isNotBlank()) users.filter { it.id == cleanUserId } else users
                val liveRows = targetUsers.map { user ->
                    val shot = api.screenshots(
                        bearer(),
                        userId = user.id,
                        startDate = start,
                        endDate = end,
                        limit = "1"
                    ).data.orEmpty().firstOrNull()
                    val activity = activityByUser[user.id]
                    LiveEmployeeUi(
                        user = user,
                        screenshot = shot,
                        lastActiveAt = activity?.lastActiveAt ?: shot?.capturedAt ?: shot?.createdAt,
                        activeApp = activity?.topApps?.firstOrNull()?.name ?: shot?.activeApplication ?: "No active app"
                    )
                }
                state = state.copy(
                    liveLoading = false,
                    employees = users.ifEmpty { state.employees },
                    liveRows = liveRows
                )
            }.onFailure {
                state = state.copy(liveLoading = false, error = it.message ?: "Unable to load live view")
            }
        }
    }

    fun loadTeamStats(date: LocalDate = state.selectedDate) {
        viewModelScope.launch {
            if (state.token.isBlank()) return@launch
            state = state.copy(selectedDate = date, teamLoading = true, error = "")
            runCatching {
                val users = ensureEmployees()
                val (start, end) = dayRangeIso(date)
                val timeline = api.activityTimeline(bearer(), start, end).data ?: ActivityTimeline()
                state = state.copy(
                    teamLoading = false,
                    employees = users.ifEmpty { state.employees },
                    teamActivity = timeline.employees
                )
            }.onFailure {
                state = state.copy(teamLoading = false, error = it.message ?: "Unable to load team stats")
            }
        }
    }

    fun openEmployee(userId: String) {
        state = state.copy(selectedEmployeeId = userId)
    }

    fun closeEmployee() {
        state = state.copy(selectedEmployeeId = null)
    }

    private suspend fun ensureEmployees(): List<UserProfile> {
        if (state.employees.isNotEmpty()) return state.employees
        return if (state.user?.role == "MANAGER") {
            api.users(bearer()).data.orEmpty()
        } else {
            state.user?.let { listOf(it) }.orEmpty()
        }
    }

    private suspend fun fetchScreenshotsForUsers(
        users: List<UserProfile>,
        userId: String,
        start: String,
        end: String,
        selectedLimit: String,
        allLimit: String
    ): List<ScreenshotItem> {
        val shots = if (userId.isNotBlank() || state.user?.role != "MANAGER") {
            api.screenshots(
                bearer(),
                userId = userId.ifBlank { null },
                startDate = start,
                endDate = end,
                limit = selectedLimit
            ).data.orEmpty()
        } else {
            users.map { employee ->
                api.screenshots(
                    bearer(),
                    userId = employee.id,
                    startDate = start,
                    endDate = end,
                    limit = allLimit
                ).data.orEmpty()
            }.flatten()
        }
        return shots.sortedByDescending { it.capturedAt ?: it.createdAt.orEmpty() }
    }

    fun logout() {
        viewModelScope.launch {
            store.clear()
            state = UiState(loading = false)
        }
    }
}

@Composable
fun TeamLensApp() {
    val context = LocalContext.current.applicationContext as Application
    val vm: TeamLensViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TeamLensViewModel(context) as T
        }
    })

    when {
        vm.state.loading && vm.state.user == null -> LoadingScreen()
        vm.state.user == null -> AuthScreen(vm)
        else -> MainShell(vm)
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(
                painter = painterResource(R.drawable.teamlens_logo),
                contentDescription = "TeamLens logo",
                modifier = Modifier.size(46.dp)
            )
            CircularProgressIndicator()
            Text("TeamLens", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AppCard(
    modifier: Modifier = Modifier,
    radius: Int = 16,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(radius.dp),
        border = BorderStroke(1.dp, Border),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun AuthScreen(vm: TeamLensViewModel) {
    var mode by remember { mutableStateOf(AuthMode.Login) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var org by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            BrandHeader()
            Spacer(Modifier.height(30.dp))
            Text(
                if (mode == AuthMode.Signup) "Create account" else "Welcome back",
                color = Ink,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (mode == AuthMode.Signup) "Start tracking workforce activity and gain insights for your team."
                else "Sign in to access your organization's dashboard and team analytics.",
                color = Muted,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(24.dp))
            AppCard(radius = 24) {
                SegmentedAuth(mode = mode, onMode = { mode = it })
                if (mode == AuthMode.Signup) {
                    FormField("Full name", name, { name = it }, "John Doe")
                    FormField("Organization", org, { org = it }, "Acme Corp")
                }
                FormField("Email address", email, { email = it }, "email@example.com")
                FormField("Password", password, { password = it }, "Password", password = true)
                if (vm.state.error.isNotBlank()) {
                    Text(vm.state.error, color = Danger, fontSize = 13.sp)
                    Spacer(Modifier.height(10.dp))
                }
                Button(
                    enabled = !vm.state.loading,
                    onClick = {
                if (mode == AuthMode.Login) vm.login(email, password.trim()) else vm.signup(name, email, password.trim(), org)
                    },
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (vm.state.loading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = Surface)
                    else Text(if (mode == AuthMode.Login) "Sign In" else "Create Workspace", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                if (mode == AuthMode.Login) {
                    Spacer(Modifier.height(14.dp))
                    Text("Forgot password?", color = Brand, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
            Spacer(Modifier.height(28.dp))
            Text("Connected to ${BuildConfig.API_BASE_URL}", color = MutedLight, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun BrandHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.teamlens_logo),
            contentDescription = "TeamLens logo",
            modifier = Modifier.size(34.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("TeamLens", color = Brand, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SegmentedAuth(mode: AuthMode, onMode: (AuthMode) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Surface2, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf(AuthMode.Login to "Sign In", AuthMode.Signup to "Register").forEach { (item, label) ->
            val active = mode == item
            if (active) {
                Button(
                    onClick = { onMode(item) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Surface, contentColor = Ink)
                ) { Text(label, fontWeight = FontWeight.SemiBold) }
            } else {
                OutlinedButton(
                    onClick = { onMode(item) },
                    border = null,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) { Text(label, color = Muted, fontWeight = FontWeight.SemiBold) }
            }
        }
    }
    Spacer(Modifier.height(18.dp))
}

@Composable
private fun FormField(label: String, value: String, onChange: (String) -> Unit, placeholder: String, password: Boolean = false) {
    Text(label.uppercase(), color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = MutedLight) },
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Surface2,
            unfocusedContainerColor = Surface2,
            focusedBorderColor = Border,
            unfocusedBorderColor = Border
        )
    )
    Spacer(Modifier.height(14.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainShell(vm: TeamLensViewModel) {
    var section by remember { mutableStateOf(Section.Home) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomSections = listOf(Section.Home, Section.Team, Section.Attendance, Section.Screenshots)
    LaunchedEffect(vm.state.token) {
        if (vm.state.token.isNotBlank()) {
            vm.loadHome()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                selected = section,
                onSelect = {
                    section = it
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Outlined.Menu, contentDescription = "Open menu")
                        }
                    },
                    title = {
                        Column {
                            Text(vm.state.organizationName.ifBlank { "TeamLens" }, fontWeight = FontWeight.SemiBold)
                            Text(vm.state.user?.fullName.orEmpty(), fontSize = 12.sp, color = Muted)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Surface) {
                    bottomSections.forEach {
                        NavigationBarItem(
                            selected = section == it,
                            onClick = { section = it },
                            icon = { Icon(it.icon, contentDescription = it.label) },
                            label = { Text(it.label) }
                        )
                    }
                }
            },
            containerColor = Background
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (section) {
                    Section.Home -> HomeScreen(vm, onOpenSection = { section = it })
                    Section.Team -> TeamScreen(vm)
                    Section.Attendance -> TimeScreen(vm)
                    Section.Screenshots -> ScreenshotsScreen(vm)
                    Section.Activities -> ActivitiesScreen(vm)
                    Section.Live -> LiveViewScreen(vm)
                    Section.Calendar -> CalendarScreenshotsScreen(vm)
                    Section.Settings -> SettingsScreen(vm)
                }
                if (vm.state.loading) {
                    Box(Modifier.fillMaxSize().background(Background.copy(alpha = 0.65f)), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun AppNavigationDrawer(selected: Section, onSelect: (Section) -> Unit) {
    val menuItems = listOf(
        Section.Home to "Overview dashboard",
        Section.Team to "Employees and teams",
        Section.Attendance to "Timesheets and attendance",
        Section.Screenshots to "Captured screens",
        Section.Activities to "Focus health and app usage",
        Section.Live to "Live employee screens",
        Section.Calendar to "Daily stats and history",
        Section.Settings to "Account and app settings"
    )

    ModalDrawerSheet(drawerContainerColor = Surface) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.teamlens_logo),
                contentDescription = "TeamLens logo",
                modifier = Modifier.size(42.dp)
            )
            Text("TeamLens", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text("Workspace navigation", color = Muted, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            menuItems.forEach { (item, subtitle) ->
                NavigationDrawerItem(
                    selected = selected == item,
                    onClick = { onSelect(item) },
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = {
                        Column {
                            Text(item.label, fontWeight = FontWeight.SemiBold)
                            Text(subtitle, color = Muted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(vm: TeamLensViewModel, onOpenSection: (Section) -> Unit) {
    val a = vm.state.analytics
    val modules = listOf(
        HomeModule("Dashboard", "Overview", Brand, Section.Home),
        HomeModule("Employees", "People", Brand, Section.Team),
        HomeModule("Attendance", "Status", Warning, Section.Attendance),
        HomeModule("Reports", "Usage", androidx.compose.ui.graphics.Color(0xFF0F766E), Section.Calendar),
        HomeModule("Screenshots", "Proof", Info, Section.Screenshots),
        HomeModule("Activities", "Focus", androidx.compose.ui.graphics.Color(0xFF6B5DD3), Section.Activities),
        HomeModule("Manual Time", "Requests", Danger, Section.Attendance),
        HomeModule("Live View", "Screens", Success, Section.Live),
        HomeModule("Settings", "Account", Muted, Section.Settings)
    )
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("TeamLens", color = Ink, fontSize = 34.sp, fontWeight = FontWeight.SemiBold)
                }
                AvatarChip(vm.state.user?.fullName ?: "User", online = true)
            }
        }
        item {
            Text("APP SCREENS", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("Tap any module below to open the mobile view.", color = Muted, fontSize = 14.sp)
        }
        item {
            AppCard(radius = 20) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenSection(Section.Home) }
                ) {
                    ModuleIcon("D", Brand, 58)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Main overview", color = Muted, fontSize = 12.sp)
                    }
                    Text("›", color = MutedLight, fontSize = 24.sp)
                }
            }
        }
        item {
            ModuleGrid(modules = modules, onOpenSection = onOpenSection)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Active now", vm.state.employees.size.toString(), "employees online", Modifier.weight(1f))
                MetricCard("Hours today", "${(a.activeSeconds / 3600).format1()}h", "team total", Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Productivity", "${a.productivityPercent.format0()}%", "${vm.state.teams.size} teams tracked", Modifier.weight(1f))
                MetricCard("Needs review", vm.state.manualTimeRequests.count { it.status == "PENDING" }.toString(), "manual requests", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ModuleGrid(modules: List<HomeModule>, onOpenSection: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        modules.chunked(3).forEach { rowModules ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                rowModules.forEach { module ->
                    ModuleLauncher(module = module, onClick = { onOpenSection(module.section) }, modifier = Modifier.weight(1f))
                }
                repeat(3 - rowModules.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ModuleLauncher(module: HomeModule, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(0.92f).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ModuleIcon(module.label.take(1), module.color, 44)
            Spacer(Modifier.height(8.dp))
            Text(
                module.label,
                color = Ink,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                module.subtitle,
                color = Muted,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ModuleIcon(text: String, color: androidx.compose.ui.graphics.Color, size: Int) {
    Box(
        Modifier
            .size(size.dp)
            .background(color, RoundedCornerShape(if (size > 60) 22.dp else 18.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Surface, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AvatarChip(name: String, online: Boolean = false) {
    val initials = name.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.take(1).uppercase() }.ifBlank { "U" }
    Box {
        Box(
            Modifier
                .size(48.dp)
                .background(Surface2, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(initials, color = Ink, fontWeight = FontWeight.SemiBold)
        }
        if (online) Box(Modifier.size(14.dp).background(Success, CircleShape).align(Alignment.BottomEnd))
    }
}

@Composable
private fun MetricCard(label: String, value: String, sub: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(118.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Muted, fontSize = 12.sp)
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(sub, color = Muted, fontSize = 12.sp)
        }
    }
}

private enum class TimeTab { Timesheets, Attendance, Manual }

@Composable
private fun TimeScreen(vm: TeamLensViewModel) {
    var tab by remember { mutableStateOf(TimeTab.Timesheets) }

    LaunchedEffect(vm.state.token) {
        if (vm.state.token.isNotBlank() && vm.state.attendance.month.isBlank()) {
            vm.loadTime()
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Time", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Timesheets, attendance and manual time", color = Muted)
                }
                OutlinedButton(onClick = vm::loadTime, enabled = !vm.state.timeLoading) {
                    Text(if (vm.state.timeLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TimeTab.entries) { item ->
                    if (tab == item) {
                        Button(onClick = { tab = item }) { Text(item.name) }
                    } else {
                        OutlinedButton(onClick = { tab = item }) { Text(item.name) }
                    }
                }
            }
        }
        item {
            val stats = vm.state.attendance.stats
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Attended", stats.attendedDays.toString(), "days", Modifier.weight(1f))
                MetricCard("Working", stats.currentlyWorking.toString(), "right now", Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Office", statsOrZero(vm.state.attendance.stats.officeDays), "days", Modifier.weight(1f))
                MetricCard("Remote", statsOrZero(vm.state.attendance.stats.remoteDays), "days", Modifier.weight(1f))
            }
        }
        if (vm.state.error.isNotBlank()) {
            item { Text(vm.state.error, color = Brand) }
        }
        when (tab) {
            TimeTab.Timesheets -> {
                val rows = vm.state.attendance.timesheets
                if (rows.isEmpty()) {
                    item { ListCard("No timesheets", "No clock-in sessions found for this month.") }
                } else {
                    items(rows.take(50)) { entry -> TimesheetCard(entry) }
                }
            }
            TimeTab.Attendance -> {
                val employees = vm.state.attendance.employees
                if (employees.isEmpty()) {
                    item { ListCard("No attendance", "Attendance records will appear after employees clock in.") }
                } else {
                    items(employees) { employee ->
                        AttendanceEmployeeCard(employee.employeeName, employee.shiftSummary, employee.attendedDays, employee.workingDays, employee.absentDays, employee.cells.takeLast(7))
                    }
                }
            }
            TimeTab.Manual -> {
                item { ManualTimeForm(vm) }
                val requests = vm.state.manualTimeRequests
                if (requests.isEmpty()) {
                    item { ListCard("No manual time", "Requests for missing/off-platform work hours will appear here.") }
                } else {
                    items(requests.take(50)) { request -> ManualTimeCard(vm, request) }
                }
            }
        }
    }
}

@Composable
private fun TimesheetCard(entry: TimesheetEntry) {
    val subtitle = listOf(
        entry.teamName ?: "No team",
        entry.locationStatus ?: "Unknown location",
        entry.shiftName.ifBlank { "Shift not set" }
    ).joinToString(" | ")
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Border), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(entry.employeeName, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Muted, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("In ${formatClock(entry.clockInAt)}", fontSize = 12.sp)
                Text("Out ${entry.clockOutAt?.let { formatClock(it) } ?: if (entry.isCurrentlyWorking) "Working" else "Open"}", fontSize = 12.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Work ${formatDuration(entry.workSeconds)}", fontWeight = FontWeight.SemiBold)
                Text("Active ${formatDuration(entry.activeSeconds)}", color = Muted)
            }
        }
    }
}

@Composable
private fun AttendanceEmployeeCard(
    name: String,
    shift: String,
    attended: Int,
    working: Int,
    absent: Int,
    cells: List<com.teamlens.nativeapp.data.model.AttendanceCell>
) {
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Border), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(name, fontWeight = FontWeight.SemiBold)
            Text("${shift.ifBlank { "No shift yet" }} | $attended attended | $working working | $absent absent", color = Muted, fontSize = 12.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(cells) { cell ->
                    val color = when (cell.status) {
                        "attended" -> Brand
                        "working" -> androidx.compose.ui.graphics.Color(0xFF24A77A)
                        "below" -> androidx.compose.ui.graphics.Color(0xFFE7A928)
                        else -> Border
                    }
                    Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(8.dp)) {
                        Column(Modifier.size(48.dp).padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(cell.day.toString(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(if (cell.workSeconds > 0) "${(cell.workSeconds / 3600).format1()}h" else "-", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManualTimeForm(vm: TeamLensViewModel) {
    var date by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:00") }
    var reason by remember { mutableStateOf("") }
    val isManager = vm.state.user?.role == "MANAGER"
    var selectedUserId by remember(vm.state.employees) { mutableStateOf(vm.state.employees.firstOrNull()?.id.orEmpty()) }

    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Border), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(if (isManager) "Create Manual Time" else "Request Manual Time", fontWeight = FontWeight.SemiBold)
            if (isManager) {
                OutlinedTextField(
                    value = selectedUserId,
                    onValueChange = { selectedUserId = it },
                    label = { Text("Employee user id") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            DatePickerButton(
                label = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                selectedDate = date,
                onSelectDate = { date = it },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Start") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = endTime, onValueChange = { endTime = it }, label = { Text("End") }, modifier = Modifier.weight(1f))
            }
            OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Reason") }, modifier = Modifier.fillMaxWidth())
            Button(
                enabled = reason.trim().length >= 3 && !vm.state.timeLoading,
                onClick = {
                    vm.createManualTime(
                        startAt = toInstantIso(date, startTime),
                        endAt = toInstantIso(date, endTime),
                        reason = reason.trim(),
                        userId = if (isManager) selectedUserId else null
                    )
                    reason = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isManager) "Create request" else "Submit request")
            }
        }
    }
}

@Composable
private fun ManualTimeCard(vm: TeamLensViewModel, request: ManualTimeRequest) {
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Border), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(request.employeeName, fontWeight = FontWeight.SemiBold)
                    Text("${formatDate(request.startAt)} | ${formatClock(request.startAt)} - ${formatClock(request.endAt)} | ${formatDuration(request.durationSeconds)}", color = Muted, fontSize = 12.sp)
                }
                Text(request.status, color = if (request.status == "APPROVED") androidx.compose.ui.graphics.Color(0xFF16895F) else Brand, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
            Text(request.reason, color = Muted, fontSize = 13.sp)
            if (vm.state.user?.role == "MANAGER" && request.status == "PENDING") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.reviewManualTime(request.id, "APPROVED") }, enabled = !vm.state.timeLoading) { Text("Approve") }
                    OutlinedButton(onClick = { vm.reviewManualTime(request.id, "REJECTED") }, enabled = !vm.state.timeLoading) { Text("Reject") }
                }
            }
        }
    }
}

@Composable
private fun EmployeesScreen(employees: List<UserProfile>) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Employees", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        items(employees) { user ->
            ListCard(title = user.fullName, subtitle = "${user.email} • ${user.role}")
        }
    }
}

@Composable
private fun TeamScreen(vm: TeamLensViewModel) {
    val activityByUser = vm.state.teamActivity.associateBy { it.userId }
    val selectedEmployee = vm.state.selectedEmployeeId?.let { id -> vm.state.employees.firstOrNull { it.id == id } }
    val sortedEmployees = vm.state.employees.sortedWith(
        compareByDescending<UserProfile> { isFreshActivity(activityByUser[it.id]?.lastActiveAt) }
            .thenBy { it.fullName.lowercase() }
    )
    LaunchedEffect(vm.state.token) {
        if (vm.state.token.isNotBlank() && vm.state.teamActivity.isEmpty()) {
            vm.loadTeamStats()
        }
    }

    if (selectedEmployee != null) {
        EmployeeDetailScreen(
            user = selectedEmployee,
            activity = activityByUser[selectedEmployee.id],
            latestScreenshot = vm.state.liveRows.firstOrNull { it.user.id == selectedEmployee.id }?.screenshot
                ?: vm.state.allScreenshots.firstOrNull { it.userId == selectedEmployee.id },
            selectedDate = vm.state.selectedDate,
            token = vm.state.token,
            onBack = vm::closeEmployee,
            onRefresh = { vm.loadTeamStats(vm.state.selectedDate) }
        )
        return
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Employees", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("${vm.state.employees.size} people, ${vm.state.teams.size} teams", color = Muted, fontSize = 14.sp)
                }
                OutlinedButton(onClick = { vm.loadTeamStats(vm.state.selectedDate) }, enabled = !vm.state.teamLoading) {
                    Text(if (vm.state.teamLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            DateSelectorCard(
                title = "Team data date",
                selectedDate = vm.state.selectedDate,
                onSelectDate = { vm.loadTeamStats(it) },
                onPrevious = { vm.loadTeamStats(vm.state.selectedDate.minusDays(1)) },
                onToday = { vm.loadTeamStats(LocalDate.now()) },
                onNext = { vm.loadTeamStats(vm.state.selectedDate.plusDays(1)) }
            )
        }
        item {
            AppCard(radius = 16) {
                Text("Invite teammate", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("employee@company.com", color = MutedLight) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Button(onClick = {}, shape = RoundedCornerShape(14.dp), modifier = Modifier.size(56.dp)) { Text("+") }
                }
            }
        }
        item { Text("TEAMS", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        items(vm.state.teams) { team ->
            AppCard(radius = 16) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ModuleIcon("T", BrandLight, 38)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(team.name, color = Ink, fontWeight = FontWeight.SemiBold)
                        Text("${team.members.size} members", color = Muted, fontSize = 13.sp)
                    }
                }
            }
        }
        item { Text("PEOPLE", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        items(sortedEmployees) { user ->
            TeamEmployeeCard(user = user, activity = activityByUser[user.id], onClick = { vm.openEmployee(user.id) })
        }
    }
}

@Composable
private fun TeamEmployeeCard(user: UserProfile, activity: ActivityTimelineEmployee?, onClick: () -> Unit) {
    val lastSeen = activity?.lastActiveAt
    val live = isFreshActivity(lastSeen)
    AppCard(radius = 16, modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarChip(user.fullName, online = live)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(user.fullName, color = Ink, fontWeight = FontWeight.SemiBold)
                Text(user.email, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(if (live) "LIVE" else relativeTime(lastSeen), color = if (live) Success else MutedLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text("Open", color = Brand, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EmployeeMetricChip("Active", formatDuration(activity?.activeSeconds ?: 0.0), Modifier.weight(1f))
            EmployeeMetricChip("Work", formatDuration(activity?.workSeconds ?: 0.0), Modifier.weight(1f))
            EmployeeMetricChip("Util", "${activity?.utilizationPercent ?: 0}%", Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Text(activity?.topApps?.firstOrNull()?.name ?: "No app activity today", color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun DateSelectorCard(
    title: String,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    onPrevious: () -> Unit,
    onToday: () -> Unit,
    onNext: () -> Unit
) {
    AppCard(radius = 18) {
        Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
        Text(selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d yyyy")), color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        DatePickerButton(
            label = selectedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
            selectedDate = selectedDate,
            onSelectDate = onSelectDate,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onPrevious, modifier = Modifier.weight(1f)) { Text("Previous") }
            OutlinedButton(onClick = onToday, modifier = Modifier.weight(1f)) { Text("Today") }
            OutlinedButton(onClick = onNext, modifier = Modifier.weight(1f)) { Text("Next") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerButton(
    label: String,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var open by remember { mutableStateOf(false) }
    val state = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toEpochMillis())

    OutlinedButton(onClick = { open = true }, modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }

    if (open) {
        DatePickerDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { onSelectDate(it.toLocalDateUtc()) }
                        open = false
                    }
                ) { Text("Apply") }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerButton(
    startDate: LocalDate,
    endDate: LocalDate,
    onSelectRange: (LocalDate, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var open by remember { mutableStateOf(false) }
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate.toEpochMillis(),
        initialSelectedEndDateMillis = endDate.toEpochMillis()
    )
    val label = if (startDate == endDate) {
        startDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    } else {
        "${startDate.format(DateTimeFormatter.ofPattern("MMM d"))} - ${endDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
    }

    OutlinedButton(onClick = { open = true }, modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Icon(Icons.Outlined.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }

    if (open) {
        DatePickerDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val start = state.selectedStartDateMillis?.toLocalDateUtc()
                        val end = state.selectedEndDateMillis?.toLocalDateUtc() ?: start
                        if (start != null && end != null) onSelectRange(start, end)
                        open = false
                    }
                ) { Text("Apply") }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(state = state, modifier = Modifier.fillMaxWidth().height(520.dp))
        }
    }
}

@Composable
private fun EmployeeDetailScreen(
    user: UserProfile,
    activity: ActivityTimelineEmployee?,
    latestScreenshot: ScreenshotItem?,
    selectedDate: LocalDate,
    token: String,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    val live = isFreshActivity(activity?.lastActiveAt)
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onBack, shape = RoundedCornerShape(12.dp)) { Text("Back") }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(user.fullName, color = Ink, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(user.email, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(onClick = onRefresh, shape = RoundedCornerShape(12.dp)) { Text("Refresh") }
            }
        }
        item {
            Text(selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d yyyy")), color = Muted, fontWeight = FontWeight.SemiBold)
        }
        item {
            AppCard(radius = 18) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarChip(user.fullName, online = live)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (live) "Currently active" else "Not active right now", color = if (live) Success else Muted, fontWeight = FontWeight.SemiBold)
                        Text("Last activity: ${relativeTime(activity?.lastActiveAt)}", color = Muted, fontSize = 13.sp)
                    }
                    Text(user.role, color = Brand, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MiniStat("Active", formatDuration(activity?.activeSeconds ?: 0.0), Modifier.weight(1f))
                MiniStat("Work", formatDuration(activity?.workSeconds ?: 0.0), Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MiniStat("Idle", formatDuration(activity?.idleSeconds ?: 0.0), Modifier.weight(1f))
                MiniStat("Utilization", "${activity?.utilizationPercent ?: 0}%", Modifier.weight(1f))
            }
        }
        item {
            AppCard(radius = 18) {
                Text("Latest screen", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(10.dp))
                if (latestScreenshot != null) {
                    ScreenshotPreview(shot = latestScreenshot, token = token)
                    Spacer(Modifier.height(8.dp))
                    Text(latestScreenshot.windowTitle ?: latestScreenshot.activeApplication ?: "Captured screen", color = Muted, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                } else {
                    Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Surface2, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                        Text("No screenshot loaded yet", color = Muted)
                    }
                }
            }
        }
        item {
            AppCard(radius = 18) {
                Text("Top apps today", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                val apps = activity?.topApps.orEmpty()
                if (apps.isEmpty()) {
                    Text("No app activity recorded today", color = Muted, fontSize = 13.sp)
                } else {
                    apps.take(5).forEach { app ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(app.name, color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(formatDuration(app.seconds), color = Muted, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployeeMetricChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Surface2, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 8.dp)) {
        Text(label, color = MutedLight, fontSize = 10.sp, maxLines = 1)
        Text(value, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun TeamsScreen(teams: List<Team>) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Teams", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        items(teams) { team ->
            ListCard(title = team.name, subtitle = "${team.members.size} members")
        }
    }
}

@Composable
private fun ActivitiesScreen(vm: TeamLensViewModel) {
    val state = vm.state
    val sortedRows = state.teamActivity
        .sortedWith(compareByDescending<ActivityTimelineEmployee> { isFreshActivity(it.lastActiveAt) }.thenByDescending { it.activeSeconds })
    var rangeMode by remember { mutableStateOf("12H") }
    var customHourRange by remember { mutableStateOf(9f..21f) }
    val zone = ZoneId.systemDefault()
    val timelineStart = remember(state.selectedDate, rangeMode, customHourRange) {
        when (rangeMode) {
            "24H" -> state.selectedDate.atStartOfDay(zone).toInstant()
            "Custom" -> instantForActivityHour(state.selectedDate, customHourRange.start.toInt(), zone)
            else -> state.selectedDate.atTime(9, 0).atZone(zone).toInstant()
        }
    }
    val timelineEnd = remember(state.selectedDate, rangeMode, customHourRange) {
        when (rangeMode) {
            "24H" -> state.selectedDate.plusDays(1).atStartOfDay(zone).toInstant()
            "Custom" -> instantForActivityHour(state.selectedDate, customHourRange.endInclusive.toInt(), zone)
            else -> state.selectedDate.atTime(21, 0).atZone(zone).toInstant()
        }
    }

    LaunchedEffect(state.token) {
        if (state.token.isNotBlank() && state.teamActivity.isEmpty()) {
            vm.loadTeamStats()
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Activities", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Focus health and app usage", color = Muted, fontSize = 14.sp)
                }
                OutlinedButton(onClick = { vm.loadTeamStats(state.selectedDate) }, enabled = !state.teamLoading) {
                    Text(if (state.teamLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().background(Surface2, RoundedCornerShape(18.dp)).padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("12H", "24H", "Custom").forEach { item ->
                    val selected = rangeMode == item
                    if (selected) {
                        Button(
                            onClick = { rangeMode = item },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text(item, fontWeight = FontWeight.SemiBold) }
                    } else {
                        OutlinedButton(
                            onClick = { rangeMode = item },
                            modifier = Modifier.weight(1f),
                            border = null,
                            shape = RoundedCornerShape(14.dp)
                        ) { Text(item, color = Muted, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
        if (rangeMode == "Custom") {
            item {
                CompactActivityDateCard(
                    selectedDate = state.selectedDate,
                    onSelectDate = { vm.loadTeamStats(it) },
                    onPrevious = { vm.loadTeamStats(state.selectedDate.minusDays(1)) },
                    onToday = { vm.loadTeamStats(LocalDate.now()) },
                    onNext = { vm.loadTeamStats(state.selectedDate.plusDays(1)) }
                )
            }
            item {
                CustomTimeRangeCard(
                    hourRange = customHourRange,
                    onRangeChange = { range ->
                        val start = range.start.coerceIn(0f, 23f).toInt().toFloat()
                        val end = range.endInclusive.coerceIn(1f, 24f).toInt().toFloat()
                        customHourRange = if (end - start < 1f) start..(start + 1f).coerceAtMost(24f) else start..end
                    }
                )
            }
        }
        item {
            ActivityTimelineAxis(rangeStart = timelineStart, rangeEnd = timelineEnd)
        }
        item {
            ActivityTimelineLegend()
        }
        if (state.teamLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (sortedRows.isEmpty()) {
            item {
                EmptyScreenCard("No activity found", "Try another date or wait for desktop agents to send activity.")
            }
        } else {
            item {
                Text("EMPLOYEE ACTIVITY", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            items(sortedRows) { employee ->
                EmployeeActivityCard(employee = employee, rangeStart = timelineStart, rangeEnd = timelineEnd)
            }
        }
    }
}

@Composable
private fun ScreenshotsScreen(vm: TeamLensViewModel) {
    val state = vm.state
    var rangeStart by remember(state.selectedDate) { mutableStateOf(state.selectedDate) }
    var rangeEnd by remember(state.selectedDate) { mutableStateOf(state.selectedDate) }
    var openShot by remember { mutableStateOf<ScreenshotItem?>(null) }
    fun loadScreenshotSelection(start: LocalDate, end: LocalDate, userId: String = state.selectedUserId) {
        rangeStart = minOf(start, end)
        rangeEnd = maxOf(start, end)
        if (rangeStart == rangeEnd) {
            vm.loadScreenshotsPage(rangeStart, userId)
        } else {
            vm.loadScreenshotsRange(rangeStart, rangeEnd, userId)
        }
    }

    LaunchedEffect(state.token) {
        if (state.token.isNotBlank() && state.allScreenshots.isEmpty()) {
            vm.loadScreenshotsPage()
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Screenshots", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Browse captured employee screens", color = Muted)
                }
                OutlinedButton(onClick = { vm.loadScreenshotsPage() }, enabled = !state.screenshotLoading) {
                    Text(if (state.screenshotLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            UserFilterRow(
                users = state.employees,
                selectedUserId = state.selectedUserId,
                onSelect = { loadScreenshotSelection(rangeStart, rangeEnd, it) }
            )
        }
        item {
            AppCard(radius = 18) {
                Text("Screenshot range", color = Ink, fontWeight = FontWeight.SemiBold)
                Text("Pick one date or a range", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                DateRangePickerButton(
                    startDate = rangeStart,
                    endDate = rangeEnd,
                    onSelectRange = { start, end ->
                        loadScreenshotSelection(start, end)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            val days = java.time.temporal.ChronoUnit.DAYS.between(rangeStart, rangeEnd).coerceAtLeast(0)
                            val start = rangeStart.minusDays(days + 1)
                            loadScreenshotSelection(start, start.plusDays(days))
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Previous") }
                    OutlinedButton(
                        onClick = { loadScreenshotSelection(LocalDate.now(), LocalDate.now()) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Today") }
                    OutlinedButton(
                        onClick = {
                            val days = java.time.temporal.ChronoUnit.DAYS.between(rangeStart, rangeEnd).coerceAtLeast(0)
                            val start = rangeEnd.plusDays(1)
                            loadScreenshotSelection(start, start.plusDays(days))
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Next") }
                }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val rangeLabel = if (rangeStart == rangeEnd) {
                    state.selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
                } else {
                    "${rangeStart.format(DateTimeFormatter.ofPattern("MMM d"))} - ${rangeEnd.format(DateTimeFormatter.ofPattern("MMM d"))}"
                }
                Text(rangeLabel, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text("${state.allScreenshots.size} shots", color = Muted, fontWeight = FontWeight.SemiBold)
            }
        }
        if (state.screenshotLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (state.allScreenshots.isEmpty()) {
            item {
                EmptyScreenCard("No screenshots found", "Try another date or employee filter.")
            }
        } else {
            items(state.allScreenshots) { shot ->
                ScreenshotCard(shot = shot, token = state.token, onOpen = { openShot = shot })
            }
        }
    }

    openShot?.let { shot ->
        ScreenshotViewerDialog(
            shot = shot,
            shots = state.allScreenshots,
            token = state.token,
            onClose = { openShot = null },
            onNavigate = { openShot = it }
        )
    }
}

@Composable
private fun LiveViewScreen(vm: TeamLensViewModel) {
    val state = vm.state
    var streamEmployee by remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect(state.token, state.selectedUserId) {
        if (state.token.isNotBlank()) {
            vm.loadLiveView(state.selectedUserId)
        }
    }

    if (streamEmployee != null) {
        LiveWebViewScreen(
            employee = streamEmployee!!,
            token = state.token,
            onClose = { streamEmployee = null }
        )
        return
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Live View", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Latest screen per employee", color = Muted)
                }
                OutlinedButton(onClick = { vm.loadLiveView() }, enabled = !state.liveLoading) {
                    Text(if (state.liveLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            Button(
                onClick = {
                    streamEmployee = state.selectedUserId
                        .takeIf { it.isNotBlank() }
                        ?.let { id -> state.employees.firstOrNull { it.id == id } }
                        ?: state.liveRows.firstOrNull()?.user
                        ?: state.employees.firstOrNull()
                },
                enabled = state.employees.isNotEmpty() || state.liveRows.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Open real live stream")
            }
        }
        item {
            UserFilterRow(
                users = state.employees,
                selectedUserId = state.selectedUserId,
                onSelect = { vm.loadLiveView(it) }
            )
        }
        if (state.liveLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (state.liveRows.isEmpty()) {
            item {
                EmptyScreenCard("No live screens found", "Start the desktop agent or refresh after activity is captured.")
            }
        } else {
            items(state.liveRows) { row ->
                LiveEmployeeCard(row = row, token = state.token, onOpenStream = {
                    streamEmployee = row.user
                })
            }
        }
    }
}

@Composable
private fun LiveEmployeeCard(row: LiveEmployeeUi, token: String, onOpenStream: () -> Unit) {
    val live = isFreshActivity(row.lastActiveAt)
    AppCard(radius = 18) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarChip(row.user.fullName, online = live)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(row.user.fullName, color = Ink, fontWeight = FontWeight.SemiBold)
                Text(row.activeApp, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(if (live) "LIVE" else "IDLE", color = if (live) Danger else Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        if (row.screenshot != null) {
            ScreenshotPreview(shot = row.screenshot, token = token, modifier = Modifier.clickable(onClick = onOpenStream))
        } else {
            Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Surface2, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Text("No screenshot today", color = Muted)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(relativeTime(row.lastActiveAt), color = MutedLight, fontSize = 12.sp, modifier = Modifier.weight(1f))
            OutlinedButton(onClick = onOpenStream, shape = RoundedCornerShape(12.dp)) {
                Text("Stream")
            }
        }
    }
}

@Composable
private fun LiveWebViewScreen(employee: UserProfile, token: String, onClose: () -> Unit) {
    val html = remember(employee.id, token) { liveStreamHtml(token = token, employee = employee) }
    Column(Modifier.fillMaxSize().background(Background)) {
        Row(Modifier.fillMaxWidth().background(Ink).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Real Live Stream", color = Surface, fontWeight = FontWeight.SemiBold)
                Text(employee.fullName, color = Surface.copy(alpha = 0.65f), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            OutlinedButton(onClick = onClose) { Text("Close") }
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                            android.util.Log.d("TeamLensLiveWeb", "${consoleMessage.messageLevel()}: ${consoleMessage.message()}")
                            return true
                        }
                    }
                    webViewClient = WebViewClient()
                    loadDataWithBaseURL(BackendConfig.baseUrl, html, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(BackendConfig.baseUrl, html, "text/html", "UTF-8", null)
            }
        )
    }
}

@Composable
private fun UserFilterRow(users: List<UserProfile>, selectedUserId: String, onSelect: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            FilterPill(label = "All", selected = selectedUserId.isBlank(), onClick = { onSelect("") })
        }
        items(users) { employee ->
            FilterPill(
                label = employee.fullName.substringBefore(" "),
                selected = selectedUserId == employee.id,
                onClick = { onSelect(employee.id) }
            )
        }
    }
}

@Composable
private fun CalendarScreenshotsScreen(vm: TeamLensViewModel) {
    val state = vm.state
    var openShot by remember { mutableStateOf<ScreenshotItem?>(null) }

    LaunchedEffect(state.token) {
        if (state.token.isNotBlank() && state.calendarDays.isEmpty()) {
            vm.loadCalendarAndScreenshots()
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Calendar", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Daily stats and screenshots", color = Muted)
                }
                OutlinedButton(onClick = { vm.loadCalendarAndScreenshots() }, enabled = !state.screenshotLoading) {
                    Text(if (state.screenshotLoading) "Loading" else "Refresh")
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterPill(
                        label = "All",
                        selected = state.selectedUserId.isBlank(),
                        onClick = { vm.loadCalendarAndScreenshots(state.selectedDate, "") }
                    )
                }
                items(state.employees) { employee ->
                    FilterPill(
                        label = employee.fullName.substringBefore(" "),
                        selected = state.selectedUserId == employee.id,
                        onClick = { vm.loadCalendarAndScreenshots(state.selectedDate, employee.id) }
                    )
                }
            }
        }
        item {
            AppCard(radius = 18) {
                Text("Go to date", color = Ink, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                DatePickerButton(
                    label = state.selectedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    selectedDate = state.selectedDate,
                    onSelectDate = { vm.loadCalendarAndScreenshots(it, state.selectedUserId) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { vm.loadCalendarAndScreenshots(state.selectedDate.minusDays(1), state.selectedUserId) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Previous") }
                    OutlinedButton(
                        onClick = { vm.loadCalendarAndScreenshots(LocalDate.now(), state.selectedUserId) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Today") }
                    OutlinedButton(
                        onClick = { vm.loadCalendarAndScreenshots(state.selectedDate.plusDays(1), state.selectedUserId) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Next") }
                }
            }
        }
        item {
            CalendarMonthCard(
                selectedDate = state.selectedDate,
                days = state.calendarDays,
                onSelectDate = { vm.loadCalendarAndScreenshots(it, state.selectedUserId) }
            )
        }
        item {
            DayStatsCard(state.selectedDate, state.selectedDayAnalytics, state.dayScreenshots.size)
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Screenshots", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text("${state.dayScreenshots.size}", color = Muted, fontWeight = FontWeight.SemiBold)
            }
        }
        if (state.screenshotLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (state.dayScreenshots.isEmpty()) {
            item {
                AppCard(radius = 18) {
                    Text("No screenshots found", color = Ink, fontWeight = FontWeight.SemiBold)
                    Text("Try another date or employee filter.", color = Muted, fontSize = 13.sp)
                }
            }
        } else {
            items(state.dayScreenshots) { shot ->
                ScreenshotCard(shot = shot, token = state.token, onOpen = { openShot = shot })
            }
        }
    }

    openShot?.let { shot ->
        ScreenshotViewerDialog(
            shot = shot,
            shots = state.dayScreenshots,
            token = state.token,
            onClose = { openShot = null },
            onNavigate = { openShot = it }
        )
    }
}

@Composable
private fun FilterPill(label: String, selected: Boolean, onClick: () -> Unit) {
    val container = if (selected) Brand else Surface
    val content = if (selected) Surface else Ink
    OutlinedButton(
        onClick = onClick,
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(containerColor = container, contentColor = content),
        border = BorderStroke(1.dp, if (selected) Brand else Border),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun CalendarMonthCard(selectedDate: LocalDate, days: List<CalendarDay>, onSelectDate: (LocalDate) -> Unit) {
    val month = YearMonth.from(selectedDate)
    val byDate = days.associateBy { it.date }
    val first = month.atDay(1)
    val blanks = first.dayOfWeek.value - 1
    val cells = buildList {
        repeat(blanks) { add(null) }
        for (day in 1..month.lengthOfMonth()) add(month.atDay(day))
    }
    AppCard(radius = 18) {
        Text(month.format(DateTimeFormatter.ofPattern("MMMM yyyy")), color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                Text(it, color = MutedLight, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(6.dp))
        cells.chunked(7).forEach { week ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    if (date == null) {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val day = byDate[date.toString()]
                        CalendarDayCell(
                            date = date,
                            selected = date == selectedDate,
                            seconds = (day?.activeSeconds ?: 0.0) + (day?.manualSeconds ?: 0.0),
                            onClick = { onSelectDate(date) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                repeat(7 - week.size) { Spacer(Modifier.weight(1f).aspectRatio(1f)) }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun CalendarDayCell(date: LocalDate, selected: Boolean, seconds: Double, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val color = when {
        selected -> Brand
        seconds <= 0.0 -> Surface2
        seconds < 2 * 3600 -> androidx.compose.ui.graphics.Color(0xFFFFD6CC)
        seconds < 5 * 3600 -> androidx.compose.ui.graphics.Color(0xFFF58E78)
        else -> Success
    }
    val textColor = if (selected || seconds >= 5 * 3600) Surface else Ink
    Box(
        modifier
            .aspectRatio(1f)
            .background(color, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(date.dayOfMonth.toString(), color = textColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DayStatsCard(date: LocalDate, analytics: DashboardAnalytics, screenshotCount: Int) {
    AppCard(radius = 18) {
        Text(date.format(DateTimeFormatter.ofPattern("EEE, MMM d")), color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MiniStat("Active", formatDuration(analytics.activeSeconds), Modifier.weight(1f))
            MiniStat("Work", formatDuration(analytics.workSeconds), Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MiniStat("Productive", "${analytics.productivityPercent.format0()}%", Modifier.weight(1f))
            MiniStat("Shots", screenshotCount.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Surface2, RoundedCornerShape(12.dp)).padding(12.dp)) {
        Text(label, color = Muted, fontSize = 12.sp)
        Text(value, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
    }
}

@Composable
private fun ScreenshotCard(shot: ScreenshotItem, token: String, onOpen: () -> Unit = {}) {
    AppCard(radius = 18, modifier = Modifier.clickable(onClick = onOpen)) {
        ScreenshotPreview(shot = shot, token = token)
        Spacer(Modifier.height(10.dp))
        Text(shot.employeeName ?: shot.user?.fullName ?: "Employee", color = Ink, fontWeight = FontWeight.SemiBold)
        Text(shot.windowTitle ?: shot.activeApplication ?: shot.domain ?: "Captured screen", color = Muted, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(formatClock(shot.capturedAt ?: shot.createdAt.orEmpty()), color = Brand, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(8.dp))
            Text(shot.projectName ?: shot.domain.orEmpty(), color = MutedLight, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ScreenshotPreview(shot: ScreenshotItem, token: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageRequest = remember(shot.id, token) {
        ImageRequest.Builder(context)
            .data(screenshotAssetUrl(shot.id))
            .addHeader("Authorization", "Bearer $token")
            .crossfade(true)
            .build()
    }
    AsyncImage(
        model = imageRequest,
        contentDescription = "Screenshot captured at ${shot.capturedAt ?: shot.createdAt.orEmpty()}",
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Surface2, RoundedCornerShape(14.dp))
    )
}

@Composable
private fun ScreenshotViewerDialog(
    shot: ScreenshotItem,
    shots: List<ScreenshotItem>,
    token: String,
    onClose: () -> Unit,
    onNavigate: (ScreenshotItem) -> Unit
) {
    val context = LocalContext.current
    val galleryShots = remember(shots, shot.id) {
        shots.ifEmpty { listOf(shot) }
    }
    val currentIndex = galleryShots.indexOfFirst { it.id == shot.id }.takeIf { it >= 0 } ?: 0
    val currentShot = galleryShots.getOrNull(currentIndex) ?: shot
    val imageRequest = remember(currentShot.id, token) {
        ImageRequest.Builder(context)
            .data(screenshotAssetUrl(currentShot.id))
            .addHeader("Authorization", "Bearer $token")
            .crossfade(true)
            .build()
    }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        offset = if (scale == 1f) Offset.Zero else offset + panChange
    }
    LaunchedEffect(currentShot.id) {
        scale = 1f
        offset = Offset.Zero
        dragOffset = Offset.Zero
    }
    fun navigateByGesture(delta: Int) {
        val target = (currentIndex + delta).coerceIn(0, galleryShots.lastIndex)
        if (target != currentIndex) {
            onNavigate(galleryShots[target])
        }
    }

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black)) {
            AsyncImage(
                model = imageRequest,
                contentDescription = "Expanded screenshot",
                contentScale = ContentScale.Fit,
                modifier = if (scale > 1f) {
                    Modifier
                        .fillMaxSize()
                        .transformable(transformState)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                } else {
                    Modifier
                        .fillMaxSize()
                        .pointerInput(currentShot.id, galleryShots.size) {
                            detectDragGestures(
                                onDragStart = { dragOffset = Offset.Zero },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffset += dragAmount
                                },
                                onDragEnd = {
                                    val threshold = 80f
                                    val horizontal = kotlin.math.abs(dragOffset.x) > kotlin.math.abs(dragOffset.y)
                                    when {
                                        horizontal && dragOffset.x < -threshold -> navigateByGesture(1)
                                        horizontal && dragOffset.x > threshold -> navigateByGesture(-1)
                                        !horizontal && dragOffset.y < -threshold -> navigateByGesture(1)
                                        !horizontal && dragOffset.y > threshold -> navigateByGesture(-1)
                                    }
                                    dragOffset = Offset.Zero
                                },
                                onDragCancel = { dragOffset = Offset.Zero }
                            )
                        }
                        .graphicsLayer {
                            translationX = dragOffset.x * 0.18f
                            translationY = dragOffset.y * 0.18f
                        }
                }
            )
            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { scale = (scale - 0.5f).coerceAtLeast(1f); if (scale == 1f) offset = Offset.Zero }) {
                    Icon(Icons.Outlined.ZoomOut, contentDescription = "Zoom out", tint = Surface)
                }
                IconButton(onClick = { scale = (scale + 0.5f).coerceAtMost(5f) }) {
                    Icon(Icons.Outlined.ZoomIn, contentDescription = "Zoom in", tint = Surface)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close", tint = Surface)
                }
            }
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                if (galleryShots.size > 1) {
                    Text("${currentIndex + 1} / ${galleryShots.size}", color = androidx.compose.ui.graphics.Color(0xFFD9D9D9), fontSize = 12.sp)
                }
                Text(currentShot.employeeName ?: currentShot.user?.fullName ?: "Employee", color = Surface, fontWeight = FontWeight.SemiBold)
                Text(
                    currentShot.windowTitle ?: currentShot.activeApplication ?: currentShot.domain ?: "Captured screen",
                    color = androidx.compose.ui.graphics.Color(0xFFD9D9D9),
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EmptyScreenCard(title: String, subtitle: String) {
    AppCard(radius = 18) {
        Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = Muted, fontSize = 13.sp)
    }
}

@Composable
private fun AlertCard(title: String, subtitle: String, color: androidx.compose.ui.graphics.Color, textColor: androidx.compose.ui.graphics.Color) {
    AppCard(radius = 16) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).background(color.copy(alpha = 0.14f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Box(Modifier.size(10.dp).background(textColor, CircleShape))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun CompactActivityDateCard(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    onPrevious: () -> Unit,
    onToday: () -> Unit,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Activity date", color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d yyyy")), color = Muted, fontSize = 12.sp)
                }
                OutlinedButton(onClick = onToday, shape = RoundedCornerShape(999.dp)) {
                    Text("Today", fontSize = 12.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onPrevious, shape = RoundedCornerShape(12.dp)) { Text("<") }
                DatePickerButton(
                    label = selectedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    selectedDate = selectedDate,
                    onSelectDate = onSelectDate,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = onNext, shape = RoundedCornerShape(12.dp)) { Text(">") }
            }
        }
    }
}

@Composable
private fun CustomTimeRangeCard(
    hourRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Time window", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("Select the exact range you want to inspect", color = Muted, fontSize = 12.sp)
                }
                Text(
                    "${formatActivityHour(hourRange.start.toInt())} - ${formatActivityHour(hourRange.endInclusive.toInt())}",
                    color = Brand,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            RangeSlider(
                value = hourRange,
                onValueChange = onRangeChange,
                valueRange = 0f..24f,
                steps = 23
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf(0, 6, 12, 18, 24).forEach { hour ->
                    Text(formatActivityHour(hour), color = MutedLight, fontSize = 10.sp, textAlign = TextAlign.Center)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimePresetChip("Morning", 9f..13f, onRangeChange)
                TimePresetChip("Afternoon", 13f..18f, onRangeChange)
                TimePresetChip("Workday", 9f..21f, onRangeChange)
            }
        }
    }
}

@Composable
private fun RowScope.TimePresetChip(
    label: String,
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    OutlinedButton(
        onClick = { onRangeChange(range) },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(label, color = Ink, fontSize = 12.sp, maxLines = 1)
    }
}

private fun instantForActivityHour(date: LocalDate, hour: Int, zone: ZoneId): Instant =
    if (hour >= 24) {
        date.plusDays(1).atStartOfDay(zone).toInstant()
    } else {
        date.atTime(hour.coerceIn(0, 23), 0).atZone(zone).toInstant()
    }

private fun formatActivityHour(hour: Int): String {
    val normalized = if (hour == 24) 0 else hour.coerceIn(0, 23)
    val suffix = if (normalized < 12) "AM" else "PM"
    val displayHour = when (val value = normalized % 12) {
        0 -> 12
        else -> value
    }
    return "$displayHour $suffix"
}

@Composable
private fun SettingsScreen(vm: TeamLensViewModel) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Settings", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.SemiBold) }
        item {
            AppCard(radius = 16) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarChip(vm.state.user?.fullName ?: "User")
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(vm.state.user?.fullName ?: "User", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text(vm.state.user?.email.orEmpty(), color = Muted, fontSize = 13.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(vm.state.organizationName.ifBlank { "No Workspace" }, color = Muted, fontSize = 11.sp, modifier = Modifier.background(Surface2, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
        }
        item { Text("ACCOUNT", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        items(listOf("Profile Details", "Notifications", "Security", "Theme", "Language")) { label ->
            MenuRow(label, "›")
        }
        item { Text("WORKSPACE CONTROLS", color = MutedLight, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        items(listOf("Screenshots", "Screen Recordings", "Productivity Labels", "Reports")) { label ->
            MenuRow(label, "›")
        }
        item {
            AppCard(radius = 16, modifier = Modifier.background(DangerTint)) {
                Button(
                    onClick = vm::logout,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = DangerTint, contentColor = Danger),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Sign Out", fontWeight = FontWeight.SemiBold) }
            }
        }
    }
}

@Composable
private fun MenuRow(label: String, value: String = "") {
    AppCard(radius = 16) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).background(Surface2, RoundedCornerShape(8.dp)))
            Spacer(Modifier.width(12.dp))
            Text(label, color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text(value, color = MutedLight)
        }
    }
}

@Composable
private fun MoreScreen() {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("More", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        items(listOf("Live View", "Activities", "Attendance", "Manual Time", "Reports", "Recordings", "Productivity Rules", "Settings")) {
            ListCard(title = it, subtitle = "Native screen scaffold pending")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String, body: String) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        ListCard(title = title, subtitle = body)
    }
}

@Composable
private fun ListCard(title: String, subtitle: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun FeatureRoadmapCard() {
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Border)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Analytics, contentDescription = null, tint = Brand)
                Spacer(Modifier.width(8.dp))
                Text("Native conversion map", fontWeight = FontWeight.SemiBold)
            }
            Text("Auth, dashboard, employees, teams and screenshot count are wired. Next screens can now be converted one-by-one from the web feature set.", color = Muted, fontSize = 13.sp)
        }
    }
}

private fun statsOrZero(value: Int): String = value.toString()

private fun formatDuration(seconds: Double): String {
    val total = seconds.toInt().coerceAtLeast(0)
    val hours = total / 3600
    val minutes = (total % 3600) / 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}

private fun formatClock(value: String): String =
    runCatching {
        DateTimeFormatter.ofPattern("HH:mm").format(Instant.parse(value).atZone(ZoneId.systemDefault()))
    }.getOrDefault("--")

private fun formatDate(value: String): String =
    runCatching {
        DateTimeFormatter.ofPattern("MMM d").format(Instant.parse(value).atZone(ZoneId.systemDefault()))
    }.getOrDefault(value.take(10))

private fun dayRangeIso(date: LocalDate): Pair<String, String> {
    val zone = ZoneId.systemDefault()
    val start = date.atStartOfDay(zone).toInstant().toString()
    val end = date.atTime(23, 59, 59).atZone(zone).toInstant().toString()
    return start to end
}

private fun parseDateKey(value: String): LocalDate? =
    runCatching { LocalDate.parse(value.trim()) }.getOrNull()

private fun LocalDate.toEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDateUtc(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

private fun screenshotAssetUrl(id: String): String =
    BackendConfig.screenshotUrl(id)

private fun liveStreamHtml(token: String, employee: UserProfile): String {
    val safeName = employee.fullName.replace("\\", "\\\\").replace("'", "\\'")
    val safeToken = token.replace("\\", "\\\\").replace("'", "\\'")
    val safeEmployeeId = employee.id.replace("\\", "\\\\").replace("'", "\\'")
    val safeIceServers = BuildConfig.WEBRTC_ICE_SERVERS.replace("\\", "\\\\").replace("'", "\\'")
    val apiBase = BackendConfig.baseUrl
    return """
<!doctype html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
  <style>
    html, body { margin:0; width:100%; height:100%; background:#050505; color:white; font-family:system-ui,-apple-system,Segoe UI,sans-serif; }
    main { min-height:100vh; display:flex; flex-direction:column; background:#050505; }
    header, footer { padding:14px 16px; background:#0d0d0d; border-color:rgba(255,255,255,.1); }
    header { border-bottom:1px solid rgba(255,255,255,.1); display:flex; align-items:center; justify-content:space-between; gap:12px; }
    footer { border-top:1px solid rgba(255,255,255,.1); }
    h1 { margin:0; font-size:16px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
    p { margin:0; }
    .sub { margin-top:3px; color:rgba(255,255,255,.55); font-size:12px; }
    .stage { position:relative; flex:1; display:flex; align-items:center; justify-content:center; background:#000; }
    video { width:100%; height:100%; max-height:calc(100vh - 150px); object-fit:contain; background:#000; }
    .overlay { position:absolute; inset:0; display:flex; align-items:center; justify-content:center; padding:28px; text-align:center; background:rgba(0,0,0,.74); }
    .dot { width:12px; height:12px; border-radius:999px; margin:0 auto 14px; background:#f59e0b; }
    .dot.live { background:#10b981; }
    .dot.error { background:#ef4444; }
    button { border:0; border-radius:999px; padding:10px 14px; font-weight:700; color:#fff; background:rgba(255,255,255,.12); }
    .stop { width:100%; margin-top:10px; background:#e2553d; padding:13px 16px; }
    .muted { margin-top:8px; color:rgba(255,255,255,.45); font-size:11px; word-break:break-all; }
    .debug { margin-top:6px; color:rgba(255,255,255,.38); font-size:10px; word-break:break-word; }
  </style>
  <script src="https://cdn.socket.io/4.7.5/socket.io.min.js"></script>
</head>
<body>
<main>
  <header>
    <div>
      <h1>$safeName</h1>
      <p class="sub">TeamLens live screen</p>
    </div>
    <button onclick="requestLiveView()">Retry</button>
  </header>
  <section class="stage">
    <video id="video" autoplay playsinline muted controls></video>
    <div id="overlay" class="overlay">
      <div>
        <div id="dot" class="dot"></div>
        <p id="status">Preparing live stream...</p>
        <p class="muted">Employee agent must be online and screen sharing-capable.</p>
        <p id="debugStatus" class="debug"></p>
      </div>
    </div>
  </section>
  <footer>
    <p id="footerStatus" class="sub">Connecting...</p>
    <button class="stop" onclick="stopViewing('ended')">Stop stream</button>
  </footer>
</main>
<script>
const token = '$safeToken';
const employeeId = '$safeEmployeeId';
const apiBase = '$apiBase';
const wsBase = '$apiBase';
const configuredIceServersRaw = '$safeIceServers';
let socket = null;
let peer = null;
let sessionId = '';
let remoteStream = null;
let pendingIce = [];
let statsTimer = null;
let hasVideoTrack = false;
let hasVideoFrame = false;
let lastFramesDecoded = 0;
const fallbackIceServers = [
  { urls: 'stun:stun.l.google.com:19302' },
  { urls: 'stun:stun1.l.google.com:19302' }
];
let sessionIceServers = normalizeIceServers(parseConfiguredIceServers());
const video = document.getElementById('video');
const overlay = document.getElementById('overlay');
const statusEl = document.getElementById('status');
const footerStatus = document.getElementById('footerStatus');
const dot = document.getElementById('dot');
const debugStatus = document.getElementById('debugStatus');

function setStatus(message, state) {
  console.log('[TeamLensLive]', message);
  statusEl.textContent = message;
  footerStatus.textContent = message;
  dot.className = 'dot' + (state ? ' ' + state : '');
  overlay.style.display = state === 'live' ? 'none' : 'flex';
}
function setDebug(message) {
  debugStatus.textContent = message || '';
  if (message) console.log('[TeamLensLiveDebug]', message);
}
function parseConfiguredIceServers() {
  if (!configuredIceServersRaw || configuredIceServersRaw === '[]') return [];
  try {
    const parsed = JSON.parse(configuredIceServersRaw);
    return Array.isArray(parsed) ? parsed : [];
  } catch (error) {
    console.warn('[TeamLensLive] invalid configured ICE servers', error);
    return [];
  }
}
function normalizeIceServers(servers) {
  const input = Array.isArray(servers) ? servers : [];
  const merged = fallbackIceServers.concat(input);
  const seen = new Set();
  const normalized = [];
  for (const server of merged) {
    if (!server || !server.urls) continue;
    const urls = Array.isArray(server.urls) ? server.urls : [server.urls];
    const cleanUrls = urls.map(url => String(url || '').trim()).filter(Boolean);
    if (!cleanUrls.length) continue;
    const key = cleanUrls.join('|') + '|' + (server.username || '');
    if (seen.has(key)) continue;
    seen.add(key);
    normalized.push({ ...server, urls: cleanUrls.length === 1 ? cleanUrls[0] : cleanUrls });
  }
  return normalized.length ? normalized : fallbackIceServers;
}
function describeIceServers(servers) {
  return servers
    .flatMap(server => Array.isArray(server.urls) ? server.urls : [server.urls])
    .map(url => String(url).replace(/\/\/[^@/]+@/, '//***@'))
    .join(', ');
}
function cleanupPeer() {
  if (statsTimer) clearInterval(statsTimer);
  statsTimer = null;
  if (peer) peer.close();
  peer = null;
  pendingIce = [];
  hasVideoTrack = false;
  hasVideoFrame = false;
  lastFramesDecoded = 0;
  if (remoteStream) remoteStream.getTracks().forEach(track => track.stop());
  remoteStream = null;
  video.srcObject = null;
}
function markVideoLive() {
  hasVideoFrame = true;
  setStatus('Live stream connected.', 'live');
}
function startStatsMonitor() {
  if (statsTimer) clearInterval(statsTimer);
  statsTimer = setInterval(async () => {
    if (!peer) return;
    try {
      const stats = await peer.getStats();
      let framesDecoded = 0;
      let bytesReceived = 0;
      stats.forEach(report => {
        if (report.type === 'inbound-rtp' && report.kind === 'video') {
          framesDecoded += report.framesDecoded || 0;
          bytesReceived += report.bytesReceived || 0;
        }
      });
      setDebug('ICE ' + peer.iceConnectionState + ' | PC ' + peer.connectionState + ' | frames ' + framesDecoded + ' | bytes ' + bytesReceived);
      if (framesDecoded > lastFramesDecoded) {
        lastFramesDecoded = framesDecoded;
        markVideoLive();
      } else if (hasVideoTrack && !hasVideoFrame && (peer.iceConnectionState === 'connected' || peer.iceConnectionState === 'completed')) {
        setStatus('Connected to employee. Waiting for video frames...', '');
      }
    } catch (error) {
      console.warn('[TeamLensLive] stats failed', error);
    }
  }, 1200);
}
function stopViewing(reason) {
  if (socket && sessionId) socket.emit('live:view-ended', { sessionId, reason });
  sessionId = '';
  cleanupPeer();
  setStatus('Live stream stopped.', 'error');
}
function requestLiveView() {
  if (!socket || !employeeId) return;
  cleanupPeer();
  setStatus('Requesting employee live screen...', '');
  socket.timeout(12000).emit('live:view-request', { employeeId }, (error, response) => {
    if (error || !response || !response.ok || !response.sessionId) {
      setStatus((response && response.error) || 'Live request failed. Make sure employee agent is online.', 'error');
      return;
    }
    sessionIceServers = normalizeIceServers(response.iceServers);
    setDebug('ICE servers: ' + describeIceServers(sessionIceServers));
    sessionId = response.sessionId;
    setStatus('Waiting for employee agent to send video...', '');
  });
}
async function validateAndConnect() {
  try {
    const auth = await fetch(apiBase + '/api/web/auth/me', { headers: { Authorization: 'Bearer ' + token }, cache: 'no-store' });
    const payload = await auth.json().catch(() => null);
    if (!auth.ok || !payload || !payload.success) throw new Error((payload && payload.message) || 'Auth failed');
    connectSocket();
  } catch (error) {
    setStatus(error.message || 'Unable to validate token.', 'error');
  }
}
function connectSocket() {
  setStatus('Connecting live signaling...', '');
  socket = io(wsBase, {
    auth: { token },
    withCredentials: true,
    transports: ['polling', 'websocket'],
    upgrade: true,
    reconnectionAttempts: Infinity,
    reconnectionDelay: 1000,
    reconnectionDelayMax: 10000
  });
  socket.on('connect', () => {
    setStatus('Connected. Starting stream...', '');
    requestLiveView();
  });
  socket.on('connect_error', error => setStatus('Live signaling failed: ' + error.message, 'error'));
  socket.on('live:view-accepted', payload => {
    if (payload.sessionId === sessionId) {
      if (payload.iceServers && payload.iceServers.length) {
        sessionIceServers = normalizeIceServers(payload.iceServers);
        setDebug('ICE servers: ' + describeIceServers(sessionIceServers));
      }
      setStatus('Employee accepted. Connecting video...', '');
    }
  });
  socket.on('live:view-ended', payload => {
    if (payload.sessionId !== sessionId) return;
    cleanupPeer();
    sessionId = '';
    setStatus(payload.reason === 'disconnect' ? 'Stream ended because a peer disconnected.' : 'Live stream ended.', 'error');
  });
  socket.on('webrtc:offer', async payload => {
    if (!payload.sessionId || payload.sessionId !== sessionId) return;
    try {
      cleanupPeer();
      if (payload.iceServers && payload.iceServers.length) {
        sessionIceServers = normalizeIceServers(payload.iceServers);
      }
      setDebug('ICE servers: ' + describeIceServers(sessionIceServers));
      remoteStream = new MediaStream();
      peer = new RTCPeerConnection({
        iceServers: sessionIceServers,
        bundlePolicy: 'max-bundle',
        rtcpMuxPolicy: 'require',
        iceTransportPolicy: 'all',
        iceCandidatePoolSize: 4
      });
      peer.ontrack = event => {
        hasVideoTrack = true;
        event.streams[0]?.getTracks().forEach(track => remoteStream.addTrack(track));
        video.srcObject = remoteStream;
        video.onloadedmetadata = () => setStatus('Video track received. Starting playback...', '');
        video.onplaying = () => markVideoLive();
        video.play().then(() => {
          if (video.readyState >= 2) markVideoLive();
        }).catch(() => setStatus('Video track received. Tap play if playback is paused.', ''));
        event.track.onunmute = () => setStatus('Video track is receiving data...', '');
      };
      peer.onicecandidate = event => {
        if (event.candidate) socket.emit('webrtc:ice-candidate', { sessionId: payload.sessionId, candidate: event.candidate });
      };
      peer.onconnectionstatechange = () => {
        console.log('[TeamLensLive] peer connection state', peer.connectionState);
        if (peer.connectionState === 'connected' && !hasVideoFrame) setStatus('Peer connected. Waiting for video track...', '');
        if (['failed','closed','disconnected'].includes(peer.connectionState)) setStatus('WebRTC connection ' + peer.connectionState + '.', 'error');
      };
      peer.oniceconnectionstatechange = () => {
        console.log('[TeamLensLive] ICE state', peer.iceConnectionState);
        if ((peer.iceConnectionState === 'connected' || peer.iceConnectionState === 'completed') && !hasVideoFrame) setStatus('ICE connected. Waiting for video frames...', '');
        if (peer.iceConnectionState === 'failed') {
          setStatus('WebRTC ICE failed. TURN/STUN relay is not reachable or credentials are wrong.', 'error');
          if (peer.restartIce) peer.restartIce();
        }
      };
      startStatsMonitor();
      await peer.setRemoteDescription(payload.offer);
      for (const candidate of pendingIce.splice(0)) await peer.addIceCandidate(candidate).catch(() => undefined);
      const answer = await peer.createAnswer();
      await peer.setLocalDescription(answer);
      socket.emit('webrtc:answer', { sessionId: payload.sessionId, answer });
    } catch (error) {
      setStatus(error.message || 'Unable to connect WebRTC stream.', 'error');
    }
  });
  socket.on('webrtc:ice-candidate', async payload => {
    if (payload.sessionId !== sessionId || !payload.candidate) return;
    if (!peer || !peer.remoteDescription) {
      pendingIce.push(payload.candidate);
      return;
    }
    await peer.addIceCandidate(payload.candidate).catch(() => undefined);
  });
}
validateAndConnect();
</script>
</body>
</html>
""".trimIndent()
}

private fun isFreshActivity(value: String?): Boolean =
    runCatching {
        value != null && Instant.now().toEpochMilli() - Instant.parse(value).toEpochMilli() <= 10 * 60 * 1000
    }.getOrDefault(false)

private fun relativeTime(value: String?): String =
    runCatching {
        if (value.isNullOrBlank()) return@runCatching "No activity"
        val minutes = ((Instant.now().toEpochMilli() - Instant.parse(value).toEpochMilli()) / 60000).coerceAtLeast(0)
        when {
            minutes < 1 -> "Now"
            minutes < 60 -> "${minutes}m ago"
            else -> "${minutes / 60}h ago"
        }
    }.getOrDefault("No activity")

private fun toInstantIso(date: LocalDate, time: String): String =
    runCatching {
        LocalDateTime.parse("${date}T${time}", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toString()
    }.getOrElse { Instant.now().toString() }

private fun Double.format1(): String = String.format("%.1f", this)
private fun Double.format0(): String = String.format("%.0f", this)
