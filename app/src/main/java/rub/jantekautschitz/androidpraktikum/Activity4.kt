package rub.jantekautschitz.androidpraktikum

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView


class Activity4 : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lichtstufe: Sensor? = null              // speichert aktuellen Lichtwert

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

}