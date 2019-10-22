package mx.edu.ittepic.tpdm_u3_ejercicio1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

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
        baseRemota.collection("eventos")
            .addSnapshotListener { querySnapshot, e ->
                if (e!=null){
                    Toast.makeText(this,"No hay acceso a los datos",Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                registros.clear()
                keys.clear()
                for (documents in querySnapshot!!){
                    var cadena= "${documents.getString("descripcion")}\n${documents.getString("fecha")}--${documents.getString("lugar")}"
                    registros.add(cadena)
                    keys.add(documents.id)
                }
                if(registros.size==0){
                    registros.add("no hay datos Capturados")
                }
                var adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,registros)
                lista?.adapter=adapter
            }

        lista.setOnItemClickListener { parent, view, position, id ->
            AlertDialog.Builder(this).setTitle("Atencion").setMessage("Que desea hacer con \n ${registros.get(position)}?")
                .setPositiveButton("Eliminar"){dialogInterface, wich ->
                    baseRemota.collection("eventos").document(keys.get(position)).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this,"se pudo eliminar",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"No se pudo Eliminar",Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Actualizar"){dialogInterface, wich ->

                    var nuevaVentana =
                        Intent(this, Main2Activity::class.java)
                    nuevaVentana.putExtra("id",keys.get(position) )
                    startActivity(nuevaVentana)
                }
                .setNeutralButton("Cancelar"){dialogInterface, wich ->  }
                .show()
        }

        }
    fun limpiarCampos(){
        descripcion?.setText("");lugar?.setText("");fecha?.setText("")

    }
}
