package com.example.myapplication.ui.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCameraBinding


class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    lateinit var pickImageBtn: Button
    lateinit var imageIV: ImageView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cameraViewModel =
            ViewModelProvider(this).get(CameraViewModel::class.java)

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pickImageBtn = root.findViewById<Button>(R.id.idBtnPickImage)
        imageIV = root.findViewById<ImageView>(R.id.idIVImage)

        pickImageBtn.setOnClickListener(object : View.OnClickListener {
            @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
            override fun onClick(view: View?) {
                val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                onActivityResult.launch(intent)
            }})

        return root
    }
    var onActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val selectedImageUri: Uri = data?.data!!
            if (null != selectedImageUri) {
                // update the image view in the layout
                imageIV.setImageURI(selectedImageUri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}