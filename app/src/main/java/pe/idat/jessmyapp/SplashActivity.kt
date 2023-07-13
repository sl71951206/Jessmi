package pe.idat.jessmyapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast

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
                toastLogueado(this)
                Intent(this, LoginExitosoActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, delayMillis)
    }

    private fun toastLogueado(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.login_detectado_toast, null)

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}