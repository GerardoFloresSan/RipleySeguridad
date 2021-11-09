package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.BlueToothDeviceData
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.inflate
import kotlinx.android.synthetic.main.item_bluetooth.view.*

class DeviceAdapter(private val listener: (BlueToothDeviceData) -> Unit) : RecyclerView.Adapter<DeviceAdapter.CommentHolder>() {
    var data: List<BlueToothDeviceData> = arrayListOf()

    override fun onBindViewHolder(holder: DeviceAdapter.CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(data: BlueToothDeviceData, listener: (BlueToothDeviceData) -> Unit) = with(itemView) {
            if(PapersManager.macPrint2 == data.addressMac) {
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryOpa))
            } else {
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            name_bluetooth.text = data.name
            mac_bluetooth.text = data.addressMac
            this.setOnClickListener {
                listener(data)
            }
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int): DeviceAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_bluetooth)
                return DeviceAdapter.CommentHolder(view)
            }
        }
    }
}