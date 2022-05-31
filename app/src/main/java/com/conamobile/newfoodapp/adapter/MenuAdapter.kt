package com.conamobile.newfoodapp.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.conamobile.newfoodapp.utils.draw.DuoOptionView

class MenuAdapter : BaseAdapter() {
    private var mOptions = ArrayList<String>()
    private val mOptionViews = ArrayList<DuoOptionView>()

    fun myMenuAdapter(options: ArrayList<String>) {
        mOptions = options
    }

    override fun getCount(): Int = mOptions.size

    override fun getItem(position: Int): Any {
        return mOptions[position]
    }

    fun setViewSelected(position: Int, selected: Boolean) {
        for (i in mOptionViews.indices) {
            if (i == position) {
                mOptionViews[i].isSelected = selected
            } else {
                mOptionViews[i].isSelected = !selected
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val option = mOptions[position]
        val optionView: DuoOptionView = if (convertView == null) {
            DuoOptionView(parent.context)
        } else {
            convertView as DuoOptionView
        }
        optionView.bind(option, null, null)
        mOptionViews.add(optionView)
        return optionView
    }
}