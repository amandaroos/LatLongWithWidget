package com.amandaroos.latlongwithwidget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Implementation of App Widget functionality.
 */
class LatLongAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val RequestPermissionCode = 1
        var mLocation: Location? = null
        lateinit var fusedLocationProviderClient: FusedLocationProviderClient

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        Log.e("Widget", "inside getLstLocation")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Widget", "requestpermission needed")
            //requestPermission()
        } else {
            Log.e("Widget", "in else")
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    Log.e("Widget", "in success listener. lcoation: " + location)
                    Log.e(
                        "Widget",
                        "in success listener. location latitude: " + location?.latitude.toString()
                    )
//                    views.setTextViewText(R.id.longitude_widget, "is this working?")
//                    if (location != null) {
//                        Log.e(
//                            "Widget",
//                            "in success listener. location check. Latitude: " + location?.latitude.toString()
//                        )
//                        Log.e(
//                            "Widget",
//                            "in success listener. location check. mLocation Latitude: " + mLocation?.latitude.toString()
//                        )
//                        views.setTextViewText(R.id.latitude_widget, mLocation?.latitude.toString())
//                        views.setTextViewText(R.id.longitude_widget, "set text")
//                    longitude.text = location.longitude.toString()
//                    time.text = android.text.format.DateFormat.getTimeFormat(applicationContext).format(location.time)
//                    date.text = android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(location.time)
                }
        }

        Log.e(
            "Widget",
            "after getlastlocationcode. mLocation latitude: " + mLocation?.latitude.toString()
        )

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mLocation)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    mLocation: Location?
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.lat_long_app_widget)
    views.setTextViewText(R.id.latitude_widget, mLocation?.latitude.toString())
    views.setTextViewText(R.id.longitude_widget, mLocation?.longitude.toString())
    views.setTextViewText(R.id.time_widget, android.text.format.DateFormat.getTimeFormat(context).format(mLocation?.time))
    views.setTextViewText(R.id.date_widget, android.text.format.DateFormat.getDateFormat(context).format(mLocation?.time))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
