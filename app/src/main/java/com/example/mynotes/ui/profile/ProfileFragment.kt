package com.example.mynotes.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mynotes.databinding.FragmentProfileBinding
import com.example.mynotes.ui.authentication.AuthActivity
import com.example.mynotes.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        getUserData()
        logout()

        return binding.root
    }

    private fun getUserData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getUsername.collect {
                        binding.tvUsername.text = it
                        Log.d("Check username", it)
                    }
                }
                launch {
                    viewModel.getEmail.collect {
                        binding.tvEmailUser.text = it
                        Log.d("Check email user", it)
                    }
                }
            }
        }
    }

    private fun logout() {
        binding.tflLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Toast.makeText(
                requireContext(),
                "Logout Successfully",
                Toast.LENGTH_LONG
            ).show()
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}