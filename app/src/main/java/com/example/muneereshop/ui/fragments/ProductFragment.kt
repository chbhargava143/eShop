package com.example.muneereshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muneereshop.product.Product
import com.example.muneereshop.R
import com.example.muneereshop.adapter.Product_adapter
import com.example.muneereshop.databinding.FragmentProductBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.progressbar.FragmentDialogueProgress
import com.example.muneereshop.ui.activities.AddProductActivity

class ProductFragment : Fragment() {
    private val loading = FragmentDialogueProgress(this)
    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
 val binding get() = _binding!!
fun deleteProductsListFromFirestore(productID:String){

}
    fun successProductsListFromFireStore(productsList:ArrayList<Product>) {
        loading.isDismiss()
    var productAdapter = binding.myProductsItems
        var productNoItemFound = binding.noProductsFound
       if (productsList.size > 0){

           productAdapter.visibility = View.VISIBLE
           productNoItemFound.visibility = View.GONE
          productAdapter.layoutManager = LinearLayoutManager(activity)
           productAdapter.setHasFixedSize(true)
           val adpaterProducts = Product_adapter(requireContext(),productsList)
           productAdapter.adapter = adpaterProducts
       }else {
           productAdapter.visibility = View.GONE
           productNoItemFound.visibility = View.VISIBLE
       }
    }
fun getProductListFromFirebase(){
    loading.startLoading()
    FireStores().getProductsDetails(this@ProductFragment)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onResume() {
        super.onResume()

        getProductListFromFirebase()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}