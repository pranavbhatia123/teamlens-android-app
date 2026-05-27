package com.teamlens.nativeapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamlens.nativeapp.data.model.ActivityTimelineApp
import com.teamlens.nativeapp.data.model.ActivityTimelineEmployee
import com.teamlens.nativeapp.data.model.ActivityTimelineSegment
import com.teamlens.nativeapp.ui.theme.Border
import com.teamlens.nativeapp.ui.theme.Brand
import com.teamlens.nativeapp.ui.theme.BrandLight
import com.teamlens.nativeapp.ui.theme.Ink
import com.teamlens.nativeapp.ui.theme.Muted
import com.teamlens.nativeapp.ui.theme.MutedLight
import com.teamlens.nativeapp.ui.theme.Surface
import com.teamlens.nativeapp.ui.theme.Surface2
import com.teamlens.nativeapp.ui.theme.Success
import com.teamlens.nativeapp.ui.theme.SuccessTint
import com.teamlens.nativeapp.ui.theme.Warning
import com.teamlens.nativeapp.ui.theme.WarningTint
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val TimelineActive = Brand
private val TimelineBreak = Color(0xFF20C5B5)
private val TimelineTrack = Color(0xFFF8F7FB)
private val TimelineIdleBase = Color(0xFFF5F3F8)
private val TimelineIdleStripe = Color(0xFFD8D2E3)

@Composable
fun ActivityOverviewCard(
    employees: List<ActivityTimelineEmployee>,
    rangeStart: Instant? = null,
    rangeEnd: Instant? = null,
    modifier: Modifier = Modifier
) {
    val totals = employees.summarize(rangeStart, rangeEnd)
    val totalActive = totals.activeSeconds
    val totalIdle = totals.idleSeconds
    val totalWork = if (rangeStart != null && rangeEnd != null) {
        totalActive + totalIdle
    } else {
        employees.sumOf { it.workSeconds }.takeIf { it > 0 } ?: (totalActive + totalIdle)
    }
    val activePercent = percent(totalActive, totalWork)
    val idlePercent = percent(totalIdle, totalWork)
    val topApps = employees.flatMap { it.topApps }.mergeTopApps().take(3)
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Timeline summary", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text(if (expanded) "Tap to collapse details" else "Tap to inspect usage", color = Muted, fontSize = 12.sp)
                }
                ActivityRing(percent = activePercent)
            }

            SplitBar(activePercent = activePercent, idlePercent = idlePercent)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                ActivityMetric("Active", formatDuration(totalActive), "$activePercent%", SuccessTint, Brand, Modifier.weight(1f))
                ActivityMetric("Idle", formatDuration(totalIdle), "$idlePercent%", WarningTint, Warning, Modifier.weight(1f))
            }

            if (expanded) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Legend("Active", Brand)
                    Legend("Break", Success)
                    Legend("Idle", Border)
                    Legend("Offline", Surface2)
                }

                if (topApps.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Top apps", color = MutedLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        topApps.forEach { app ->
                            AppUsagePill(app = app, maxSeconds = topApps.first().seconds)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeActivityCard(
    employee: ActivityTimelineEmployee,
    rangeStart: Instant? = null,
    rangeEnd: Instant? = null,
    modifier: Modifier = Modifier
) {
    val work = employee.workSeconds.takeIf { it > 0 } ?: (employee.activeSeconds + employee.idleSeconds)
    val activePercent = percent(employee.activeSeconds, work)
    val utilization = employee.utilizationPercent.takeIf { it > 0 } ?: activePercent
    var selectedSegmentIndex by remember(employee.userId, employee.segments.size) { mutableStateOf<Int?>(null) }
    val selectedSegment = selectedSegmentIndex?.let { employee.segments.getOrNull(it) }
    var expanded by remember(employee.userId) { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                InitialsBadge(employee.employeeName.ifBlank { employee.email })
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        employee.employeeName.ifBlank { employee.email.ifBlank { "Employee" } },
                        color = Ink,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Legend("Active", TimelineActive)
                        Legend("Break", TimelineBreak)
                        Legend("Idle", TimelineIdleStripe)
                        Legend("Offline", Surface2)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${formatDuration(work)} Worked", color = Brand, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    Text("${utilization}% util.", color = Muted, fontSize = 11.sp)
                }
            }

            InteractiveActivityBar(
                employee = employee,
                selectedSegment = selectedSegment,
                rangeStart = rangeStart,
                rangeEnd = rangeEnd,
                onSelectSegment = { selectedSegmentIndex = it }
            )

            if (expanded || selectedSegment != null) {
                ExpandedTimelineDetail(employee = employee, segment = selectedSegment)
            }
        }
    }
}

@Composable
fun ActivityTimelineAxis(
    rangeStart: Instant,
    rangeEnd: Instant,
    modifier: Modifier = Modifier
) {
    val totalHours = Duration.between(rangeStart, rangeEnd).toHours().coerceAtLeast(1)
    val stepHours = when {
        totalHours <= 12 -> 2L
        totalHours <= 18 -> 3L
        else -> 4L
    }
    val labels = buildList {
        var cursor = rangeStart
        while (!cursor.isAfter(rangeEnd)) {
            add(cursor)
            cursor = cursor.plus(Duration.ofHours(stepHours))
        }
        if (lastOrNull() != rangeEnd) add(rangeEnd)
    }

    Column(
        modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEachIndexed { index, instant ->
                val pattern = if (index == labels.lastIndex) "h a" else "h"
                Text(
                    DateTimeFormatter.ofPattern(pattern).format(instant.atZone(ZoneId.systemDefault())),
                    color = Muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEach {
                Box(Modifier.size(width = 1.dp, height = 7.dp).background(Border))
            }
        }
    }
}

@Composable
private fun InitialsBadge(name: String) {
    val initials = name.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.take(1).uppercase() }.ifBlank { "U" }
    Box(
        Modifier.size(36.dp).background(BrandLight, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Brand, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InteractiveActivityBar(
    employee: ActivityTimelineEmployee,
    selectedSegment: ActivityTimelineSegment?,
    rangeStart: Instant?,
    rangeEnd: Instant?,
    onSelectSegment: (Int?) -> Unit
) {
    val blocks = timelineBlocks(employee, rangeStart, rangeEnd)
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(TimelineTrack),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (blocks.isEmpty()) {
                val active = employee.activeSeconds.toFloat().coerceAtLeast(0.1f)
                val idle = employee.idleSeconds.toFloat().coerceAtLeast(if (employee.idleSeconds > 0) 0.1f else 0f)
                Box(Modifier.weight(active).height(14.dp).background(TimelineActive))
                if (idle > 0f) IdleTextureBlock(Modifier.weight(idle).height(14.dp))
            } else {
                blocks.forEach { block ->
                    val segment = block.segmentIndex?.let { employee.segments.getOrNull(it) }
                    val selected = selectedSegment != null && selectedSegment == segment
                    val active = block.kind.equals("active", ignoreCase = true)
                    val offline = block.kind.equals("offline", ignoreCase = true)
                    val breakLike = !active && !offline && block.mouseMoves + block.keyPresses > 0
                    val idle = !active && !offline && !breakLike
                    val color = when {
                        active -> TimelineActive
                        offline -> Surface
                        breakLike -> TimelineBreak
                        else -> TimelineIdleBase
                    }
                    var blockModifier = Modifier
                        .weight(segmentDurationSeconds(block).toFloat().coerceAtLeast(1f))
                        .height(14.dp)
                    if (block.segmentIndex != null) {
                        blockModifier = blockModifier.clickable {
                            onSelectSegment(if (selected) null else block.segmentIndex)
                        }
                    }
                    if (idle) {
                        IdleTextureBlock(blockModifier, selected = selected)
                    } else {
                        Box(blockModifier.background(color))
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(formatClock(employee.firstActiveAt), color = MutedLight, fontSize = 10.sp)
            Text(formatClock(employee.lastActiveAt), color = MutedLight, fontSize = 10.sp)
        }
    }
}

@Composable
private fun IdleTextureBlock(modifier: Modifier = Modifier, selected: Boolean = false) {
    val stripe = if (selected) Color(0xFFCFC7E0) else TimelineIdleStripe
    Box(
        modifier
            .background(TimelineIdleBase)
            .drawBehind {
                val stroke = 1.4.dp.toPx()
                val gap = 14.dp.toPx()
                if (size.width < 8.dp.toPx()) return@drawBehind

                var x = if (size.width < 18.dp.toPx()) {
                    size.width / 2f - size.height / 2f
                } else {
                    3.dp.toPx()
                }
                while (x < size.width) {
                    drawLine(
                        color = stripe,
                        start = Offset(x, size.height),
                        end = Offset(x + size.height, 0f),
                        strokeWidth = stroke
                    )
                    x += gap
                }
            }
    )
}

@Composable
private fun ExpandedTimelineDetail(employee: ActivityTimelineEmployee, segment: ActivityTimelineSegment?) {
    val mousePercent: Int
    val keyboardPercent: Int
    val title: String
    val subtitle: String

    if (segment != null) {
        val totalInputs = (segment.mouseMoves + segment.keyPresses).coerceAtLeast(0)
        mousePercent = if (totalInputs > 0) ((segment.mouseMoves.toDouble() / totalInputs) * 100).roundToInt() else 0
        keyboardPercent = if (totalInputs > 0) 100 - mousePercent else 0
        title = segment.kind.uppercase()
        subtitle = "${formatClock(segment.start)} - ${formatClock(segment.end)} | ${formatDuration(segmentDurationSeconds(segment))}"
    } else {
        val totalInputs = (employee.mouseMoves + employee.keyPresses).coerceAtLeast(0)
        mousePercent = employee.mousePercent ?: if (totalInputs > 0) ((employee.mouseMoves.toDouble() / totalInputs) * 100).roundToInt() else 0
        keyboardPercent = employee.keyboardPercent ?: if (totalInputs > 0) 100 - mousePercent else 0
        title = "Day input mix"
        subtitle = "Tap an activity block to inspect that period."
    }

    Row(
        Modifier
            .fillMaxWidth()
            .background(BrandLight, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            employee.topApps.take(4).forEach { app ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(app.name, color = Ink, fontSize = 11.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${percent(app.seconds, employee.topApps.sumOf { it.seconds })}%", color = Muted, fontSize = 10.sp)
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("INPUT USAGE", color = MutedLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            InputUsageBar("Mouse", mousePercent, Success)
            InputUsageBar("Keyboard", keyboardPercent, Brand)
        }
    }
}

@Composable
private fun InputUsageBar(label: String, value: Int, color: Color) {
    Column(Modifier.width(112.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = Ink, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("${value.coerceIn(0, 100)}%", color = Ink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        Box(Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(999.dp)).background(Surface2)) {
            Box(
                Modifier
                    .fillMaxWidth((value.coerceIn(0, 100) / 100f).coerceAtLeast(0.02f))
                    .height(7.dp)
                    .background(color)
            )
        }
    }
}

@Composable
private fun InputDonut(mousePercent: Int, keyboardPercent: Int) {
    Box(Modifier.size(70.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(70.dp)) {
            val stroke = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Butt)
            val diameter = this.size.minDimension - stroke.width
            val topLeft = Offset(stroke.width / 2, stroke.width / 2)
            drawArc(color = Border, startAngle = -90f, sweepAngle = 360f, useCenter = false, topLeft = topLeft, size = Size(diameter, diameter), style = stroke)
            drawArc(color = Success, startAngle = -90f, sweepAngle = 360f * (mousePercent.coerceIn(0, 100) / 100f), useCenter = false, topLeft = topLeft, size = Size(diameter, diameter), style = stroke)
            drawArc(color = Brand, startAngle = -90f + 360f * (mousePercent.coerceIn(0, 100) / 100f), sweepAngle = 360f * (keyboardPercent.coerceIn(0, 100) / 100f), useCenter = false, topLeft = topLeft, size = Size(diameter, diameter), style = stroke)
        }
        Text("${mousePercent.coerceIn(0, 100)}%", color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActivityRing(percent: Int, size: Int = 68) {
    Box(Modifier.size(size.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size.dp)) {
            val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
            val diameter = this.size.minDimension - stroke.width
            val topLeft = Offset(stroke.width / 2, stroke.width / 2)
            drawArc(
                color = Border,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = stroke
            )
            drawArc(
                color = Success,
                startAngle = -90f,
                sweepAngle = 360f * (percent.coerceIn(0, 100) / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = stroke
            )
        }
        Text("$percent%", color = Ink, fontWeight = FontWeight.Bold, fontSize = if (size > 60) 14.sp else 12.sp)
    }
}

@Composable
private fun SplitBar(activePercent: Int, idlePercent: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(18.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Surface2)
    ) {
        Box(Modifier.weight(activePercent.coerceAtLeast(1).toFloat()).height(18.dp).background(Success))
        if (idlePercent > 0) {
            Box(Modifier.weight(idlePercent.coerceAtLeast(1).toFloat()).height(18.dp).background(Warning))
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Legend("Active", Success)
        Legend("Idle", Warning)
    }
}

@Composable
private fun Legend(label: String, color: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(label, color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun ActivityMetric(
    label: String,
    value: String,
    percentLabel: String,
    background: androidx.compose.ui.graphics.Color,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(modifier.background(background, RoundedCornerShape(8.dp)).padding(12.dp)) {
        Text(label, color = Muted, fontSize = 11.sp)
        Text(value, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(percentLabel, color = accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CompactMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Surface2, RoundedCornerShape(8.dp)).padding(10.dp)) {
        Text(label, color = Muted, fontSize = 10.sp)
        Text(value, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

@Composable
private fun AppUsagePill(app: ActivityTimelineApp, maxSeconds: Double) {
    val widthPercent = (app.seconds / maxSeconds.coerceAtLeast(1.0)).toFloat().coerceIn(0.08f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(app.name, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(formatDuration(app.seconds), color = Muted, fontSize = 11.sp)
        }
        Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(BrandLight)) {
            Box(Modifier.fillMaxWidth(widthPercent).height(8.dp).background(Brand))
        }
    }
}

private fun List<ActivityTimelineApp>.mergeTopApps(): List<ActivityTimelineApp> =
    groupBy { it.name.ifBlank { "Unknown app" } }
        .map { (name, apps) -> ActivityTimelineApp(name = name, seconds = apps.sumOf { it.seconds }) }
        .sortedByDescending { it.seconds }

private fun percent(value: Double, total: Double): Int =
    if (total <= 0.0) 0 else ((value / total) * 100).roundToInt().coerceIn(0, 100)

private data class ActivitySummaryTotals(
    val activeSeconds: Double,
    val idleSeconds: Double
)

private fun List<ActivityTimelineEmployee>.summarize(rangeStart: Instant?, rangeEnd: Instant?): ActivitySummaryTotals {
    if (rangeStart == null || rangeEnd == null || none { it.segments.isNotEmpty() }) {
        return ActivitySummaryTotals(
            activeSeconds = sumOf { it.activeSeconds },
            idleSeconds = sumOf { it.idleSeconds }
        )
    }

    var active = 0.0
    var idle = 0.0
    forEach { employee ->
        timelineBlocks(employee, rangeStart, rangeEnd).forEach { block ->
            if (block.segmentIndex == null) return@forEach
            val seconds = segmentDurationSeconds(block)
            if (block.kind.equals("active", ignoreCase = true)) {
                active += seconds
            } else {
                idle += seconds
            }
        }
    }
    return ActivitySummaryTotals(activeSeconds = active, idleSeconds = idle)
}

private fun relativeClock(value: String?): String =
    runCatching {
        if (value.isNullOrBlank()) return@runCatching "not available"
        DateTimeFormatter.ofPattern("HH:mm").format(Instant.parse(value).atZone(ZoneId.systemDefault()))
    }.getOrDefault("not available")

private fun formatClock(value: String?): String =
    runCatching {
        if (value.isNullOrBlank()) return@runCatching "--"
        DateTimeFormatter.ofPattern("h:mm a").format(Instant.parse(value).atZone(ZoneId.systemDefault()))
    }.getOrDefault("--")

private fun segmentDurationSeconds(segment: ActivityTimelineSegment): Double =
    runCatching {
        ((Instant.parse(segment.end).toEpochMilli() - Instant.parse(segment.start).toEpochMilli()).coerceAtLeast(0)) / 1000.0
    }.getOrDefault(0.0)

private fun segmentDurationSeconds(segment: TimelineBlock): Double =
    Duration.between(segment.start, segment.end).seconds.coerceAtLeast(1).toDouble()

private data class TimelineBlock(
    val segmentIndex: Int?,
    val start: Instant,
    val end: Instant,
    val kind: String,
    val mouseMoves: Int = 0,
    val keyPresses: Int = 0
)

private fun timelineBlocks(
    employee: ActivityTimelineEmployee,
    rangeStart: Instant?,
    rangeEnd: Instant?
): List<TimelineBlock> {
    if (rangeStart == null || rangeEnd == null || !rangeEnd.isAfter(rangeStart)) {
        return employee.segments.mapIndexedNotNull { index, segment ->
            val start = parseInstant(segment.start) ?: return@mapIndexedNotNull null
            val end = parseInstant(segment.end) ?: return@mapIndexedNotNull null
            TimelineBlock(index, start, end, segment.kind, segment.mouseMoves, segment.keyPresses)
        }
    }
    val actualRangeStart = rangeStart!!
    val actualRangeEnd = rangeEnd!!

    val parsed = employee.segments.mapIndexedNotNull { index, segment ->
        val start = parseInstant(segment.start) ?: return@mapIndexedNotNull null
        val end = parseInstant(segment.end) ?: return@mapIndexedNotNull null
        if (!end.isAfter(actualRangeStart) || !start.isBefore(actualRangeEnd)) return@mapIndexedNotNull null
        TimelineBlock(
            segmentIndex = index,
            start = maxInstant(start, actualRangeStart),
            end = minInstant(end, actualRangeEnd),
            kind = segment.kind,
            mouseMoves = segment.mouseMoves,
            keyPresses = segment.keyPresses
        )
    }.filter { it.end.isAfter(it.start) }.sortedBy { it.start }

    if (parsed.isEmpty()) {
        return listOf(TimelineBlock(null, actualRangeStart, actualRangeEnd, "offline"))
    }

    val blocks = mutableListOf<TimelineBlock>()
    var cursor = actualRangeStart
    parsed.forEach { block ->
        if (block.start.isAfter(cursor)) {
            blocks += TimelineBlock(null, cursor, block.start, "offline")
        }
        blocks += block
        if (block.end.isAfter(cursor)) cursor = block.end
    }
    if (cursor.isBefore(actualRangeEnd)) {
        blocks += TimelineBlock(null, cursor, actualRangeEnd, "offline")
    }
    return blocks
}

private fun parseInstant(value: String?): Instant? =
    runCatching {
        if (value.isNullOrBlank()) null else Instant.parse(value)
    }.getOrNull()

private fun maxInstant(first: Instant, second: Instant): Instant =
    if (first.isAfter(second)) first else second

private fun minInstant(first: Instant, second: Instant): Instant =
    if (first.isBefore(second)) first else second

private fun formatDuration(seconds: Double): String {
    val total = seconds.toInt().coerceAtLeast(0)
    val hours = total / 3600
    val minutes = (total % 3600) / 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
