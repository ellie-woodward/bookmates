package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.data.api.BookMatesApi
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class LoginActivity : ComponentActivity() {

    var currentUser : String?= null

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

//    var csrf_token : String? = null

    private var api: BookMatesApi

    init{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://bookmate.discovery.cs.vt.edu/")
            .client(provideOkHttpClient())
//            .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(BookMatesApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_login)

        val username = findViewById<TextView>(R.id.username);
        val password = findViewById<TextView>(R.id.password);

        val loginbtn = findViewById<Button>(R.id.login_btn);
        val registerbtn = findViewById<Button>(R.id.register_btn);

        var jsonUser: User

        loginbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                var response = fetchLogin(username.getText().toString(), password.getText().toString())
//                jsonUser = fetchLogin(username.getText().toString(), password.getText().toString())
//                if (username.getText().toString().equals("user") && password.getText().toString().equals("password")){
                if (response){
                    Toast.makeText(this@LoginActivity, "login successful", Toast.LENGTH_SHORT).show()
                    currentUser =  username.getText().toString()
                    onStart()
                }
                else{
                    Toast.makeText(this@LoginActivity, "login not successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?){
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        })

    }

    override fun onStart(){
        super.onStart()
        if (currentUser != null){
            sendToMainActivity()
        }
    }

    private fun sendToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("username", currentUser) // replace "key" and "value" with your actual key-value pair
        startActivity(intent)
    }

    fun fetchLogin(name: String, password: String): Boolean {

//        val token = api.getToken()

        val userPass = JSONObject()
        userPass.put("username", name)
        userPass.put("password", password)

        val jsonObject = JSONObject()
        jsonObject.put("login_data", userPass)

        val requestBody = userPass.toString().toRequestBody(JSON)

        Log.e("HErE",  jsonObject.toString())

        api.loginData(requestBody).enqueue(
            object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("The failure Response", t.message.toString())
                }
                override fun onResponse( call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    val addedUser = response
                    Log.e("The success Response", response.toString())
//                    onResult(addedUser)
                }
            })

//        val response =  api.loginData(requestBody)tha


        return false
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
        return okhttpClientBuilder.build()
    }

}