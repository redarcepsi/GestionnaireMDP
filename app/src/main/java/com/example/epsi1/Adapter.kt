package com.example.epsi1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

class Adapter(private val context: Context, private val SiteName: Array<String>) : BaseAdapter()
{
    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return SiteName.size
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cv = convertView
        if (cv == null) {
            cv = inflater!!.inflate(R.layout.cardview_slide_panel, parent, false)
        }
        val holder = initHolder(cv!!)

        holder.tvSiteName.text=SiteName[position]
        cv.setOnClickListener{
            Toast.makeText(context, SiteName[position], Toast.LENGTH_LONG).show()
        }
        return cv
    }
}