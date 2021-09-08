package com.example.mems.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mems.R
import com.example.mems.model.BucketListItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_add_bucket_list_item.view.*

class AddBucketListItemDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val nullParent: ViewGroup? = null
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_bucket_list_item, nullParent)
        dialogView.buttonPositive.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val generatedId: String = databaseReference.push().key!!
            databaseReference.child(resources.getString(R.string.firebase_bucket_list)).child(generatedId)
                .setValue(
                    BucketListItem(
                        generatedId,
                        dialogView.itemName.editableText.toString(), false, String()
                    )
                )
            dismiss()
        }
        dialogView.buttonNegative.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireActivity()).setView(dialogView).create()
    }
}