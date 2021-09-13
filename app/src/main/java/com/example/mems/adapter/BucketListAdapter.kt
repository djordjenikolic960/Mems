package com.example.mems.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mems.R
import com.example.mems.model.BucketListItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BucketListAdapter(private val dataSet: ArrayList<BucketListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_bucket_list_item, parent, false)
        databaseReference = FirebaseDatabase.getInstance().reference
        return BucketItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = dataSet[position]
        (holder as BucketItemViewHolder).checkBox.isChecked = current.checked
        holder.bucketItemLabel.setText(current.name)
        holder.bucketItemLabel.isEnabled = false
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            databaseReference.child(holder.itemView.context.resources.getString(R.string.firebase_bucket_list)).child(current.id!!).child("checked")
                .setValue(isChecked)
        }
        holder.bucketItemMore.setOnClickListener {
            val popupWindow = getPopupWindow(it.context)
            popupWindow.showAsDropDown(it, 0, 0)
            popupWindow.contentView.findViewById<LinearLayout>(R.id.popupWindowDeleteBucketItem).setOnClickListener {
                databaseReference.child(holder.itemView.context.resources.getString(R.string.firebase_bucket_list)).child(current.id!!).removeValue()
                notifyItemRemoved(holder.adapterPosition)
                popupWindow.dismiss()
            }
            popupWindow.contentView.findViewById<LinearLayout>(R.id.popupWindowEditBucketItem).setOnClickListener {
                holder.bucketItemCheck.isVisible = true
                holder.bucketItemMore.isVisible = false
                holder.bucketItemLabel.isEnabled = true
                holder.bucketItemLabel.requestFocus()
                holder.bucketItemLabel.setSelection(holder.bucketItemLabel.text.length)
                holder.bucketItemCheck.setOnClickListener {
                    holder.bucketItemCheck.isVisible = false
                    holder.bucketItemMore.isVisible = true
                    holder.bucketItemLabel.isEnabled = false
                    databaseReference.child(holder.itemView.context.resources.getString(R.string.firebase_bucket_list)).child(current.id!!).child("name")
                        .setValue(holder.bucketItemLabel.editableText.toString().trim())
                    notifyItemChanged(holder.adapterPosition)
                }
                popupWindow.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class BucketItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.bucketItemCheckBox)
        val bucketItemLabel: EditText = view.findViewById(R.id.bucketItemLabel)
        val bucketItemMore: ImageView = view.findViewById(R.id.bucketItemMore)
        val bucketItemCheck: ImageView = view.findViewById(R.id.bucketItemCheck)
    }

    private fun getPopupWindow(context: Context): PopupWindow {
        val popupWindow = PopupWindow(context)
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.bucket_item_popup_window, null)
        view.findViewById<FrameLayout>(R.id.popupWindowRoot).setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = view
        return popupWindow
    }
}