package com.example.muneereshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.muneereshop.R
import com.example.muneereshop.databinding.ItemDashboardBinding
import com.example.muneereshop.databinding.ItemsListBinding
import com.example.muneereshop.product.Product
import com.example.muneereshop.utils.GlideLoader

internal class DashBoard_Adapter(private val context:Context, private var dashBoardItems:ArrayList<Product>):
RecyclerView.Adapter<DashBoard_Adapter.ViewHolder>(){

    internal class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var binding = ItemDashboardBinding.bind(view)
        val dashboardItemImage = binding.dashboardItemImage
        val dashboardItemTitle = binding.dashboardItemTitle
        val dashboardItemPrice = binding.dasboardItemPrice
        fun bind(product: Product,context: Context) {
                dashboardItemTitle.text = product.title
            dashboardItemPrice.text = "Rs.${product.price}"
            GlideLoader(context).loadProductPicture(product.image,dashboardItemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false)
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(dashBoardItems[position],context)
    }

    override fun getItemCount(): Int {
        return dashBoardItems.size
    }


}