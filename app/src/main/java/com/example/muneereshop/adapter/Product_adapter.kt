package com.example.muneereshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.muneereshop.R
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ItemsListBinding
import com.example.muneereshop.product.Product
import com.example.muneereshop.ui.activities.ProductDetailsActivity
import com.example.muneereshop.ui.fragments.ProductFragment
import com.example.muneereshop.utils.GlideLoader

internal class Product_adapter(private val context : Context,private val fragment:ProductFragment,private var items:ArrayList<Product>) :
RecyclerView.Adapter<Product_adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Product_adapter.ViewHolder {
        val MyViewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.items_list,parent,false))
        return MyViewHolder
    }

    override fun onBindViewHolder(holder: Product_adapter.ViewHolder, position: Int) {
        holder.bind(items[position],context,fragment)


    }
    override fun getItemCount(): Int {
        return items.size
    }
    internal class ViewHolder(view: View):RecyclerView.ViewHolder(view){
         var binding = ItemsListBinding.bind(view)
        var productImage = binding.productImageShows
        var productTitle = binding.productTitleShow
        var productPrice = binding.productPricesShow
        var deleteProduct = binding.ibDelete
        fun bind(product: Product,context: Context,fragment: ProductFragment){
            GlideLoader(context).loadProductPicture(product.image,productImage)
            productTitle.text = product.title
            productPrice.text = "Rs.${product.price}"
            deleteProduct.setOnClickListener {
                fragment.deleteProductsListFromFirestore(product.product_id)
            }
            itemView.setOnClickListener {
                val intent = Intent(context,ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                context.startActivity(intent)
            }
          }


    }
}