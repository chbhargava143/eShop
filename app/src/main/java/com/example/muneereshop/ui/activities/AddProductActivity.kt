package com.example.muneereshop.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.muneereshop.product.Product
import com.example.muneereshop.R
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ActivityAddProductBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.utils.GlideLoader
import java.io.IOException

class AddProductActivity : AppCompatActivity() {
    val loading = DialogueProgress(this)

    // A global variable for URI of a selected image from phone storage.
    private var mySelectedImageFileUri: Uri? = null

    // A global variable for uploaded product image URL.
    private var myProductImageURL: String = ""

    private lateinit var binding: ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val addProductImage = binding.addProductImage
        val submitProduct = binding.saveProductButton
        addProductImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        submitProduct.setOnClickListener {
            if (validateProductDetails()) {
                uploadProductImage()
            }
        }
    }

    private fun uploadProductImage() {

        loading.startLoading()

        FireStores().uploadImageToCloudStorage(
            this@AddProductActivity,
            mySelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        myProductImageURL = imageURL

        uploadProductDetails()
    }


    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MUNEERESHOP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val product = Product(
            FireStores().getCurrentUserId(),
            username,
            binding.productTitle.text.toString().trim { it <= ' ' },
            binding.productPrice.text.toString().trim { it <= ' ' },
            binding.productDescription.text.toString().trim { it <= ' ' },
            binding.productQuantity.text.toString().trim { it <= ' ' },
            myProductImageURL
        )

        FireStores().uploadProductDetails(this@AddProductActivity, product)
    }

    fun productUploadSuccess() {

        // Hide the progress dialog
        loading.isDismiss()

        Toast.makeText(
            this@AddProductActivity,
            "Prodcut Uploaded Successfully",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun validateProductDetails(): Boolean {

        return when {

            mySelectedImageFileUri == null -> {
                Toast.makeText(this@AddProductActivity, "Select Product Image", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            TextUtils.isEmpty(binding.productTitle.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@AddProductActivity, "Select Product Title", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            TextUtils.isEmpty(binding.productPrice.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@AddProductActivity, "Select Product Price", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            TextUtils.isEmpty(binding.productDescription.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "Select Product Description",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(binding.productQuantity.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "Select Procut Quantity",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            // if permission granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)

                // Toast.makeText(this, "The storage permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "The storage permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val ivUserPhoto = binding.productImageShow
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data != null) {
                  binding.addProductImage.setImageDrawable(ContextCompat.getDrawable(this@AddProductActivity,R.drawable.ic_edit))
                mySelectedImageFileUri = data.data!!
            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddProductActivity).loadUserPicture(
                    mySelectedImageFileUri!!,
                    binding.productImageShow
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }


        } else if (resultCode == Activity.RESULT_CANCELED) {


            Log.e("Result cancelled", "Image selection Cancelled")
        }
    }

}