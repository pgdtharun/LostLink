package com.lostlink.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val GitHubIcon: ImageVector
    get() = ImageVector.Builder(
        name = "GitHub",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = androidx.compose.ui.graphics.SolidColor(Color.Black)) {
        moveTo(12f, 2f)
        curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
        curveTo(2f, 16.417f, 4.867f, 20.162f, 8.832f, 21.482f)
        curveTo(9.332f, 21.574f, 9.515f, 21.265f, 9.515f, 21.001f)
        curveTo(9.515f, 20.766f, 9.506f, 20.142f, 9.501f, 19.317f)
        curveTo(6.727f, 19.919f, 6.142f, 17.982f, 6.142f, 17.982f)
        curveTo(5.689f, 16.831f, 5.036f, 16.525f, 5.036f, 16.525f)
        curveTo(4.131f, 15.907f, 5.105f, 15.919f, 5.105f, 15.919f)
        curveTo(6.106f, 15.989f, 6.632f, 16.947f, 6.632f, 16.947f)
        curveTo(7.521f, 18.47f, 8.966f, 18.03f, 9.535f, 17.775f)
        curveTo(9.626f, 17.131f, 9.883f, 16.692f, 10.167f, 16.443f)
        curveTo(7.953f, 16.191f, 5.625f, 15.334f, 5.625f, 11.512f)
        curveTo(5.625f, 10.424f, 6.014f, 9.534f, 6.651f, 8.837f)
        curveTo(6.548f, 8.585f, 6.206f, 7.568f, 6.749f, 6.191f)
        curveTo(6.749f, 6.191f, 7.586f, 5.923f, 9.494f, 7.215f)
        curveTo(10.289f, 7.001f, 11.144f, 6.894f, 11.994f, 6.89f)
        curveTo(12.844f, 6.894f, 13.699f, 7.001f, 14.496f, 7.215f)
        curveTo(16.402f, 5.923f, 17.238f, 6.191f, 17.238f, 6.191f)
        curveTo(17.782f, 7.568f, 18.44f, 8.585f, 18.338f, 8.837f)
        curveTo(18.976f, 9.534f, 19.363f, 10.424f, 19.363f, 11.512f)
        curveTo(19.363f, 15.344f, 17.031f, 16.188f, 14.811f, 16.435f)
        curveTo(15.169f, 16.743f, 15.488f, 17.352f, 15.488f, 18.284f)
        curveTo(15.488f, 19.626f, 15.476f, 20.711f, 15.476f, 21.036f)
        curveTo(15.476f, 21.302f, 15.657f, 21.614f, 16.166f, 21.512f)
        curveTo(20.127f, 20.188f, 23f, 16.433f, 23f, 12f)
        curveTo(23f, 6.477f, 18.523f, 2f, 12.994f, 2f)
        close()
    }.build()
