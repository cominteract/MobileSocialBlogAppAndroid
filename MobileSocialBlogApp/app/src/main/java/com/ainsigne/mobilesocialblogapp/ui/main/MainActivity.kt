package com.ainsigne.mobilesocialblogapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.base.BaseActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import com.ainsigne.mobilesocialblogapp.utils.toStringFull
import com.facebook.react.modules.core.PermissionListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.content_main.*
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.InputStream
import java.util.*


interface PhotoRetrieval{
    fun photoRetrieved(uriString : String?, bitmap : Bitmap?)

}

class MainActivity : MainView, BaseActivity(), JitsiMeetActivityInterface {



    var injector = MainImplementation()

    var presenter: MainPresenter? = null

    lateinit var easyImage : EasyImage

    var sheetBehavior: BottomSheetBehavior<View>? = null

    var photoRetrieval : PhotoRetrieval? = null

    var sheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {


        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN ->{

                }
                BottomSheetBehavior.STATE_EXPANDED ->{

                }
                BottomSheetBehavior.STATE_COLLAPSED ->{

                }
                else -> print("")
            }
        }
    }

    private val PICK_FROM_CAMERA = 1

    private val PICK_FROM_GALLERY = 2

    var bitmap : Bitmap? = null

    var photoTaken = ""

    fun navigateOpt()
    {
        loadFragment(UINavigation.optsignup)
        //navigation.visibility = View.GONE
        val layoutParams = navigation.layoutParams
        layoutParams.height = 1
        navigation.layoutParams =  layoutParams
        invalidateOptionsMenu()
    }

    fun navigateApp()
    {
        if(Config.getUser().isNullOrEmpty()){

            loadFragment(UINavigation.signup)
            //navigation.visibility = View.GONE
            hideTab()

        }
        else{

            loadFragment(UINavigation.feed)
//            navigation.visibility = View.VISIBLE
            showTab()
        }

        invalidateOptionsMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Config.context = this

        easyImage = EasyImage.Builder(this)
                .setCopyImagesToPublicGalleryFolder(false) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
                .setFolderName("images") // Allow multiple picking
                .allowMultiple(true)
                .build()

        sheetBehavior = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet))

        tv_close.setOnClickListener {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        tv_choosephoto.setOnClickListener {

            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, PICK_FROM_GALLERY)
        }
        tv_takephoto.setOnClickListener {

            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoTaken = "img_${Date().toStringFormat()}.jpg"
            camera.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFile(this))
            camera.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            camera.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)

            startActivityForResult(camera, PICK_FROM_CAMERA)
        }

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigateApp()
        setSupportActionBar(toolbar)
    }

    fun hideTab(){
        val layoutParams = navigation.layoutParams
        layoutParams.height = 1
        navigation.layoutParams =  layoutParams
    }

    fun showTab(){
        val layoutParams = navigation.layoutParams
        layoutParams.height = dimensionPoint(48).toInt()
        navigation.layoutParams =  layoutParams
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_logout -> {
                Config.updateUser("")
                Config.user = null
                navigateApp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_overflow_list, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        menu?.findItem(R.id.action_logout)?.isVisible = !(menu is MenuBuilder && Config.getUser().isNullOrBlank())

        return true
    }

    fun getPhotoFile(context: Context): Uri? {
        val file = File(context.externalCacheDir, photoTaken)
        return FileProvider.getUriForFile(this, "${this.applicationContext.packageName}.provider",  file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            PICK_FROM_CAMERA -> {
                if(resultCode == Activity.RESULT_OK)
                {
                    val file =
                        File(this.externalCacheDir, photoTaken)
                    if(file.exists()){
                        val outPutfileUri = FileProvider.getUriForFile(
                            this,
                            this.applicationContext.packageName + ".provider",
                            file
                        )
                        Log.d(" Request Code "," Request Code $outPutfileUri ${file.path}")
                        val cr = applicationContext.contentResolver
                        cr.openInputStream(outPutfileUri)?.let {
                            bitmap = BitmapFactory.decodeStream(it)
                            photoRetrieval?.photoRetrieved(file.path, bitmap)
                        }
                    }
                }
                else{
                    Log.d(" Failed path camera "," Failed path camera $resultCode")
                }
            }
            PICK_FROM_GALLERY -> {
                if(resultCode == Activity.RESULT_OK){
                    data?.data?.let {
                        val filePathColumn =
                            arrayOf(MediaStore.Images.Media.DATA)

                        val cursor: Cursor? = contentResolver.query(it, filePathColumn, null, null, null)
                        cursor?.moveToFirst()
                        cursor?.getColumnIndex(filePathColumn[0])?.let {column ->
                            val imgDecodableString: String? = cursor.getString(column)
                            cursor.close()
                            bitmap = BitmapFactory.decodeFile(imgDecodableString)
                            Log.d(" Current path gallery "," Current path gallery $imgDecodableString")
                            photoRetrieval?.photoRetrieved(imgDecodableString, bitmap)
                        }
                    }
                }
            }
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var count = 0
        while(supportFragmentManager.backStackEntryCount > 0)
        {
            count++
            if(count == 3)
                break
            dismiss()
        }
        if(presenter != null) {
            loadFragment(UINavigation.tabState(item.itemId, presenter!!.isLogged()))
        }
        else
            loadFragment(UINavigation.tabState(item.itemId, false))
    }

    fun toggleBottomSheet() {
        if (sheetBehavior!!.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)


        } else {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    override fun tokenRefreshedUpdateView() {

    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {

    }
}
