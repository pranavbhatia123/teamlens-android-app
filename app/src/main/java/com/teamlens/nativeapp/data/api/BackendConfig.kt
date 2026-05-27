package com.teamlens.nativeapp.data.api

import com.teamlens.nativeapp.BuildConfig

object BackendConfig {
    val baseUrl: String = BuildConfig.API_BASE_URL.trimEnd('/').removeSuffix("/api")
    val webBaseUrl: String = BuildConfig.WEB_BASE_URL.trimEnd('/')

    fun screenshotUrl(id: String): String = "$baseUrl/api/agent/screenshots/$id"

    fun liveViewUrl(token: String, userId: String? = null): String {
        val employeePart = userId?.takeIf { it.isNotBlank() }?.let { "&employeeId=$it" }.orEmpty()
        return "$webBaseUrl/mobile-live?mobileToken=$token$employeePart&mobileApiBase=$baseUrl&mobileWsBase=$baseUrl"
    }
}
