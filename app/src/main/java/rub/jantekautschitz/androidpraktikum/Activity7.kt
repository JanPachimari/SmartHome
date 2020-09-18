package rub.jantekautschitz.androidpraktikum

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Activity7 : AppCompatActivity() {

    var mAdapter: NfcAdapter? = null
    var mPendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }

        mAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mAdapter == null) {
            // Gerät unterstützt kein NFC
            return
        }
        mPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                javaClass
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var tag: Tag = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
        getDataFromTag(tag, intent)
    }

    fun getDataFromTag(tag: Tag, intent: Intent) {
        Log.d("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", tag.toString())

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