package com.example.randombear

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.randombear.databinding.FragmentFirstBinding
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.Scalars
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {


    val retrofit = Retrofit.Builder()
        .baseUrl("https://meowfacts.herokuapp.com/")
        //.baseUrl("https://example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        //.addConverterFactory(Converter.create())
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply{level=HttpLoggingInterceptor.Level.BODY}).build())
        .build()


    var bundle : Bundle = Bundle()
    var factGlobal = "no facts yet"

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFact()
        binding.buttonFirst.setOnClickListener {
            bundle = bundleOf(key_cat to binding.textviewFirst.text.toString(),
            key_fact to factGlobal)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment,bundle)
        }
    }

    private fun loadFact()
    {
        val apiService = retrofit.create(FactsInterface::class.java)
        apiService.getText().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val text = response.body()
                factGlobal =text?.get("data")?.asJsonArray?.get(0).toString()
                Log.i("kpopp", factGlobal)
                // Handle the response
                // bundle = bundleOf(key_fact to text)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Handle the error
                Log.i("kpopp", "faild get string")
                Log.i("kpopp", t.toString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {val key_cat = "memtext"; val key_fact = "memfact"}
}