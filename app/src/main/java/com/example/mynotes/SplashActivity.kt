package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mynotes.databinding.ActivitySplashBinding
import com.example.mynotes.ui.authentication.AuthActivity
import com.example.mynotes.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromBottomAnim = AnimationUtils.loadAnimation(this, R.anim.from_bottom)
        binding.ivSplash.startAnimation(fromBottomAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getToken.collect {
                        if (it.isNotEmpty()) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        } else {
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    AuthActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
            }
        }, 3000)
    }
}