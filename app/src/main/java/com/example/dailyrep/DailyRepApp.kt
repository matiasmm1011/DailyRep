package com.example.dailyrep

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.dataclases.Ejercicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val ejerciciosPredeterminados = listOf<Ejercicio>(
    Ejercicio(
        id = 0L,
        nombre = "Aperturas con mancuernas",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Ejercicio de aislamiento para el pecho realizado en banco plano.",
        nombreImagen = "aperturas_con_mancuernas_banco_plano"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Aperturas en máquina (Peck Deck)",
        tipo = "Maquina",
        parteCuerpo = "Pecho",
        descripcion = "Ejercicio guiado para aislar los pectorales.",
        nombreImagen = "aperturas_en_maquina_contractor"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Curl de bíceps con barra",
        tipo = "Barra",
        parteCuerpo = "Brazos",
        descripcion = "El clásico constructor de masa para los bíceps.",
        nombreImagen = "curl_de_biceps_barra"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Curl predicador",
        tipo = "Maquina",
        parteCuerpo = "Brazos",
        descripcion = "Ejercicio de aislamiento para bíceps que evita trampas con el cuerpo.",
        nombreImagen = "curl_en_banco_predicador"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Curl con cuerda en polea",
        tipo = "Polea",
        parteCuerpo = "Brazos",
        descripcion = "Excelente para trabajar el bíceps y el braquial con tensión constante.",
        nombreImagen = "curl_en_polea_con_cuerda"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Curl femoral tumbado",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "Ejercicio específico para aislar los isquiotibiales (femorales).",
        nombreImagen = "curl_femoral_maquina"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Curl martillo",
        tipo = "Mancuernas",
        parteCuerpo = "Brazos",
        descripcion = "Variante del curl que trabaja bíceps y antebrazo.",
        nombreImagen = "curl_martillo_mancuernas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Dominadas pronas",
        tipo = "Peso Corporal",
        parteCuerpo = "Espalda",
        descripcion = "Ejercicio fundamental de peso corporal para la amplitud de la espalda.",
        nombreImagen = "dominadas_pronas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Elevaciones frontales",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "Aisla la cabeza frontal del deltoides.",
        nombreImagen = "elevaciones_frontales_mancuernas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Extensión de cuádriceps",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "El mejor ejercicio de aislamiento para definir los cuádriceps.",
        nombreImagen = "extension_de_cuadriceps_maquina"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Extensión de tríceps en polea alta",
        tipo = "Polea",
        parteCuerpo = "Brazos",
        descripcion = "Ejercicio básico para trabajar la cabeza lateral y media del tríceps.",
        nombreImagen = "extension_de_triceps_polea_alta"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Extensión de tríceps tras nuca",
        tipo = "Mancuernas",
        parteCuerpo = "Brazos",
        descripcion = "Enfatiza la cabeza larga del tríceps al estirarla.",
        nombreImagen = "extension_triceps_mancuernas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Face Pull",
        tipo = "Polea",
        parteCuerpo = "Hombro",
        descripcion = "Gran ejercicio para la salud del hombro y el deltoides posterior.",
        nombreImagen = "facepull_en_polea"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Fondos en paralelas",
        tipo = "Peso Corporal",
        parteCuerpo = "Pecho",
        descripcion = "Potente constructor de masa para pecho y tríceps.",
        nombreImagen = "fondos_en_paralelas_pecho"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Jalón al pecho",
        tipo = "Polea",
        parteCuerpo = "Espalda",
        descripcion = "Alternativa a las dominadas para trabajar la anchura dorsal.",
        nombreImagen = "jalon_al_pecho_polea"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Peso muerto convencional",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Ejercicio compuesto total que trabaja toda la cadena posterior.",
        nombreImagen = "peso_muerto_convencional"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Press Arnold",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "Variante del press de homro con rotación para mayor rango de movimiento.",
        nombreImagen = "press_arnold"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Press inclinado con mancuernas",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Enfatiza la parte superior (clavicular) del pectoral.",
        nombreImagen = "press_inclinado_mancuernas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Pullover con mancuerna",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Clásico ejercicio que estira la caja torácica y trabaja pecho y dorsal.",
        nombreImagen = "pullover_mancuerna"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Remo con barra",
        tipo = "Barra",
        parteCuerpo = "Espalda",
        descripcion = "Constructor fundamental de densidad y fuerza para la espalda.",
        nombreImagen = "remo_con_barra"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Remo con mancuerna a una mano",
        tipo = "Mancuernas",
        parteCuerpo = "Espalda",
        descripcion = "Permite un gran rango de movimiento y trabajo unilateral para la espalda.",
        nombreImagen = "remo_con_manucuerna_una_mano"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Pájaros (Reverse Fly)",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "Aisla la parte posterior del hombro.",
        nombreImagen = "reverse_fly_mancuernas"
    ),
    Ejercicio(
        id = 0L,
        nombre = "Sentadilla libre",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "El rey de los ejercicios de pierna para fuerza y masa total.",
        nombreImagen = "sentadilla_libre"
    )
)
class DailyRepApp: Application() {
    companion object{
        const val NOMBRE_FICHERO_SHARED_PREFERENCES:String="Datos basicos"
        const val KEY_MODO_OSCURO = "modo_oscuro_activado"
        const val NOMBRE_BASE_DE_DATOS="nombre_base_de_datos"
        const val DATOS_INTRODUCIDOS="datos_introducidos"
    }
    val context: Context =this
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ejercicioDao: EjercicioDao
    lateinit var rutinaDao: RutinaDao
    lateinit var usuarioDao: UsuarioDao
    lateinit var usuarioActualId:String
    lateinit var serieDao: SerieDao
    lateinit var database: DataBase
    lateinit var relacionEjeRutDao: RelacionEjeRutDao

    override fun onCreate(){
        super.onCreate()
        sharedPreferences = getSharedPreferences(NOMBRE_FICHERO_SHARED_PREFERENCES,MODE_PRIVATE)
        val modoOscuro=sharedPreferences.getBoolean(KEY_MODO_OSCURO, true)
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        database=Room.databaseBuilder(context, DataBase::class.java, NOMBRE_BASE_DE_DATOS).build()
        ejercicioDao=database.ejercicioDao()
        rutinaDao=database.rutinaDao()
        relacionEjeRutDao=database.relacionEjeRutDao()
        usuarioDao=database.usuarioDao()
        serieDao=database.serieDao()
        verificarEInsertarDatosIniciales()
    }
    private fun verificarEInsertarDatosIniciales() {
        val yaInicializada = sharedPreferences.getBoolean(DATOS_INTRODUCIDOS, false)

        if (!yaInicializada) {
            CoroutineScope(Dispatchers.IO).launch {
                ejercicioDao.insertAll(*ejerciciosPredeterminados.toTypedArray())
                sharedPreferences.edit().putBoolean(DATOS_INTRODUCIDOS, true).apply()
            }
        }
    }
}