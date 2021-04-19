package com.example.myfirstapp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.inflate
import com.example.myfirstapp.utils.toSimpleString
import com.example.myfirstapp.utils.toSimpleTime
import kotlinx.android.synthetic.main.activity_validation.view.*
import kotlinx.android.synthetic.main.item_detail_cart.view.*
import java.text.SimpleDateFormat

class SalesAdapter(private val listener: (Int, SalesGetByResponse) -> Unit) :
    RecyclerView.Adapter<SalesAdapter.CommentHolder>() {
    var data: List<SalesGetByResponse> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) =
        holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(sale: SalesGetByResponse, listener: (Int, SalesGetByResponse) -> Unit) =
            with(itemView) {
                txt_number.text = "Orden de compra: " + sale.orderId.toString()
                val clientSos = Methods.getParameter("sgRiskClientText").value

                if (sale.trxNumber == null) {
                    txt_number_order.text = "Pendiente"
                } else {
                    txt_number_order.text = sale.trxNumber
                }


                txt_total_detail.text = Methods.formatMoney((sale.totalAmount.toDouble() / 100))
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.date)
                //TODO txt_status_detail color morado !! LOGICA === Estado verificado == 3
                if (sale.statusId == 3) {
                    txt_status_detail.text = sale.status
                    txt_status_detail.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                        )
                    )
                } else {
                    txt_status_detail.text = sale.status
                    txt_status_detail.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorCuponText
                        )
                    )
                }

                if (sale.riskClient) {
                    lnl_client_sos.visibility = View.VISIBLE
                    txt_sos_client.text = clientSos
                } else {
                    lnl_client_sos.visibility = View.GONE
                }


                Log.wtf("Cliente Sospechoso", "Valor de -->" + clientSos)


                txt_date_detail.text = date.toSimpleString()
                txt_hour_detail.text = date.toSimpleTime()
                this.setOnClickListener {
                    listener(0, sale)
                }
            }

        companion object {
            fun init(parent: ViewGroup, viewType: Int): SalesAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_detail_cart)
                return SalesAdapter.CommentHolder(view)
            }
        }
    }
}