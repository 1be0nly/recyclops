package com.example.recyclops.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.databinding.FragmentTransaksiBinding

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: TransaksiViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TransaksiViewModel::class.java]

        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // TODO: ui ngebug dikit
        showRecyclerList()

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList() {
        recyclerView = _binding?.rvTransaksi!!
        layoutManager = LinearLayoutManager(requireContext())

        viewModel.dataList.observe(viewLifecycleOwner) { dataList ->
            val adapter = TransaksiAdapter(
                onIncreaseQuantity = { id, position ->
                    viewModel.increaseQuantity(id)

                },
                onDecreaseQuantity = { id, position ->
                    viewModel.decreaseQuantity(id)

                },
                onDeleteItem = { id, position ->
                    viewModel.deleteItem(id)

                }
            )

            val previousScrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

            recyclerView.adapter = adapter
            adapter.submitList(dataList)

            recyclerView.scrollToPosition(previousScrollPosition)
        }

        recyclerView.layoutManager = layoutManager
    }

//    private fun changeDataRV(id: Int, position: Int, type: String, dataList: List<TrashScanned>) {
//        val updatedDataList = dataList.toMutableList()
//        if (type == "increase") {
//            updatedDataList[id].quantity++
//            recyclerView.adapter?.notifyItemChanged(position)
//        } else if (type == "decrease") {
//            if (updatedDataList[position].quantity > 0) {
//                updatedDataList[position].quantity--
//            }
//            recyclerView.adapter?.notifyItemChanged(position)
//        } else if (type == "delete") {
//            updatedDataList.removeAt(position)
//            recyclerView.adapter?.notifyItemRemoved(position)
//        }
//    }
}