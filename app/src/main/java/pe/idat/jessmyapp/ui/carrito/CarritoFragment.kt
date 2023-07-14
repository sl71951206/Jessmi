package pe.idat.jessmyapp.ui.carrito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.CarritoAdapter
import pe.idat.jessmyapp.adapter.ProductoAdapter
import pe.idat.jessmyapp.entities.Producto

class CarritoFragment : Fragment(), ProductoAdapter.AddToCartListener {
    private lateinit var rvCarrito: RecyclerView
    private lateinit var txtTotalPagar: TextView
    private lateinit var btnComprarCarrito: Button
    private lateinit var carritoAdapter: CarritoAdapter
    private var productoList = ArrayList<Producto>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)
        rvCarrito = view.findViewById(R.id.rvCarrito)
        txtTotalPagar = view.findViewById(R.id.txtTotalPagar)
        btnComprarCarrito = view.findViewById(R.id.btnComprarCarrito)

        // Configurar el RecyclerView
        rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        carritoAdapter = CarritoAdapter(productoList)
        rvCarrito.adapter = carritoAdapter

        // Actualizar el total a pagar
        val totalPagar = calcularTotalPagar()
        txtTotalPagar.text = "$totalPagar"

        // Configurar el botón de compra
        btnComprarCarrito.setOnClickListener {
            // Lógica para realizar la compra
        }

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

    override fun onAddToCartClicked(producto: Producto) {
        val carritoFragment = requireActivity().supportFragmentManager.findFragmentByTag("carritoFragment") as? CarritoFragment
        carritoFragment?.let {
            if (it.isVisible) {
                it.onAddToCartClicked(producto)
            }
        }
    }

}