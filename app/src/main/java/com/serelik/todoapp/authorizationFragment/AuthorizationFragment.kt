package com.serelik.todoapp.authorizationFragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.databinding.FragmentAuthorizationBinding
import com.serelik.todoapp.list.TodoListFragment
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.strategy.LoginType

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private val binding by viewBinding(FragmentAuthorizationBinding::bind)

    private val tokenStorage by lazy { TokenStorage() }

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val sdk: YandexAuthSdk by lazy {
        YandexAuthSdk(
            requireContext(), YandexAuthOptions(
                context = requireContext(),
                loggingEnabled = true,
            )
        )
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val yandexAuthToken = sdk.extractToken(result.resultCode, result.data)
                    if (yandexAuthToken?.value != null) {
                        tokenStorage.saveToken(yandexAuthToken.value)
                        openListFragment()
                    }

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            requestAuth()
        }
    }

    private fun openListFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, TodoListFragment())
            .commit()
    }


    private fun requestAuth() {
        val loginOptionsBuilder = YandexAuthLoginOptions
            .Builder()
            .setLoginType(LoginType.NATIVE)

        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        resultLauncher.launch(intent)
    }

}