package com.example.dailyrep.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.R
import com.example.dailyrep.databinding.ActivityAdapterRutinaBinding
import com.example.dailyrep.databinding.ActivityRutinasBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Rutina

class RutinaAdapter(
    private val onRutinaPlayClick: (Rutina) -> Unit,
    private val onRutinaEditarClick: (Rutina) -> Unit,
    private val onRutinaBorrarClick: (Rutina) -> Unit
) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    private var context: Context? = null
    private val listaRutinas = mutableListOf<Rutina>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        context = parent.context
        return RutinaViewHolder(
            ActivityAdapterRutinaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RutinaAdapter.RutinaViewHolder, position: Int) {
        holder.binding(listaRutinas[position])
    }

    override fun getItemCount(): Int = listaRutinas.size

    inner class RutinaViewHolder(private val binding: ActivityAdapterRutinaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(rutina: Rutina) {
            binding.nombreRutina.text = rutina.nombreRutina
            binding.comenzarRutina.setOnClickListener { onRutinaPlayClick(rutina) }
            binding.editarRutina.setOnClickListener { onRutinaEditarClick(rutina) }
            binding.eliminarRutina.setOnClickListener { onRutinaBorrarClick(rutina) }
        }
    }

    fun ponerListaRutinas(nuevaLista: List<Rutina>) {
        listaRutinas.clear()
        listaRutinas.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}