package pe.idat.jessmyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.CompraMapper
import pe.idat.jessmyapp.entities.DetalleCompra
import pe.idat.jessmyapp.entities.Producto
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class HistorialAdapter(private val historialList: ArrayList<CompraMapper>)
    :RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>(){


    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCodigo: TextView = itemView.findViewById(R.id.txtCodigoHistorial)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFechaHistorial)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotalHistorial)

        val ibDetallesHistorial: ImageButton = itemView.findViewById(R.id.ibDetallesHistorial)
        val ViewSeparacionHistorial2: View = itemView.findViewById(R.id.ViewSeparacionHistorial2)
        val txtDetallesHistorialTitles: TextView = itemView.findViewById(R.id.txtDetallesHistorialTitles)
        val txtDetallesHistorial: TextView = itemView.findViewById(R.id.txtDetallesHistorial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historial_item, parent, false)
        return HistorialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historialList.size
    }


    override fun onBindViewHolder(holder:HistorialViewHolder, position: Int) {
        val historial=historialList[position]
        val codcomp=historial.cod_compra.toString()
        when (codcomp.length) {
            1 -> {
                holder.txtCodigo.text= "N° Pedido: 00000"+historial.cod_compra.toString()
            }
            2 -> {
                holder.txtCodigo.text= "N° Pedido: 0000"+historial.cod_compra.toString()
            }
            3 -> {
                holder.txtCodigo.text= "N° Pedido: 000"+historial.cod_compra.toString()
            }
            4 -> {
                holder.txtCodigo.text= "N° Pedido: 00"+historial.cod_compra.toString()
            }
            else -> {
                holder.txtCodigo.text= "N° Pedido: 0"+historial.cod_compra.toString()
            }
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val inputDate = inputFormat.parse(historial.fecha_compra)
        val txtDate = outputFormat.format(inputDate)
        holder.txtFecha.text="Fecha y Hora: ${txtDate}"
        holder.txtTotal.text="Monto Total: S/"+historial.total.toString()
        var intentos = 0
        holder.ibDetallesHistorial.setOnClickListener {
            intentos++
            if (intentos%2 != 0) {
                cargarDetalles(historial, holder)
                holder.ibDetallesHistorial.setImageResource(R.drawable.ic_up)
                holder.ViewSeparacionHistorial2.visibility = View.VISIBLE
                holder.txtDetallesHistorialTitles.visibility = View.VISIBLE
                holder.txtDetallesHistorial.visibility = View.VISIBLE
            } else {
                holder.ibDetallesHistorial.setImageResource(R.drawable.ic_down)
                holder.ViewSeparacionHistorial2.visibility = View.GONE
                holder.txtDetallesHistorialTitles.visibility = View.GONE
                holder.txtDetallesHistorial.visibility = View.GONE
            }
        }
    }

    fun cargarDetalles(historial: CompraMapper, holder: HistorialViewHolder) {
        val callStaff = JessmiAdapter.getApiService().buscarDetalleCompraPorCodCompra(historial.cod_compra)
        callStaff.enqueue(object : Callback<List<DetalleCompra>> {
            override fun onResponse(call: Call<List<DetalleCompra>>, response: Response<List<DetalleCompra>>) {
                if (response.isSuccessful) {
                    val lista = ArrayList(response.body())
                    holder.txtDetallesHistorial.text = ""
                    var detallesHistorial = ""
                    val espacios = "       "
                    var cont = 0
                    for (detalle in lista) {
                        cont++
                        if (cont < 10) detallesHistorial += "0${cont}"
                        else detallesHistorial += cont
                        detallesHistorial += espacios
                        when (detalle.cantidad.toString().length) {
                            1 -> detallesHistorial += "   ${detalle.cantidad}"
                            2 -> detallesHistorial += "  ${detalle.cantidad}"
                            3 -> detallesHistorial += " ${detalle.cantidad}"
                            else -> detallesHistorial += detalle.cantidad
                        }
                        detallesHistorial += espacios
                        detallesHistorial += "S/ "
                        detallesHistorial += detalle.producto!!.precio
                        detallesHistorial += espacios
                        detallesHistorial += "${detalle.producto!!.nombre} ${detalle.producto!!.marca}"
                        detallesHistorial += "\n"
                    }
                    holder.txtDetallesHistorial.text = detallesHistorial
                }
            }

            override fun onFailure(call: Call<List<DetalleCompra>>, t: Throwable) {
                println("HAY UN ERROR JOVEN")
            }
        })
    }
}