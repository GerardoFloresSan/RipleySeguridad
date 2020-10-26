package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.utils.inflate
import kotlinx.android.synthetic.main.item_coupon_card.view.*

class CouponAdapter(private val listener: (Int, String, Int) -> Unit) : RecyclerView.Adapter<CouponAdapter.CommentHolder>() {
    var data: List<String> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(code: String, listener: (Int, String, Int) -> Unit) = with(itemView) {
            txt_title_product.text = code
            btn_close_add.setOnClickListener {
                listener(2, code, 0)// remove consume service
            }
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : CouponAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_coupon_card)
                return CouponAdapter.CommentHolder(view)
            }
        }
    }
}