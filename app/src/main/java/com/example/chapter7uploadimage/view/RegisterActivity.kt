package com.example.chapter7uploadimage.view

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.chapter7uploadimage.R
import com.example.chapter7uploadimage.databinding.ActivityRegisterBinding
import com.example.chapter7uploadimage.viewmodel.ViewModelUser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegisterActivity : AppCompatActivity() {
    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null
    lateinit var viewModelUser : ViewModelUser
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelUser = ViewModelProvider(this).get(ViewModelUser::class.java)

        binding.imageView.setOnClickListener {
            openGallery()
        }
        binding.btnRegister.setOnClickListener {
            postDataUser()
        }
    }

    fun postDataUser(){
        val email = binding.editEmail.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val password = binding.editConfirmPassword.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val fullname = binding.editNama.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val address = binding.editAddress.text.toString().toRequestBody("multipart/form-data".toMediaType())

        viewModelUser.addLiveDataUser.observe(this,{
            if (it != null){
                Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show()
            }
        })
        viewModelUser.postApiUser(email, password, fullname, address, imageMultiPart!!)
    }

    fun openGallery(){
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = this!!.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                val fileNameimg = "${System.currentTimeMillis()}.png"

                binding.imageView.setImageURI(it)
                Toast.makeText(this, "$imageUri", Toast.LENGTH_SHORT).show()

                val tempFile = File.createTempFile("and1-", fileNameimg, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use    { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart = MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }
}