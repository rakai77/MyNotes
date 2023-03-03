package com.example.mynotes.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.R
import com.example.mynotes.data.Resource
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.detail.DetailNoteActivity
import com.example.mynotes.ui.detail.DetailNoteActivity.Companion.DIARY_ID
import com.example.mynotes.ui.home.adapter.ListNotesAdapter
import com.example.mynotes.ui.home.addnote.AddNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private var token: String = ""

    private lateinit var adapter: ListNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(requireActivity(), AddNoteActivity::class.java))
        }

        getToken()
        setupRecyclerView()
        initObserver()
    }

    private fun getToken() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getToken.collect {
                    token = it
                    Log.d("Check token", token)
                }
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllNotes("Bearer $token")
            }
        }
        viewModel.listNotes.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Resource.Loading -> {
                    binding.apply {
                        shimmerNotes.root.startShimmer()
                        shimmerNotes.root.visibility = View.VISIBLE
                        fabAddNote.visibility = View.GONE
                        rvNotes.visibility = View.GONE
                        emptyState.root.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    if (result.data.data.isNotEmpty()) {
                        binding.apply {
                            shimmerNotes.root.stopShimmer()
                            shimmerNotes.root.visibility = View.GONE
                            rvNotes.visibility = View.VISIBLE
                            fabAddNote.visibility = View.VISIBLE
                            emptyState.root.visibility = View.GONE
                            adapter.submitList(result.data.data)
                        }
                    } else {
                        binding.apply {
                            shimmerNotes.root.stopShimmer()
                            shimmerNotes.root.visibility = View.GONE
                            rvNotes.visibility = View.GONE
                            fabAddNote.visibility = View.GONE
                            emptyState.root.visibility = View.VISIBLE
                        }
                    }

                }
                is Resource.Error -> {
                    binding.apply {
                        shimmerNotes.root.stopShimmer()
                        shimmerNotes.root.visibility = View.GONE
                        rvNotes.visibility = View.GONE
                        fabAddNote.visibility = View.GONE
                        emptyState.root.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            adapter = ListNotesAdapter(
                onClickItem = {
                    val intent = Intent(requireContext(), DetailNoteActivity::class.java)
                    intent.putExtra(DIARY_ID, it)
                    startActivity(intent)
                }
            )
            rvNotes.adapter = adapter
            rvNotes.layoutManager = LinearLayoutManager(requireContext())
            rvNotes.setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}