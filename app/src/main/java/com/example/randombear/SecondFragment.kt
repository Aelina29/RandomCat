package com.example.randombear

//import android.R
//import android.os.Build.VERSION_CODES.R
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.randombear.FirstFragment.Companion.key_cat
import com.example.randombear.databinding.FragmentSecondBinding
import java.io.File
import java.io.FileOutputStream


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val viewModel: CatViewModel by viewModels()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var picture_title = arguments?.getString(key_cat) ?: "meeeeeow"
        if (picture_title == "") {
            picture_title = "U don`t want talk with me"
        }
        val url = "https://cataas.com/cat/says/$picture_title"
        Log.i("kpop", url)

        setImage(url)

        setListener()

    }

    private fun setListener() {
        binding.buttonSave.setOnClickListener {
            //catImage = BitmapFactory.decodeResource(resources, R.drawable.item0)
            if (viewModel.catImage == null) return@setOnClickListener
            saveImage(viewModel.catImage!!)
        }
    }

    private fun setImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivCat.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.item0
                        )
                    )
                    binding.progress.isVisible = false
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModel.catImage = resource
                    //catImage = Bitmap.createBitmap(binding.ivCat.drawable.getIntrinsicWidth(), binding.ivCat.drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    //saveImage(resource!!)
                    binding.progress.isVisible = false
                    return false
                }
            })
            .into(binding.ivCat)
    }

    override fun onDestroyView() {
        Log.i("kpop", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    internal fun saveImage(image: Bitmap) {
        val savedImagePath: String

        val imageFileName = System.currentTimeMillis().toString() + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).toString() + "/Folder Name"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
                Toast.makeText(requireContext(), "Загрузка прошла успешно", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}