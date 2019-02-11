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
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import ch.bfh.mad.R
import ch.bfh.mad.databinding.FragmentAddProjectBinding
import ch.bfh.mad.eazytime.TAG


class AddProjectFragment : Fragment() {

    private lateinit var projectNameEditText: EditText
    private lateinit var shortcode: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_project, container, false)
        requireActivity().title = getString(R.string.add_project_fragment_title)

        view.findViewById<TextView>(R.id.tv_label_add_project_color_spinner).text = "Warum?"

        projectNameEditText = view.findViewById(R.id.et_add_project_name)
        shortcode = view.findViewById(R.id.et_add_project_short_code)


        val addProjectViewModel = ViewModelProviders.of(this).get(AddProjectViewModel::class.java)
        val dataBinding = DataBindingUtil.setContentView<FragmentAddProjectBinding>(requireActivity(), R.layout.fragment_add_project)
            .apply {
                this.lifecycleOwner = this@AddProjectFragment
                this.addProjectViewModel = addProjectViewModel
            }

        addProjectViewModel.shortCode.observe(requireActivity(), Observer { shortCodeText ->
            shortCodeText?.let {
                if (shortCodeText.length >= 3){
                    dataBinding.etAddProjectShortCode.error = getString(R.string.error_short_code_lengt)
                }else{
                    dataBinding.etAddProjectShortCode.error = null
                }
            }
        })

        val otherStrings = arrayOf("#38d9a9", "#69db7c", "#a9e34b").toList()

//        val colorSpinnerAdapter = ColorSpinnerAdapter(requireContext(), R.layout.item_color_spinner, otherStrings)

        val colorSpinnerAdapter = ColorSpinnerAdapter(requireContext(), android.R.layout.simple_list_item_1, otherStrings)
        dataBinding.snAddProjectColorSpinner.adapter = colorSpinnerAdapter

//
//            ArrayAdapter.createFromResource(
//                requireContext(),
//                R.array.eazyTime_project_colors,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                // Apply the adapter to the spinner
//                dataBinding.snAddProjectColorSpinner.adapter = adapter
//            }

        return view
    }
}
