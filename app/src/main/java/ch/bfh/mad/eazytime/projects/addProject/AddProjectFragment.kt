package ch.bfh.mad.eazytime.projects.addProject


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ch.bfh.mad.R
import ch.bfh.mad.databinding.FragmentAddProjectBinding
import ch.bfh.mad.eazytime.TAG

class AddProjectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_project, container, false)
        requireActivity().title = getString(R.string.add_project_fragment_title)

        val addProjectViewModel = ViewModelProviders.of(this).get(AddProjectViewModel::class.java)
        DataBindingUtil.setContentView<FragmentAddProjectBinding>(requireActivity(), R.layout.fragment_add_project)
            .apply {
                this.lifecycleOwner = this@AddProjectFragment
                this.addProjectViewModel = addProjectViewModel
            }

        addProjectViewModel.projectName.observe(this, Observer {
            Log.i(TAG, "addProjectViewModel.projectName $it")
        })
        return view
    }
}
