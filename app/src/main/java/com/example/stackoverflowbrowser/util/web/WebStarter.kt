package pl.wp.videostar.viper.web

import android.content.Context
import android.content.Intent

const val URL_EXTRA = "URL_EXTRA"

class WebStarter {

    fun start(context: Context, url: String) =
        Intent(context, WebActivity::class.java)
            .apply { putExtra(URL_EXTRA, url) }
            .let(context::startActivity)
}
