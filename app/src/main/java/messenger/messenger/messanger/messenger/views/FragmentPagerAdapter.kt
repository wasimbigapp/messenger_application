package messenger.messenger.messanger.messenger.views

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import messenger.messenger.messanger.messenger.views.fragments.VerticalPagerFragment


data class PageConfig(val pageName: String,
                      val pageId: String,
                      var pageType: String,
                      var data: Any)

class FragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    var pages: List<PageConfig> = ArrayList()

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return VerticalPagerFragment()
    }

    fun updatePages(newPages: List<PageConfig>) {
        pages = newPages
        notifyDataSetChanged()
    }

}