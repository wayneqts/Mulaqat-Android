package com.mulaqat.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.mulaqat.app.R
import com.mulaqat.app.activities.MainActivity
import com.mulaqat.app.adapter.NotiAdapter
import com.mulaqat.app.databinding.FrmListBinding
import com.mulaqat.app.model.Notification
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FrmNoti : Fragment() {
    lateinit var adapter: NotiAdapter
    lateinit var list: MutableList<Notification>
    lateinit var activity: MainActivity
    lateinit var binding: FrmListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity = getActivity() as MainActivity
        binding = FrmListBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        list = ArrayList()
        adapter = NotiAdapter(list, activity)
        binding.rcv.adapter = adapter
        binding.tvNoData.text = activity.getString(R.string.no_notifications)
        binding.rcv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.swRefresh.setColorSchemeColors(ContextCompat.getColor(activity, R.color.red))
        getNoti()
    }

    private fun getNoti() {
        binding.swRefresh.isEnabled = false
        binding.swRefresh.isRefreshing = true

        activity.api.callApiWithParam("push_notifications.php", activity.pref.getPf()?.id)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    list.clear()
                    if (response.isSuccessful) {
                        try {
                            val jsonObject = JSONObject(response.body().toString())
                            if (jsonObject.optBoolean("Status")) {
                                val jsAr = jsonObject.optJSONArray("Data")
                                if (jsAr != null && jsAr.length() > 0) {
                                    for (i in 0 until jsAr.length()) {
                                        val jsObj = jsAr.getJSONObject(i)
                                        val data = Notification(
                                            jsObj.optString("id"),
                                            jsObj.optString("description"),
                                            jsObj.optString("seen"),
                                            jsObj.optString("created")
                                        )
                                        list.add(data)
                                    }
                                } else {
                                    binding.tvNoData.visibility = View.VISIBLE
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    adapter.notifyDataSetChanged()
                    binding.tvNoData.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
                    binding.swRefresh.isEnabled = true
                    binding.swRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    binding.swRefresh.isEnabled = true
                    binding.swRefresh.isRefreshing = false
                    Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                }
            })
    }
}