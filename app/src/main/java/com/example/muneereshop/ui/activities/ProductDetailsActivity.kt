package com.example.muneereshop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muneereshop.R
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ActivityProductDetailsBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.product.Product
import com.example.muneereshop.progressbar.DialogueProgress

class ProductDetailsActivity : AppCompatActivity() {
    val loading = DialogueProgress(this)
    private lateinit var myProductDetails: Product

    // A global variable for product id.
    private var myProductId: String = ""

    private lateinit var binding : ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            myProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productOwnerId : String = ""
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
    }
    fun productDetailsSuccess(product: Product){
     loading.isDismiss()




    }
    private fun setupActionBar() {

        setSupportActionBar(binding.detailsToolBar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24)
        }

        binding.detailsToolBar.setNavigationOnClickListener { onBackPressed() }
    }
fun getProductDetails(){
    loading.startLoading()

}

}