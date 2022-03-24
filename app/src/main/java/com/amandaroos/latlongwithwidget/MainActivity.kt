package com.amandaroos.latlongwithwidget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        latitude.text = location.latitude.toString()
                        longitude.text = location.longitude.toString()
                        time.text = android.text.format.DateFormat.getTimeFormat(applicationContext).format(location.time)
                        date.text = android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(location.time)
                    }
                    updateWidget()
                }
        }
    }

    fun updateWidget() {
        val ids = AppWidgetManager.getInstance(applicationContext).getAppWidgetIds(
            ComponentName(
                applicationContext,
                LatLongAppWidget::class.java
            )
        )
        for (id in ids) {
            updateAppWidget(applicationContext, AppWidgetManager.getInstance(applicationContext), id, mLocation)
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
    }

    fun buttonClicked(view: View) {
        getLastLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_copy -> {
                val coordinates = "${latitude.text}, ${longitude.text}"

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("text", coordinates)
                clipboard.setPrimaryClip(clip)
                true
            }
            R.id.action_clear -> {
                longitude.text = ""
                latitude.text = ""
                time.text = ""
                date.text = ""
                true
            }
            R.id.action_donate ->{
                val intent = Intent(this@MainActivity, DonateActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}