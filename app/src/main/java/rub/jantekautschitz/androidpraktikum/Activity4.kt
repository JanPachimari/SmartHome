package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class Activity4 : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lichtstufe: Sensor? = null              // speichert aktuellen Lichtwert

    var grenzwert : Double = 100.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lichtstufe = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)      // Helligkeitssensor
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lichtstufe, SensorManager.SENSOR_DELAY_NORMAL)       // registriere Listener für Sensor
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("D", "onAccuracyChanged")
    }

    override fun onSensorChanged(event: SensorEvent) {      // neue Sensordaten erhalten
        val neueLichtstufe = event.values[0]
        var textHelligkeit : TextView = findViewById<TextView>(R.id.textHelligkeit)
        textHelligkeit.text = "${neueLichtstufe}"           // aktualisiere momentanen Wert in TextView

        var unterGrenzwert : Boolean = true

        if(neueLichtstufe > grenzwert) {
            unterGrenzwert = false
            Log.d("D", "Licht ist größer als Grenzwert")
        }

        if(neueLichtstufe < grenzwert) {     // Grenzwert unterschritten

            Log.d("D", "Licht ist kleiner als Grenzwert")

            val client = OkHttpClient()
            val postBody = "ON"

            val request = Request.Builder()
                .url("https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/GF_LivingRoom_Light/state")
                .put(postBody.toRequestBody(MEDIA_TYPE_PLAIN))
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

            Toast.makeText(
                this@Activity4,
                "Das Licht wurde eingeschaltet!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onPause() {        // falls App pausiert, setze diese Activity als zuletzt benutzt
        super.onPause()
        val prefs =
            getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("lastActivity", javaClass.name)
        editor.commit()
        sensorManager.unregisterListener(this)      // unterbreche Datenstrom bei Pausieren der App
    }

    companion object {
        val MEDIA_TYPE_PLAIN = "text/plain; charset=utf-8".toMediaType()
    }
}