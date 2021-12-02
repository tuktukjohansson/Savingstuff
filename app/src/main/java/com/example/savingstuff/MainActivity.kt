package com.example.savingstuff

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var Textname: EditText
    lateinit var Textnameroom: EditText
    lateinit var Savebutton: Button
    lateinit var Savebuttonroom: Button
    lateinit var Hellouser: TextView
    lateinit var Hellouserroom: TextView
    private lateinit var dbroom: AppDatabase

    lateinit var theuser: String
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Variabels IDS//
        Hellouser = findViewById<TextView>(R.id.hellouser)
        Hellouserroom = findViewById<TextView>(R.id.hellouserroom)
        Textname = findViewById<EditText>(R.id.textname)
        Textnameroom = findViewById<EditText>(R.id.textnameroom)
        Savebutton = findViewById<Button>(R.id.savebutton)
        Savebuttonroom = findViewById<Button>(R.id.savebuttonroom)

        //Variabels IDS//


        //Firestore read oncreate//

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    theuser = document.data.values.toString()
                    var theuserremove1 = theuser.replace("[", "")
                    var theuserfinished = theuserremove1.replace("]", "")
                    Hellouser.setText("Firebase : " + theuserfinished)
                }
            }

        //Firestore read//

        //Room Database read//


        dbroom = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "minanamn")
            .fallbackToDestructiveMigration().build()
        var nameonstart = loadonstart().toString()
        Hellouserroom.setText(nameonstart)

        //Room Database //

        //Main Code//
        Savebutton.setOnClickListener() {
            savemynamefirebase()
        }
        Savebuttonroom.setOnClickListener() {
            savemynameroom()
        }
        //Main Code//


    }

    private fun savemynamefirebase() {
        var namn = Textname.text.toString().trim()

        var user = hashMapOf(
            "name" to namn
        )

        db.collection("users").document("myusername")
            .set(user)
        Hellouser.setText("Firebase : " + namn)
    }

    private fun savemynameroom() {
        var namn = Textnameroom.text.toString()
        val username = Myname(0, "$namn")
        savename(username)
    }
    fun savename(myname: Myname) {
        GlobalScope.launch(Dispatchers.IO) {
            var nameList = loadmyname().await()
            for (id in nameList) {
                dbroom.nameDao().delete(id)
            }
            dbroom.nameDao().insert(myname)
            nameList = loadmyname().await()
            Hellouserroom.setText("Android Room : " + nameList.toString())
        }
    }
    fun loadmyname() : Deferred<List<Myname>> =
        GlobalScope.async(Dispatchers.IO)
    {
        dbroom.nameDao().getAll()
    }



    fun loadonstart() {
        GlobalScope.launch(Dispatchers.IO) {
            var nameList = loadmynameonstart().await()
            Hellouserroom.setText("Android Room : " + nameList.toString())
        }
    }
    fun loadmynameonstart(): Deferred<List<Myname>> =
        GlobalScope.async(Dispatchers.IO)
        {
            dbroom.nameDao().getAll()
        }
}