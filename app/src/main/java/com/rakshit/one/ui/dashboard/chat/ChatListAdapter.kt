package com.rakshit.one.ui.dashboard.chat

import android.net.Uri
import android.sleek.construction.config.Config
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rakshit.one.R
import com.test.papers.config.CHAT_TYPE
import com.test.papers.utils.extension.gone
import com.test.papers.utils.extension.visible

class ChatListAdapter(
    val itemClick: (MyChat, Uri) -> Unit,
    val showToastLoading: (Boolean) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.MyHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPR_RIGHT = 1
    var list: ArrayList<MyChat> = arrayListOf()

    fun addItems(myChat: MyChat) {
        list.add(myChat)
        notifyItemInserted(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        return if (viewType == MSG_TYPE_LEFT) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_chat_left, parent, false)
            MyHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_chat_right, parent, false)
            MyHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyHolder, p: Int) {
        val data = list[p]

        when (data.type) {
            CHAT_TYPE.TEXT -> {
                holder.cardPdf.gone()
                holder.mMessage.visible()
                holder.cardImage.gone()
                holder.mMessage.text = data.message
            }
            CHAT_TYPE.IMAGE -> {
//                holder.cardPdf.gone()
//                val storageRef = storage.reference
//                holder.cardImage.visible()
//                holder.mMessage.gone()
//
//                val islandRef = storageRef.child(data.message)
//                val localFile = File.createTempFile("images", "jpg")
//                islandRef.getFile(localFile).addOnSuccessListener { response ->
//
//                    holder.image.loadImage(localFile, showBorder = true)
//                    holder.itemView.setOnClickListener { view ->
//                        showToastLoading.invoke(true)
//                        response.storage.downloadUrl.addOnCompleteListener {
//                            itemClick.invoke(data, it.result)
//                        }.addOnFailureListener {
//                            showToastLoading.invoke(false)
//                        }
//                    }
//
//                }.addOnFailureListener {
//                    showToastLoading.invoke(false)
//                }
            }
            CHAT_TYPE.PDF -> {
//                holder.cardImage.gone()
//                holder.mMessage.gone()
//                holder.cardPdf.visible()
//
//                val storageRef = storage.reference
//                val islandRef = storageRef.child(data.message)
//                val localFile = File.createTempFile("pdf", "pdf")
//
//                islandRef.getFile(localFile).addOnSuccessListener { response ->
//                    val fileName = response.storage.name
//                    holder.mPdfName.text = fileName
//
//                    holder.itemView.setOnClickListener { view ->
//                        showToastLoading.invoke(true)
//                        response.storage.downloadUrl.addOnCompleteListener {
//                            itemClick.invoke(data, it.result)
//                        }.addOnFailureListener {
//                            showToastLoading.invoke(false)
//                        }
//                    }
//                }.addOnFailureListener {
//                    showToastLoading.invoke(false)
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].senderId == Config.uid) {
            MSG_TYPR_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mMessage: TextView
        val image: ImageView
        val cardImage: CardView
        val cardPdf: CardView
        val mPdfName: TextView


        init {
            mMessage = itemView.findViewById(R.id.tv_message)
            image = itemView.findViewById(R.id.iv_image)
            cardImage = itemView.findViewById(R.id.card_image)
            cardPdf = itemView.findViewById(R.id.card_pdf)
            mPdfName = itemView.findViewById(R.id.tv_pdf_name)
        }
    }
}