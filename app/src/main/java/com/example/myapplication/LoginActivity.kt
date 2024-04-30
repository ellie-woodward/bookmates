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
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.Account
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import com.example.myapplication.UserStorage


class LoginActivity : ComponentActivity() {

    var currentUser : String?= null

    private var api: BookMatesApi

    init{
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://bookmate.discovery.cs.vt.edu/")
//            .client(provideOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_login)

        val username = findViewById<TextView>(R.id.username);
        val password = findViewById<TextView>(R.id.password);

        val loginbtn = findViewById<Button>(R.id.login_btn);
        val registerbtn = findViewById<Button>(R.id.register_btn);

        loginbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val response = fetchLogin(username.getText().toString(), password.getText().toString())
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

    suspend fun fetchLogin(name: String, password: String): Boolean {
        val response: Account
//        val token = api.getToken()
        try{
            response = api.loginData(name)
        } catch (e:Exception){
            return false
        }
        if (response.accountData.password.equals(password)){
            currentUser = name
            var myUser = UserStorage()
            myUser.setUser(response.accountData)
            return true
        }
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