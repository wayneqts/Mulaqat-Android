package com.mulaqat.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mulaqat.app.R
import com.mulaqat.app.activities.MessDetail
import com.mulaqat.app.databinding.ItemMessBinding
import com.mulaqat.app.model.Message

class MessAdapter(
    list: List<Message>,
    var context: Context
) :
    RecyclerView.Adapter<MessAdapter.VH>() {
    var list: List<Message> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding: ItemMessBinding = ItemMessBinding.inflate(LayoutInflater.from(parent.context))
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ib: Message = list[position]
        holder.bind.tvName.text = ib.username
        holder.bind.tvCountry.text = ib.msg
        holder.bind.tvTime.text = ib.time
        if (ib.readstatus == "3" || ib.readstatus == "1") {
            holder.bind.ivSeen.setImageResource(R.drawable.ic_received)
        } else {
            holder.bind.ivSeen.setImageResource(R.drawable.ic_sent)
        }
        holder.itemView.setOnClickListener { v: View? ->
            context.startActivity(
                Intent(
                    context,
                    MessDetail::class.java
                ).putExtra("sId", ib.sender_id)
                    .putExtra("rId", ib.rId).putExtra("name", ib.username)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class VH(binding: ItemMessBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var bind: ItemMessBinding = binding
    }
}