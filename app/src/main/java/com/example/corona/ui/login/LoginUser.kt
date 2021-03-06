package com.example.corona.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.corona.R
import com.example.corona.ui.MainActivity.Companion.conected
import com.example.corona.ui.Util
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_user_fragment.*

class LoginUser : Fragment() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    companion object {
        fun newInstance() = LoginUser()

    }

    private lateinit var viewModel: LoginUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginUserViewModel::class.java)

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= "تسجيل الدخول"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Util.getProperty("gmailToken", context!!))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        google_login_btn.setOnClickListener {
            signIn()


        }

        google_logout_btn.setOnClickListener{
            signOut()
            Toast.makeText(context,getString(R.string.outMsg),Toast.LENGTH_SHORT).show()
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            conected=handleSignInResult(task)

            if(conected){
                Toast.makeText(context,getString(R.string.inMsg),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>):Boolean {
        val account = completedTask.getResult(
            ApiException::class.java
        )

        val googleId = account?.id ?: ""
        try {

            // Signed in successfully

            Log.i(getString(R.string.GoogleID),googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i(getString(R.string.GoogleFirstName), googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i(getString(R.string.GoogleLastName), googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i(getString(R.string.GoogleEmail), googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i(getString(R.string.GoogleProfilePicURL), googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i(getString(R.string.GoogleIDToken), googleIdToken)



        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                getString(R.string.failedCode), e.statusCode.toString()
            )
        }
        return googleId!=""
    }

    private fun signOut() {
        conected=false
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(activity!!) {
                // Update your UI here
            }
    }

    private fun revokeAccess() { // delete account
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(activity!!) {
                // Update your UI here
            }
    }
}
