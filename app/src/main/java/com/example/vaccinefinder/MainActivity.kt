package com.example.vaccinefinder

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var centerList : ArrayList<Center>
    private lateinit var adapter : VaccineListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        centerList = arrayListOf()
        recyclerView.layoutManager = LinearLayoutManager(this)

        search.setOnClickListener{
            closeKeyboard()
            val pincode = pin_code.text.toString()
            if(pincode.length != 6 ){
               pin_code.error = "Please enter valid pincode"
            }
            else{
               centerList.clear()
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    this, { _, year, monthOfYear, dayOfMonth ->
                        val dateStr = """$dayOfMonth-${monthOfYear + 1}-${year}"""

                        fetchData(pincode, dateStr)
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
        }
    }
    private fun fetchData(pincode: String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=$pincode&date=$date"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    Log.e("request", "Success response is $response")
                    try {
                        val sessionsJsonArray = response.getJSONArray("sessions")
                        if (sessionsJsonArray.length() == 0) {
                            Toast.makeText(this, "No centers available", Toast.LENGTH_SHORT).show()
                        }
                        for (i in 0 until sessionsJsonArray.length()) {
                            val sessionObject = sessionsJsonArray.getJSONObject(i)
                            val centerName = sessionObject.getString("name")
                            val address = sessionObject.getString("address")
                            val from = sessionObject.getString("from")
                            val to = sessionObject.getString("to")
                            val feetype = sessionObject.getString("fee_type")
                            val fee = sessionObject.getString("fee")
                            val dose1 = sessionObject.getString("available_capacity_dose1")
                            val dose2 = sessionObject.getString("available_capacity_dose2")
                            val vaccine = sessionObject.getString("vaccine")
                            val age = sessionObject.getString("min_age_limit")

                            val center = Center(
                                    centerName,
                                    address,
                                    from,
                                    to,
                                    feetype,
                                    fee,
                                    dose1,
                                    dose2,
                                    vaccine,
                                    age
                            )
                            centerList.add(center)
                        }
                        adapter = VaccineListAdapter(centerList)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        //adapter.updateCenter(centerList)
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }
                },
                { error ->
                    Log.e("Error", "Response is $error")
                    Toast.makeText(this@MainActivity, "Failed to get response", Toast.LENGTH_SHORT).show()
                }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}