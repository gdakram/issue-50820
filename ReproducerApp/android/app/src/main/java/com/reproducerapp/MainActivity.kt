package com.reproducerapp

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Arguments
import com.facebook.react.jstasks.HeadlessJsTaskConfig
import com.facebook.react.jstasks.HeadlessJsTaskContext

class MainActivity : ReactActivity() {

    private var JSTaskID: Int? = null
    private fun startJSTask() {
        val taskConfig = HeadlessJsTaskConfig(
            taskKey = "NFL_KEEP_EXECUTING_ASYNC_JS_IN_PIP",
            timeout = 0,
            data = Arguments.createMap(),
            isAllowedInForeground = true
        )
        HeadlessJSModule.currentContext?.let {
            val headlessJsTaskContext = HeadlessJsTaskContext.getInstance(it)
            JSTaskID = headlessJsTaskContext.startTask(taskConfig)
        }
    }

    private fun stopJSTask() {
        HeadlessJSModule.currentContext?.let {
            JSTaskID?.let { id ->
                val headlessJsTaskContext = HeadlessJsTaskContext.getInstance(it)
                headlessJsTaskContext.finishTask(id)
                JSTaskID = null
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration,
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        // needed so that async JS code keeps executing on the main thread
        if (isInPictureInPictureMode) {
            startJSTask()
        } else {
            stopJSTask()
        }
    }

  override fun onUserLeaveHint() {
      super.onUserLeaveHint()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          enterPipMode()
      }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun enterPipMode() {
      val aspectRatio = Rational(16, 9)
      val pipBuilder = PictureInPictureParams.Builder()
          .setAspectRatio(aspectRatio)
          .build()
      enterPictureInPictureMode(pipBuilder)
  }

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "ReproducerApp"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
