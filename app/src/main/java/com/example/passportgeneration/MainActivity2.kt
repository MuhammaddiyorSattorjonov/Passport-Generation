package com.example.passportgeneration

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.passportgeneration.databinding.ActivityMain2Binding
import com.example.passportgeneration.db.AppDataBase
import com.example.passportgeneration.db.MyData.state
import com.example.passportgeneration.models.MyPerson
import kotlin.random.Random

class MainActivity2 : AppCompatActivity() {
    private lateinit var appDataBase: AppDataBase
    private lateinit var adapter:RvAdapter
    private var picturePath:String? = null
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appDataBase = AppDataBase.getInstance(this)
        adapter = RvAdapter(appDataBase.myDao().getAllPerson() as ArrayList,this)
        binding.image.setOnClickListener {
            choosePhotoFromGallary()
        }

        binding.btnSave.setOnClickListener {
            binding.apply {
                val name = binding.name.text.toString().trim()
                if (name.isNotEmpty()&&picturePath!=null){
                    val myPerson = MyPerson(name,generatePassportId(),picturePath,appDataBase.myDao().getMaxPosition()+1)
                    appDataBase.myDao().addPerson(myPerson)
                    Toast.makeText(this@MainActivity2, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@MainActivity2, "Ma'lumotlaringinzi to'ldiring", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = this.contentResolver.query(
                selectedImage!!,
                filePathColumn,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex)
            cursor.close()
            binding.imageProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show()
        }
    }
    private fun generatePassportId(): String {
        val result = randomLetter() + randomNumber()
        return if (appDataBase.myDao().checkForPassportId(result).isEmpty()) {
            result
        } else {
            generatePassportId()
        }
    }

    fun randomNumber(): String {
        var result = ""
        for (i in 0..6) {
            result += Random.nextInt(10)
        }

        return result
    }

    fun randomLetter(): String {
        var result = ""
        result += Char(Random.nextInt(65, 91))
        result += Char(Random.nextInt(65, 91))

        return result
    }

}