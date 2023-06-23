package com.test.papers.kotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.test.papers.utils.extension.checkCameraAndStoragePermission
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

abstract class KotlinBaseImageSelectActivity : KotlinBaseActivity() {

    companion object {
        const val RES_IMAGE = 100
    }

    private val compositeDisposable by lazy { CompositeDisposable() }


    fun chooseImage() {
        checkCameraAndStoragePermission {
            startActivityForResult(getPickImageIntent()!!, RES_IMAGE)
        }
    }

    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        /*  val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())*/

        intentList = addIntentsToList(this, intentList, pickIntent)
        //intentList = addIntentsToList(this.activity!!, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                "Choose Image"
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun setImageUri(): Uri {
        val folder = File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")
        if (!folder.exists())
            folder.mkdirs()
        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        val imageUri = FileProvider.getUriForFile(
            this,
            "com.beejameditation.beeja.provider",
            file
        )
        return imageUri!!
    }

    private fun createImageFile(): File {
        var image: File? = null
        try {
            var timeStamp =
                SimpleDateFormat(
                    "yyyyMMdd_HHmmss",
                    Locale.getDefault()
                ).format(Date());
            var imageFileName = "IMG_" + timeStamp + "_";
            var storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
            );
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return image!!;
    }

    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    abstract fun onHandledImageResult(file: File)
}