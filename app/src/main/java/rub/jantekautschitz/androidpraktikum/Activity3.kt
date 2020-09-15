package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException


class Activity3 : AppCompatActivity() {

    val client = OkHttpClient()
    var antwort : String = "..."
    var tempGefuehlt : String = ""
    var tempAussen : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        Log.d("D", "onCreate wird aufgerufen")

        var textGefuehlt: TextView = findViewById(R.id.textGefuehlt)
        var textAussen: TextView = findViewById(R.id.textAussen)

        val urlGefuehlt = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/openweathermap_weather_and_forecast_7174e084_local_current_apparent_temperature"
        val urlAussen = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/openweathermap_weather_and_forecast_7174e084_local_current_temperature"

        urlAbrufen(urlGefuehlt)
        Thread.sleep(750)
        tempGefuehlt = antwort
        textGefuehlt.text = antwort

        urlAbrufen(urlAussen)
        Thread.sleep(750)
        tempAussen = antwort
        textAussen.text = antwort

        var shareButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Hallo!\nGerade haben wir ${tempAussen} Außentemperatur\nund gefühlte ${tempGefuehlt} Außentemperatur."
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Aktuelles Wetter")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Teilen via"))
        }

        var weiterButton : Button = findViewById<Button>(R.id.weiterButton)
        weiterButton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)        // Weiter-Button zu nächster Activity
            startActivity(intent)
        }
    }

    fun urlAbrufen(url : String) {

        Log.d("D", "urlAbrufen wird aufgerufen")

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(request: Request, e: IOException?) {

                antwort = "Fehler bei der Anfrage"
            }

            override fun onResponse(response: Response) {

                val jsonString : String = response.body().string()
                val result = jsonString.substringAfter("\"state\":\"").substringBefore('"')
                antwort = result

            }
        })
    }

    override fun onPause() {        // falls App pausiert, setze diese Activity als zuletzt benutzt
        super.onPause()
        val prefs =
            getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("lastActivity", javaClass.name)
        editor.commit()
    }

}