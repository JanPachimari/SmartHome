package rub.jantekautschitz.androidpraktikum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Dispatcher : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityClass: Class<*>
        activityClass = try {
            val prefs =
                getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)    // greife auf LetzteActivity.xml zu
            Class.forName(
                prefs.getString("lastActivity", MainActivity::class.java.getName())   // rufe zuletzt genutzte Activity ab
            )
        } catch (ex: ClassNotFoundException) {
            MainActivity::class.java
        }
        startActivity(Intent(this, activityClass))      // starte die (laut Speicher) zuletzt genutzte Activity
    }
}