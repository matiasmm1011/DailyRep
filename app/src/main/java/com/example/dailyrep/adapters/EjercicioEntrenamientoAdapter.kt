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
    private val onTresPuntosClick: (View, itemEntrenamiento) -> Unit
) : RecyclerView.Adapter<EjercicioEntrenamientoAdapter.EjercicioEntrenamientoViewHolder>() {
class EjercicioEntrenamientoAdapter(private val onCheckClick:(SeriePlanificada)->Unit): RecyclerView.Adapter<EjercicioEntrenamientoAdapter.EjercicioEntrenamientoViewHolder>() {
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
    override fun onBindViewHolder(holder: EjercicioEntrenamientoAdapter.EjercicioEntrenamientoViewHolder, position: Int) {
        val itemEntrenamiento=listaEjerciciosEntrenamiento[position]
        holder.binding(itemEntrenamiento)

    }

    override fun getItemCount(): Int = listaEjerciciosEntrenamiento.size

    inner class EjercicioEntrenamientoViewHolder(private val binding: EjercicioEntrenamientoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
            private val serieAdapter: SerieAdapter by lazy{
                SerieAdapter(onCheckClick)
            }
            fun binding(item: itemEntrenamiento) {
            if(binding.recyclerSeries.adapter == null){
                binding.recyclerSeries.layoutManager= LinearLayoutManager(context)
                binding.recyclerSeries.adapter=serieAdapter
            }
                if(item.relacion.notas!=null){
                    binding.notas.visibility= View.VISIBLE
                    binding.descripcionNotas.setText(item.relacion.notas)
                }
                val nombreEjercicio=item.ejercicio.nombre
                val listaSeries=item.series
            serieAdapter.ponerListaSeries(listaSeries)
            binding.nombreEjercicio.setText(nombreEjercicio)
                binding.editarRutinaEnEntrenamiento.setOnClickListener {
                    onTresPuntosClick(it, item)
                }
        }
    }
    fun ponerListaEjercicios(nuevaLista: List<itemEntrenamiento>) {
        listaEjerciciosEntrenamiento.clear()
        listaEjerciciosEntrenamiento.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}