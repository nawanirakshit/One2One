package com.rakshit.one.ui.dashboard

import android.sleek.construction.config.Config
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.papers.config.ConstantsFirestore
import com.test.papers.kotlin.viewmodel.KotlinBaseViewModel
import com.test.papers.kotlin.viewmodel.VolatileLiveData

class DashboardViewModel : KotlinBaseViewModel() {

    var successUserListing = VolatileLiveData<ArrayList<Users>>()

    fun getAllUsers() {

        Firebase.firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val listUsers = arrayListOf<Users>()
                for (document in result) {
                    Log.d("ALL USER DATA >>", "${document.id} => ${document.data}")

                    val map = document.data

                    val email = map[ConstantsFirestore.EMAIL]
                    val image = map[ConstantsFirestore.IMAGE]
                    val name = map[ConstantsFirestore.NAME]
                    val uid = map[ConstantsFirestore.UID]

                    if (uid != Config.uid) {
                        val user = Users(
                            name = name.toString(),
                            email = email.toString(),
                            image = image.toString(),
                            uid = uid.toString(),
                            documentId = document.id
                        )
                        listUsers.add(user)
                    }

                }
                successUserListing.postValue(listUsers)
            }
            .addOnFailureListener { exception ->
                Log.w("ALL USER DATA >>", "Error getting documents.", exception)
            }
    }

}

data class Users(
    var name: String,
    var email: String,
    var image: String,
    var uid: String,
    var documentId: String
)