package intelin.vn.DemoPushAPNs.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}