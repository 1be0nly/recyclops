package com.example.recyclops.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclops.R
import com.example.recyclops.data.SetoranTerakhir
import com.example.recyclops.databinding.FragmentHomeBinding
import com.example.recyclops.ui.camera.CameraActivity
import com.example.recyclops.ui.history.HistoryActivity
import com.example.recyclops.ui.maps.MapsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = ArrayList<SetoranTerakhir>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        list.addAll(getListSetoran())
        showRecyclerList()

        binding.tvHomeSetoran.setOnClickListener{
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val camera: CardView = binding.cvCamera
        camera.setOnClickListener {
            val intent = Intent(requireActivity(), CameraActivity::class.java)
            startActivity(intent)
        }

        val maps: CardView = binding.cvBankSampah
        maps.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    @SuppressLint("Recycle")
    private fun getListSetoran():ArrayList<SetoranTerakhir>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataQuantity = resources.getStringArray(R.array.data_quantity)
        val dataBank = resources.getStringArray(R.array.data_bank)
        val dataImage = resources.obtainTypedArray(R.array.data_image)

        val listSetoran =ArrayList<SetoranTerakhir>()
        for (i in dataName.indices){
            val setoran = SetoranTerakhir(dataName[i], dataQuantity[i], dataBank[i], dataImage.getResourceId(i, -1))
            listSetoran.add(setoran)
        }
        return listSetoran
    }

    private fun showRecyclerList() {
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listSetoranAdapter = SetoranAdapter(list)
        binding.rvHome.adapter = listSetoranAdapter
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}