package rub.jantekautschitz.androidpraktikum

import android.R.array
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class Activity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)

        var hauptschlafzimmer : Switch = findViewById<Switch>(R.id.hauptschlafzimmer)
        var studio : Switch = findViewById<Switch>(R.id.studio)
        var terrasse : Switch = findViewById<Switch>(R.id.terrasse)
        var garderobe : Switch = findViewById<Switch>(R.id.garderobe)
        var heimkino : Switch = findViewById<Switch>(R.id.heimkino)
        var routineSpeichern : Button = findViewById<Button>(R.id.routineSpeichern)
        var routineAusfuehren : Button = findViewById<Button>(R.id.routineAusfuehren)

        var gespeicherteRoutine : String = ""

        var speicher = getSharedPreferences("Routine", Context.MODE_PRIVATE)      // Speicherzugriff
        gespeicherteRoutine = speicher.getString("GespeicherteRoutine", "").toString()

        if(gespeicherteRoutine.contains("hauptschlafzimmerON")) hauptschlafzimmer.setChecked(true)
        if(gespeicherteRoutine.contains("studioON")) studio.setChecked(true)
        if(gespeicherteRoutine.contains("terrasseON")) terrasse.setChecked(true)
        if(gespeicherteRoutine.contains("garderobeON")) garderobe.setChecked(true)
        if(gespeicherteRoutine.contains("heimkinoON")) heimkino.setChecked(true)

        routineSpeichern.setOnClickListener {

            gespeicherteRoutine = ""
            if (hauptschlafzimmer.isChecked()) {                // Hauptschlafzimmer
                gespeicherteRoutine += "hauptschlafzimmerON"
            }
            else {
                gespeicherteRoutine += "hauptschlafzimmerOFF"
            }
            if (studio.isChecked()) {                           // Studio
                gespeicherteRoutine += "studioON"
            }
            else {
                gespeicherteRoutine += "studioOFF"
            }
            if (terrasse.isChecked()) {                         // Terrasse
                gespeicherteRoutine += "terrasseON"
            }
            else {
                gespeicherteRoutine += "terrasseOFF"
            }
            if (garderobe.isChecked()) {                        // Garderobe
                gespeicherteRoutine += "garderobeON"
            }
            else {
                gespeicherteRoutine += "garderobeOFF"
            }
            if (heimkino.isChecked()) {                         // Heimkino
                gespeicherteRoutine += "heimkinoON"
            }
            else {
                gespeicherteRoutine += "heimkinoOFF"
            }

            Log.d("Debug", gespeicherteRoutine)
            val prefs = getSharedPreferences("Routine", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("GespeicherteRoutine", gespeicherteRoutine)
            editor.commit()

            Toast.makeText(
                this@Activity6,
                getString(R.string.routineGespeichert),
                Toast.LENGTH_SHORT
            ).show()
        }

        routineAusfuehren.setOnClickListener {

            if(gespeicherteRoutine.contains("hauptschlafzimmerON")) httpRequest("FF_MasterBedroom_Light", "ON")     // Hauptschlafzimmer
            else httpRequest("FF_MasterBedroom_Light", "OFF")
            if(gespeicherteRoutine.contains("studioON")) httpRequest("AT_Studio_Light", "ON")                       // Studio
            else httpRequest("AT_Studio_Light", "OFF")
            if(gespeicherteRoutine.contains("terrasseON")) httpRequest("OU_Terrace_Light", "ON")                    // Terrasse
            else httpRequest("OU_Terrace_Light", "OFF")
            if(gespeicherteRoutine.contains("garderobeON")) httpRequest("GF_Wardrobe_Light", "ON")                  // Garderobe
            else httpRequest("GF_Wardrobe_Light", "OFF")
            if(gespeicherteRoutine.contains("heimkinoON")) httpRequest("GF_HomeCinema_Light", "ON")                 // Heimkino
            else httpRequest("GF_HomeCinema_Light", "OFF")

        }

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }
    }

    private fun httpRequest(raum: String, postBody: String) {

        val client = OkHttpClient()
        var url : String = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/"      // REST-Endpunkt

        val request = Request.Builder()
            .url(url + raum)
            .post(postBody.toRequestBody(Activity6.MEDIA_TYPE_PLAIN))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {                    // Request fehlgeschlagen
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")      // Antwort nicht erfolgreich

                    for ((name, value) in response.headers) {                                       // Antwort erfolgreich
                        println("$name: $value")
                    }

                    println(response.body!!.string())       // gebe Antwort in Konsole aus
                }
            }
        })

    }

    companion object {
        val MEDIA_TYPE_PLAIN = "text/plain; charset=utf-8".toMediaType()        // MediaType: einfacher Text
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