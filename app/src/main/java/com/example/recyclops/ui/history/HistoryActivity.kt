package com.example.recyclops.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclops.R
import com.example.recyclops.data.SetoranTerakhir
import com.example.recyclops.databinding.ActivityHistoryBinding
import com.example.recyclops.databinding.ActivityLoginBinding
import com.example.recyclops.ui.home.SetoranAdapter
import com.example.recyclops.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding
    private val list = ArrayList<SetoranTerakhir>()

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

        list.addAll(getListSetoran())
        showRecyclerList()

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
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val historyAdapter = HistoryAdapter(list)
        binding.rvHistory.adapter = historyAdapter
    }

}