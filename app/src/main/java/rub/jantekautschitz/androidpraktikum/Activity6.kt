package rub.jantekautschitz.androidpraktikum

import android.R.array
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Activity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)

        var hauptschlafzimmer : Switch = findViewById<Switch>(R.id.hauptschlafzimmer)
        var studio : Switch = findViewById<Switch>(R.id.studio)
        var terrasse : Switch = findViewById<Switch>(R.id.terrasse)
        var garderobe : Switch = findViewById<Switch>(R.id.garderobe)
        var heimkino : Switch = findViewById<Switch>(R.id.heimkino)
        var aktuelleRoutine : TextView = findViewById<TextView>(R.id.aktuelleRoutine)
        var routineSpeichern : Button = findViewById<Button>(R.id.routineSpeichern)
        var routineLoeschen : Button = findViewById<Button>(R.id.routineLoeschen)

        var alleEinstellungen = arrayOf(hauptschlafzimmer, studio, terrasse, garderobe, heimkino)
        var gespeicherteRoutine = arrayOf<String>("", "", "", "", "")

        routineSpeichern.setOnClickListener {

            for (x in 0 until 5) {
                if ((alleEinstellungen[x].isChecked()) && (gespeicherteRoutine.contains(alleEinstellungen[x].text))) {

                    for (i in 0 until 5) {
                        if (gespeicherteRoutine[i].contentEquals("")) {
                            gespeicherteRoutine[i] = alleEinstellungen[x].text.toString()
                            break
                        }
                    }
                }
            }

            for (j in 0 until 5) {
                aktuelleRoutine.append("\n${gespeicherteRoutine[j]}")
            }
        }
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