package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.utils.inflate
import kotlinx.android.synthetic.main.item_product_detail.view.*

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.CommentHolder>() {
    var data: List<SalesGetByResponse.Product> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(prod: SalesGetByResponse.Product) = with(itemView) {
            txt_title_product.text = "(${prod.quantity}) ${prod.description}"
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : ProductAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_product_detail)
                return ProductAdapter.CommentHolder(view)
            }
        }
    }
}