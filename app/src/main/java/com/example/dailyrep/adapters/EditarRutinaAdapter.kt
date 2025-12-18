package com.example.dailyrep.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.RutinasActivity
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.databinding.ActivityEditarRutinaAdapterBinding
import com.example.dailyrep.databinding.EjercicioEntrenamientoAdapterBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.RelacionEjeRut
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.itemEntrenamiento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditarRutinaAdapter(
    private val onAgregarSerieClick: (itemEntrenamiento) -> Unit,
    private val onDisminuirSerieClick: (itemEntrenamiento) -> Unit,
    private val onGuardarCambiosClick: (SeriePlanificada, mandarPesoNuevo: Int, mandarRepsNuevas: Int) -> Unit,
    private val onBorrarEjercicioClick: (itemEntrenamiento) -> Unit,
    ): RecyclerView.Adapter<EditarRutinaAdapter.EjercicioEditarRutinaViewHolder>() {
    private var context: Context? = null
    private var listaEjerciciosEntrenamiento: MutableList<itemEntrenamiento> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioEditarRutinaViewHolder {
        context = parent.context
        return EjercicioEditarRutinaViewHolder(
            ActivityEditarRutinaAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: EditarRutinaAdapter.EjercicioEditarRutinaViewHolder, position: Int) {
        val itemEntrenamiento=listaEjerciciosEntrenamiento[position]
        holder.binding(itemEntrenamiento)
    }

    override fun getItemCount(): Int = listaEjerciciosEntrenamiento.size

    inner class EjercicioEditarRutinaViewHolder(private val binding: ActivityEditarRutinaAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val serieTablaAdapter: SerieTablaAdapter by lazy{
            SerieTablaAdapter(onGuardarCambiosClick)
        }
        fun binding(item: itemEntrenamiento) {
            if(binding.recyclerViewSeries.adapter == null){
                binding.recyclerViewSeries.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewSeries.adapter = serieTablaAdapter
            }
            val nombreEjercicio = item.ejercicio.nombre
            val listaSeries = item.series
            serieTablaAdapter.ponerListaSeries(listaSeries)
            binding.nombreEjercicio.setText(nombreEjercicio)
            binding.editarEjercicio.setOnClickListener {
                binding.apartadoEditarEjercicio.visibility = View.VISIBLE
                binding.botonAgregarNota.setOnClickListener {
                        binding.tituloNotas.visibility= View.VISIBLE
                        binding.apartadoNotas.visibility= View.VISIBLE
                        binding.agregarNotas.setHint(item.relacion.notas)
                }
                binding.botonAgregarSerie.setOnClickListener { onAgregarSerieClick(item) }
                binding.botonDisminuirSerie.setOnClickListener { onDisminuirSerieClick(item) }
            }
            binding.borrarEjercicio.setOnClickListener {
                onBorrarEjercicioClick(item)
            }
        }
    }
    fun ponerListaEjercicios(nuevaLista: MutableList<itemEntrenamiento>) {
        listaEjerciciosEntrenamiento.clear()
        listaEjerciciosEntrenamiento.addAll(nuevaLista)
        notifyDataSetChanged()
    }



}