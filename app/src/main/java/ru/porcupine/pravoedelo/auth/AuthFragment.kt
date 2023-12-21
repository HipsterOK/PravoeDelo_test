package ru.porcupine.pravoedelo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.porcupine.pravoedelo.R
import ru.porcupine.pravoedelo.api.ApiService
import ru.porcupine.pravoedelo.databinding.FragmentAuthBinding
import ru.porcupine.pravoedelo.network.Either
import ru.porcupine.pravoedelo.network.RetrofitClient

class AuthFragment() : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val viewModel = AuthViewModel(apiService)

        binding.getCodeBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.toString()
            viewModel.getCode(phoneNumber)
        }

        binding.getTokenBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.toString()
            val password = binding.passwordEt.text.toString()
            viewModel.getToken(phoneNumber, password)
        }

        binding.regenerateCodeBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.toString()
            viewModel.regenerateCode(phoneNumber)
        }


        viewModel.codeResponse.observe(viewLifecycleOwner) { either ->
            if (either is Either.Success) {
                val codeResponse = either.value
                if (codeResponse.status == "new") {
                    Toast.makeText(requireContext(), "Код отправлен в СМС", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Вы уже зарегистрированы. Введите Ваш код.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.getTokenBtn.visibility = View.VISIBLE
                binding.passwordEt.visibility = View.VISIBLE
            } else if (either is Either.Failure) {
                val errorResponse = either.error
                Toast.makeText(requireContext(), errorResponse.error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.regenerateCodeResponse.observe(viewLifecycleOwner) { either ->
            if (either is Either.Success) {
                Toast.makeText(requireContext(), "Код отправлен в СМС", Toast.LENGTH_SHORT).show()
            } else if (either is Either.Failure) {
                val errorResponse = either.error
                Toast.makeText(requireContext(), errorResponse.error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.tokenResponse.observe(viewLifecycleOwner) { either ->
            if (either is Either.Success) {
                val token = either.value
                val phoneNumber = binding.phoneNumberEt.text.toString()

                val bundle = Bundle().apply {
                    putString("phoneNumber", phoneNumber)
                    putString("token", token.string())
                }
                Navigation.findNavController(view)
                    .navigate(R.id.action_authFragment_to_mainFragment, bundle)
                Toast.makeText(requireContext(), "Успех!", Toast.LENGTH_SHORT).show()
            } else if (either is Either.Failure) {
                val errorResponse = either.error
                Toast.makeText(requireContext(), errorResponse.error, Toast.LENGTH_SHORT).show()
                binding.regenerateCodeBtn.visibility = View.VISIBLE
            }
        }

    }
}
