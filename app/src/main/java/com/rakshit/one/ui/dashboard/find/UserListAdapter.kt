package com.rakshit.one.ui.dashboard.find

import android.content.Context
import android.sleek.construction.kotlin.adapter.BaseAdapter
import com.rakshit.one.utils.extension.loadProfileImage
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.rakshit.one.R
import com.rakshit.one.ui.dashboard.Users
import de.hdodenhof.circleimageview.CircleImageView

class UserListAdapter(val itemClick: (Users) -> Unit, val context: Context) :
    BaseAdapter<Users>(R.layout.item_find_user) {

    override fun onBindViewHolder(holder: IViewHolder, position: Int) {
        val data = list[position]
        val iView = holder.itemView

        val mParent: CardView = iView.findViewById(R.id.card_parent)
        val mUserName: TextView = iView.findViewById(R.id.user_name)
        val mUserImage: CircleImageView = iView.findViewById(R.id.user_image)

        mUserName.text = data.name

//        if (data.image != null && data.image.isNotEmpty()) {
//            println("DATA IMAGE >>>>> ${data.image}")
//            mUserImage.loadProfileImage(data.image, context = context)
//        }

        mParent.setOnClickListener {
            itemClick.invoke(data)
        }
    }
}