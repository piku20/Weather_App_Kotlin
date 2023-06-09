package com.example.weather

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.example.weather.databinding.ActivityMainBinding
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_main)

        val lat = intent.getStringExtra("lat")
        var long = intent.getStringExtra("long")

        window.statusBarColor = Color.parseColor("#1383C3")
        getJsonData(lat, long)
    }

    private fun getJsonData(lat:String?, long:String?)
    {
        val API_KEY="d782c3641dcc9cbcb8528dfb0245b3e4"
        val queue = Volley.newRequestQueue(this)
        val url ="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response -> setValues(response) },
            Response.ErrorListener { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() }
        )
        queue.add(jsonRequest)
    }

    private fun setValues(response:JSONObject){
        //binding.city = response.optString("name")
        binding.city.text = response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        binding.coordinates.text = "${lat}, ${long}"
        binding.weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr=response.getJSONObject("main").getString("temp")
        tempr=((((tempr).toFloat()-273.15)).toInt()).toString()
        binding.temp.text="${tempr}°C"

        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        binding.minTemp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        binding.maxTemp.text=maxtemp+"°C"

        binding.pressure.text=response.getJSONObject("main").getString("pressure")
        binding.humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        binding.wind.text=response.getJSONObject("wind").getString("speed")
        binding.degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"
    }
}