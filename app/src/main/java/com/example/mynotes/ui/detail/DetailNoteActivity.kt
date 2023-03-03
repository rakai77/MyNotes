package com.example.mynotes.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.DetailNotesResponse
import com.example.mynotes.databinding.ActivityDetailNoteBinding
import com.example.mynotes.ui.detail.update.UpdateNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNoteBinding

    private val viewModel by viewModels<DetailNoteViewModel>()

    private var diaryId: String = ""
    private var token: String = ""
    private lateinit var dataItem: DetailNotesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val noteId = intent.getStringExtra(DIARY_ID)
        diaryId = noteId!!
        if (diaryId.isEmpty()) {
            val data: Uri? = intent?.data
            val id = data?.getQueryParameter("diary_id")
            if (id != null) {
                diaryId = id.toString()
            }
        }

        binding.btnEditNote.setOnClickListener {
            val intent = Intent(this@DetailNoteActivity, UpdateNoteActivity::class.java)
            intent.putExtra(DIARY_ID, diaryId)
            startActivity(intent)
        }

        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getToken.collect {
                        token = it
                        Log.d("check token", token)
                    }
                }
                launch {
                    initViewModel()
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.getDetailNotes("Bearer $token", diaryId)
        viewModel.detailNotes.observe(this@DetailNoteActivity) { result ->
            when(result) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    result.data.let { dataItem = it }
                    initData(result.data)

                }
                is Resource.Error -> {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }

    private fun initData(dataItem: DetailNotesResponse) {
        with(binding) {
            tvTitle.text = dataItem.title
            tvContent.text = dataItem.content
            tvDateUpdated.text = dataItem.updatedAt
            tvDateCreated.text = dataItem.createdAt
        }
    }

    companion object {
        const val DIARY_ID = "diary_id"
    }
}