package pe.idat.jessmyapp

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtRecuperar = findViewById(R.id.txtRecuperar)
        tilCorreo = findViewById(R.id.til_correo)
        tilContrasena = findViewById(R.id.til_contrasena)

        jessmiService = JessmiAdapter.getApiService()



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
        call.enqueue(object : retrofit2.Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful) {
                    val clientes = response.body()
                    // Realiza acciones adicionales según sea necesario

                    for (cliente in clientes!!) {
                        val user = "${cliente.nombres}\n" +
                                " ${cliente.apellidos}\n"
                        mostrarDialogoLogin(user)

                    }
                } else {

                    mostrarDialogoError()

                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
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

    private fun mostrarDialogoLogin(user:String) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("¡LOGIN EXITOSO!")
            .setContentText("Hola, Bienvenid@ "+user)
            .setConfirmButton("Continuar") { sweetAlertDialog ->
                // Acciones a realizar al hacer clic en el botón "Entiendo"
                sweetAlertDialog.dismissWithAnimation()
                window.decorView.postDelayed({
                    val intent = Intent(this, LoginSuccess::class.java)
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