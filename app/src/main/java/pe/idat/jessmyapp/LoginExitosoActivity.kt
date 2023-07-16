package pe.idat.jessmyapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import pe.idat.jessmyapp.databinding.ActivityLoginExitosoBinding

class LoginExitosoActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginExitosoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginExitosoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Bloquear la Activity en Modo Vertical
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setSupportActionBar(binding.appBarLoginExitoso.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_login_exitoso)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        recuperarDatosNavigationView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.login_exitoso, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnCerrarSesion -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("¿QUIERE CERRAR SESIÓN?")
            .setContentText("Parece que quiere Cerrar Sesión...")
            .setConfirmButton("Si Quiero") { sweetAlertDialog ->
                sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("logged_in")
                editor.remove("name_key")
                editor.remove("lastname_key")
                editor.remove("email_key")
                editor.apply()
                sweetAlertDialog.dismissWithAnimation()
                window.decorView.postDelayed({
                    toastLogout(this)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 500)

            }
            .setCancelButton("No quiero") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
            }
            .show()

    }

    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("¿QUIERE CERRAR LA APP?")
            .setContentText("Parece que quiere Cerrar la App...")
            .setConfirmButton("Si Quiero") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                window.decorView.postDelayed({
                    finishAffinity()
                }, 350)

            }
            .setCancelButton("No quiero") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
            }
            .show()
    }

    private fun toastLogout(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.logout_toast, null)
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login_exitoso)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun recuperarDatosNavigationView() {
        // Recuperar datos de SharedPreferences
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name_key", "")
        val lastname = sharedPreferences.getString("lastname_key", "")
        val email = sharedPreferences.getString("email_key", "")

        // Mostrar los datos en el NavigationView
        val headerView = binding.navView.getHeaderView(0)
        val txtPerfil_Nombres = headerView.findViewById<TextView>(R.id.perfilnombres)
        val txt_Perfil_Apellidos = headerView.findViewById<TextView>(R.id.perfilapellidos)
        val txt_Perfil_Correo=headerView.findViewById<TextView>(R.id.perfilcorreo)

        txtPerfil_Nombres.text = "$name"
        txt_Perfil_Apellidos.text = "$lastname"
        txt_Perfil_Correo.text = "$email"
    }
}