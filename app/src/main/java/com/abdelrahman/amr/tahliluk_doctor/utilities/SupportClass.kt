package com.amrmedhatandroid.tahliluk_laboratory.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Base64
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.abdelrahman.amr.tahliluk_doctor.R

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SupportClass : AppCompatActivity() {

    companion object {
        private lateinit var mProgressBar: Dialog
        fun showProgressBar(context: Context, text: String, progressText: TextView) {
            mProgressBar = Dialog(context)
            mProgressBar.setContentView(R.layout.dialog_progress)
            mProgressBar.setCancelable(false)
            mProgressBar.setCanceledOnTouchOutside(false)
            progressText.text = text
            mProgressBar.show()
        }

        fun hideDialog() {
            mProgressBar.dismiss()
        }

        fun showToast(context: Context, message: String) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.show()
        }

        fun loading(isLoading: Boolean, button: View?, progressBar: View) {
            if (button != null) {
                if (isLoading) {
                    button.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    button.visibility = View.VISIBLE
                }
            } else {
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }

        fun encodedImage(bitmap: Bitmap): String {
            val previewWidth = 150
            val previewHeight = bitmap.height * previewWidth / bitmap.width
            val previewBitmap: Bitmap =
                Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
            val byteArrayOutputStream = ByteArrayOutputStream()
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val bytes: ByteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }


        fun startActivity(context: Context, activity: Class<*>) {
            startActivity(context, Intent(context, activity), null)
        }

        fun startActivityWithFlag(context: Context, activity: Class<*>) {
            val intent = Intent(context, activity)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)
        }

        fun startActivityWithFlags(context: Context, activity: Class<*>) {
            val intent = Intent(context, activity)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)
        }

        fun setLocale(language: String, resources: Resources) {
            val metrics: DisplayMetrics = resources.displayMetrics
            val configuration: Configuration = resources.configuration
            configuration.setLocale(Locale(language))
            resources.updateConfiguration(configuration, metrics)
        }

//        fun checkForInternet(context: Context): Boolean {
//            // register activity with the connectivity manager service
//            val connectivityManager =
//                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            // if the android version is equal to M
//            // or greater we need to use the
//            // NetworkCapabilities to check what type of
//            // network has the internet connection
//
//            // Returns a Network object corresponding to
//            // the currently active default data network.
//            val network = connectivityManager.activeNetwork ?: return false
//            // Representation of the capabilities of an active network.
//            val activeNetwork =
//                connectivityManager.getNetworkCapabilities(network) ?: return false
//            return when {
//                // Indicates this network uses a Wi-Fi transport,
//                // or WiFi has network connectivity
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//
//                // Indicates this network uses a Cellular transport. or
//                // Cellular has network connectivity
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//
//                // else return false
//                else -> false
//            }
//        }
//
//        fun showNoInternetSnackBar(fragmentHomeBinding: FragmentHomeBinding) {
//            val snackBarView = Snackbar.make(
//                fragmentHomeBinding.constraintLayout,
//                R.string.msg_check_internet_error,
//                Snackbar.LENGTH_LONG
//            )
//            val view = snackBarView.view
//            val params = view.layoutParams as FrameLayout.LayoutParams
//            params.gravity = Gravity.BOTTOM
//            view.layoutParams = params
//            snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
//            snackBarView.show()
//        }

        fun getBitmapFromBytes(bytes: ByteArray): Bitmap? {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun getReadableDateTime(date: Date): String {
            return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
        }

        fun startNightMode() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        fun startLightMode() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getFileExtensions(fragment: Fragment, uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fragment.context?.contentResolver?.getType(uri!!))
    }
}