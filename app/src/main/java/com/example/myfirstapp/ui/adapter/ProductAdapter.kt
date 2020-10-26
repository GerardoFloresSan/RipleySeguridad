package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.inflate
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.item_shopping_cart.view.*

class ProductAdapter(private val listener: (Int, CheckPricesResponse.Product, Int) -> Unit) : RecyclerView.Adapter<ProductAdapter.CommentHolder>() {
    var data: List<CheckPricesResponse.Product> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(product: CheckPricesResponse.Product, listener: (Int, CheckPricesResponse.Product, Int) -> Unit) = with(itemView) {
            val count = product.quantity.toInt()

            val maxByProduct = if(BuildConfig.DEBUG) 3 else Methods.getParameter(123).value.toInt()

            txt_title_product.text = product.description
            txt_description_product.text = "SKU: ${product.sku}"

            /*txt_price_normal.text = "S/ " + "%.2f".format(((product.pricePromo.toDouble() / 100)).toBigDecimal())
            txt_price_ripley.text = "S/ " + "%.2f".format(((product.priceRipley.toDouble() / 100)).toBigDecimal())*/
            txt_price_normal.text = Methods.formatMoney((product.pricePromo.toDouble() / 100))// "S/ " + "%.2f".format(((product.pricePromo.toDouble() / 100)).toBigDecimal())
            txt_price_ripley.text = Methods.formatMoney((product.priceRipley.toDouble() / 100))//"S/ " + "%.2f".format(((product.priceRipley.toDouble() / 100)).toBigDecimal())

            txt_count_product.text = count.toString()

            btn_one_rest.setOnClickListener {
                if (count > 1) {
                    val rest = (count - 1)
                    listener(0, product, rest)
                }
            }
            btn_one_more.setOnClickListener {
                if (count < maxByProduct) {
                    val sum = (count + 1)
                    listener(1, product, sum)
                }
            }
            btn_close_add.setOnClickListener {
                listener(2, product, 0)// remove consume service
            }
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : ProductAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_shopping_cart)
                return ProductAdapter.CommentHolder(view)
            }
        }
    }
}