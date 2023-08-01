package pe.idat.jessmyapp.ui.historial
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pe.idat.jessmyapp.R

class HistorialFragment : Fragment()  {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)


        return view
    }

    private fun recuperarCodCliente():Int{
        // Recuperar datos de SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val id_cliente = sharedPreferences.getInt("id_key",0)
        return id_cliente
    }
}