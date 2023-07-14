package com.serelik.todoapp.ui.authorization

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.data.workers.SyncListTodoWorker
import com.serelik.todoapp.data.workers.WorkRepository
import com.serelik.todoapp.databinding.FragmentAuthorizationBinding
import com.serelik.todoapp.di.AuthorizationFragmentComponent
import com.serelik.todoapp.ui.MainActivity
import com.serelik.todoapp.ui.list.TodoListFragment
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.strategy.LoginType
import javax.inject.Inject

/**  Fragment for authorization*/
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private val binding by viewBinding(FragmentAuthorizationBinding::bind)

    @Inject
    lateinit var tokenStorage: TokenStorage

    lateinit var component: AuthorizationFragmentComponent

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val sdk: YandexAuthSdk by lazy {
        YandexAuthSdk(
            requireContext(),
            YandexAuthOptions(
                context = requireContext(),
                loggingEnabled = true
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

                        planUpdateList()
                        openListFragment()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .authorizationFragmentComponent().create()

        component.inject(this)
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

    private fun planUpdateList() {
        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                SyncListTodoWorker.PERIODICAL_TAG,
                ExistingPeriodicWorkPolicy.UPDATE,
                WorkRepository.loadListRequestPeriodical()
            )
    }
}
