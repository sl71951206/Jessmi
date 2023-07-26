package pe.idat.jessmyapp.ui.carrito
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import pe.idat.jessmyapp.LoginExitosoActivity

import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.adapter.CarritoAdapter
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.retrofit.JessmiService
import pe.idat.jessmyapp.ui.viewmodel.ComunicacionViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


class CarritoFragment : Fragment() {
    private lateinit var jessmiService: JessmiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rvCarrito: RecyclerView
    private lateinit var txtTotalPagar: TextView
    private lateinit var btnComprarCarrito: Button
    private lateinit var btnGenerarCompra: Button
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
        //Llamar al Servicio

        jessmiService= JessmiAdapter.getApiService()
        rvCarrito = view.findViewById(R.id.rvCarrito)
        txtTotalPagar = view.findViewById(R.id.txtTotalPagar)
        btnComprarCarrito = view.findViewById(R.id.btnComprarCarrito)
        layoutFooter = view.findViewById(R.id.layoutFooter)
        imgCarritoVacio = view.findViewById(R.id.imgCarritoVacio)
        btnGenerarCompra=view.findViewById(R.id.btnGenerarPedido)

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
        txtTotalPagar.text = "S/$totalPagar"

        btnComprarCarrito.setOnClickListener {
            mostrarAlertDialogPago()
        }

        btnGenerarCompra.setOnClickListener{
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("GENERAR PEDIDO")
                .setContentText("¿Estás seguro de que desea realizar el pedido de Productos?")
                .setCancelText("Cancelar")
                .setConfirmText("Realizar Pedido")
                .setConfirmClickListener { sweetAlertDialog ->
                    //En Pedido Confirmado
                    sweetAlertDialog.dismissWithAnimation()
                    requireActivity().window.decorView.postDelayed({
                        val idCliente=recuperarCodCliente()
                        registrarCompra(idCliente,productoList)
                        verificarPermisos(it)
                    }, 175)

                }
                .show()

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

    private fun registrarCompra(idCliente: Int ,productos:List<Producto>) {
        val call = jessmiService.registrarPedido(idCliente,productos)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toastPedidoRegistrado(requireContext())
                } else {
                    println("Error en Registro de Compra")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                println("Error de RED!!!!")
            }
        })
    }

    private fun mostrarDialogoPed_Exitoso() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("¡PEDIDO DE COMPRA EXITOSO!")
            .setContentText("Su Doc. de Pedido Virtual(PDF) se aloja en la carpeta DESCARGAS," +
                    " revíselo detalladamente")
            .setConfirmButton("Confirmar") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isAccepted ->
        if (isAccepted) {
            crearPDF()
        } else {
            Toast.makeText(requireContext(), "PERMISOS DENEGADOS", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificarPermisos(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                crearPDF()
                viewModel.borrarTodo()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                Snackbar.make(
                    view,
                    "ESTE PERMISO ES NECESARIO PARA CREAR EL ARCHIVO",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }.show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }


    private fun crearPDF() {
            try {
                val carpeta = "/archivosJESSMIpdf"
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + carpeta

                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                    Toast.makeText(requireContext(), "CARPETA CREADA", Toast.LENGTH_SHORT).show()
                }

                //recuperarShared
                sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                val name = sharedPreferences.getString("name_key", "")
                val lastname = sharedPreferences.getString("lastname_key", "")

                val fechahora = obtenerFechaHoraActualEnFormato()
                val formatohora=obtenerhora()

                val file = File(dir, "PEDIDOJESSMI$formatohora.pdf")
                val fileOutputStream = FileOutputStream(file)

                val documento = Document()
                PdfWriter.getInstance(documento, fileOutputStream)

                documento.open()

                // Agregar la imagen desde los recursos de la app
                val imagenDrawable = R.drawable.jessmilogooriginal
                val bitmap = BitmapFactory.decodeResource(resources, imagenDrawable)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val image = Image.getInstance(byteArray)
                image.scaleToFit(98f, 98f)

                // Alineamos la imagen al centro del documento
                image.alignment = Element.ALIGN_LEFT

                val descjessmi=Paragraph("Av. 13 de Enero 948 - SJL\n" +
                        "Teléfono: (01)4580547",
                    FontFactory.getFont("arial", 9f, Font.BOLD, BaseColor.BLACK))

                val titulo = Paragraph(
                    "DESCRIPCION DEL PEDIDO: \n\n",
                    FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
                )
                titulo.alignment= Element.ALIGN_CENTER
                val infoclient = Paragraph(
                    "NOMBRE DEL CLIENTE: $name $lastname\n",
                    FontFactory.getFont("arial", 15f, Font.BOLD, BaseColor.MAGENTA)
                )
                val fh = Paragraph(
                    "FECHA Y HORA PEDIDO: $fechahora\n\n",
                    FontFactory.getFont("arial", 15f, Font.BOLD, BaseColor.BLACK)
                )


                // Agregamos la imagen al documento
                documento.add(image)
                documento.add(descjessmi)
                documento.add(titulo)
                documento.add(infoclient)
                documento.add(fh)

                val tabla = PdfPTable(3)
                tabla.addCell("NOMBRE PROD.")
                tabla.addCell("MARCA")
                tabla.addCell("PRECIO")

                for (producto in productoList) {
                    tabla.addCell(producto.nombre)
                    tabla.addCell(producto.marca)
                    tabla.addCell("S/"+producto.precio.toString())
                }

                val total=calcularTotalPagar()
                val totalapagar=Paragraph("\nIMPORTE TOTAL: S/$total",
                    FontFactory.getFont("arial", 15f, Font.BOLD, BaseColor.RED))
                val nota=Paragraph("\nNOTA: Usted deberá presentar este documento de manera presencial en la ferretería JESSMI SRL" +
                        " para el respectivo pago y recojo de sus productos.\n" +
                        "¡MUCHAS GRACIAS POR SU PREFERENCIA!",
                    FontFactory.getFont("arial", 15f, Font.BOLD, BaseColor.BLACK))
                documento.add(tabla)
                documento.add(totalapagar)
                documento.add(nota)

                documento.close()

                mostrarDialogoPed_Exitoso()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
    }

    fun obtenerFechaHoraActualEnFormato(): String {
        val calendario = Calendar.getInstance()
        val formatoFechaHora = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        return formatoFechaHora.format(calendario.time)
    }

    fun obtenerhora():String{
        val calendario=Calendar.getInstance()
        val formatohora=SimpleDateFormat("_dd_MMM&HH_mm_ss")
        return formatohora.format(calendario.time)

    }

    private fun recuperarCodCliente():Int{
        // Recuperar datos de SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val id_cliente = sharedPreferences.getInt("id_key",0)
        return id_cliente
    }

    private fun toastPedidoRegistrado(context: Context) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_pedido_compra_success, null)
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    companion object {
        private const val PAYPAL_REQUEST_CODE = 123
    }




}