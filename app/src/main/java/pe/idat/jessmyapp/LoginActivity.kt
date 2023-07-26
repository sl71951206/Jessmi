package pe.idat.jessmyapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.retrofit.JessmiService
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var jessmiService: JessmiService
    private lateinit var txtRecuperar: TextView
    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tilContrasena: TextInputLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Bloquear la Activity en Modo Vertical
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        txtRecuperar = findViewById(R.id.txtRecuperar)
        tilCorreo = findViewById(R.id.til_correo)
        tilContrasena = findViewById(R.id.til_contrasena)

        jessmiService = JessmiAdapter.getApiService()


        //Se Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val correo = tilCorreo.editText?.text.toString()
            val contrasena = tilContrasena.editText?.text.toString()

            tilCorreo.clearFocus()
            tilContrasena.clearFocus()
            if (validateInputs(correo, contrasena)) {
                login(correo, contrasena)
            }
        }

        //Al Cambio de Texto en tils
        tilCorreo.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilCorreo.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        tilContrasena.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilContrasena.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })




        txtRecuperar = findViewById(R.id.txtRecuperar)
        txtRecuperar.setOnClickListener {
            showRecuperarDialog()
        }
    }

    private fun showRecuperarDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.recuperar_dialog_layout, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        val dialogButton = dialogView.findViewById<Button>(R.id.btnRecuperar)
        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun login(correo: String, contrasena: String) {
        // Crea la llamada a la API para el endpoint de validarCredenciales
        val call = jessmiService.validarCredenciales(correo, contrasena)

        // Realiza la llamada asíncrona
        call.enqueue(object : retrofit2.Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    val clientes = response.body()
                    // Realiza acciones adicionales según sea necesario
                        val user = "${clientes?.nombres}\n" +
                                " ${clientes?.apellidos}\n"
                        val idkey=clientes!!.id_cliente
                        val namekey="${clientes?.nombres}"
                        val lastnamekey="${clientes?.apellidos}"
                        val emailkey="${clientes?.correo}"
                        mostrarDialogoLogin(user,idkey,namekey,lastnamekey,emailkey)
                } else {

                    mostrarDialogoError()

                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                // Error de red u otro error en la llamada
                // ...
            }
        })
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            tilCorreo.error = "Ingrese su correo electrónico"
            isValid = false
        } else {
            tilCorreo.error = null
        }

        if (password.isEmpty()) {
            tilContrasena.error = "Ingrese su contraseña"
            isValid = false
        } else {
            tilContrasena.error = null
        }

        return isValid
    }

    private fun mostrarDialogoLogin(user:String,id_key:Int,name_key:String,lastname_key:String,email_key:String) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("¡LOGIN EXITOSO!")
            .setContentText("Hola, Bienvenid@ "+user)
            .setConfirmButton("Continuar") { sweetAlertDialog ->
                // Acciones a realizar al hacer clic en el botón "Entiendo"
                sweetAlertDialog.dismissWithAnimation()
                // Guardar el indicador de inicio de sesión exitoso en SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean("logged_in", true)
                editor.putInt("id_key", id_key)
                editor.putString("name_key", name_key) // Almacena el valor de userkey en SharedPreferences
                editor.putString("lastname_key",lastname_key)
                editor.putString("email_key",email_key)
                editor.apply()
                window.decorView.postDelayed({
                    val intent = Intent(this, LoginExitosoActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 200)
            }
            .show()
    }

    private fun mostrarDialogoError() {

        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("UY, PARECE QUE HUBO UN ERROR...")
            .setContentText("Tus Credenciales son Incorrectas.\nVuelve a Intentarlo")
            .setConfirmButton("Entiendo") { sweetAlertDialog ->
                // Acciones a realizar al hacer clic en el botón "Entiendo"
                sweetAlertDialog.dismissWithAnimation()
            }
            .show()
    }

}