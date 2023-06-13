package com.example.recyclops.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.example.recyclops.data.UserHistoryItem
import com.example.recyclops.databinding.FragmentHomeBinding
import com.example.recyclops.ui.camera.CameraActivity
import com.example.recyclops.ui.history.HistoryActivity
import com.example.recyclops.ui.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: SetoranAdapter
    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        adapter = SetoranAdapter()
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

    override fun onStart() {
        super.onStart()
        getAllRVData()

//        getPoints(homeViewModel)
//        adapter = SetoranAdapter()
//
//
//        setUserHistory(homeViewModel)
//        getList(homeViewModel)
//        showRecyclerList()
    }

    override fun onResume() {
        super.onResume()
        getAllRVData()

//        getPoints(homeViewModel)
//        adapter = SetoranAdapter()


//        setUserHistory(homeViewModel)
//        getList(homeViewModel)
//        showRecyclerList()
    }

    private fun getAllRVData() {
        getPoints(homeViewModel)
        setUserHistory(homeViewModel)
        getList(homeViewModel)
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
    private fun showRecyclerList() {
        binding.rvHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHome.adapter = adapter
    }

    private fun getList(homeViewModel: HomeViewModel){
        homeViewModel.getUserHistory().observe(requireActivity()){
            if (it != null){
                Log.d("UserHistory", it.toString())
//                adapter = SetoranAdapter()
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setUserHistory(homeViewModel: HomeViewModel){
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token.toString()
                    val token  = "Bearer $idToken"
                    homeViewModel.setUserHistory(token)
                } else {
                    Log.d("Exception", task.exception.toString())
                }
            }
    }

    private fun getPoints(homeViewModel: HomeViewModel){
        val textView: TextView = binding.textHome
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token.toString()
                    val token  = "Bearer $idToken"
                    homeViewModel.getUserPoint(token)
                    homeViewModel.point.observe(requireActivity()){
                        if (it != null){
                            textView.text = it.totalPoints.toString()
                        }else{
                            Toast.makeText(requireContext(), "Gagal Mendapatkan Poin", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Log.d("Exception", task.exception.toString())
                }
            }
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}