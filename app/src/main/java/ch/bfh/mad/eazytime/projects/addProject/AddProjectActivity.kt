package ch.bfh.mad.eazytime.projects.addProject


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ch.bfh.mad.R
import ch.bfh.mad.databinding.FragmentAddProjectBinding
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.projects.FakeProjectProviderService
import ch.bfh.mad.eazytime.projects.FakeProjectRepo
import ch.bfh.mad.eazytime.projects.ProjectModelFactory
import com.thebluealliance.spectrum.SpectrumDialog
import javax.inject.Inject
import kotlin.random.Random


class AddProjectActivity : AppCompatActivity() {

    @Inject
    lateinit var fakeProjectProviderService: FakeProjectProviderService

    @Inject
    lateinit var fakeProjectRepo: FakeProjectRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_project)
        title = getString(R.string.add_project_fragment_title)

        Injector.appComponent.inject(this)
        val addProjectViewModel = ViewModelProviders.of(this, ProjectModelFactory(fakeProjectProviderService, fakeProjectRepo))
            .get(AddProjectViewModel::class.java)

        val dataBinding = DataBindingUtil.setContentView<FragmentAddProjectBinding>(this, R.layout.fragment_add_project)
            .apply {
                this.lifecycleOwner = this@AddProjectActivity
                this.addProjectViewModel = addProjectViewModel
            }

        val colors = resources.getIntArray(R.array.eazyTime_project_colors)
        addProjectViewModel.selectProjectColor(colors[Random.nextInt(colors.size)])

        addProjectViewModel.shortCode.observe(this, Observer { shortCodeText ->
            shortCodeText?.let {
                if (shortCodeText.length > 3){
                    dataBinding.etAddProjectShortCode.error = getString(R.string.error_short_code_length)
                }else{
                    dataBinding.etAddProjectShortCode.error = null
                }
            }
        })

        addProjectViewModel.projectName.observe(this, Observer { projectName ->
            projectName?.let {
                if (projectName.isBlank()){
                    dataBinding.etAddProjectName.error = getString(R.string.error_project_name_length)
                }else{
                    dataBinding.etAddProjectName.error = null
                }
            }
        })

        addProjectViewModel.colorId.observe(this, Observer { colorId ->
            colorId?.let { dataBinding.buAddProjectColorPicker.setBackgroundColor(colorId) }
        })

        dataBinding.buAddProjectColorPicker.setOnClickListener {
            showColorPickerDialog(addProjectViewModel)
        }
    }

    private fun showColorPickerDialog(addProjectViewModel: AddProjectViewModel) {
        val selectedColor = addProjectViewModel.colorId.value ?: R.color.eazyTime_colorProject1
        val spectrumDialogBuilder = SpectrumDialog.Builder(this)
        spectrumDialogBuilder.setTitle(getString(R.string.color_picker_dialog_title))
            .setColors(R.array.eazyTime_project_colors)
            .setSelectedColor(selectedColor)
            .setDismissOnColorSelected(true)
            .setOnColorSelectedListener { positiveResult, color -> addProjectViewModel.selectProjectColor(color) }
            .build()
            .show(supportFragmentManager, "spectrumDialog")
    }

    companion object {
        fun getAddProjectActivityIntent(ctx: Context) = Intent(ctx, AddProjectActivity::class.java)
    }
}
