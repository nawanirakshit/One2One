package com.rakshit.one.ui.dashboard.main

import android.content.Context
import android.sleek.construction.config.Config
import android.sleek.construction.kotlin.adapter.BaseAdapter
import android.util.Log
import com.rakshit.one.utils.extension.loadProfileImage
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rakshit.one.R
import com.rakshit.one.model.chatdata.ReceiverData
import com.rakshit.one.model.chatdata.ResponseAvailableChat
import com.test.papers.config.ConstantsFirestore
import com.test.papers.config.IntentKey
import com.test.papers.utils.extension.gone
import com.test.papers.utils.extension.visible
import de.hdodenhof.circleimageview.CircleImageView

class MainChatAdapter(
    val itemClick: (ResponseAvailableChat, ReceiverData) -> Unit,
    val context: Context
) :
    BaseAdapter<ResponseAvailableChat>(R.layout.item_user) {

    override fun onBindViewHolder(holder: IViewHolder, position: Int) {
        val data = list[position].metadata
        val iView = holder.itemView

        val mParent: CardView = iView.findViewById(R.id.card_parent)
        val mUserName: TextView = iView.findViewById(R.id.user_name)
        val mUserImage: CircleImageView = iView.findViewById(R.id.user_image)
        val unreadCount: TextView = iView.findViewById(R.id.tv_unread_count)
        val lastMessage: TextView = iView.findViewById(R.id.tv_last_message)
        val mStatus: TextView = iView.findViewById(R.id.tv_status)

        val userData = if (Config.uid == data.receiverData.userId) {
            data.senderData
        } else {
            data.receiverData
        }

        val docRef =
            Firebase.firestore.collection(IntentKey.FIRESTORE_USERS).document(userData.userId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                if (snapshot.data?.get(ConstantsFirestore.IS_ONLINE) != null) {
                    val isOnline = snapshot.data?.get(ConstantsFirestore.IS_ONLINE) as Boolean
                    if (isOnline) {
                        mStatus.setBackgroundResource(R.drawable.bg_online);
                    } else {
                        mStatus.setBackgroundResource(R.drawable.bg_offline);
                    }
                } else {
                    println("IS ONLINE IS NULL >>>>>> ")
                }
            }
        }

        if (data.unread_me > 0) {
            unreadCount.visible()
            unreadCount.text = data.unread_me.toString()
        } else unreadCount.gone()

        lastMessage.text = data.lastMessage

        mUserName.text = userData.fullName
        if (userData.image != null) {
            mUserImage.loadProfileImage(userData.image, context = context)
        }

        mParent.setOnClickListener {
            itemClick.invoke(list[position], userData)
        }
    }
}