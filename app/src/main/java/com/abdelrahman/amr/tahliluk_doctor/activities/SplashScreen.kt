package com.abdelrahman.amr.tahliluk_doctor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivitySplashScreenBinding
import com.abdelrahman.amr.tahliluk_doctor.viewModels.SplashScreenViewModel
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class SplashScreen : AppCompatActivity() {
    private lateinit var mSplashScreenBinding: ActivitySplashScreenBinding
    private lateinit var mSplashScreenViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mSplashScreenBinding.root)
        mSplashScreenViewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]


        lifecycleScope.launchWhenResumed {
            setAnimation()
        }

        lifecycleScope.launchWhenResumed {
            delay(3800)
            moveToSignIn()
        }
    }


    private fun setAnimation() {
        val appName: Animation = AnimationUtils.loadAnimation(this, R.anim.app_name)
        val description: Animation = AnimationUtils.loadAnimation(this, R.anim.description)
        mSplashScreenBinding.tvDescription.startAnimation(description)
        mSplashScreenBinding.tvAppName.startAnimation(appName)
    }

    private suspend fun moveToSignIn() {
        mSplashScreenViewModel.getSignInState(application).collect { state ->
            if (state) {
                SupportClass.startActivityWithFlag(this, MainActivity::class.java)

            } else {
                SupportClass.startActivityWithFlag(this, SignInActivity::class.java)
            }
            finish()
        }
    }
}