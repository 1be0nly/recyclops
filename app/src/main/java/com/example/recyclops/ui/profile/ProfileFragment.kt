package com.example.recyclops.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recyclops.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = profileViewModel.user

        showLoading(true)

        if (user != null){
            user.let {
                val name = it.displayName
                val email = it.email
                val photoUrl = it.photoUrl

                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.ivProfile)
                binding.tvProfileNama.text = name
                binding.tvProfileEmail.text = email
            }
        } else{
            Toast.makeText(requireContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show()
        }
        showLoading(false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}