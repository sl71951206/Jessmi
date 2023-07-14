package pe.idat.jessmyapp.ui.catalogo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.ui.carrito.CarritoFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoFragment : Fragment(), ProductoAdapter.AddToCartListener {
    private lateinit var rvProductos: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)
        rvProductos = view.findViewById(R.id.rv_Catalogue)
        rvProductos.layoutManager = LinearLayoutManager(context)
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
                    productoAdapter = ProductoAdapter(list, this@CatalogoFragment) // Añadir 'this@CatalogoFragment'
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

    override fun onAddToCartClicked(producto: Producto) {
        val carritoFragment = requireActivity().supportFragmentManager.findFragmentByTag("carritoFragment") as? CarritoFragment
        carritoFragment?.onAddToCartClicked(producto)
    }

}