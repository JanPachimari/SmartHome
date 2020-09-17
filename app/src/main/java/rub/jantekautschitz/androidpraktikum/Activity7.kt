package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Activity7 : AppCompatActivity() {

    var mNfcAdapter: NfcAdapter? = null
    //var nfcText: TextView = findViewById<TextView>(R.id.nfcText)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }

        /*m
        NfcAdapter = NfcAdapter.getDefaultAdapter(this);       // NFC-Adapter

        if (mNfcAdapter == null) {
            Toast.makeText(this, "Dieses Gerät unterstützt NFC nicht.", Toast.LENGTH_LONG).show();
            finish();                           // beende Activity, falls Gerät NFC nicht unterstützt
            return;
        }

        if (!mNfcAdapter!!.isEnabled()) {       // NFC ist auf dem Gerät nicht eingeschaltet
            nfcText.text="NFC ist ausgeschaltet."
        }

        handleIntent(getIntent());
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

            }
        }
        else {
            Log.d("NFC", "Fehler: Intent = null")
        }
        */
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