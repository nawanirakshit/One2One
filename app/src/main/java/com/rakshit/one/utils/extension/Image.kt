package com.rakshit.one.utils.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rakshit.one.R

fun <T> ImageView.loadImage(
    source: T,
    showBorder: Boolean = true,
    @DrawableRes errorImage: Int = 0
) {

    Glide.with(this.context)
        .load(source)
        .apply(requestOptions(errorImage))
        .placeholder(R.mipmap.ic_launcher_round)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun <T> ImageView.loadProfileImage(
    source: T,
    showBorder: Boolean = true,
    @DrawableRes errorImage: Int = 0, context: Context
) {

    val options: RequestOptions =
        RequestOptions().override(350)
            .transform(CenterCrop(), RoundedCorners(200))

    Glide.with(this.context)
        .load(source)
        .timeout(60000)
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .apply(options)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun <T> ImageView.loadFullImage(
    source: T,
    @DrawableRes errorImage: Int = 0, context: Context
) {

//    Fresco.newBuilder(this as SimpleDraweeView)
//        .setPlaceholderImage(R.color.white)
//        .withProgressBar(true)
//        .setRoundCircleColor(android.R.color.white)
//        .roundAsCircle(false)
//        .setRoundCircleColor(R.color.white)
//        .setUri(Uri.parse(source as String))
//        .build(context)
}

fun ImageView.requestOptions(errorImage: Int): RequestOptions {
    return RequestOptions()
        .useUnlimitedSourceGeneratorsPool(true)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .fitCenter()
//        .placeholder(GradientDrawable().apply { setColor(context.getColorCompat(R.color.teal_200)) })
        .error(R.mipmap.ic_launcher)
        .override(width, height)
}

/**
 * Setting Image to a background
 *
 * @param imageURL: The URL needed to be Displayed
 */
fun ViewGroup.loadImageAsBackground(imageURL: String) {
    Glide.with(context)
        .load(imageURL)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(object :
            CustomTarget<Drawable>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                background = resource
            }

        })
}

/*
inline fun palletListener(view: ImageView, crossinline body: (Palette) -> Unit): BitmapImageViewTarget {
    return object : BitmapImageViewTarget(view) {
        override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
            super.onResourceReady(resource, transition)
            Palette.from(resource).generate { palette: Palette ->
                body.invoke(palette)
            }
        }
    }
}*/