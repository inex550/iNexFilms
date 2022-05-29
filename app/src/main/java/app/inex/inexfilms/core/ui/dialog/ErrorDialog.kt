package app.inex.inexfilms.core.ui.dialog

import androidx.core.view.isVisible
import app.inex.inexfilms.R
import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.common.ext.isNotNull
import app.inex.inexfilms.core.common.utils.uiLazy
import app.inex.inexfilms.core.ui.base.BaseDialogFragment
import app.inex.inexfilms.core.ui.ext.viewBinding
import app.inex.inexfilms.core.ui.ext.withArgs
import app.inex.inexfilms.databinding.DialogErrorBinding

class ErrorDialog: BaseDialogFragment(R.layout.dialog_error) {

    private val errorModel: ErrorModel by uiLazy {
        requireNotNull(requireArguments().getParcelable(ARG_ERROR_MODEL))
    }

    private val binding by viewBinding(DialogErrorBinding::bind)

    override fun setupUi() {
        with(binding) {
            codeTv.isVisible = errorModel.code.isNotNull()

            codeTv.text = errorModel.code.toString()
            messageTv.text = errorModel.message

            okBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        private const val ARG_ERROR_MODEL = "ARG_ERROR_MODEL"

        fun newInstance(errorModel: ErrorModel): ErrorDialog = ErrorDialog().withArgs {
            putParcelable(ARG_ERROR_MODEL, errorModel);
        }

        fun newInstance(message: String): ErrorDialog = newInstance(
            errorModel = ErrorModel(code = null, message = message)
        )
    }
}