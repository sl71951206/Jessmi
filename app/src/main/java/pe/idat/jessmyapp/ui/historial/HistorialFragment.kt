package pe.idat.jessmyapp.ui.historial
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.HistorialAdapter
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.adapter.ProductoAdapterGuest
import pe.idat.jessmyapp.entities.DetalleCompraMapper
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialFragment : Fragment()  {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rvHistorial: RecyclerView
    private lateinit var imgHistorialEmpty:ImageView
    private lateinit var historialAdapter: HistorialAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        rvHistorial = view.findViewById(R.id.rvHistorial)
        imgHistorialEmpty=view.findViewById(R.id.imgHistorialVacio)
        rvHistorial.layoutManager = LinearLayoutManager(context)

        val callStaff = JessmiAdapter.getApiService().historialPedidos(recuperarCodCliente())
        callStaff.enqueue(object : Callback<List<DetalleCompraMapper>> {
            override fun onResponse(call: Call<List<DetalleCompraMapper>>, response: Response<List<DetalleCompraMapper>>) {
                if (response.isSuccessful) {
                    val list = ArrayList(response.body())
                    if (list.isNotEmpty()){
                        //RV VISIBLE - MENSAJE OCULTO
                        imgHistorialEmpty.visibility=View.GONE
                        rvHistorial.visibility=View.VISIBLE
                        historialAdapter = HistorialAdapter(list)
                        rvHistorial.adapter = historialAdapter
                        historialAdapter.notifyDataSetChanged()
                    }

                    println("HISTORIAL SIN REGISTROS")

                }
            }

            override fun onFailure(call: Call<List<DetalleCompraMapper>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })





        return view
    }

    private fun recuperarCodCliente():Int{
        // Recuperar datos de SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val id_cliente = sharedPreferences.getInt("id_key",0)
        return id_cliente
    }
}