package pe.idat.jessmyapp.ui.catalogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.ui.carrito.CarritoFragment
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoFragment : Fragment(){
    private lateinit var rvProductos: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var viewModel: ComunicacionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)
        rvProductos = view.findViewById(R.id.rv_Catalogue)
        rvProductos.layoutManager = LinearLayoutManager(context)

        // Obtener referencia al ComunicacionViewModel
        viewModel = ViewModelProvider(requireActivity())[ComunicacionViewModel::class.java]

        val callStaff = JessmiAdapter.getApiService().getProductos()
        callStaff.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val list = ArrayList(response.body())
                    for (producto in list) {
                        println("Nombre: ${producto.nombre}")
                        println("Marca: ${producto.marca}")
                        println("Precio: ${producto.precio}")
                        // Opcionalmente, puedes mostrar los resultados en un TextView o en otro elemento de la interfaz de usuario
                    }
                    productoAdapter = ProductoAdapter(list,viewModel)
                    rvProductos.adapter = productoAdapter
                    productoAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })
        //retornar vista
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configurar los listeners de los botones u otros elementos de la interfaz
    }


}