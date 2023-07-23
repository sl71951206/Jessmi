package pe.idat.jessmyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.idat.jessmyapp.entities.Producto

class ComunicacionViewModel : ViewModel() {
    private val listaProductos: MutableLiveData<List<Producto>?> = MutableLiveData()

    // Función para agregar un producto a la lista
    fun agregarProducto(producto: Producto) {
        val currentList = listaProductos.value.orEmpty().toMutableList()
        currentList.add(producto)
        listaProductos.value = currentList
    }

    // Método para obtener el contenido del ViewModel
    fun obtenerContenido(): LiveData<List<Producto>?> {
        return listaProductos
    }

    fun eliminarProducto(position: Int) {
        val currentList = listaProductos.value.orEmpty().toMutableList()
        if (position in currentList.indices) {
            currentList.removeAt(position)
            listaProductos.value = currentList
        }
    }

    fun borrarTodo() {
        listaProductos.value = emptyList()
    }

}