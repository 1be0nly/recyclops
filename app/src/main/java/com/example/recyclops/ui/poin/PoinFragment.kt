package com.example.recyclops.ui.poin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recyclops.databinding.FragmentPoinBinding
import com.example.recyclops.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

class PoinFragment : Fragment() {

    private var _binding: FragmentPoinBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val poinViewModel =
            ViewModelProvider(this).get(PoinViewModel::class.java)

        _binding = FragmentPoinBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getPoints(poinViewModel)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPoints(homeViewModel: PoinViewModel){
        val textView: TextView = binding.tvPoin
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
}