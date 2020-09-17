package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent


class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        var buttonNull: Button = findViewById<Button>(R.id.buttonNull)
        var buttonEins: Button = findViewById<Button>(R.id.buttonEins)
        var buttonZwei: Button = findViewById<Button>(R.id.buttonZwei)
        var buttonDrei: Button = findViewById<Button>(R.id.buttonDrei)
        var buttonVier: Button = findViewById<Button>(R.id.buttonVier)
        var buttonFuenf: Button = findViewById<Button>(R.id.buttonFuenf)
        var buttonSechs: Button = findViewById<Button>(R.id.buttonSechs)
        var buttonSieben: Button = findViewById<Button>(R.id.buttonSieben)
        var buttonAcht: Button = findViewById<Button>(R.id.buttonAcht)
        var buttonNeun: Button = findViewById<Button>(R.id.buttonNeun)
        var buttonLoeschen: Button = findViewById<Button>(R.id.buttonLoeschen)
        var textView: TextView = findViewById<TextView>(R.id.pinEingabe)
        var textView2: TextView = findViewById<TextView>(R.id.textView2)

        var speicher = getSharedPreferences("Benutzerdaten", Context.MODE_PRIVATE)      // Speicherzugriff
        var pin = speicher.getString("Zugangscode", "")
        var pinExistiert : Boolean = true

        if (pin.isNullOrEmpty()) {
            textView2.text = getString(R.string.nutzerNeu)       // kein Code im Speicher vorhanden
            pinExistiert = false
        } else {
            textView2.text = getString(R.string.nutzerBekannt)   // Code bereits im Speicher vorhanden
        }

        var eingabe: String = ""    // Benutzereingabe

        buttonLoeschen.setOnClickListener {     // Taste zum Löschen der bisherigen Eingabe
            eingabe = ""
            textView.text = ""
        }

        buttonNull.setOnClickListener {     // Taste 0
            eingabe += "0"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonEins.setOnClickListener {     // Taste 1
            eingabe += "1"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonZwei.setOnClickListener {     // Taste 2
            eingabe += "2"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonDrei.setOnClickListener {     // Taste 3
            eingabe += "3"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonVier.setOnClickListener {     // Taste 4
            eingabe += "4"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonFuenf.setOnClickListener {     // Taste 5
            eingabe += "5"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonSechs.setOnClickListener {     // Taste 6
            eingabe += "6"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonSieben.setOnClickListener {     // Taste 7
            eingabe += "7"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonAcht.setOnClickListener {     // Taste 8
            eingabe += "8"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }

        buttonNeun.setOnClickListener {     // Taste 9
            eingabe += "9"
            textView.text = textView.getText().toString() + "*"
            if (eingabe.length == 4) {
                if(!pinExistiert)           // es existiert kein Code, speichere neuen Code ab
                    savePin(eingabe)
                else                        // es existiert bereits ein Code, vergleiche diesen mit Eingabe
                    eingabePruefen(pin, eingabe)
                eingabe = ""
                textView.text = ""
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)        // Activity wird nach Unterbrechung fortgesetzt
    override fun onResume() {
        super.onResume()
        // ???
    }

    private fun eingabePruefen(pin: String?, eingabe: String) {     // vergleiche Eingabe mit gespeichertem Code
        if (eingabe == pin) {
            Toast.makeText(
                this@Activity2,
                getString(R.string.codeRichtig),
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, HomeActivity::class.java)    // falls Übereinstimmung, weiter
            startActivity(intent)
            finish()

        } else {
            Toast.makeText(
                this@Activity2,
                getString(R.string.codeFalsch),     // keine Übereinstimmung, Fehlermeldung
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun savePin(pin: String) {              // speichere Code in Benutzerdaten.xml
        val prefs = getSharedPreferences("Benutzerdaten", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("Zugangscode", pin)
        editor.commit()

        Toast.makeText(
            this@Activity2,
            getString(R.string.codeGespeichert),
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, HomeActivity::class.java)        // sobald gespeichert, wechsle in nächste Activity
        startActivity(intent)
        finish()

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