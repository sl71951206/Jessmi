package pe.idat.jessmyapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val delayMillis = 4000L

        window.decorView.postDelayed({
            val Logueado = sharedPreferences.getBoolean("logged_in", false)
            val intent = if (Logueado) {
                Intent(this, LoginExitosoActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, delayMillis)
    }
}