package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException


class Activity3 : AppCompatActivity() {

    val client = OkHttpClient()
    var antwort : String = "..."    // Antwort auf http Request
    var tempGefuehlt : String = "..."
    var tempAussen : String = "..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        @Serializable
        data class Project(val state: String)

        // REST-Endpunkte
        val urlGefuehlt = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/openweathermap_weather_and_forecast_7174e084_local_current_apparent_temperature"
        val urlAussen = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/openweathermap_weather_and_forecast_7174e084_local_current_temperature"

        var textGefuehlt : TextView = findViewById<TextView>(R.id.textGefuehlt)
        var textAussen : TextView = findViewById<TextView>(R.id.textAussen)

        var shareButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.shareButton)   // Teilen-Button
        shareButton.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"                                                           // Nachricht beim Teilen
            val shareBody = getString(R.string.msgBody1) + " ${tempAussen} " + getString(R.string.msgBody2) + " ${tempGefuehlt} " + getString(
                R.string.msgBody3
            )
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.aktuellesWetter))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.teilenVia)))
        }

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }

        fun urlAbrufen(url: String) {

            val request = Request.Builder()     // erstelle Request mit übergebener URL
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                    Log.d("Fehler", "Fehler")

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonString = response.body?.string().toString()
                    val obj =
                        Json { ignoreUnknownKeys = true }.decodeFromString<Project>(jsonString)

                    if (url == urlGefuehlt) {
                        tempGefuehlt = obj.state
                        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", tempGefuehlt)
                        runOnUiThread { textGefuehlt.text = obj.state }
                    }
                    if (url == urlAussen) {
                        tempAussen = obj.state
                        Log.d("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", tempAussen)
                        runOnUiThread { textAussen.text = obj.state }
                    }
                }
            })
        }
        urlAbrufen(urlGefuehlt)
        urlAbrufen(urlAussen)
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