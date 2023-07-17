package pe.idat.jessmyapp.ui.carrito

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.CarritoAdapter
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class CarritoFragment : Fragment() {
    private lateinit var rvCarrito: RecyclerView
    private lateinit var txtTotalPagar: TextView
    private lateinit var btnComprarCarrito: Button
    private lateinit var carritoAdapter: CarritoAdapter
    private var productoList = ArrayList<Producto>()
    private lateinit var viewModel: ComunicacionViewModel
    private lateinit var layoutFooter: LinearLayout
    private lateinit var imgCarritoVacio: ImageView
    private lateinit var config: PayPalConfiguration


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)
        rvCarrito = view.findViewById(R.id.rvCarrito)
        txtTotalPagar = view.findViewById(R.id.txtTotalPagar)
        btnComprarCarrito = view.findViewById(R.id.btnComprarCarrito)
        layoutFooter = view.findViewById(R.id.layoutFooter)
        imgCarritoVacio = view.findViewById(R.id.imgCarritoVacio)

        imgCarritoVacio = view.findViewById(R.id.imgCarritoVacio)
        viewModel = ViewModelProvider(requireActivity())[ComunicacionViewModel::class.java]


        // RecyclerView
        rvCarrito.layoutManager = LinearLayoutManager(context)
        val contenido = viewModel.obtenerContenido().value
        if (contenido != null) {
            productoList.addAll(contenido)
        }
        carritoAdapter = CarritoAdapter(productoList, viewModel)
        rvCarrito.adapter = carritoAdapter

        // Actualizar el total a pagar
        val totalPagar = calcularTotalPagar()
        txtTotalPagar.text = "$totalPagar"

        btnComprarCarrito.setOnClickListener {
            mostrarAlertDialogPago()
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
            txtTotalPagar.text = "$totalPagar"
            // Mostrar u ocultar el ImageView según el carrito esté vacío o no
            if (productoList.isEmpty()) {
                imgCarritoVacio.visibility = View.VISIBLE
                rvCarrito.visibility = View.GONE
                layoutFooter.visibility = View.GONE
            } else {
                imgCarritoVacio.visibility = View.GONE
                rvCarrito.visibility = View.VISIBLE
                layoutFooter.visibility = View.VISIBLE
            }
        }

        // Configurar PayPal
        config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AZLGXKBVJ5fJ16Qw0oRYD_QTyzWChS8mUWfQXYoqG2k8O8GpeaZm48QeoqargkuAb-lwZ6pLr36SJcbj")


        /* Acceder al contenido del ViewModel y mostrarlo en la consola
        val contenido = viewModel.obtenerContenido().value
        contenido?.let {
            for (producto in it) {
                println("Nombre: ${producto.nombre}, Marca: ${producto.marca}, Precio: ${producto.precio}")
            }
        }*/

        return view
    }

    private fun mostrarAlertDialogPago() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Realizar pago con PayPal")
            .setContentText("¿Estás seguro de que deseas realizar el pago?")
            .setCancelText("Cancelar")
            .setConfirmText("Realizar Pago")
            .setConfirmClickListener { sweetAlertDialog ->
                iniciarPagoPayPal()
                val zoneId = ZoneId.of("America/Lima") // Zona horaria de Lima, Perú
                val currentTime = LocalDateTime.now(zoneId)

                val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

                val formattedDate = currentTime.format(dateFormatter)
                val formattedTime = currentTime.format(timeFormatter)

                println("Fecha: $formattedDate") // Imprimir la fecha en el formato deseado
                println("Hora: $formattedTime")
                sweetAlertDialog.dismiss()
            }
            .show()
    }

    private fun iniciarPagoPayPal() {
        val totalPagar = calcularTotalPagar()

        val payment = PayPalPayment(
            BigDecimal(totalPagar.toString()),
            "USD",
            "Descripción del pago",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm: PaymentConfirmation? =
                    data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    // Procesar confirmación de pago
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Método para calcular el total a pagar
    private fun calcularTotalPagar(): Double {
        var total = 0.0
        for (producto in productoList) {
            total += producto.precio
        }
        return total
    }

    companion object {
        private const val PAYPAL_REQUEST_CODE = 123
    }

}