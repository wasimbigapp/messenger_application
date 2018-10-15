package messenger.messenger.messanger.messenger.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import messenger.messenger.messanger.messenger.R
import messenger.messenger.messanger.messenger.views.FragmentPagerAdapter
import messenger.messenger.messanger.messenger.views.PageConfig

class VerticalPagerFragment : Fragment(), View.OnClickListener {

    private lateinit var pager: ViewPager
    private lateinit var adapter: FragmentPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vertical_pager, container, false)
        findViews(view)
        return view
    }

    private fun findViews(view: View) {
        pager = view.findViewById(R.id.pager)
        adapter = FragmentPagerAdapter(childFragmentManager)
        pager.adapter = adapter

        view.findViewById<View>(R.id.btn_refresh).setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun create(): Fragment {
            return VerticalPagerFragment()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_refresh -> {
                (pager.adapter as FragmentPagerAdapter).updatePages(getPages())
            }
        }

    }

    private fun getPages(): List<PageConfig> {
        var pages: List<PageConfig> = ArrayList()
        pages += PageConfig("demo", "demo", "demo", "demo")
        pages += PageConfig("demo", "demo", "demo", "demo")
        return pages
    }

}