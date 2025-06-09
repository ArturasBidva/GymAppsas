package com.example.gymappsas.ui.screens.uploadimage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadImageScreen(
    popStackBack: () -> Unit,
    navigateToCameraFragment: () -> Unit) {
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Content(
        popStackBack = popStackBack,
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        navigateToCameraFragment = navigateToCameraFragment
    )
}

@Composable
private fun Content(
    popStackBack: () -> Unit,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navigateToCameraFragment: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                "Choose a photo or video",
                fontFamily = lexenBold,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 20.dp)
            )
            TakeImageScreenItem("Take Photo Or Video",hasPermission,onRequestPermission, navigateToCameraFragment = navigateToCameraFragment)
            UploadImageScreenItem(
                text = "Upload From Photos",
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TakeImageScreenItem(
    text: String,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navigateToCameraFragment: () -> Unit
) {
    // State to control the visibility of the NoPermissionScreen
    var showNoPermissionScreen by remember { mutableStateOf(false) }

    if (showNoPermissionScreen) {
        // Display NoPermissionScreen if permissions are not granted
        NoPermissionScreen(onRequestPermission = {
            onRequestPermission()
            showNoPermissionScreen = false  // Hide NoPermissionScreen once permission is requested
        })
    } else {
        // Main content of TakeImageScreenItem
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    if (hasPermission) {
                        navigateToCameraFragment()
                    } else {
                        showNoPermissionScreen = true
                    }
                }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text, fontFamily = lexendRegular, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Back",
                    tint = Color(0xFF0d141c)
                )
            }
        }
    }
}

@Composable
fun UploadImageScreenItem(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontFamily = lexendRegular, fontSize = 16.sp)
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Back",
                tint = Color(0xFF0d141c)
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        popStackBack = {}, navigateToCameraFragment = {}, onRequestPermission = {}, hasPermission = false)
}
@Composable
fun NoPermissionScreen(
    onRequestPermission: () -> Unit
) {

    NoPermissionContent(
        onRequestPermission = onRequestPermission
    )
}

@Composable
private fun NoPermissionContent(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Please grant the permission to use the camera to use the core functionality of this app.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Icon(imageVector = Icons.Default.Call, contentDescription = "Camera")
            Text(text = "Grant permission")
        }
    }
}

@Preview
@Composable
private fun Preview_NoPermissionContent() {
    NoPermissionContent(
        onRequestPermission = {}
    )
}