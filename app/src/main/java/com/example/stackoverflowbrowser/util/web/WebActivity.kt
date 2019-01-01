package pl.wp.videostar.viper.web

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.stackoverflowbrowser.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    private val url: String
        get() = intent?.extras?.getString(URL_EXTRA) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webView.loadUrl(url)
    }

}
