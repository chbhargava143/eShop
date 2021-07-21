package com.example.muneereshop.firebase.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.muneereshop.product.Product
import com.google.firebase.storage.FirebaseStorage
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.ui.activities.*
import com.example.muneereshop.ui.fragments.DashboardFragment
import com.example.muneereshop.ui.fragments.ProductFragment
import com.example.muneereshop.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference

class FireStores {
    private val myFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        myFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.UserRegisteredOnSuccess()

            }.addOnFailureListener { e ->
                activity.loading.isDismiss()
                Log.e(activity.javaClass.simpleName, "Error while registering the User.", e)
            }
    }

    fun getCurrentUserId():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity:Activity){
        myFirestore.collection(Constants.USERS).document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user = document.toObject(User::class.java)
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MUNEERESHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                var editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME,"${user?.firstName} ${user?.lastName}")
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userDetailsSuccess(user!!)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user!!)
                    }

                }

            }
            .addOnFailureListener { e ->
        when(activity){
            is LoginActivity ->{
                activity.loading.isDismiss()
            }
            is SettingsActivity -> {
                activity.loading.isDismiss()
            }
        }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        myFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())

            .update(userHashMap) // Error Here video no 46

            .addOnSuccessListener {
            when (activity){
                is ProfileActivity -> {
                    activity.userProfileUpdateSuccess()

                }
            }
        }.addOnFailureListener { e ->
            when (activity){
                is ProfileActivity -> {
                    activity.loading.isDismiss()
                }
            }
            Log.e(activity.javaClass.simpleName,"Error while upadating the user details.",e)
        }
    }

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imageType:String){
        val myRef : StorageReference = FirebaseStorage.getInstance().reference.child(imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(activity,imageFileURI))
        myRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            // Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Download Image URL", uri.toString())

                // Here call a function of base activity for transferring the result to it.
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is AddProductActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }

            }
        }.addOnFailureListener { exception ->
                when (activity){
                    is ProfileActivity -> {
                        activity.loading.isDismiss()
                    }
                    is AddProductActivity -> {
                        activity.loading.isDismiss()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }


    }
    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {

        myFirestore.collection(Constants.PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.loading.isDismiss()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }
    fun getProductsDetails(fragment:Fragment){
        myFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List",document.documents.toString())
                val productsList : ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)

                }
                when (fragment){
                    is ProductFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }


            }
    }
    fun gettingProductsDetails(activity: ProductDetailsActivity,productId: String){
        myFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName ,document.toString())
                val product = document.toObject(Product::class.java)
            }.addOnFailureListener { e ->
                activity.loading.isDismiss()
                Log.e(
                    activity.javaClass.simpleName,
                    e.message,
                    e
                )

            }
    }
    fun deleteProduct(fragment: ProductFragment,productId:String){
    myFirestore.collection(Constants.PRODUCTS)
        .document(productId)
        .delete()
        .addOnSuccessListener {
        fragment.productDeleteSuccess()
        }.addOnFailureListener { e ->
            fragment.loading.isDismiss()
            Log.e(fragment.requireActivity().javaClass.simpleName,"Error while deleting the Product",e)
        }
    }
    fun getDashBoardItemsList(fragment: DashboardFragment){
        myFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Dashboard Products List",document.documents.toString())
                val productsList : ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)

                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener { e ->
                fragment.loading.isDismiss()
                Log.e(fragment.javaClass.simpleName,"Error while gettin dashboard items list,",e)
            }
    }

}