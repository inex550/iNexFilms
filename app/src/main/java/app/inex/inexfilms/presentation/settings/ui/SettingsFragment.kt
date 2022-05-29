package app.inex.inexfilms.presentation.settings.ui

import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.prefs.SettingsPreferences
import app.inex.inexfilms.core.ui.base.BaseFragment
import app.inex.inexfilms.core.ui.base.BaseMvvmFragment
import app.inex.inexfilms.core.ui.dialog.ErrorDialog
import app.inex.inexfilms.core.ui.ext.bgOnFocus
import app.inex.inexfilms.core.ui.ext.renderIn
import app.inex.inexfilms.core.ui.ext.viewBinding
import app.inex.inexfilms.core.ui.ext.viewModels
import app.inex.inexfilms.databinding.FragmentSettingsBinding
import app.inex.inexfilms.presentation.settings.vm.SettingsViewAction
import app.inex.inexfilms.presentation.settings.vm.SettingsViewModel
import app.inex.inexfilms.presentation.settings.vm.SettingsViewState

class SettingsFragment: BaseMvvmFragment<SettingsViewState, SettingsViewAction, SettingsViewModel>(
    layoutResId = R.layout.fragment_settings
) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override val viewModel: SettingsViewModel by viewModels {
        App.component.settingsViewModel()
    }

    override fun setupUi() {
        with(binding) {
            makeEditTextFocusable(baseUriEt)
            makeEditTextFocusable(usefulUriEt)

            refreshUsefulUriBtn.setOnClickListener {
                viewModel.refreshUri()
            }

            saveBtn.setOnClickListener {
                viewModel.saveUriSettings(
                    baseUri = binding.baseUriEt.text.toString(),
                    usefulUri = binding.usefulUriEt.text.toString()
                )
            }
        }
    }

    override fun proceedAction(action: SettingsViewAction) {
        when(action) {
            is SettingsViewAction.DialogError -> ErrorDialog.newInstance(errorModel = action.errorModel)
                .show(childFragmentManager, null)

            is SettingsViewAction.ToastShort -> Toast.makeText(requireContext(), action.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setupRender() {
        with(viewModel.uiState) {
            renderIn(lifecycle, { it.refreshLoading }, ::renderRefreshLoading)
            renderIn(lifecycle, { it.baseUri }, ::renderBaseUri)
            renderIn(lifecycle, { it.usefulUri }, ::renderUsefulUri)
        }
    }

    private fun renderRefreshLoading(refreshLoading: Boolean) {
        with(binding) {
            usefulUriEt.isEnabled = refreshLoading.not()
            refreshUsefulUriLoadingPb.isVisible = refreshLoading
            refreshUsefulUriTextTv.isInvisible = refreshLoading
            refreshUsefulUriBtn.isEnabled = refreshLoading.not()
        }
    }

    private fun renderBaseUri(baseUri: String) {
        binding.baseUriEt.setText(baseUri)
    }

    private fun renderUsefulUri(usefulUri: String) {
        binding.usefulUriEt.setText(usefulUri)
    }

    private fun makeEditTextFocusable(editText: EditText) {
        editText.bgOnFocus(
            bgHasFocus = R.drawable.bg_border_color_primary_corners_8,
            bgNotFocus = R.drawable.bg_border_color_grey_corners_8,
        )
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }
}