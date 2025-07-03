package com.mulaqat.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mulaqat.app.R
import com.mulaqat.app.api.APIUtils
import com.mulaqat.app.databinding.ItemNotificationBinding
import com.mulaqat.app.model.Notification
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotiAdapter(
    private val list: MutableList<Notification>,
    private val context: Context
) : RecyclerView.Adapter<NotiAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = list[position]

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val displayFormatter = DateTimeFormatter.ofPattern("MMM d, hh:mm a")
        val parsedDate = LocalDateTime.parse(data.created, formatter)

        holder.bind.tvDateCreate.text = parsedDate.format(displayFormatter)
        holder.bind.tvMess.text = data.description

        if (data.seen == "0") {
            holder.bind.rlSeen.visibility = View.VISIBLE
        } else {
            holder.bind.rlSeen.visibility = View.INVISIBLE
            val typeface = ResourcesCompat.getFont(context, R.font.inter_regular)
            holder.bind.tvMess.typeface = typeface
        }

        holder.itemView.setOnClickListener {
            seenNotificationApi(data.id, holder.bind.rlSeen, holder.bind.tvMess)
        }

        holder.bind.tvMess.setTextColor(ContextCompat.getColor(context, R.color.black))
    }

    override fun getItemCount(): Int = list.size

    inner class VH(val bind: ItemNotificationBinding) : RecyclerView.ViewHolder(bind.root)

    private fun seenNotificationApi(id: String, rlSeen: RelativeLayout, tv: TextView) {
        val apiService = APIUtils.getAPIService()
        apiService.callApiWithParam("seen.php", id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optBoolean("Status")) {
                            rlSeen.visibility = View.INVISIBLE
                            val typeface = ResourcesCompat.getFont(context, R.font.inter_regular)
                            tv.typeface = typeface
                        } else {
                            Toast.makeText(context, jsonObject.optString("Message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }
}