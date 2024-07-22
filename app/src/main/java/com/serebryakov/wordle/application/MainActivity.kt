package com.serebryakov.wordle.application

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.serebryakov.wordle.R
import com.serebryakov.wordle.application.view.mainscreen.MainScreenFragment
import com.serebryakov.wordle.databinding.MainActivityBinding
import com.serebryakov.wordle.foundation.ActivityScopeViewModel
import com.serebryakov.wordle.foundation.navigator.FragmentNavigator
import com.serebryakov.wordle.foundation.navigator.IntermediateNavigator
import com.serebryakov.wordle.foundation.tools.viewModelCreator
import com.serebryakov.wordle.foundation.uiactions.AndroidUiActions
import com.serebryakov.wordle.foundation.views.FragmentsHolder

typealias SavingCallback = (uri: Uri) -> Unit

class MainActivity : AppCompatActivity(), FragmentsHolder {

    private lateinit var navigator: FragmentNavigator
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private lateinit var resultLauncherHistorySaving: ActivityResultLauncher<Intent>
    private lateinit var resultLauncherStatsSaving: ActivityResultLauncher<Intent>

    lateinit var historySavingCallback: SavingCallback
    lateinit var statsSavingCallback: SavingCallback

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUiActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        resultLauncherHistorySaving = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                historySavingCallback(data!!.data!!)
            }
        }

        resultLauncherStatsSaving = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                statsSavingCallback(data!!.data!!)
            }
        }

        navigator = FragmentNavigator(
            activity = this,
            containerId = R.id.container,
            initialScreenCreator = { MainScreenFragment.Screen() }
        )

        navigator.onCreate(savedInstanceState)
    }

    fun createFile(fileType: String, fileName: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = fileType
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        if (requestCode == TYPE_STATS_SAVING)
            resultLauncherStatsSaving.launch(intent)
        if (requestCode == TYPE_HISTORY_SAVING)
            resultLauncherHistorySaving.launch(intent)
    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        activityViewModel.navigator.setTarget(null)
    }


    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }

    override fun onBackPressed() {
        navigator.onBackPressed()
        super.onBackPressed()
    }

    val TYPE_HISTORY_SAVING = 1
    val TYPE_STATS_SAVING = 2
}