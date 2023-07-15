package pe.idat.jessmyapp.ui.carrito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.CarritoAdapter
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel

class CarritoFragment : Fragment(){
    private lateinit var rvCarrito: RecyclerView
    private lateinit var txtTotalPagar: TextView
    private lateinit var btnComprarCarrito: Button
    private lateinit var carritoAdapter: CarritoAdapter
    private var productoList = ArrayList<Producto>()
    private lateinit var viewModel: ComunicacionViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)
        rvCarrito = view.findViewById(R.id.rvCarrito)
        txtTotalPagar = view.findViewById(R.id.txtTotalPagar)
        btnComprarCarrito = view.findViewById(R.id.btnComprarCarrito)

        viewModel = ViewModelProvider(requireActivity())[ComunicacionViewModel::class.java]


        // Configurar el RecyclerView
        rvCarrito.layoutManager= LinearLayoutManager(context)
        val contenido = viewModel.obtenerContenido().value
        if (contenido != null) {
            productoList.addAll(contenido)
        }
        carritoAdapter = CarritoAdapter(productoList,viewModel)
        rvCarrito.adapter = carritoAdapter

        // Actualizar el total a pagar
        val totalPagar = calcularTotalPagar()
        txtTotalPagar.text = "$totalPagar"

        // Configurar el botón de compra
        btnComprarCarrito.setOnClickListener {
            // Lógica para realizar la compra
        }

        // Observar los cambios en la lista de productos del ViewModel
        viewModel.obtenerContenido().observe(viewLifecycleOwner) { productos ->
            productoList.clear()
            if (productos != null) {
                productoList.addAll(productos)
            }
            carritoAdapter.notifyDataSetChanged()
            // Recalcular el total a pagar
            val totalPagar = calcularTotalPagar()
            txtTotalPagar.text = "S/$totalPagar"
        }



        /* Acceder al contenido del ViewModel y mostrarlo en la consola
        val contenido = viewModel.obtenerContenido().value
        contenido?.let {
            for (producto in it) {
                println("Nombre: ${producto.nombre}, Marca: ${producto.marca}, Precio: ${producto.precio}")
            }
        }*/

        return view
    }

    // Método para calcular el total a pagar
    private fun calcularTotalPagar(): Double {
        var total = 0.0
        for (producto in productoList) {
            total += producto.precio
        }
        return total
    }

}