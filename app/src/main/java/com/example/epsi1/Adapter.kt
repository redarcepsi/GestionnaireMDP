package com.example.epsi1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.DialogInterface
import android.os.PersistableBundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.epsi1.activity.HomeActivity
import com.example.epsi1.model.AccountList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Adapter(private val context: HomeActivity, private var accounts: Array<AccountList>) : BaseAdapter()
{
    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun updateList(newList:Array<AccountList>){
        accounts = newList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return accounts.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class Holder {
        lateinit var tvSiteName: TextView
    }
    private fun initHolder(view: View): Holder {
        val holder = Holder()
        holder.tvSiteName = view.findViewById(R.id.tvSiteName)
        holder.tvSiteName.maxLines = 1
        holder.tvSiteName.isSelected = true
        holder.tvSiteName.isSingleLine = true
        holder.tvSiteName.isFocusable = true
        holder.tvSiteName.isFocusableInTouchMode = true
        return holder
    }

    @SuppressLint("InlinedApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cv = convertView
        if (cv == null) {
            cv = inflater!!.inflate(R.layout.cardview_slide_panel, parent, false)
        }
        val holder = initHolder(cv!!)
        val account = accounts[position]
        val icon = cv.findViewById<ImageView>(R.id.ivCopyIcon)
        val modifyButton = cv.findViewById<LinearLayout>(R.id.carte)

        holder.tvSiteName.text=account.site
        // icon pour copié le bouton
        icon.setOnClickListener{
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Texte copié", account.mdp)
            clip.apply {
                description.extras = PersistableBundle().apply {
                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                }
            }
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Texte copié dans le presse-papiers !", Toast.LENGTH_SHORT).show()
        }
        // bouton modification
        modifyButton.setOnClickListener {
            // Create an alert builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Modifier un compte")

            // set the custom layout
            val customLayout: View = inflater!!.inflate(R.layout.custom_layout, null)
            builder.setView(customLayout)

            // send data from the AlertDialog to the Activity
            val editTextEmail: EditText = customLayout.findViewById(R.id.editTextEmail)
            val editTextSite: EditText = customLayout.findViewById(R.id.editTextSite)
            val editTextMdp: EditText = customLayout.findViewById(R.id.editTextMdp)

            editTextEmail.text = Editable.Factory.getInstance().newEditable(account.email)
            editTextSite.text = Editable.Factory.getInstance().newEditable(account.site)
            editTextMdp.text = Editable.Factory.getInstance().newEditable(account.mdp)

            // add a button
            builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->

                //save en bdd
                if (editTextEmail.text.toString().isNotEmpty() && editTextMdp.text.toString().isNotEmpty() && editTextSite.text.toString().isNotEmpty()) {
                    account.mdp=editTextMdp.text.toString().trim()
                    account.site=editTextSite.text.toString().trim()
                    account.email=editTextEmail.text.toString().trim()
                    CoroutineScope(Dispatchers.IO).launch {
                        (context.applicationContext as UserDataApplication).database.AccountDao().updateaccount(
                            account.email,
                            account.mdp,
                            account.site,
                            account.accountId
                        )
                    }
                } else {
                    Toast.makeText(context, "un ou plusieurs champs est vide", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.cancel()
            }
            builder.setNeutralButton("Delete"){
                dialog,_->
                CoroutineScope(Dispatchers.IO).launch {
                    (context.applicationContext as UserDataApplication).database.AccountDao().deleteaccount(
                        account.accountId
                    )
                    context.loadData()
                }

                dialog.cancel()
            }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
        return cv
    }
}