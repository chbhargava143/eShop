package com.example.muneereshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muneereshop.R
import com.example.muneereshop.adapter.DashBoard_Adapter
import com.example.muneereshop.databinding.FragmentDashboardBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.product.Product
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.progressbar.FragmentDialogueProgress
import com.example.muneereshop.ui.activities.SettingsActivity

class DashboardFragment : Fragment() {

   val loading = FragmentDialogueProgress(this)
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_settings -> {
                startActivity(Intent(activity,SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun successDashboardItemsList(dashboardItemsList:ArrayList<Product>) {
        val dashboardRv = binding.dashbordRecyclerView
        val noDashboardItem = binding.noDashboatditemFound
        loading.isDismiss()
       if (dashboardItemsList.size > 0) {
           dashboardRv.visibility = View.VISIBLE
           noDashboardItem.visibility = View.GONE
           //dashboardRv.layoutManager = LinearLayoutManager(activity)
           dashboardRv.layoutManager = GridLayoutManager(activity,2)
           dashboardRv.setHasFixedSize(true)
           val dashboardItems = DashBoard_Adapter(requireContext(),dashboardItemsList)
           dashboardRv.adapter = dashboardItems
       }else {
           dashboardRv.visibility = View.GONE
           noDashboardItem.visibility = View.VISIBLE
       }
    }
    private fun getDashboardItemsList(){
        loading.startLoading()
        FireStores().getDashBoardItemsList(this@DashboardFragment)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }
}