package com.zahid.mathly

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zahid.mathly.data.local.SessionManager
import com.zahid.mathly.presentation.navigation.MathlyNavigation
import com.zahid.mathly.presentation.ui.screens.auth.SplashScreen
import com.zahid.mathly.presentation.ui.theme.MathlyTheme
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import com.zahid.mathly.presentation.viewmodel.ThemeViewModel
import com.zahid.mathly.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.zahid.mathly.presentation.ui.theme.md_theme_dark_onPrimary
import com.zahid.mathly.presentation.ui.theme.md_theme_dark_primary
import com.zahid.mathly.presentation.ui.theme.md_theme_light_onPrimary
import com.zahid.mathly.presentation.ui.theme.md_theme_light_primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var appUpdateManager: AppUpdateManager
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(this, "Update flow failed! Result code: ${result.resultCode}", Toast.LENGTH_SHORT).show()
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                md_theme_light_primary.hashCode(),    // icon color (dark icons)
            ),
            navigationBarStyle = SystemBarStyle.dark(
                md_theme_light_primary.hashCode(), // background color of nav bar
            )
        )

        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()

            MathlyTheme(darkTheme = themeViewModel.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    
                    if (showSplash) {
                        SplashScreen(
                            onSplashComplete = {
                                showSplash = false
                            }
                        )
                    } else {

                        checkForUpdate()
                        MathlyNavigation(
                            themeViewModel = themeViewModel,
                            languageViewModel = languageViewModel,
                            sessionManager = sessionManager
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    resultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
            }
        }
    }

    
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("selected_language", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, savedLanguage))
    }

    fun checkForUpdate(){
        appUpdateManager.registerListener(installUpdateStateListener)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            when {
                // Case 1: Update already in progress → resume it
                appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        resultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                            .build()
                    )
                }

                // Case 2: New update available → go to update screen
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        resultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                            .setAllowAssetPackDeletion(true)
                            .build()
                    )
                }
            }
        }

        appUpdateInfoTask.addOnFailureListener {
            Toast.makeText(this, "Update check failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private val installUpdateStateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED){
            Toast.makeText(this, "App updated successfully", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                delay(4.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(installUpdateStateListener)
    }
}