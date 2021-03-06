package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_4.*
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

        var undoButton : Button = findViewById<Button>(R.id.undoButton)
        var lichtNachricht : TextView = findViewById<TextView>(R.id.lichtNachricht)
        undoButton.alpha = 0.0F

        undoButton.setOnClickListener {
            httpRequest("OFF")                  // sende Request, um Licht wieder auszuschalten
            lichtNachricht.text = getString(R.string.lichtWiederAus)
            undoButton.alpha = 0.0F
        }

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }
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

        if(neueLichtstufe > grenzwert) {
            Log.d("D", "Licht ist größer als Grenzwert")
        }

        if(neueLichtstufe < grenzwert) {     // Grenzwert unterschritten

            Log.d("D", "Licht ist kleiner als Grenzwert")

            val postBody = "ON"
            httpRequest(postBody)           // sende Request, um Licht einzuschalten

            lichtNachricht.text = getString(R.string.lichtAn)
            undoButton.alpha = 1.0F
        }
    }

    fun httpRequest(postBody : String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://smarthome-imtm.iaw.ruhr-uni-bochum.de/rest/items/FF_MasterBedroom_Light")     // Licht im Hauptschlafzimmer
            .post(postBody.toRequestBody(MEDIA_TYPE_PLAIN))
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {        // Request fehlgeschlagen
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")      // keine Reponse erhalten

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())       // gebe Response aus
                }
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
        sensorManager.unregisterListener(this)      // unterbreche Datenstrom bei Pausieren der App
    }

    companion object {
        val MEDIA_TYPE_PLAIN = "text/plain; charset=utf-8".toMediaType()
    }
}