package ch.bfh.mad.eazytime.util

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ch.bfh.mad.R


class GeoFenceDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_info, null)
            val checkBox = view.findViewById<CheckBox>(R.id.cb_info_dialog)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) storeDialogStatus(true) else storeDialogStatus(false)
            }
            val buttonOk = view.findViewById<Button>(R.id.btn_info_dialog)
            buttonOk.setOnClickListener { dismiss() }
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun storeDialogStatus(checked: Boolean) {
        val prefs = requireActivity().getSharedPreferences("ch.bfh.mad.eazytime", MODE_PRIVATE)
        prefs.edit().putBoolean("ignorePowerSafe", checked).apply()
    }

}