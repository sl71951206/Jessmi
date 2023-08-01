package pe.idat.jessmyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.idat.jessmyapp.R
import pe.idat.jessmyapp.entities.DetalleCompraMapper

class HistorialAdapter(private val historialList: ArrayList<DetalleCompraMapper>)
    :RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>(){


    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCodigo: TextView = itemView.findViewById(R.id.txtCodigoHistorial)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFechaHistorial)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotalHistorial)
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

        holder.txtFecha.text="Fecha y Hora: ${historial.fecha_compra}"
        holder.txtTotal.text="Monto Total: S/"+historial.total.toString()
    }
}