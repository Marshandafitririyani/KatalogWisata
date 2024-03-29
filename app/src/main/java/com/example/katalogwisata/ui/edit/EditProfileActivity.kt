package com.example.katalogwisata.ui.edit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.*
import com.crocodic.core.helper.DateTimeHelper
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.databinding.ActivityEditProfileBinding
import com.example.katalogwisata.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(R.layout.activity_edit_profile) {

    private var photoFile: File? = null
    private var username: String? = null
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //untuk getExstra menyimpan username
        username = intent.getStringExtra("username")
        phoneNumber = intent.getStringExtra("phoneNumber")
        binding.activity = this
        binding.etNameEditProfil.setText(username)
        binding.etPhoneEditProfil.setText(phoneNumber)

        initClick()
        observe()

    }

    private fun validateForm() {
        val name = binding.etNameEditProfil.textOf()
        val phone = binding.etPhoneEditProfil.textOf()

        if (name.isEmpty())
            if (phone.isEmpty()){
                    return
                }

        //untuk foto saja
        if (photoFile == null) {
            if (name == username)
                if (phone == phoneNumber) {
                        tos("Tidak ada yang berubah")
                        return
                    }
            viewModel.userUpdate("put",name,phone)
        } else {
            lifecycleScope.launch {
                tos("Tunggu")
                val compressPhoto = compressFile(photoFile!!)
                if (compressPhoto != null) {
                    viewModel.userUpdateWithPhoto("put",name,phone, compressPhoto)
                }
            }
        }

    }

    //compres photo
    suspend fun compressFile(filePhoto: File): File? {
        println("Compress 1")
        try {
            println("Compress 2")
            return Compressor.compress(this, filePhoto) {
                resolution(720, 720)
                quality(80)
                format(Bitmap.CompressFormat.JPEG)
                size(520)
            }
        } catch (e: Exception) {
            println("Compress 3")
            tos("Gagal kompress anda bisa mengganti foto lain")
            e.printStackTrace()
            return null
        }

    }

    private fun initClick() {
        binding.ivCentangEditProfil.setOnClickListener {
            validateForm()

        }

        binding.ivEditProfil.setOnClickListener {
            if (checkPermissionGallery()) {
                openGallery()
            } else {
                requestPermissionGallery()
            }
        }

    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show(" Please Wait Save Profil")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<EditProfileActivity>()
                                finish()
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }
            }
        }
    }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == 200) {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Ijin gallery ditolak", Toast.LENGTH_SHORT).show()
                }
            }

        }

        private var activityLauncherGallery =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.data?.let {
                    generateFileImage(it)
                }
            }

        private fun openGallery() {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityLauncherGallery.launch(galleryIntent)
        }

        private fun checkPermissionGallery(): Boolean {
            return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun requestPermissionGallery() {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                110
            )
        }

        private fun generateFileImage(uri: Uri) {
            try {
                val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
                val fileDescriptor = parcelFileDescriptor?.fileDescriptor
                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor?.close()

                val orientation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getOrientation2(uri)
                } else {
                    getOrientation(uri)
                }

                val file = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    createImageFile()
                } else {
                    //File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}" + File.separator + "BurgerBangor", getNewFileName())
                    File(externalCacheDir?.absolutePath, getNewFileName())
                }

                val fos = FileOutputStream(file)
                var bitmap = image

                if (orientation != -1 && orientation != 0) {

                    val matrix = Matrix()
                    when (orientation) {
                        6 -> matrix.postRotate(90f)
                        3 -> matrix.postRotate(180f)
                        8 -> matrix.postRotate(270f)
                        else -> matrix.postRotate(orientation.toFloat())
                    }
                    bitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                binding.ivEditProfil.setImageBitmap(bitmap)
                photoFile = file
            } catch (e: Exception) {
                e.printStackTrace()
                binding.root.snacked("File ini tidak dapat digunakan")
            }
        }

        @SuppressLint("Range")
        private fun getOrientation(shareUri: Uri): Int {
            val orientationColumn = arrayOf(MediaStore.Images.Media.ORIENTATION)
            val cur = contentResolver.query(
                shareUri,
                orientationColumn,
                null,
                null,
                null
            )
            var orientation = -1
            if (cur != null && cur.moveToFirst()) {
                if (cur.columnCount > 0) {
                    orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
                }
                cur.close()
            }
            return orientation
        }

        @SuppressLint("NewApi")
        private fun getOrientation2(shareUri: Uri): Int {
            val inputStream = contentResolver.openInputStream(shareUri)
            return getOrientation3(inputStream)
        }

        @SuppressLint("NewApi")
        private fun getOrientation3(inputStream: InputStream?): Int {
            val exif: ExifInterface
            var orientation = -1
            inputStream?.let {
                try {
                    exif = ExifInterface(it)
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return orientation
        }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            // Create an image file name
            val timeStamp = DateTimeHelper().createAtLong().toString()
            val storageDir =
                getAppSpecificAlbumStorageDir(Environment.DIRECTORY_DOCUMENTS, "Attachment")
            return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
        }

        private fun getNewFileName(isPdf: Boolean = false): String {
            val timeStamp = DateTimeHelper().createAtLong().toString()
            return if (isPdf) "PDF_${timeStamp}_.pdf" else "JPEG_${timeStamp}_.jpg"
        }

        private fun getAppSpecificAlbumStorageDir(albumName: String, subAlbumName: String): File {
            val file = File(getExternalFilesDir(albumName), subAlbumName)
            if (!file.mkdirs()) {
            }
            return file
    }
}


/*
        binding.ivSilangEditProfil.setOnClickListener {
            openActivity<ProfileActivity>()
        }

        binding.ivCentangEditProfil.setOnClickListener {
            val name = binding.etNameEditProfil.textOf()
            val phoneNumber = binding.etPhoneEditProfil.textOf()
            tos("save")
            viewModel.userUpdate("put",name, phoneNumber)


            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.apiResponse.collect {
                            when (it.status) {
                                ApiStatus.LOADING -> loadingDialog.show(" Please Wait Save Profil")
                                ApiStatus.SUCCESS -> {
                                    loadingDialog.dismiss()
                                    openActivity<ProfileActivity>()
                                    finish()
                                }
                                else -> loadingDialog.setResponse(it.message ?: return@collect)
                            }
                        }
                    }
                }
            }

        }*/

