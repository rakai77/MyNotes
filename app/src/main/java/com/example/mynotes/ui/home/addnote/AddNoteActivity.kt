package com.example.mynotes.ui.home.addnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mynotes.R
import com.example.mynotes.data.Resource
import com.example.mynotes.databinding.ActivityAddNoteBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private val viewModel by viewModels<AddNoteViewModel>()

    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        initObserver()
    }

    private fun setupAction() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getToken.collect {
                    token = it
                    Log.d("Check Token", token)
                }
            }
        }
        binding.apply {
            btnAddNotes.setOnClickListener {
                viewModel.addNotes(
                    "Bearer $token",
                    title = edtTitle.text.toString(),
                    content = edtContent.text.toString(),
                )
            }
        }
    }

    private fun initObserver() {
        viewModel.addNotes.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Added note successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Failed to add, please try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}