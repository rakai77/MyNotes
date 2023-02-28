package com.example.mynotes.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.example.mynotes.data.Resource
import com.example.mynotes.data.remote.response.LoginErrorResponse
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.databinding.FragmentLoginBinding
import com.example.mynotes.ui.authentication.AuthViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        setupAction()
    }

    private fun setupForm() : Boolean{
        var form = false

        val email = binding.edtEmailLogin.text.toString()
        val password = binding.edtPasswordLogin.text.toString()

        when {
            email.isEmpty() -> {
                binding.tflEmailLogin.error = "Please fill your email address."
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tflEmailLogin.error = "Please enter a valid email address."
            }
            password.isEmpty() -> {
                binding.tflPassword.error = "Please enter your password."
            }
            password.length < 6 -> {
                binding.tflPassword.error = "Password length must be 6 character."
            }
            else -> form = true
        }
        return form
    }

    private fun initObserver() {
        viewModel.loginState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Login Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
                is Resource.Error -> {
                    binding.progressbar.visibility = View.GONE
                    val errors = result.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, LoginErrorResponse::class.java)

//                    Toast.makeText(
//                        requireContext(),
//                        errorResponse.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (setupForm()) {
                    viewModel.login(
                        email = edtEmailLogin.text.toString(),
                        password = edtPasswordLogin.text.toString(),
                    )
                }
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}