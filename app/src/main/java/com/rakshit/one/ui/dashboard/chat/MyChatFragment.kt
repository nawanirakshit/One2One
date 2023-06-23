package com.rakshit.one.ui.dashboard.chat

import android.os.Bundle
import android.sleek.construction.config.Config
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.rakshit.one.R
import com.rakshit.one.ui.dashboard.DashboardViewModel
import com.test.papers.config.CHAT_TYPE
import com.test.papers.config.ChatConst
import com.test.papers.config.ConstantsFirestore
import com.test.papers.config.IntentKey
import com.test.papers.config.IntentKey.FIRESTORE_USERS
import com.test.papers.config.IntentKey.REALTIME_CHAT
import com.test.papers.config.MetadataConst
import com.test.papers.config.RECEIVERDATA
import com.test.papers.kotlin.KotlinBaseFragment
import com.test.papers.utils.extension.showToast
import org.koin.android.ext.android.inject

class MyChatFragment : KotlinBaseFragment(R.layout.fragment_my_chat) {

    private val viewModel: DashboardViewModel by inject()

    private val mMessage: AppCompatEditText by lazy { requireView().findViewById(R.id.et_message) }
    private val mNoBill: ConstraintLayout by lazy { requireView().findViewById(R.id.no_list_data) }
    private val mOpponentName: TextView by lazy { requireView().findViewById(R.id.toolbar_title) }

    private val mRecycler: RecyclerView by lazy { requireView().findViewById(R.id.recycler_single_chat) }
    private lateinit var adapter: ChatListAdapter

    private val database by lazy { FirebaseDatabase.getInstance() }

    private lateinit var key: String

    private val myChatList: ArrayList<MyChat> = arrayListOf()

    private val opponentName: String by lazy {
        requireArguments().getString(ConstantsFirestore.NAME).toString()
    }
    private val opponentId: String by lazy {
        requireArguments().getString(ConstantsFirestore.UID).toString()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        if (requireArguments().getString(IntentKey.CHAT_KEY) != null) {
            key = requireArguments().getString(IntentKey.CHAT_KEY).toString()
            findChats(key)
        } else {
            findKey()
        }

        getOpponentDetails()
    }

    private fun getOpponentDetails() {
        println("OPPONENT ID >>> $opponentId")
        val docRef = Firebase.firestore.collection(FIRESTORE_USERS).document(opponentId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                val name = document.data?.get(ConstantsFirestore.NAME)?.toString()
                val image = document.data?.get(ConstantsFirestore.IMAGE)?.toString()

                println("NAME >>>> $name")
                println("image >>>> $image")

                mOpponentName.text = name
            }
        }.addOnFailureListener { exception ->
            println("EXCEPTION >>>>> ${exception.localizedMessage}")
        }

    }

    private fun initViews() {
        mOpponentName.text = opponentName

        requireView().findViewById<AppCompatImageView>(R.id.button_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        requireView().findViewById<ImageView>(R.id.iv_send_message).setOnClickListener {
            val message = mMessage.text.toString().trim()

            if (message.isBlank()) {
                showToast("Enter a message")
            } else {

                sendMessage(opponentId, message, CHAT_TYPE.TEXT)
            }
        }

        adapter = ChatListAdapter({ it, downloadUrl ->
            hideLoading()
            when (it.type) {
                CHAT_TYPE.IMAGE -> {
//                    replaceFragment<DisplayImageFragment>(
//                        userTag = "Display Image",
//                        container = com.google.firebase.database.R.id.frame_layout_main,
//                        addToBackStack = true,
//                        animation = true
//                    ) {
//                        putString(IntentKey.IMAGE_URL, downloadUrl.toString())
//                    }
                }

                CHAT_TYPE.PDF -> {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            downloadUrl
//                        )
//                    )
                }
            }
        }, {
            if (it) {
                showToast("Downloading")
                showLoading()
            } else {
                showToast("Something went wrong try again")
                hideLoading()
            }
        })

        mRecycler.layoutManager = LinearLayoutManager(requireContext())
        mRecycler.adapter = adapter

    }

    private fun sendMessage(opponentId: String, message: String, type: String) {
        val myRef = database.reference.child(REALTIME_CHAT)

        val ourChat = myRef.child(key)

        val time: MutableMap<String, String> = ServerValue.TIMESTAMP

        val key = ourChat.child("chat").push().key
        if (key == null) {
            Log.w("TAG", "Couldn't get push key for posts")
            return
        }

        val chats = hashMapOf(
            ChatConst.MESSAGE to message,
            ChatConst.RECEIVER_ID to opponentId,
            ChatConst.SENDER_ID to Config.uid,
            ChatConst.CREATED_AT to time,
            ChatConst.TYPE to type,
        )

        val childUpdates = hashMapOf(
            "/metadata/${MetadataConst.LAST_MESSAGE}" to message,
            "/metadata/unread_$opponentId" to ServerValue.increment(1),
            "/metadata/unread_${Config.uid}" to 0,
            "/chat/$key" to chats,
        )

        ourChat.updateChildren(childUpdates)

        mMessage.text?.clear()
    }


    private fun findKey() {

        database.reference.child(REALTIME_CHAT).get().addOnSuccessListener {
            for (data in it.children) {
                if (data.key!!.contains(Config.uid) && data.key!!.contains(opponentId)) {
                    key = data.key!!
                    println("KEY >>>> $key")
                    return@addOnSuccessListener
                }
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }.addOnCompleteListener {

            if (!::key.isInitialized) {
                generateInitialChat()
            } else {
                findChats(key)
            }
        }
    }

    private fun generateInitialChat() {
        val myRef = database.reference.child(REALTIME_CHAT)
        key = "${Config.uid}+$opponentId"

        val ourChat = myRef.child(key)

        val senderData = hashMapOf(
            RECEIVERDATA.FULL_NAME to Config.name,
            RECEIVERDATA.USER_ID to Config.uid,
        )

        val receiverData = hashMapOf(
            RECEIVERDATA.FULL_NAME to opponentName,
            RECEIVERDATA.USER_ID to opponentId,
        )

        val childUpdates: Map<String, Any> = hashMapOf(
            "/metadata/unread_$opponentId" to 0,
            "/metadata/unread_${Config.uid}" to 0,
            "/metadata/senderData" to senderData,
            "/metadata/receiverData" to receiverData,
        )

        ourChat.updateChildren(childUpdates)

        findChats(key)
    }

    private fun findChats(key: String) {

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildAdded:" + dataSnapshot.key!!)

                val gson = Gson()
                val jsonElement = gson.toJsonTree(dataSnapshot.value)
                val myChat: MyChat = gson.fromJson(jsonElement, MyChat::class.java)

                myChatList.add(myChat)
                adapter.addItems(myChat)

                mRecycler.postDelayed({
                    mRecycler.scrollToPosition(mRecycler.adapter?.itemCount!! - 1)
                }, 100)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("MY CHAT >>", "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildMoved:" + dataSnapshot.key!!)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("MY CHAT >>", "postComments:onCancelled", databaseError.toException())
                Toast.makeText(
                    context, "Failed to load chat.", Toast.LENGTH_SHORT
                ).show()
            }
        }

        val channelKey = database.reference.child(REALTIME_CHAT).child(key)

        channelKey.child("chat").addChildEventListener(childEventListener)

        //Update unRead count ot 0 on metadata
        val childUpdates = hashMapOf(
            "/metadata/unread_${Config.uid}" to 0,
        )
        channelKey.updateChildren(childUpdates as Map<String, Any>)
    }
}

data class MyChat(
    var createdAt: String,
    var message: String,
    var receiverId: String,
    var senderId: String,
    var type: String,
)