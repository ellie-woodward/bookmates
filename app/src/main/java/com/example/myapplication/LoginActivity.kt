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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


class LoginActivity : ComponentActivity() {

    var currentUser : String?= null

    var csrf_token : String? = null

    private lateinit var api: BookMatesApi

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
                if (response.message){
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

    suspend fun fetchLogin(name: String, password: String): LoginResponse {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://bookmate.discovery.cs.vt.edu/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        api = retrofit.create()

        val token = api.getToken()

        Log.e("THE TOKENNNNNNNNNNN", token.toString())

        val jsonObject = JSONObject()
        jsonObject.put("username", name)
        jsonObject.put("password", password)

        return api.loginData(jsonObject, csrf_token!!)
    }

}