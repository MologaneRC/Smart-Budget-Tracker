package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbudgettrack.data.AppDatabase
import com.example.smartbudgettrack.model.UserStats
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirect: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        // Bind views
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        signupButton = findViewById(R.id.signupButton)
        loginRedirect = findViewById(R.id.loginButton)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.updateProfile(userProfileChangeRequest { displayName = name })

                        val uid = user?.uid
                        val userMap = mapOf(
                            "name" to name,
                            "email" to email,
                            "phone" to phone
                        )

                        if (uid != null) {
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(uid)
                                .setValue(userMap)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        // Insert default UserStats with user's UID (not id=1)
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                val userStats = UserStats(id = 1, points = 0, level = 1)

                                                AppDatabase.getDatabase(applicationContext)
                                                    .userStatsDao().insertUserStats(userStats)

                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        this@SignupActivity,
                                                        "Signup successful",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                                                    finish()
                                                }
                                            } catch (e: Exception) {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        this@SignupActivity,
                                                        "Failed to initialize user stats: ${e.localizedMessage}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Signup failed: UID not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Unknown error occurred"
                        Toast.makeText(this, "Signup failed: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
