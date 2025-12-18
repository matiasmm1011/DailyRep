package com.example.dailyrep.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.CrearRutinaActivity
import com.example.dailyrep.RutinasActivity.Companion.ID_RUTINA
import com.example.dailyrep.databinding.EjercicioEntrenamientoAdapterBinding
import com.example.dailyrep.databinding.MenuCrearRutinaBinding
import com.example.dailyrep.databinding.MenuEditarRutinaBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.itemEntrenamiento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EjercicioEntrenamientoAdapter(
    private val onTresPuntosClick: (View, itemEntrenamiento) -> Unit,
    private val onCheckClick:(SeriePlanificada,Int,Int)->Unit,
    private val onGuardarNotasClick: (itemEntrenamiento, mandarNotaNueva: String) -> Unit,
    private val onBorrarNotasClick: (itemEntrenamiento) -> Unit,
    private val onMostrarNotasClick: (itemEntrenamiento) -> Unit
) : RecyclerView.Adapter<EjercicioEntrenamientoAdapter.EjercicioEntrenamientoViewHolder>() {

    private var context: Context? = null
    private var listaEjerciciosEntrenamiento: MutableList<itemEntrenamiento> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioEntrenamientoViewHolder {
        context = parent.context
        return EjercicioEntrenamientoViewHolder(
            EjercicioEntrenamientoAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(
        holder: EjercicioEntrenamientoAdapter.EjercicioEntrenamientoViewHolder,
        position: Int
    ) {
        val itemEntrenamiento = listaEjerciciosEntrenamiento[position]
        holder.binding(itemEntrenamiento)
    }


    override fun getItemCount(): Int = listaEjerciciosEntrenamiento.size

    inner class EjercicioEntrenamientoViewHolder(private val binding: EjercicioEntrenamientoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
            private val serieAdapter: SerieAdapter by lazy{
                SerieAdapter(onCheckClick)
            }
        fun mostrarNotas(item: itemEntrenamiento) {
            binding.tituloNotas.visibility = View.VISIBLE
            binding.apartadoNotas.visibility = View.VISIBLE
            binding.agregarNotas.setText(item.relacion.notas ?: "")
        }

        fun ocultarNotas() {
            binding.tituloNotas.visibility = View.GONE
            binding.apartadoNotas.visibility = View.GONE
        }

        fun binding(item: itemEntrenamiento) {
            if (binding.recyclerSeries.adapter == null) {
                binding.recyclerSeries.layoutManager = LinearLayoutManager(context)
                binding.recyclerSeries.adapter = serieAdapter
            }
            serieAdapter.ponerListaSeries(item.series)
            binding.nombreEjercicio.text = item.ejercicio.nombre
            if (item.relacion.notas != null) {
                mostrarNotas(item)
            } else {
                ocultarNotas()
            }
            binding.editarRutinaEnEntrenamiento.setOnClickListener {
                onTresPuntosClick(it, item)
            }
            binding.guardarNotas.setOnClickListener {
                val notaNueva = binding.agregarNotas.text.toString()
                if (notaNueva.isNotBlank()) {
                    onGuardarNotasClick(item, notaNueva)
                    ocultarNotas()
                }
            }
            binding.borrarNotas.setOnClickListener {
                onBorrarNotasClick(item)
                ocultarNotas()
            }
        }

    }
    fun ponerListaEjercicios(nuevaLista: List<itemEntrenamiento>) {
        listaEjerciciosEntrenamiento.clear()
        listaEjerciciosEntrenamiento.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}