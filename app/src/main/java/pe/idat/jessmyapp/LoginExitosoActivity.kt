package pe.idat.jessmyapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.textfield.TextInputLayout
import pe.idat.jessmyapp.databinding.ActivityLoginExitosoBinding
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.retrofit.JessmiService
import retrofit2.Call
import retrofit2.Response

class LoginExitosoActivity : AppCompatActivity() {
    private lateinit var jessmiService: JessmiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginExitosoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginExitosoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        jessmiService = JessmiAdapter.getApiService()
        //Bloquear la Activity en Modo Vertical
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setSupportActionBar(binding.appBarLoginExitoso.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_login_exitoso)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_historial
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
            R.id.btnEliminarCuenta -> eliminarCuenta()
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
                editor.remove("id_key")
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

    private fun eliminarCuenta() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("¿ESTÁ SEGURO QUE QUIERE ELIMINAR SU CUENTA?")
            .setContentText("Esta acción no se puede deshacer.")
            .setConfirmButton("Si Quiero") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                window.decorView.postDelayed({
                    credencialesDeNuevo(this)
                }, 500)

            }
            .setCancelButton("No quiero") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
            }
            .show()
    }

    private fun credencialesDeNuevo(contextEliminacion: Context) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.eliminar_cuenta_layout, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        val dialogText = dialogView.findViewById<TextInputLayout>(R.id.til_correo)
        dialogText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialogText.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })
        val dialogText2 = dialogView.findViewById<TextInputLayout>(R.id.til_contrasena)
        dialogText2.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialogText2.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        val dialogButton = dialogView.findViewById<Button>(R.id.btnEliminacion)
        dialogButton.setOnClickListener {
            sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("email_key", "")
            val txt = dialogText.editText?.text.toString()
            val txt2 = dialogText2.editText?.text.toString()
            if (txt == "" || txt2 == "") {
                if (txt == "") {
                    dialogText.error = "Este campo no puede estar vacío"
                }
                if (txt2 == "") {
                    dialogText2.error = "Este campo no puede estar vacío"
                }
            } else if (txt != email) {
                dialogText.error = "Este no es su correo"
            }
            else {
                buscarCliente(txt, txt2, dialogText, dialogText2, alertDialog, contextEliminacion)
            }
        }
        alertDialog.show()
    }

    private fun buscarCliente(correo: String, contrasena: String, txt: TextInputLayout, txt2: TextInputLayout, alerta: AlertDialog, contextEliminacion: Context) {
        val cliente = Cliente(0, "", "", correo, contrasena)
        val intent2 = Intent(this, MainActivity::class.java)
        val call = jessmiService.eliminarCuenta(cliente)
        call.enqueue(object: retrofit2.Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    alerta.dismiss()
                    sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.remove("logged_in")
                    editor.remove("id_key")
                    editor.remove("name_key")
                    editor.remove("lastname_key")
                    editor.remove("email_key")
                    editor.apply()
                    window.decorView.postDelayed({
                        toastEliminacion(contextEliminacion)
                        startActivity(intent2)
                        finish()
                    }, 500)
                } else {
                    txt2.error = "Contraseña incorrecta"
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {}
        })
    }

    private fun toastEliminacion(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.eliminacion_toast, null)
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
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