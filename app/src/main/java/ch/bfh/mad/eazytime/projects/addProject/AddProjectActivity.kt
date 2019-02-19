package ch.bfh.mad.eazytime.projects.addProject


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import ch.bfh.mad.R
import ch.bfh.mad.databinding.ActivityAddProjectBinding
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project
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

    @Inject
    lateinit var projectDao: ProjectDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        title = getString(R.string.add_project_fragment_title)

        Injector.appComponent.inject(this)
        val addProjectViewModel = ViewModelProviders.of(this, ProjectModelFactory(fakeProjectProviderService, fakeProjectRepo))
            .get(AddProjectViewModel::class.java)

        val dataBinding = DataBindingUtil.setContentView<ActivityAddProjectBinding>(this, R.layout.activity_add_project)
            .apply {
                this.lifecycleOwner = this@AddProjectActivity
                this.addProjectViewModel = addProjectViewModel
            }

        val colors = resources.getIntArray(R.array.eazyTime_project_colors)
        addProjectViewModel.selectProjectColor(colors[Random.nextInt(colors.size)])
        dataBinding.buAddProjectSave.isEnabled = false


        addProjectViewModel.shortCode.observe(this, Observer { shortCodeText ->
            shortCodeText?.let {
                if (shortCodeText.length > 3){
                    dataBinding.etAddProjectShortCode.error = getString(R.string.error_short_code_length)
                    dataBinding.buAddProjectSave.isEnabled = false
                }else{
                    dataBinding.etAddProjectShortCode.error = null
                    dataBinding.buAddProjectSave.isEnabled = true
                }
            }
        })

        addProjectViewModel.projectName.observe(this, Observer { projectName ->
            projectName?.let {
                if (projectName.isBlank()){
                    dataBinding.etAddProjectName.error = getString(R.string.error_project_name_length)
                    dataBinding.buAddProjectSave.isEnabled = false
                }else{
                    dataBinding.etAddProjectName.error = null
                    dataBinding.buAddProjectSave.isEnabled = true
                }
            }
        })

        addProjectViewModel.colorId.observe(this, Observer { colorId ->
            colorId?.let { dataBinding.buAddProjectColorPicker.setBackgroundColor(colorId) }
        })

        dataBinding.buAddProjectColorPicker.setOnClickListener {
            showColorPickerDialog(addProjectViewModel)
        }

        dataBinding.buAddProjectSave.setOnClickListener { createAndSaveProject(addProjectViewModel) }

        dataBinding.buAddProjectDelete.setOnClickListener { deleteProject(addProjectViewModel) }
    }

    private fun deleteProject(addProjectViewModel: AddProjectViewModel) {
        Toast.makeText(this, "Not yet implemented!!", Toast.LENGTH_SHORT).show()
    }

    private fun createAndSaveProject(addProjectViewModel: AddProjectViewModel) {
        Toast.makeText(this, "Todo: only oneDefaultProject???", Toast.LENGTH_SHORT).show()
        // Todo: only oneDefaultProject???
        // Todo: Move this to a Service/Repo???
        val tmpProject = Project()
        tmpProject.name = addProjectViewModel.projectName.value ?: getString(R.string.default_projectName)
        tmpProject.shortCode = addProjectViewModel.shortCode.value ?: ""
        tmpProject.isDefault = addProjectViewModel.defaultProject.value ?: false
        tmpProject.onWidget = addProjectViewModel.onWidget.value ?: false
        tmpProject.color = getColorStringFromViewModel(addProjectViewModel)
        Log.i(TAG, "project to save: $tmpProject")
        InsertAsyncTask(projectDao).execute(tmpProject)
        Log.i(TAG, "Save Project started in InsertAsyncTask, close the Activity")
        finish()
    }

    private fun getColorStringFromViewModel(addProjectViewModel: AddProjectViewModel): String {
        try {
            // This seem a bit odd, but te return-value from the colorPicker is the id form the color of the array.
            // This is not the same id as the id of the same color not in the array
            val colorId = addProjectViewModel.colorId.value ?: 0
            var color = getString(R.string.defaul_color_string)
            val intArray = resources.getIntArray(R.array.eazyTime_project_colors)
            val stringArray = resources.getStringArray(R.array.eazyTime_project_colors_as_string)
            for (i in 0 until intArray.size) {
                if (colorId == intArray[i]) {
                    color = stringArray[i]
                    Log.i(TAG, "found color by resources: $color")
                    break
                }
            }
            return color

        } catch (e: Exception) {
            Log.e(TAG, "getColorStringFromViewModel failed: ${e.message}", e)
            return getString(R.string.defaul_color_string)
        }
    }


    private fun showColorPickerDialog(addProjectViewModel: AddProjectViewModel) {
        val selectedColor = addProjectViewModel.colorId.value ?: R.color.eazyTime_colorProject1
        val spectrumDialogBuilder = SpectrumDialog.Builder(this)
        spectrumDialogBuilder.setTitle(getString(R.string.color_picker_dialog_title))
            .setColors(R.array.eazyTime_project_colors)
            .setSelectedColor(selectedColor)
            .setDismissOnColorSelected(true)
            .setOnColorSelectedListener { positiveResult, color ->
                Log.i(TAG, "Choose projectColor by spectrumDialog: id=$color positiveResult=$positiveResult")
                addProjectViewModel.selectProjectColor(color)
            }
            .build()
            .show(supportFragmentManager, "spectrumDialog")
    }

    companion object {
        fun getAddProjectActivityIntent(ctx: Context) = Intent(ctx, AddProjectActivity::class.java)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: ProjectDao) : AsyncTask<Project, Void, Void>() {
        override fun doInBackground(vararg params: Project): Void? {
            val project = params[0]
            mAsyncTaskDao.insert(project)
            Log.i(TAG, "Saved Project: $project")
            return null
        }
    }
}
