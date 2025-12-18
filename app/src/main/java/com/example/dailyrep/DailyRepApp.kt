package com.example.dailyrep

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.EjercicioFavoritoDao
import com.example.dailyrep.dao.HistorialDao
import com.example.dailyrep.dao.RachaDao
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.dataclases.Ejercicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val ejerciciosPredeterminados = listOf<Ejercicio>(
    // 1. ab_wheel_rollout
    Ejercicio(
        id = 0L,
        nombre = "Ab Wheel Rollout",
        tipo = "Peso Corporal",
        parteCuerpo = "Abdomen",
        descripcion = "De rodillas, sujeta la rueda con ambas manos. Rueda hacia adelante extendiendo el cuerpo manteniendo el abdomen contraído. Regresa tirando del core sin arquear la espalda baja.",
        nombreImagen = "ab_wheel_rollout"
    ),Ejercicio(
        id = 0L,
        nombre = "Press de Banca Plano",
        tipo = "Barra",
        parteCuerpo = "Pecho",
        descripcion = "Acostado en un banco plano, sujeta la barra con un agarre ligeramente más ancho que los hombros. Desciende la barra de forma controlada hasta el pecho y empuja hacia arriba extendiendo los brazos sin bloquear los codos.",
        nombreImagen = "press_banca_plano"
    ),
    // 2. aperturas_con_mancuernas_banco_plano
    Ejercicio(
        id = 0L,
        nombre = "Aperturas con Mancuernas",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Acostado en un banco plano, inicia con las mancuernas juntas sobre el pecho con ligera flexión de codos. Abre los brazos hacia los lados hasta sentir estiramiento y vuelve a juntar.",
        nombreImagen = "aperturas_con_mancuernas_banco_plano"
    ),
    // 3. aperturas_en_maquina_contractor
    Ejercicio(
        id = 0L,
        nombre = "Aperturas en Máquina (Peck Deck)",
        tipo = "Maquina",
        parteCuerpo = "Pecho",
        descripcion = "Siéntate en la máquina con la espalda apoyada. Cierra los brazos al frente juntando las manos a la altura del pecho. Regresa lentamente manteniendo la tensión.",
        nombreImagen = "aperturas_en_maquina_contractor"
    ),
    // 4. bicicleta_estatica
    Ejercicio(
        id = 0L,
        nombre = "Bicicleta Estática",
        tipo = "Cardio",
        parteCuerpo = "Cardio",
        descripcion = "Ajusta el asiento a la altura correcta y pedalea manteniendo un ritmo continuo. Modifica la resistencia según la intensidad deseada.",
        nombreImagen = "bicicleta_estatica"
    ),
    // 5. burpees
    Ejercicio(
        id = 0L,
        nombre = "Burpees",
        tipo = "Peso Corporal",
        parteCuerpo = "Cardio",
        descripcion = "De pie, agáchate y apoya manos, lanza pies atrás a plancha, opcional flexión, salta adelante y termina con un salto vertical.",
        nombreImagen = "burpees"
    ),
    // 6. correr cinta
    Ejercicio(
        id = 0L,
        nombre = "Correr en Cinta",
        tipo = "Cardio",
        parteCuerpo = "Cardio",
        descripcion = "Corre o trota manteniendo una postura erguida, sin inclinarte hacia adelante, y controla tu respiración durante toda la sesión.",
        nombreImagen = "correr_cinta" // Ajustado sin espacios para evitar errores de recurso
    ),
    // 7. crunch_cable
    Ejercicio(
        id = 0L,
        nombre = "Crunch con Cable",
        tipo = "Polea",
        parteCuerpo = "Abdomen",
        descripcion = "Frente a la polea alta, de rodillas, flexiona el torso llevando los codos hacia las rodillas mientras mantienes el abdomen contraído.",
        nombreImagen = "crunch_cable"
    ),
    // 8. crunch_colchoneta
    Ejercicio(
        id = 0L,
        nombre = "Crunch en Colchoneta",
        tipo = "Peso Corporal",
        parteCuerpo = "Abdomen",
        descripcion = "Acostado boca arriba, flexiona rodillas. Eleva el tronco llevando el pecho hacia las rodillas sin despegar la zona lumbar.",
        nombreImagen = "crunch_colchoneta"
    ),
    // 9. crunch_maquina
    Ejercicio(
        id = 0L,
        nombre = "Crunch en Máquina",
        tipo = "Maquina",
        parteCuerpo = "Abdomen",
        descripcion = "Sentado en la máquina, sujeta las agarraderas y flexiona el torso hacia adelante contrayendo el abdomen.",
        nombreImagen = "crunch_maquina"
    ),
    // 10. curl_de_biceps_barra
    Ejercicio(
        id = 0L,
        nombre = "Curl de Bíceps con Barra",
        tipo = "Barra",
        parteCuerpo = "Brazos",
        descripcion = "Con la barra recta y agarre supino, flexiona los codos para llevar la barra al pecho sin balancear el torso.",
        nombreImagen = "curl_de_biceps_barra"
    ),
    // 11. curl_en_banco_predicador
    Ejercicio(
        id = 0L,
        nombre = "Curl Predicador",
        tipo = "Barra",
        parteCuerpo = "Brazos",
        descripcion = "Sentado en el banco predicador, brazos sobre el soporte. Flexiona los codos para elevar el peso y baja lentamente casi estirando por completo.",
        nombreImagen = "curl_en_banco_predicador"
    ),
    // 12. curl_en_polea_con_cuerda
    Ejercicio(
        id = 0L,
        nombre = "Curl con Cuerda en Polea",
        tipo = "Polea",
        parteCuerpo = "Brazos",
        descripcion = "De frente a la polea baja, sujeta la cuerda. Flexiona codos llevando manos a hombros, separando la cuerda al final.",
        nombreImagen = "curl_en_polea_con_cuerda"
    ),
    // 13. curl_femoral_maquina
    Ejercicio(
        id = 0L,
        nombre = "Curl Femoral Tumbado",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "Acostado boca abajo, flexiona las piernas llevando los talones hacia los glúteos. Controla el regreso.",
        nombreImagen = "curl_femoral_maquina"
    ),
    // 14. curl_martillo_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Curl Martillo",
        tipo = "Mancuernas",
        parteCuerpo = "Brazos",
        descripcion = "De pie, mancuernas con agarre neutro (palmas enfrentadas). Flexiona codos elevando pesas sin girar muñecas.",
        nombreImagen = "curl_martillo_mancuernas"
    ),
    // 15. curl_piernas_maquina
    Ejercicio(
        id = 0L,
        nombre = "Curl de Piernas Sentado",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "Sentado en la máquina, flexiona las rodillas llevando el rodillo hacia abajo y atrás, contrayendo isquiotibiales.",
        nombreImagen = "curl_piernas_maquina"
    ),
    // 16. dominadas
    Ejercicio(
        id = 0L,
        nombre = "Dominadas (Pull-ups)",
        tipo = "Peso Corporal",
        parteCuerpo = "Espalda",
        descripcion = "Agarra la barra con palmas hacia afuera. Eleva el cuerpo hasta acercar el pecho a la barra y baja controladamente.",
        nombreImagen = "dominadas"
    ),
    // 17. dominadas_pronas
    Ejercicio(
        id = 0L,
        nombre = "Dominadas Pronas (Variante)",
        tipo = "Peso Corporal",
        parteCuerpo = "Espalda",
        descripcion = "Agarre prono. Sube el cuerpo llevando el pecho hacia la barra enfocándote en la amplitud dorsal.",
        nombreImagen = "dominadas_pronas"
    ),
    // 18. elevaciones_frontales_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Elevaciones Frontales",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "De pie, eleva las mancuernas al frente hasta la altura de los hombros con codos ligeramente flexionados.",
        nombreImagen = "elevaciones_frontales_mancuernas"
    ),
    // 19. elevaciones_piernas_colgado
    Ejercicio(
        id = 0L,
        nombre = "Elevaciones de Piernas",
        tipo = "Peso Corporal",
        parteCuerpo = "Abdomen",
        descripcion = "Colgado de la barra, eleva las piernas juntas hacia el frente hasta la altura de la cadera sin balancearte.",
        nombreImagen = "elevaciones_piernas_colgado"
    ),
    // 20. eliptica
    Ejercicio(
        id = 0L,
        nombre = "Elíptica",
        tipo = "Cardio",
        parteCuerpo = "Cardio",
        descripcion = "Mantén un movimiento fluido usando brazos y piernas con postura recta en la máquina elíptica.",
        nombreImagen = "eliptica"
    ),
    // 21. encogimiento_trapecios_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Encogimientos de Trapecio",
        tipo = "Mancuernas",
        parteCuerpo = "Espalda",
        descripcion = "De pie, eleva los hombros hacia las orejas verticalmente sin flexionar los codos y baja controlado.",
        nombreImagen = "encogimiento_trapecios_mancuernas"
    ),
    // 22. extension_de_cuadriceps_maquina
    Ejercicio(
        id = 0L,
        nombre = "Extensión de Cuádriceps",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "Sentado, extiende las piernas elevando la carga hasta casi bloquear rodillas. Baja lento.",
        nombreImagen = "extension_de_cuadriceps_maquina"
    ),
    // 23. extension_de_triceps_polea_alta
    Ejercicio(
        id = 0L,
        nombre = "Jalón de Tríceps en Polea",
        tipo = "Polea",
        parteCuerpo = "Brazos",
        descripcion = "De pie, agarre prono. Empuja la barra hacia abajo extendiendo brazos, codos pegados al cuerpo.",
        nombreImagen = "extension_de_triceps_polea_alta"
    ),
    // 24. extension_triceps_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Extensión Tríceps Tras Nuca",
        tipo = "Mancuernas",
        parteCuerpo = "Brazos",
        descripcion = "Sostén mancuerna sobre la cabeza. Flexiona codos llevando peso tras la nuca y extiende arriba.",
        nombreImagen = "extension_triceps_mancuernas"
    ),
    // 25. facepull_en_polea
    Ejercicio(
        id = 0L,
        nombre = "Face Pull",
        tipo = "Polea",
        parteCuerpo = "Hombro",
        descripcion = "Tira de la cuerda hacia la cara separando manos y llevando codos afuera. Enfoca en deltoides posterior.",
        nombreImagen = "facepull_en_polea"
    ),
    // 26. farmers_walks_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Farmer's Walk (Paseo de Granjero)",
        tipo = "Mancuernas",
        parteCuerpo = "Pierna",
        descripcion = "Camina en línea recta sujetando mancuernas pesadas a los lados, con torso erguido y abdomen firme.",
        nombreImagen = "farmers_walks_mancuernas"
    ),
    // 27. flexiones
    Ejercicio(
        id = 0L,
        nombre = "Flexiones (Push-Ups)",
        tipo = "Peso Corporal",
        parteCuerpo = "Pecho",
        descripcion = "En plancha, baja el pecho al suelo flexionando codos y empuja arriba extendiendo brazos.",
        nombreImagen = "flexiones"
    ),
    // 28. fondos_en_paralelas_pecho
    Ejercicio(
        id = 0L,
        nombre = "Fondos en Paralelas (Pecho)",
        tipo = "Peso Corporal",
        parteCuerpo = "Pecho",
        descripcion = "En paralelas, inclina torso adelante y baja flexionando codos. Empuja para subir.",
        nombreImagen = "fondos_en_paralelas_pecho"
    ),
    // 29. fondos_paralelas
    Ejercicio(
        id = 0L,
        nombre = "Fondos en Paralelas (Tríceps)",
        tipo = "Peso Corporal",
        parteCuerpo = "Brazos",
        descripcion = "En paralelas, torso vertical. Baja flexionando codos pegados al cuerpo y sube.",
        nombreImagen = "fondos_paralelas"
    ),
    // 30. hip_thrust_barra
    Ejercicio(
        id = 0L,
        nombre = "Hip Thrust",
        tipo = "Barra",
        parteCuerpo = "Gluteos",
        descripcion = "Espalda en banco, barra en cadera. Eleva cadera hasta alinear con rodillas y hombros contrayendo glúteos.",
        nombreImagen = "hip_thrust_barra"
    ),
    // 31. jalon_agarrecerrado_polea
    Ejercicio(
        id = 0L,
        nombre = "Jalón Agarre Cerrado",
        tipo = "Polea",
        parteCuerpo = "Espalda",
        descripcion = "Con agarre en V, tira hacia el pecho alto manteniendo el torso ligeramente inclinado atrás.",
        nombreImagen = "jalon_agarrecerrado_polea"
    ),
    // 32. jalon_al_pecho_polea
    Ejercicio(
        id = 0L,
        nombre = "Jalón al Pecho",
        tipo = "Polea",
        parteCuerpo = "Espalda",
        descripcion = "Agarre abierto. Tira la barra hacia el pecho superior llevando codos hacia abajo.",
        nombreImagen = "jalon_al_pecho_polea"
    ),
    // 33. lunge_barra
    Ejercicio(
        id = 0L,
        nombre = "Lunge con Barra",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Barra en espalda. Da un paso al frente, flexiona ambas rodillas y regresa a la posición inicial.",
        nombreImagen = "lunge_barra"
    ),
    // 34. mountain_climbers
    Ejercicio(
        id = 0L,
        nombre = "Mountain Climbers",
        tipo = "Peso Corporal",
        parteCuerpo = "Cardio",
        descripcion = "En plancha alta, lleva rodillas al pecho alternadamente de forma rápida y controlada.",
        nombreImagen = "mountain_climbers"
    ),
    // 35. patada_triceps_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Patada de Tríceps",
        tipo = "Mancuernas",
        parteCuerpo = "Brazos",
        descripcion = "Torso inclinado, brazo pegado al cuerpo. Extiende el codo llevando mancuerna atrás.",
        nombreImagen = "patada_triceps_mancuernas"
    ),
    // 36. peso_muerto_convencional
    Ejercicio(
        id = 0L,
        nombre = "Peso Muerto Convencional",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Barra en suelo. Espalda recta, levanta el peso extendiendo cadera y rodillas.",
        nombreImagen = "peso_muerto_convencional"
    ),
    // 37. peso_muerto_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Peso Muerto Rumano Mancuernas",
        tipo = "Mancuernas",
        parteCuerpo = "Pierna",
        descripcion = "Rodillas semi-flexionadas. Baja mancuernas deslizando por piernas, espalda recta, siente isquios y sube.",
        nombreImagen = "peso_muerto_mancuernas"
    ),
    // 38. peso_muerto_rumano
    Ejercicio(
        id = 0L,
        nombre = "Peso Muerto Rumano Barra",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Similar al de mancuernas pero con barra. Enfocado en la cadena posterior e isquiotibiales.",
        nombreImagen = "peso_muerto_rumano"
    ),
    // 39. peso_muerto_sumo_barra
    Ejercicio(
        id = 0L,
        nombre = "Peso Muerto Sumo",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Pies muy separados. Agarre por dentro de piernas. Levanta manteniendo torso más vertical.",
        nombreImagen = "peso_muerto_sumo_barra"
    ),
    // 40. plancha_frontal
    Ejercicio(
        id = 0L,
        nombre = "Plancha Frontal",
        tipo = "Peso Corporal",
        parteCuerpo = "Abdomen",
        descripcion = "Apoya antebrazos y pies. Mantén cuerpo alineado y abdomen tenso isométricamente.",
        nombreImagen = "plancha_frontal"
    ),
    // 41. prensa_piernas
    Ejercicio(
        id = 0L,
        nombre = "Prensa de Piernas",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "Empuja la plataforma con los pies hasta casi extender piernas. Baja controlando el peso.",
        nombreImagen = "prensa_piernas"
    ),
    // 42. press_arnold
    Ejercicio(
        id = 0L,
        nombre = "Press Arnold",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "Press de hombros con rotación. Empieza palmas hacia ti, gira al subir hasta palmas al frente.",
        nombreImagen = "press_arnold"
    ),
    // 43. press_frances_barraz
    Ejercicio(
        id = 0L,
        nombre = "Press Francés",
        tipo = "Barra",
        parteCuerpo = "Brazos",
        descripcion = "Acostado, barra Z. Flexiona codos llevando barra a la frente y extiende arriba.",
        nombreImagen = "press_frances_barraz"
    ),
    // 44. press_hombros_maquina
    Ejercicio(
        id = 0L,
        nombre = "Press de Hombros en Máquina",
        tipo = "Maquina",
        parteCuerpo = "Hombro",
        descripcion = "Sentado, empuja los agarres hacia arriba hasta extender brazos.",
        nombreImagen = "press_hombros_maquina"
    ),
    // 45. press_inclinado_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Press Inclinado",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Banco inclinado. Empuja mancuernas desde hombros hacia arriba juntándolas al centro.",
        nombreImagen = "press_inclinado_mancuernas"
    ),
    // 46. pullover_mancuerna
    Ejercicio(
        id = 0L,
        nombre = "Pullover",
        tipo = "Mancuernas",
        parteCuerpo = "Pecho",
        descripcion = "Espalda en banco. Lleva mancuerna con ambas manos hacia atrás de la cabeza y regresa al pecho.",
        nombreImagen = "pullover_mancuerna"
    ),
    // 47. remo_barra_t
    Ejercicio(
        id = 0L,
        nombre = "Remo Barra T",
        tipo = "Barra",
        parteCuerpo = "Espalda",
        descripcion = "Torso inclinado. Tira del agarre de la barra T hacia el pecho/abdomen contrayendo espalda.",
        nombreImagen = "remo_barra_t"
    ),
    // 48. remo_con_barra
    Ejercicio(
        id = 0L,
        nombre = "Remo con Barra",
        tipo = "Barra",
        parteCuerpo = "Espalda",
        descripcion = "Torso inclinado 45 grados. Tira la barra hacia el abdomen bajo.",
        nombreImagen = "remo_con_barra"
    ),
    // 49. remo_con_mancuerna_una_mano
    Ejercicio(
        id = 0L,
        nombre = "Remo con Mancuerna",
        tipo = "Mancuernas",
        parteCuerpo = "Espalda",
        descripcion = "Apoya rodilla y mano en banco. Tira mancuerna a la cadera con el otro brazo.",
        nombreImagen = "remo_con_mancuerna_una_mano"
    ),
    // 50. remo_maquina
    Ejercicio(
        id = 0L,
        nombre = "Remo en Máquina (Cardio)",
        tipo = "Cardio",
        parteCuerpo = "Cardio",
        descripcion = "Máquina de remo. Empuja con piernas y tira con brazos coordinadamente.",
        nombreImagen = "remo_maquina"
    ),
    // 51. remo_maquina_sentado
    Ejercicio(
        id = 0L,
        nombre = "Remo en Máquina Sentado",
        tipo = "Maquina",
        parteCuerpo = "Espalda",
        descripcion = "Tira del agarre hacia el abdomen manteniendo espalda recta y contrayendo dorsales.",
        nombreImagen = "remo_maquina_sentado"
    ),
    // 52. reverse_fly_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Pájaros (Reverse Fly)",
        tipo = "Mancuernas",
        parteCuerpo = "Hombro",
        descripcion = "Torso inclinado. Abre los brazos lateralmente para trabajar deltoides posterior.",
        nombreImagen = "reverse_fly_mancuernas"
    ),
    // 53. russian_twist_disco
    Ejercicio(
        id = 0L,
        nombre = "Russian Twist",
        tipo = "Peso Corporal",
        parteCuerpo = "Abdomen",
        descripcion = "Sentado, torso atrás, pies elevados. Gira torso lado a lado con disco o manos.",
        nombreImagen = "russian_twist_disco"
    ),
    // 54. saltar_cuerda
    Ejercicio(
        id = 0L,
        nombre = "Saltar la Cuerda",
        tipo = "Cardio",
        parteCuerpo = "Cardio",
        descripcion = "Salta con pies juntos o alternos manteniendo ritmo constante con la cuerda.",
        nombreImagen = "saltar_cuerda"
    ),
    // 55. sentadilla_bulgara_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Sentadilla Búlgara",
        tipo = "Mancuernas",
        parteCuerpo = "Pierna",
        descripcion = "Un pie apoyado atrás en banco. Flexiona pierna delantera bajando cadera.",
        nombreImagen = "sentadilla_bulgara_mancuernas"
    ),
    // 56. sentadilla_hack_maquina
    Ejercicio(
        id = 0L,
        nombre = "Sentadilla Hack",
        tipo = "Maquina",
        parteCuerpo = "Pierna",
        descripcion = "En máquina Hack, apoya espalda y baja flexionando rodillas. Empuja para subir.",
        nombreImagen = "sentadilla_hack_maquina"
    ),
    // 57. sentadilla_libre
    Ejercicio(
        id = 0L,
        nombre = "Sentadilla Libre",
        tipo = "Barra",
        parteCuerpo = "Pierna",
        descripcion = "Barra tras nuca. Baja cadera como sentándote hasta romper paralelo. Sube explosivo.",
        nombreImagen = "sentadilla_libre"
    ),
    // 58. step_up_banco_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Step-Up",
        tipo = "Mancuernas",
        parteCuerpo = "Pierna",
        descripcion = "Sube un pie al banco y eleva el cuerpo. Baja controlado y alterna o repite.",
        nombreImagen = "step_up_banco_mancuernas"
    ),
    // 59. swing_kettlebell
    Ejercicio(
        id = 0L,
        nombre = "Kettlebell Swing",
        tipo = "Kettlebell",
        parteCuerpo = "Pierna",
        descripcion = "Balancea pesa entre piernas y empuja fuerte con cadera hacia adelante elevando pesa.",
        nombreImagen = "swing_kettlebell"
    ),
    // 60. zancadas_mancuernas
    Ejercicio(
        id = 0L,
        nombre = "Zancadas (Lunges)",
        tipo = "Mancuernas",
        parteCuerpo = "Pierna",
        descripcion = "Da paso largo adelante, baja rodilla trasera al suelo y regresa.",
        nombreImagen = "zancadas_mancuernas"
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
    lateinit var rachaDao: RachaDao
    lateinit var database: DataBase
    lateinit var ejercicioFavoritoDao: EjercicioFavoritoDao
    lateinit var relacionEjeRutDao: RelacionEjeRutDao

    lateinit var historialDao: HistorialDao

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
        ejercicioFavoritoDao=database.ejercicioFavoritoDao()
        rutinaDao=database.rutinaDao()
        relacionEjeRutDao=database.relacionEjeRutDao()
        usuarioDao=database.usuarioDao()
        serieDao=database.serieDao()
        rachaDao=database.rachaDao()
        historialDao=database.historialDao()
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