package com.app.incroyable.lockscreen.activity

import android.content.Intent
import com.app.incroyable.lockscreen.apihelper.APIClient
import com.app.incroyable.lockscreen.apihelper.APIInterface
import com.app.incroyable.lockscreen.apihelper.decodeUrl
import com.app.incroyable.lockscreen.apihelper.updateUrl
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivitySplashBinding
import com.app.incroyable.lockscreen.model.MultipleResource
import com.app.incroyable.lockscreen.storage.clearAdPreferences
import com.app.incroyable.lockscreen.storage.resetCounter
import com.app.incroyable.lockscreen.storage.saveWallpaperList
import com.app.incroyable.lockscreen.storage.setAdPreferences
import com.app.incroyable.lockscreen.utilities.currentVersion
import com.app.incroyable.lockscreen.utilities.firstCharColor
import com.app.incroyable.lockscreen.utilities.forceUpdateDialog
import com.app.incroyable.lockscreen.utilities.networkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    override fun setBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@SplashActivity
    }

    override fun initView() {
        super.initView()
        resetCounter()
        setMainTitle()
        startCall()
    }

    private fun setMainTitle() {
        with(mBinding) {
            txtTitle.text = firstCharColor(txtTitle.text.toString())
        }
    }

    private fun startCall() {
        networkError(
            onNetworkAvailable = {
//                apiCall()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1500)
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            StartActivity::class.java
                        )
                    )
                    finish()
                }
            },
            negativeClickListener = {
                finishAffinity()
            }
        )
    }

    private var latestVersionCode: Int = 0
    private var currentVersionCode: Int = 0

    private fun apiCall() {
        clearAdPreferences()
        currentVersionCode = currentVersion()
        val apiInterface = APIClient.client?.create(APIInterface::class.java)
        val url = decodeUrl(updateUrl)
        val call: Call<MultipleResource> = apiInterface!!.doGetListResources(url)
        call.enqueue(object : Callback<MultipleResource> {
            override fun onResponse(
                call: Call<MultipleResource>,
                response: Response<MultipleResource>
            ) {


//                val response = response.body()
//                response?.let {
//                    setAdPreferences(response.data)
//                    saveWallpaperList(response.wallpaper)
//                    latestVersionCode = response.version
//
//                    if (latestVersionCode != 0 && currentVersionCode < latestVersionCode)
//                        forceUpdateDialog(response.title, response.msg, response.packageName)
//                    else {
//
//                    }
//                }
            }

            override fun onFailure(call: Call<MultipleResource>, t: Throwable) {
                call.cancel()
                startCall()
            }
        })
    }
}