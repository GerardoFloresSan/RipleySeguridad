package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.inflate
import kotlinx.android.synthetic.main.activity_validation.view.*
import kotlinx.android.synthetic.main.item_detail_cart.view.*

class SalesAdapter(private val listener: (Int, SalesGetByResponse) -> Unit) : RecyclerView.Adapter<SalesAdapter.CommentHolder>() {
    var data: List<SalesGetByResponse> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(sale: SalesGetByResponse, listener: (Int, SalesGetByResponse) -> Unit) = with(itemView) {
            txt_total_detail.text = Methods.formatMoney((sale.totalAmount.toDouble() / 100))
            txt_date_detail.text = sale.date
            txt_hour_detail.text = sale.date
            this.setOnClickListener {
                listener(0, sale)
            }
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : SalesAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_detail_cart)
                return SalesAdapter.CommentHolder(view)
            }
        }
    }
}