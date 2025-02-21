package com.example.epsi1.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.epsi1.R
import com.example.epsi1.UserDataApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // bouton de connexion sur l'écran qui s'ouvre au lencement de l'application
        val btnConnection = findViewById<Button>(R.id.btn_connexion)
        btnConnection.setOnClickListener {

            //recupération des infos entrées dans les champs
            val editTextIdentifiantConnexion = findViewById<EditText>(R.id.EditTextIdentifiantConnexion)
            val editTextMdpConnexion = findViewById<EditText>(R.id.EditTextMdpConnexion)
            val identifiant = editTextIdentifiantConnexion.text.toString()
            val mdp = editTextMdpConnexion.text.toString()

            //lancement sur un autre thread grâce a CoroutineScope
            CoroutineScope(Dispatchers.IO).launch {
                val users = (applicationContext as UserDataApplication).database.UserDao().getalluser().toTypedArray()
                val user = users.find { it.email == identifiant && it.mdp == mdp }

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(this@MainActivity, getString(R.string.connexion_ok), Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))//start Homeactivity pour avoir la liste des comptes
                    } else {
                        Toast.makeText(this@MainActivity, getString(R.string.connexion_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        val editTextMdpConnexion = findViewById<EditText>(R.id.EditTextMdpConnexion)
        val toggleVisibility = findViewById<ImageButton>(R.id.icon_toggle_visibilty_mdp)
        toggleVisibility.setOnClickListener{// toggle de l'affichage du mot de passe ou non
            if (editTextMdpConnexion.inputType!=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                editTextMdpConnexion.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD// visible
            }
            else {
                editTextMdpConnexion.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD // invisible
            }
        }
    }
}