package com.example.epsi1.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.epsi1.Adapter
import com.example.epsi1.R
import com.example.epsi1.UserDataApplication
import com.example.epsi1.Utils
import com.example.epsi1.model.AccountList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val listView = findViewById<ListView>(R.id.listview)
        adapter = Adapter(this@HomeActivity, arrayOf())
        listView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addButton = findViewById<ImageButton>(R.id.addButton)
        loadData()

        //bouton ajout
        addButton.setOnClickListener {
            // Create an alert builder
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ajouter un compte")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.custom_layout, null)
            builder.setView(customLayout)

            val editTextEmail: EditText = customLayout.findViewById(R.id.editTextEmail)
            val editTextSite: EditText = customLayout.findViewById(R.id.editTextSite)
            val editTextMdp: EditText = customLayout.findViewById(R.id.editTextMdp)

            // add a button
            builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                // send data from the AlertDialog to the Activity

                if (editTextEmail.text.toString().isNotEmpty() && editTextMdp.text.toString().isNotEmpty() && editTextSite.text.toString().isNotEmpty()) {
                    val newAccount = AccountList(
                        email = editTextEmail.text.toString().trim(),
                        mdp = editTextMdp.text.toString().trim(),
                        site = editTextSite.text.toString().trim(),
                        userId = 1
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        (applicationContext as UserDataApplication).database.AccountDao()
                            .addaccount(newAccount)
                    }
                    finish()
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this, "un ou plusieurs champs est vide", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.cancel()
            }

            val generateur = customLayout.findViewById<ImageButton>(R.id.iconGenerateur)
            generateur.setOnClickListener{
                editTextMdp.text = Editable.Factory.getInstance().newEditable(Utils.generateur())
            }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }
    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val accounts = (applicationContext as UserDataApplication).database.AccountDao()
                .getallaccount(userid = 1).toTypedArray()

            withContext(Dispatchers.Main) {
                adapter.updateList(accounts)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}