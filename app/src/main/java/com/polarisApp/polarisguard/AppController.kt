package com.polarisApp.polarisguard

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class AppController : Application() {
    var requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                field = Volley.newRequestQueue(getApplicationContext())
            }
            return field
        }
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun <T> addToRequestQueue(req: Request<T?>?) {
        this.requestQueue!!.add<T?>(req)
    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}
