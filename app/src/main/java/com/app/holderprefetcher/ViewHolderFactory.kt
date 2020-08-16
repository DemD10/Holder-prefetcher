package com.app.holderprefetcher

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

object ViewHolderFactory {

    const val TYPE_GOOGLE = 0
    const val TYPE_FACEBOOK = 1
    const val TYPE_APPLE = 2
    const val TYPE_MICROSOFT = 3
    const val TYPE_AMAZON = 4
    const val TYPE_IBM = 5
    const val TYPE_INTEL = 6
    const val TYPE_QUALCOMM = 7

    fun createHolder(parent: ViewGroup, viewType: Int) : CompanyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        Thread.sleep(30) // simulation of hard work
        return when (viewType) {
            TYPE_GOOGLE -> GoogleViewHolder(view)
            TYPE_FACEBOOK -> FacebookViewHolder(view)
            TYPE_APPLE -> AppleViewHolder(view)
            TYPE_MICROSOFT -> MicrosoftViewHolder(view)
            TYPE_AMAZON -> AmazonViewHolder(view)
            TYPE_IBM -> IbmViewHolder(view)
            TYPE_INTEL -> IntelViewHolder(view)
            TYPE_QUALCOMM -> QualcommViewHolder(view)
            else -> throw IllegalArgumentException("View type not found")
        }
    }
}

abstract class CompanyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract val logo: Drawable?

    private val textView = view.findViewById<TextView>(R.id.text)

    fun bind(text: String) {
        textView.text = text
        textView.setCompoundDrawablesWithIntrinsicBounds(logo, null, null, null)
    }
}

class GoogleViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_google)
}

class FacebookViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_facebook)
}

class AppleViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_apple)
}

class MicrosoftViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_microsoft)
}

class AmazonViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_amazon)
}

class IbmViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_ibm)
}

class IntelViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_intel)
}

class QualcommViewHolder(view: View): CompanyViewHolder(view) {
    override val logo: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_qualcomm)
}