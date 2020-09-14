package dany.hilt.sample

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SampleApp : Application() {
    @Inject
    lateinit var authenticator: Authenticator

    override fun onCreate() {
        super.onCreate()

        Log.d("DANY", authenticator.toString())
    }
}