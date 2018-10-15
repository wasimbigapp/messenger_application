package app.common.adapters

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.UiThread
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.io.Serializable
import java.util.*


/**
 * Created by bedprakash.r on 05/06/18.
 */


data class FragmentPayload(@PageTypes val pageType: String, @Nullable val pageName: String?, @Nullable val params: Map<String, Any>?) : Serializable

abstract class FragmentAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    protected var items: List<FragmentPayload> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return getPagerItem(items[position])
    }

    open fun getPagerItem(fragmentPayload: FragmentPayload): Fragment {
        throw IllegalArgumentException(" Unhandled Payload $fragmentPayload")
    }

    @UiThread
    fun updateItems(@NonNull newItems: List<FragmentPayload>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return items[position].pageName ?: ""
    }
}