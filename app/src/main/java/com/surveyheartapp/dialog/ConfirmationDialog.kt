package com.surveyheartapp.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.surveyheartapp.R
import com.surveyheartapp.databinding.ConfirmationDialogBinding


class ConfirmationDialog(var activity: Activity) {
    var dialog: Dialog? = Dialog(activity)
    var binding: ConfirmationDialogBinding? = null
    fun show() {
        try {
            if (dialog != null) {
                dialog!!.show()
            } else {
                ConfirmationDialog(activity)
                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isShowing(): Boolean {
        try {
            if (dialog != null) {
                return dialog!!.isShowing
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun dismiss() {
        try {
            if (dialog != null && !activity.isFinishing) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        binding = DataBindingUtil.inflate(
            activity.layoutInflater,
            R.layout.confirmation_dialog,
            null,
            false
        )
        dialog?.setContentView(binding?.root!!)
        // Get the layout params of the CardView
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = WindowManager.LayoutParams.WRAP_CONTENT
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setLayout(width, height)

        dialog!!.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}