package br.com.fiap.calculaflex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.fiap.calculaflex.extensions.getValue
import br.com.fiap.calculaflex.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btCreate.setOnClickListener{
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    inputEmail.getValue(),
                    inputPassword.getValue()
                ).addOnCompleteListener{
                    if(it.isSuccessful) {
                        saveDatabase()
                    } else {
                        Toast.makeText(this@SignUpActivity,
                            it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveDatabase() {
        val user = User(inputName.getValue(), inputEmail.getValue(), inputPhone.getValue())
        FirebaseDatabase.getInstance()
            .getReference("User")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(this@SignUpActivity,
                    "Usuario criado com sucesso!", Toast.LENGTH_SHORT).show()

                val intent = Intent()
                intent.putExtra("email", inputEmail.getValue())
                setResult(Activity.RESULT_OK,  intent)
                finish()
            } else {
                Toast.makeText(this@SignUpActivity,
                    it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
