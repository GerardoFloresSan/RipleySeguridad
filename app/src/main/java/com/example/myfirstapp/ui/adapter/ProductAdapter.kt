package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.utils.inflate

class ProductAdapter(private val listener: (Int, CheckPricesResponse.Product, Int) -> Unit) : RecyclerView.Adapter<ProductAdapter.CommentHolder>() {
    var data: List<CheckPricesResponse.Product> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(product: CheckPricesResponse.Product, listener: (Int, CheckPricesResponse.Product, Int) -> Unit) = with(itemView) {

        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : ProductAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_shopping_cart)
                return ProductAdapter.CommentHolder(view)
            }
        }
    }
}