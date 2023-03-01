package com.example.mynotes.ui.authentication.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.data.Resource
import com.example.mynotes.databinding.FragmentRegisterBinding
import com.example.mynotes.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        setupAction()
    }

    private fun setupForm() : Boolean{
        var form = false

        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmailRegister.text.toString()
        val password = binding.edtPasswordRegister.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()

        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "Please fill your first name."
            }
            email.isEmpty() -> {
                binding.tflEmailRegister.error = "Please fill your email address."
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tflEmailRegister.error = "Please enter a valid email address."
            }
            password.isEmpty() -> {
                binding.tflPasswordRegister.error = "Please enter your password."
            }
            password.length < 6 -> {
                binding.tflPasswordRegister.error = "Password length must be 6 character."
            }
            confirmPassword.isEmpty() -> {
                binding.edtConfirmPassword.error = "Please fill your last name."
            }
            else -> form = true
        }
        return form
    }

    private fun initObserver() {
        viewModel.registerState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Register Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is Resource.Error -> {
                    binding.progressbar.visibility = View.GONE
                    //TODO : Handle error response `422` not implemented yet
                    Toast.makeText(
                        requireContext(),
                        "The password confirmation does not match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (setupForm()) {
                    viewModel.register(
                        username = edtUsername.text.toString(),
                        email = edtEmailRegister.text.toString(),
                        password = edtPasswordRegister.text.toString(),
                        confirmPassword = edtConfirmPassword.text.toString()
                    )
                }
            }
            binding.btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}