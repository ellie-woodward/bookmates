package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.data.api.BookMatesApi
import com.example.myapplication.data.model.JsonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import kotlin.reflect.typeOf

class RegisterActivity : ComponentActivity() {

    var currentUser : String?= null

    private lateinit var api: BookMatesApi

    private lateinit var jsonObject: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        val email  = findViewById<TextView>(R.id.email);
        val username = findViewById<TextView>(R.id.username)

        val password = findViewById<TextView>(R.id.password);
        val passwordConfirm = findViewById<TextView>(R.id.password_confirm);

        val loginbtn = findViewById<Button>(R.id.login_btn);

        loginbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                if (password.getText().toString().equals(passwordConfirm.getText().toString())){
                    if (email.getText().toString() == "" || !email.getText().toString().contains("@")){
                        Toast.makeText(this@RegisterActivity, "Email is invalid", Toast.LENGTH_SHORT).show()
                    }
                    else if (username.getText().toString() == ("")){
                        Toast.makeText(this@RegisterActivity, "Username is blank", Toast.LENGTH_SHORT).show()
                    }
                    else if (password.getText().toString() == ""){
                        Toast.makeText(this@RegisterActivity, "Password is blank", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        currentUser = username.getText().toString();
                        var response = createAccount(username.getText().toString(), password.getText().toString(), email.getText().toString())
                        if (response.status == 200){
                            sendToMainActivity()
                        }
                        else{
                            Log.d("response", response.toString())
                        }
                    }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }}
    }

    suspend fun createAccount(name: String, password: String, email: String): JsonResponse {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://bookmate.discovery.cs.vt.edu/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create()

        val jsonObject = JSONObject()
        jsonObject.put("username", name)
        jsonObject.put("password", password)
        jsonObject.put("email", email)
        jsonObject.put("games_logged", 0)
        jsonObject.put("unique_games", 0)

        Log.d("JSON", jsonObject.toString())

        return api.createAccount(jsonObject)
    }

    private fun sendToMainActivity() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.putExtra("username", currentUser) // replace "key" and "value" with your actual key-value pair
        startActivity(intent)
    }
}