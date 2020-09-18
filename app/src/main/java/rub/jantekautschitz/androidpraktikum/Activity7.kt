package rub.jantekautschitz.androidpraktikum

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class Activity7 : AppCompatActivity() {

    var mAdapter: NfcAdapter? = null                    // NFC-Adapter
    var mPendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }

        var textEingabe : EditText = findViewById<EditText>(R.id.textEingabe)     // Textfeld zum NFC-Schreiben

        var nfcText: TextView = findViewById<TextView>(R.id.nfcText)

        mAdapter = NfcAdapter.getDefaultAdapter(this)       // prüfe, ob Gerät NFC unterstützt
        if (mAdapter == null) {
            // Gerät unterstützt kein NFC
            Toast.makeText(
                applicationContext,
                getString(R.string.keinNFC),             // Fehlermeldung: Gerät unterstützt NFC nicht
                Toast.LENGTH_LONG
            )
        }
        mPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                javaClass
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        var clearButton : Button = findViewById<Button>(R.id.clearButton)       // lösche aktuelle Eingabe im Textfeld
        clearButton.setOnClickListener {
            textEingabe.setText("")
        }
    }

    override fun onNewIntent(intent: Intent?) {          // NFC-Tag entdeckt, neuer Intent
        super.onNewIntent(intent)
        var tag: Tag = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
        var textEingabe : EditText = findViewById<EditText>(R.id.textEingabe)
        if(textEingabe.getText().isEmpty()) {
            getDataFromTag(tag, intent)                  // falls Textfeld leer: LESEN
        }
        else {
            writeDataToTag(tag, intent)                  // falls Textfeld nicht leer: SCHREIBEN
        }
    }

    fun writeDataToTag(tag: Tag, intent: Intent) {       // SCHREIBEN
        var textEingabe : EditText = findViewById<EditText>(R.id.textEingabe)

        val r1 = NdefRecord.createMime(
            "co/info",
            textEingabe.getText().toString().toByteArray(charset("US-ASCII"))
        )
        val r2 = NdefRecord.createApplicationRecord("rub.jantekautschitz.androidpraktikum")
        val msg = NdefMessage(r1, r2)       // erstelle Nachricht

        val ndef = Ndef.get(tag)
        ndef.connect()                      // verbinde mit Tag und schreibe Nachricht
        ndef.writeNdefMessage(msg)
        ndef.close()

        runOnUiThread {
            Toast.makeText(
                applicationContext,
                getString(R.string.writeSuccess),       // Schreiben erfolgreich
                Toast.LENGTH_LONG
            )
        }
    }

    fun getDataFromTag(tag: Tag, intent: Intent) {      // LESEN

        val ndef = Ndef.get(tag)
        try {
            ndef.connect()                  // verbinde mit Tag und lese Nachricht
            val messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (messages != null) {
                val ndefMessages = arrayOfNulls<NdefMessage>(messages.size)
                for (i in messages.indices) {
                    ndefMessages[i] = messages[i] as NdefMessage
                }
                val record = ndefMessages[0]!!.records[0]
                val payload = record.payload
                var text = String(payload)          // baue Nachricht in passenden String um
                text = text.substringAfter("=U")
                text = text.substringBefore("dePanel")
                Log.e("tag", "Tag: ${text}")
                Toast.makeText(
                    applicationContext,
                    "${text}",                // Ausgabe des gespeicherten Texts
                    Toast.LENGTH_LONG
                )
                    .show()
                ndef.close()

                sendeBefehl(text)

            }
        } catch (e: Exception) {                   // Fehler beim Lesen des NFC-Tags
            Toast.makeText(
                applicationContext,
                "Tag konnte nicht gelesen werden.",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun sendeBefehl(text: String) {
        var text2 = text.toUpperCase()
        if (text2.contains("HAUPTSCHLAFZIMMER AN") || text2.contains("MASTER BEDROOM ON"))
            httpRequest("FF_MasterBedroom_Light", "ON")
        if (text2.contains("HAUPTSCHLAFZIMMER AUS") || text2.contains("MASTER BEDROOM OFF"))
            httpRequest("FF_MasterBedroom_Light", "OFF")
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

    override fun onResume() {
        super.onResume()
        mAdapter?.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    override fun onPause() {        // falls App pausiert, setze diese Activity als zuletzt benutzt
        super.onPause()
        val prefs =
            getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("lastActivity", javaClass.name)
        editor.commit()

        mAdapter?.disableForegroundDispatch(this)
    }


}