package com.mulaqat.app.helper

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(
    context: Context,
    resource: Int,
    items: Array<String>
) : ArrayAdapter<String>(context, resource, items) {

    override fun isEnabled(position: Int): Boolean {
        // Disable the first item (used as hint)
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.setTextColor(
            if (position == 0) Color.parseColor("#BFBFBF")
            else Color.BLACK
        )
        return view
    }
}