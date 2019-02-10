package ch.bfh.mad.eazytime.projects.addProject


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.bfh.mad.R

class AddProjectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        activity!!.title = getString(R.string.add_project_fragment_title)
        return view
    }
}
