package com.qdot.exptrackapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.qdot.exptrackapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var account: Auth0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        account = Auth0(
            "UhxICLL8v8LptoxG2XOYith5TnUSWW4A",
            "dev-smkd0rr5.us.auth0.com"
        )
        binding.loginBtn.setOnClickListener {
            loginWithBrowser()
        }
    }
    private fun loginWithBrowser() {
        WebAuthProvider.login(account)
            .withScheme("https")
            .withScope("openid profile email")
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(this@MainActivity,
                        error.message,Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: Credentials) {
                    val intent = Intent(this@MainActivity,HomeActivity::class.java)
                    intent.putExtra("email",result.user.email)
                    intent.putExtra("dp",result.user.pictureURL)
                    intent.putExtra("ev",result.user.isEmailVerified)
                    intent.putExtra("uid",result.user.getId())
                    startActivity(intent)
                    finish()
                }
            })
    }
}