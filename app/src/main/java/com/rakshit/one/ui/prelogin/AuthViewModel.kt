package com.rakshit.one.ui.prelogin

import android.app.Activity
import android.sleek.construction.config.Config
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.papers.config.ConstantsFirestore
import com.test.papers.kotlin.viewmodel.KotlinBaseViewModel
import com.test.papers.kotlin.viewmodel.VolatileLiveData

class AuthViewModel : KotlinBaseViewModel() {

    var successRegister = VolatileLiveData<String>()
    var successForgotPassword = VolatileLiveData<Boolean>()
    var successLogin = VolatileLiveData<String>()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(name: String, email: String, password: String, activity: Activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser!!

                    Config.uid = user.uid
                    Config.email = email
                    Config.name = name
                    Config.isLoggedIn = true


                    successRegister.postValue("")
                    updateProfile(name, user)

                } else {
                    println("EXCEPTION >>>> ${task.exception?.localizedMessage}")
                    successRegister.postValue(task.exception?.localizedMessage)
                }
            }
    }

    private fun updateProfile(name: String, user: FirebaseUser) {
        val map = hashMapOf(
            ConstantsFirestore.NAME to name,
            ConstantsFirestore.IMAGE to user.photoUrl,
            ConstantsFirestore.EMAIL to user.email,
            ConstantsFirestore.UID to user.uid,
        )

        Firebase.firestore.collection("users").document(user.uid).set(map)
            .addOnSuccessListener { documentReference ->
//                Log.d("FIRESTORE >>", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
//                Log.w("FIRESTORE >>", "Error adding document", e)
            }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
//                        .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
            .build()

        user.updateProfile(profileUpdates)
    }

    fun login(email: String, password: String, activity: Activity) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {

                    val user = mAuth.currentUser!!

                    Config.uid = user.uid
                    Config.email = email
                    Config.name = user.displayName.toString()
                    Config.isLoggedIn = true

                    successLogin.postValue("")

                } else {
                    println("EXCEPTION >>>> ${task.exception?.localizedMessage}")
                    successLogin.postValue(task.exception?.localizedMessage)
                }
            }
    }

    fun checkForgotPassword(email: String) {

    }

}