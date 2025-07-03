package com.mulaqat.app.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.mulaqat.app.R
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

class Tool(var context: Context) {
    lateinit var dialog: Dialog

    // hide keyboard
    fun hideKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // check email valid
    fun isEmailValid(email: String): Boolean {
        var isValid = false

        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val inputStr: CharSequence = email

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun showLoading() {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun hideLoading() {
        if (dialog != null) {
            dialog.dismiss()
        }
    }

    fun loadJsonArrayFromAssets(context: Context, fileName: String): JSONArray? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonStr = String(buffer, Charsets.UTF_8)
            JSONArray(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileFromBm(bm: Bitmap): File {
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bm,
            "IMG_${System.currentTimeMillis()}",
            null
        )
        val uri = Uri.parse(path)
        val realUri = RealPathUtil.getRealPath(context, uri)
        return File(realUri)
    }
}