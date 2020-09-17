package rub.jantekautschitz.androidpraktikum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Activity7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_7)
    }

    // todo

    override fun onPause() {        // falls App pausiert, setze diese Activity als zuletzt benutzt
        super.onPause()
        val prefs =
            getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("lastActivity", javaClass.name)
        editor.commit()
    }
}