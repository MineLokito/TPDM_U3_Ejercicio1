package mx.edu.ittepic.tpdm_u3_ejercicio1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    var descripcion: EditText?=null
    var fecha: EditText?=null
    var lugar: EditText?=null
    var insertar: Button?=null
    var listView: ListView?=null
    // declara objeto FIREBASE FIRESTORE
    var baseRemota = FirebaseFirestore.getInstance()
    var registros= ArrayList<String>()
    var keys =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        descripcion=findViewById(R.id.descripcion)
        lugar=findViewById(R.id.lugar)
        fecha=findViewById(R.id.fecha)
        insertar=findViewById(R.id.boton)
        listView=findViewById(R.id.lista)

        insertar?.setOnClickListener {
            //Metodo de como insertar datos
            var datosInsertar= hashMapOf(
                "descripcion" to descripcion?.text.toString(),
                "fecha" to fecha?.text.toString(),
                "lugar" to lugar?.text.toString()
            )
            baseRemota.collection("eventos")
                .add(datosInsertar as Map<String,Any>)
                .addOnSuccessListener {
                    //Si se ejecuta este es que si se pudo
                    Toast.makeText(this, "Si se inserto con Exito",Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{
                    //si se ejecuta este no se pudo
                    it.message
                    Toast.makeText(this, "Error al Insertar",Toast.LENGTH_LONG).show()

                }
            limpiarCampos()

        }

        }
    fun limpiarCampos(){
        descripcion?.setText("");lugar?.setText("");fecha?.setText("")

    }
}
