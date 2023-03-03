package com.example.mynotes.ui.detail.update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mynotes.MainActivity
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.UpdateNoteResponse
import com.example.mynotes.databinding.ActivityUpdateNoteBinding
import com.example.mynotes.ui.detail.DetailNoteActivity.Companion.DIARY_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding

    private val viewModel by viewModels<UpdateNoteViewModel>()

    private var diaryId: String = ""
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val noteId = intent.getStringExtra(DIARY_ID)
        if (noteId != null) {
            diaryId = noteId
        }
        if (diaryId.isEmpty()) {
            val data: Uri? = intent?.data
            val id = data?.getQueryParameter("diary_id")
            if (id != null) {
                diaryId = id.toString()
            }
        }

        initObserver()
        setupAction()

    }

    private fun setupAction() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getToken.collect {
                    token = it
                    Log.d("Check Token", token)
                }
                binding.apply {
                    btnUpdateNote.setOnClickListener {
                        viewModel.updateNotes(
                            "Bearer $token",
                            diaryId,
                            title = edtUpdateTitle.text.toString(),
                            content = edtUpdateContent.text.toString(),
                        )
                        Log.d("check diary id", diaryId)
                    }
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.updateNotes.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Update note successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@UpdateNoteActivity, MainActivity::class.java))
                    finishAffinity()
                }
                is Resource.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Failed to update, please try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}