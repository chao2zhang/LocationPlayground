package com.example.locationplayground

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.locationplayground.ui.theme.LocationPlaygroundTheme

class MainActivity : ComponentActivity() {

    private lateinit var multiplePermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var singlePermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        multiplePermissionLauncher = registerForActivityResult(
            RequestMultiplePermissions(),
        ) { result ->
            Log.e(TAG, "Activity result: $result")
        }
        singlePermissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { result ->
            Log.e(TAG, "Activity result: $result")
        }
        setContent {
            LocationPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LocationContent(
                        requestFineLocation = {
                            request(true)
                        },
                        requestCoarseLocation = {
                            request(false)
                        }
                    )
                }
            }
        }

    }

    private fun request(
        requestFineLocation: Boolean
    ) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && requestFineLocation) {
            Log.e(TAG, "Fine location permission already granted")
            return
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && !requestFineLocation) {
            Log.e(TAG, "Coarse location permission already granted")
            return
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.e(TAG, "Show location rationale")
        }

        if (requestFineLocation) {
            multiplePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            singlePermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private companion object {
        private const val TAG = "Location"
    }
}

@Composable
fun LocationContent(
    requestFineLocation: () -> Unit,
    requestCoarseLocation: () -> Unit,
) {
    Column {
        Button(onClick = requestFineLocation, modifier = Modifier.wrapContentSize()) {
            Text("Request fine location")
        }
        Button(onClick = requestCoarseLocation, modifier = Modifier.wrapContentSize()) {
            Text("Request coarse location")
        }
    }
}
