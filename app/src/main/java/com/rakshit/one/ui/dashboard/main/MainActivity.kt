package com.rakshit.one.ui.dashboard.main

import android.content.Context
import android.os.Bundle
import android.sleek.construction.config.Config
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.rakshit.one.R
import com.rakshit.one.model.chatdata.Metadata
import com.rakshit.one.model.chatdata.ReceiverData
import com.rakshit.one.model.chatdata.ResponseAvailableChat
import com.rakshit.one.ui.dashboard.chat.MyChatFragment
import com.rakshit.one.ui.dashboard.find.FindUserFragment
import com.test.papers.config.ConstantsFirestore
import com.test.papers.config.IntentKey
import com.test.papers.kotlin.KotlinBaseActivity
import com.test.papers.kotlin.replaceFragment
import com.test.papers.utils.extension.gone
import com.test.papers.utils.extension.visible

class MainActivity : KotlinBaseActivity() {

    private val database by lazy { FirebaseDatabase.getInstance() }

    private val mNoBill: ConstraintLayout by lazy { findViewById(R.id.no_list_data) }
    private val adapter by lazy { MainChatAdapter(this::onSelect, this) }

    private fun onSelect(users: ResponseAvailableChat, userData: ReceiverData) {
        replaceFragment<MyChatFragment>(
            userTag = MyChatFragment::class.java.name,
            container = android.R.id.content,
            addToBackStack = true,
            animation = true
        ) {

            val opponentId = users.metadata.key.replace(Config.uid, "").replace("+", "")
            putString(ConstantsFirestore.UID, opponentId)
            putString(ConstantsFirestore.NAME, userData.fullName)
        }
    }

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        initView()

        observeViews()

        getChatData()
    }

    private fun getChatData() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildAdded:" + dataSnapshot.key!!)

                if (dataSnapshot.key!!.contains(Config.uid)) {

                    val gson = Gson()
                    val jsonElement = gson.toJsonTree(dataSnapshot.value)
                    val metadata: ResponseAvailableChat =
                        gson.fromJson(jsonElement, ResponseAvailableChat::class.java)

                    metadata.metadata.unread_me =
                        dataSnapshot.child("metadata").child("unread_${Config.uid}").value as Long
                    metadata.metadata.key = dataSnapshot.key!!

                    adapter.addtoList(metadata)

                    mNoBill.gone()
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildChanged: ${dataSnapshot.key}")

                val gson = Gson()
                val jsonElement = gson.toJsonTree(dataSnapshot.value)
                val metadata: ResponseAvailableChat =
                    gson.fromJson(jsonElement, ResponseAvailableChat::class.java)

                metadata.metadata.unread_me =
                    dataSnapshot.child("metadata").child("unread_${Config.uid}").value as Long
                metadata.metadata.key = dataSnapshot.key!!

                adapter.getOriginalList().forEachIndexed { index, element ->
                    if (element.metadata.key == dataSnapshot.key) {
                        adapter.updateItem(index, metadata)
                        return@forEachIndexed
                    }
                }
                mNoBill.gone()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("MY CHAT >>", "onChildRemoved:" + dataSnapshot.key!!)

                adapter.getOriginalList().forEachIndexed { index, element ->
                    if (element.metadata.key == dataSnapshot.key) {
                        adapter.removeItem(index)
                    }
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MY CHAT >>", "onChildMoved:" + dataSnapshot.key!!)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("MY CHAT >>", "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load chat.", Toast.LENGTH_SHORT).show()
            }
        }

        val channelKey = database.reference.child(IntentKey.REALTIME_CHAT)
        channelKey.addChildEventListener(childEventListener)
    }

    private fun initView() {
        findViewById<FloatingActionButton>(R.id.fab_find_user).setOnClickListener {
            replaceFragment<FindUserFragment>(
                userTag = FindUserFragment::class.java.name,
                container = android.R.id.content,
                addToBackStack = true,
                animation = true
            )
        }

        val mRecycler: RecyclerView = findViewById(R.id.recycler_chat)
        mRecycler.layoutManager = LinearLayoutManager(this)
        mRecycler.adapter = adapter
    }

    private fun observeViews() {

    }


}