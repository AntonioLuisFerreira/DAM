package dam.a47500.whereto.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dam.a47500.whereto.R
import dam.a47500.whereto.data.User
import dam.a47500.whereto.database.DBModule
import dam.a47500.whereto.viewmodel.UserViewModel


class RegisterActivity: AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var buttonRegister: Button

    val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val includedLayout = findViewById<View>(R.id.register)

        usernameEditText = includedLayout.findViewById(R.id.editTextUsername)
        emailEditText = includedLayout.findViewById(R.id.editTextEmail)
        passwordEditText = includedLayout.findViewById(R.id.editTextNumberPassword)

        buttonRegister = includedLayout.findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun registerUser(email: String, password: String) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        registerUserFirebase(user)
                    }
                    signInUser(email, password)
                    Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInUser(email: String, password: String) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun registerUserFirebase(user: FirebaseUser) {
        val email = user.email
        val username = usernameEditText.text.toString()
        val uid = user.uid
        val firebaseUser = User(username.toString(), email.toString(),uid.toString())


        viewModel.initViewModel(DBModule.getInstance(this).firebaseAuthRepository)
        viewModel.setUser(firebaseUser)
    }

}