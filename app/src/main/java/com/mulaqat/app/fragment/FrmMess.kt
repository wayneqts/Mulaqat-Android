package com.mulaqat.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.mulaqat.app.R
import com.mulaqat.app.activities.MainActivity
import com.mulaqat.app.adapter.MessAdapter
import com.mulaqat.app.databinding.FrmListBinding
import com.mulaqat.app.model.Message
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class FrmMess : Fragment() {

    lateinit var binding: FrmListBinding
    lateinit var list: MutableList<Message>
    lateinit var adapter: MessAdapter
    lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity = getActivity() as MainActivity
        binding = FrmListBinding.inflate(layoutInflater)
        init()
        binding.swRefresh.setOnRefreshListener { this.getMess() }
        return binding.root
    }

    // init UI
    private fun init() {
        list = ArrayList()
        adapter = MessAdapter(list, activity)
        binding.rcv.adapter = adapter
        binding.rcv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.tvNoData.text = activity.getString(R.string.no_messages_yet)
        binding.swRefresh.setColorSchemeColors(activity.getColor(R.color.red))
        getMess()
    }

    // get message
    private fun getMess() {
        binding.swRefresh.isEnabled = false
        binding.swRefresh.isRefreshing = true
        activity.api.callApiWithParamArr("messages.php", activity.pref.getPf()?.id)
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray?>?,
                    response: Response<JsonArray?>
                ) {
                    val jsonArray: JSONArray
                    list.clear()
                    if (response.isSuccessful) {
                        try {
                            jsonArray = JSONArray(java.lang.String.valueOf(response.body()))
                            if (jsonArray.length() > 0) {
                                for (i in 0..<jsonArray.length()) {
                                    val jsOj = jsonArray[i] as JSONObject
                                    var date = ""
                                    val format = SimpleDateFormat("MMM,dd hh:mm a", Locale.ENGLISH)
                                    format.timeZone = TimeZone.getTimeZone("America/New_York")
                                    try {
                                        val newDate = format.parse(jsOj.optString("time"))
                                        date = SimpleDateFormat(
                                            "MMM dd, hh:mm a",
                                            Locale.ENGLISH
                                        ).format(newDate)
                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                    }
                                    val notesModel = Message(
                                        jsOj.optString("value"),
                                        jsOj.optString("id"),
                                        jsOj.optString("sender_id"),
                                        jsOj.optString("name"),
                                        jsOj.optString("pic"),
                                        jsOj.optString("msg")
                                            .replace("%3F".toRegex(), "?")
                                            .replace("%20".toRegex(), " "),
                                        date,
                                        jsOj.optString("readstatus"), jsOj.optString("id2")
                                    )
                                    if (jsOj.optString("id") != "null") {
                                        list.add(notesModel)
                                    }
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    adapter.notifyDataSetChanged()
                    binding.tvNoData.visibility = if (adapter.itemCount === 0) View.VISIBLE else View.GONE
                    binding.swRefresh.isEnabled = true
                    binding.swRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<JsonArray?>?, t: Throwable?) {
                    binding.swRefresh.isEnabled = true
                    binding.swRefresh.isRefreshing = false
                    binding.tvNoData.visibility = View.VISIBLE
                    Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}