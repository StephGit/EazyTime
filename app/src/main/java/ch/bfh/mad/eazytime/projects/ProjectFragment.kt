package ch.bfh.mad.eazytime.projects


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ch.bfh.mad.R


class ProjectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        val textView = view.findViewById<TextView>(R.id.tv_project_placeholder)
        textView.text = getString(R.string.placeholder) +" Projects"
        activity!!.title = getString(R.string.project_fragment_title)
        return view
    }


}
