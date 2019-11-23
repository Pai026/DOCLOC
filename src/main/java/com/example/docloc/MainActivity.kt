package com.example.docloc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import com.example.docloc.UploadPDF
import com.google.android.gms.common.api.GoogleApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import java.sql.Types.NULL
import java.util.jar.Manifest
import java.util.logging.Level
import kotlin.Long
class MainActivity : AppCompatActivity() {
    private val PDF = 0
    lateinit var uri: Uri
    private lateinit var context:Context
    lateinit var mStorage: StorageReference
    lateinit var mref: DatabaseReference
    private val user = FirebaseAuth.getInstance().currentUser
    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val selectFile = findViewById<View>(R.id.selectFile) as Button
        val upload = findViewById<View>(R.id.upload) as Button
        val dwnTxt = findViewById<View>(R.id.notification2) as TextView
        val show=findViewById<View>(R.id.showUploads)as Button
        val signout1 = findViewById<View>(R.id.signOut)as Button
        mStorage = FirebaseStorage.getInstance().getReference()

        selectFile.setOnClickListener (View.OnClickListener{
                view: View? ->val intent = Intent()
            intent.type="application/pdf"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
        })

        upload.setOnClickListener {
           dwnTxt.text="Nothing Uploaded Yet"

            uploadFile()




        }
        show.setOnClickListener(View.OnClickListener {
            show1()
        })

        signout1.setOnClickListener(View.OnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,Login::class.java))
            finish()
        })
        mref = FirebaseDatabase.getInstance().reference

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uriTxt = findViewById<View>(R.id.notification2) as TextView
        if (resultCode == RESULT_OK) {
            if (requestCode == PDF) {
                uri = data!!.data!!
                uriTxt.text = uri.toString()
            }
        }

    }

    private fun uploadFile() {
        val b = "pdf"
        val a= System.currentTimeMillis().toString()
        val email=user!!.email
        val pattern = "\\W+".toRegex()
        val charac =pattern.split(email.toString()).filter{ it.isNotBlank() }

        val chara= a.plus(b)
        var mReference = mStorage.child("Uploads/"+System.currentTimeMillis()+"pdf")
        try {
            mReference.putFile(uri).addOnSuccessListener {
                    taskSnapshot: UploadTask.TaskSnapshot? -> var url = taskSnapshot!!.getStorage().getDownloadUrl()
                val dwnTxt = findViewById<View>(R.id.notification2) as TextView
                dwnTxt.text = "SucessFully Uploaded"
                val uploadpdf=UploadPDF(user.toString(),chara)
                mref.child("uploads").child(charac[0]).child(chara).setValue(uploadpdf) //.child(chara)
                Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
                }

    }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
    private fun show1(){
        startActivity(Intent(this@MainActivity, Retrieve::class.java))

    }

}
