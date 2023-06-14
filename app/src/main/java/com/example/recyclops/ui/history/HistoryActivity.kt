package com.example.recyclops.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclops.R
import com.example.recyclops.data.UserHistoryItem
import com.example.recyclops.databinding.ActivityHistoryBinding
import com.example.recyclops.ui.home.HomeViewModel
import com.example.recyclops.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding
    lateinit var viewModel: HistoryViewModel
    private val list = ArrayList<UserHistoryItem>()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        showLoading(true)

        setUserHistory(viewModel)

        viewModel.historyTrash.observe(this) { history ->
            if (history != null) {
                Log.d("Logas", history.toString())
                list.addAll(history)
                showRecyclerList()
            }
        }

//        list.addAll(getListSetoran())
        showRecyclerList()
        showLoading(false)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUserHistory(HistoryViewModel: HistoryViewModel){
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token.toString()
                    val token  = "Bearer $idToken"
                    HistoryViewModel.setUserHistory(token)
                } else {
                    Log.d("Exception", task.exception.toString())
                }
            }
    }

    @SuppressLint("Recycle")
    private fun getListSetoran():ArrayList<UserHistoryItem>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataQuantity = resources.getStringArray(R.array.data_quantity)
        val dataBank = resources.getIntArray(R.array.data_bank)
        val dataImage = resources.obtainTypedArray(R.array.data_image)

        val listSetoran = ArrayList<UserHistoryItem>()
        for (i in dataName.indices){
            val setoran = UserHistoryItem(dataName[i], dataQuantity[i], dataBank[i], dataImage.getResourceId(i, -1))
            listSetoran.add(setoran)
        }

        return listSetoran
    }

    private fun showRecyclerList() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val historyAdapter = HistoryAdapter(list)
        binding.rvHistory.adapter = historyAdapter
    }

}