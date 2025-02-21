package com.example.epsi1.activity

import android.app.AlertDialog
import android.content.DialogInterface
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

        // initialisation de l'adapter
        val listView = findViewById<ListView>(R.id.listview)
        adapter = Adapter(this@HomeActivity, arrayOf())
        listView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addButton = findViewById<ImageButton>(R.id.addButton)
        loadData()// charge les infos dans l'adapter

        //bouton pour ajouter un compte (fait appel a un AlertDialog custom)
        addButton.setOnClickListener {
            // Create an alert builder
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ajouter un compte")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.custom_layout, null)
            builder.setView(customLayout)

            // send data from the AlertDialog to the Activity
            val editTextEmail: EditText = customLayout.findViewById(R.id.editTextEmail)
            val editTextSite: EditText = customLayout.findViewById(R.id.editTextSite)
            val editTextMdp: EditText = customLayout.findViewById(R.id.editTextMdp)

            // ajout du bouton OK
            builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                // send data from the AlertDialog to the Activity

                //vérifie si les champs ne sont pas nul, sinon ne fait pas la sauvegarde
                if (editTextEmail.text.toString().isNotEmpty() && editTextMdp.text.toString().isNotEmpty() && editTextSite.text.toString().isNotEmpty()) {
                    val newAccount = AccountList(
                        email = editTextEmail.text.toString().trim(),
                        mdp = editTextMdp.text.toString().trim(),
                        site = editTextSite.text.toString().trim(),
                        userId = 1 // pour l'instant le userId est set a 1 car il n'y a pas encore de gestion de compte
                    )// TODO : changer le userId selon l'utilisateur connecter
                    CoroutineScope(Dispatchers.IO).launch {
                        (applicationContext as UserDataApplication).database.AccountDao().addaccount(newAccount)
                        loadData()
                    }
                } else {
                    Toast.makeText(this, "un ou plusieurs champs est vide", Toast.LENGTH_LONG).show()
                }
            }
            // ajout du bouton cancel
            builder.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.cancel()
            }

            //  ajoute un setOnClickListener sur le bouton dans custom_layout.xml pour générer un mot de passe
            val generateur = customLayout.findViewById<ImageButton>(R.id.iconGenerateur)
            generateur.setOnClickListener{
                editTextMdp.text = Editable.Factory.getInstance().newEditable(Utils.generateur())
            }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }
    // fonction pour update l'adapter
    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val accounts = (applicationContext as UserDataApplication).database.AccountDao()
                .getallaccount(userid = 1).toTypedArray()

            withContext(Dispatchers.Main) {
                adapter.updateList(accounts)
            }
        }
    }
    // a chaque chargement de page, on actualise les infos de l'adapter
    override fun onResume() {
        super.onResume()
        loadData()
    }
}