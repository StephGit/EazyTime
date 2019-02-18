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
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.projects.FakeProjectProviderService
import ch.bfh.mad.eazytime.projects.FakeProjectRepo
import ch.bfh.mad.eazytime.projects.ProjectModelFactory
import com.thebluealliance.spectrum.SpectrumDialog
import javax.inject.Inject
import kotlin.random.Random


class AddProjectFragment : Fragment() {

    @Inject
    lateinit var fakeProjectProviderService: FakeProjectProviderService

    @Inject
    lateinit var fakeProjectRepo: FakeProjectRepo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_project, container, false)
        requireActivity().title = getString(R.string.add_project_fragment_title)

        Injector.appComponent.inject(this)
        val addProjectViewModel = ViewModelProviders.of(this, ProjectModelFactory(fakeProjectProviderService, fakeProjectRepo))
            .get(AddProjectViewModel::class.java)

        val dataBinding = DataBindingUtil.setContentView<FragmentAddProjectBinding>(requireActivity(), R.layout.fragment_add_project)
            .apply {
                this.lifecycleOwner = this@AddProjectFragment
                this.addProjectViewModel = addProjectViewModel
            }

        val colors = resources.getIntArray(R.array.eazyTime_project_colors)
        addProjectViewModel.selectProjectColor(colors[Random.nextInt(colors.size)])

        addProjectViewModel.shortCode.observe(requireActivity(), Observer { shortCodeText ->
            shortCodeText?.let {
                if (shortCodeText.length > 3){
                    dataBinding.etAddProjectShortCode.error = getString(ch.bfh.mad.R.string.error_short_code_length)
                }else{
                    dataBinding.etAddProjectShortCode.error = null
                }
            }
        })

        addProjectViewModel.projectName.observe(requireActivity(), Observer { projectName ->
            projectName?.let {
                if (projectName.isBlank()){
                    dataBinding.etAddProjectName.error = getString(R.string.error_project_name_length)
                }else{
                    dataBinding.etAddProjectName.error = null
                }
            }
        })

        addProjectViewModel.colorId.observe(requireActivity(), Observer { colorId ->
            colorId?.let { dataBinding.buAddProjectColorPicker.setBackgroundColor(colorId) }
        })

        dataBinding.buAddProjectColorPicker.setOnClickListener {
            showColorPickerDialog(addProjectViewModel)
        }

        return view
    }

    private fun showColorPickerDialog(addProjectViewModel: AddProjectViewModel) {
        val selectedColor = addProjectViewModel.colorId.value ?: R.color.eazyTime_colorProject1
        val spectrumDialogBuilder = SpectrumDialog.Builder(requireContext())
        spectrumDialogBuilder.setTitle(getString(R.string.color_picker_dialog_title))
            .setColors(R.array.eazyTime_project_colors)
            .setSelectedColor(selectedColor)
            .setDismissOnColorSelected(true)
            .setOnColorSelectedListener { positiveResult, color -> addProjectViewModel.selectProjectColor(color) }
            .build()
            .show(requireActivity().supportFragmentManager, "spectrumDialog")
    }
}
