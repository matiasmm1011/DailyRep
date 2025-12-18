package com.example.dailyrep.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.databinding.ActivitySerieTablaAdapterBinding
import com.example.dailyrep.databinding.SerieAdapterBinding
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.itemEntrenamiento

class SerieTablaAdapter(private val esCardio:Boolean,
    private val onGuardarCambiosClick: (SeriePlanificada, mandarPesoNuevo: Int, mandarRepsNuevas: Int) -> Unit,
    ): RecyclerView.Adapter<SerieTablaAdapter.SerieTablaViewHolder>(){
    private var context: Context? = null
    private var listaSeries: MutableList<SeriePlanificada> = mutableListOf<SeriePlanificada>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SerieTablaViewHolder {
        context = parent.context
        return SerieTablaViewHolder(
            ActivitySerieTablaAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: SerieTablaAdapter.SerieTablaViewHolder, position: Int) {
        val serie=listaSeries[position]
        holder.binding(serie)

    }

    override fun getItemCount(): Int = listaSeries.size

    inner class SerieTablaViewHolder(private val binding: ActivitySerieTablaAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(seriePlanificada: SeriePlanificada) {
            if (esCardio) {
                binding.etPeso.visibility = View.GONE
                binding.textoKg.visibility = View.GONE

            } else {
                binding.etPeso.visibility = View.VISIBLE
                binding.textoKg.setText(seriePlanificada.peso.toString())
            }
            binding.tvSerie.text=seriePlanificada.numeroSerie.toString()
            binding.etPeso.hint = seriePlanificada.peso.toString()
            binding.etReps.hint = seriePlanificada.repeticiones.toString()
            binding.guardarDatos.setOnClickListener {
                val pesoNuevo: Int? = binding.etPeso.text.toString().toIntOrNull()
                val repsNuevas: Int? = binding.etReps.text.toString().toIntOrNull()
                onGuardarCambiosClick(seriePlanificada,
                    pesoNuevo ?: seriePlanificada.peso,
                    repsNuevas ?: seriePlanificada.repeticiones)
            }
        }
    }
    fun ponerListaSeries(nuevaLista: List<SeriePlanificada>) {
        listaSeries.clear()
        listaSeries.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
