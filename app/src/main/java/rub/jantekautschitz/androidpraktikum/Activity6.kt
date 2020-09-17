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
            if (hauptschlafzimmer.isChecked()) {
                gespeicherteRoutine += "hauptschlafzimmerON"
            }
            else {
                gespeicherteRoutine += "hauptschlafzimmerOFF"
            }
            if (studio.isChecked()) {
                gespeicherteRoutine += "studioON"
            }
            else {
                gespeicherteRoutine += "studioOFF"
            }
            if (terrasse.isChecked()) {
                gespeicherteRoutine += "terrasseON"
            }
            else {
                gespeicherteRoutine += "terrasseOFF"
            }
            if (garderobe.isChecked()) {
                gespeicherteRoutine += "garderobeON"
            }
            else {
                gespeicherteRoutine += "garderobeOFF"
            }
            if (heimkino.isChecked()) {
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
                "Routine gespeichert!",
                Toast.LENGTH_SHORT
            ).show()
        }

        routineAusfuehren.setOnClickListener {

            if(gespeicherteRoutine.contains("hauptschlafzimmerON")) httpRequest("FF_MasterBedroom_Light", "ON")
            else httpRequest("FF_MasterBedroom_Light", "OFF")
            if(gespeicherteRoutine.contains("studioON")) httpRequest("AT_Studio_Light", "ON")
            else httpRequest("AT_Studio_Light", "OFF")
            if(gespeicherteRoutine.contains("terrasseON")) httpRequest("OU_Terrace_Light", "ON")
            else httpRequest("OU_Terrace_Light", "OFF")
            if(gespeicherteRoutine.contains("garderobeON")) httpRequest("GF_Wardrobe_Light", "ON")
            else httpRequest("GF_Wardrobe_Light", "OFF")
            if(gespeicherteRoutine.contains("heimkinoON")) httpRequest("GF_HomeCinema_Light", "ON")
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
        var url : String = "https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/"

        val request = Request.Builder()
            .url(url + raum)
            .post(postBody.toRequestBody(Activity6.MEDIA_TYPE_PLAIN))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })

    }

    companion object {
        val MEDIA_TYPE_PLAIN = "text/plain; charset=utf-8".toMediaType()
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