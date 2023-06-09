package com.example.weather

import android.annotation.SuppressLint
import android.app.Activity
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class SplashScreen : AppCompatActivity() {

    lateinit var mfusedlocation:FusedLocationProviderClient
    private var myRequestCode = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }

    //    1. location permission --> deny
    //    2. location denied through settings
    //    3. gps off
    //    4. permission le lo

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(checkPermission()){
            if(LocationEnale()){
                mfusedlocation.lastLocation.addOnCompleteListener{
                    task ->
                    //{
                        var location: Location? = task.result

                        if(location==null){
                            NewLocation()
                        }else{
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("lat", location.latitude.toString())
                                intent.putExtra("long", location.longitude.toString())
                                startActivity(intent)
                                finish()
                            }, 2000)
                        }
                    //}
                }
            }else{
                Toast.makeText(this, "Please Turn ON your GPS location", Toast.LENGTH_LONG).show()
            }
        }else {
            RequestPermission()
        }

    }

    private fun checkPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    private fun LocationEnale(): Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation(){
        var locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1

        mfusedlocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private val locationCallback = object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location? = p0.lastLocation // ? added by me - experiment
        }
    }

    private fun RequestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), myRequestCode)
    }


}