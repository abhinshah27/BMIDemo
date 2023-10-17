package bmi.demo.application.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.SystemClock
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.View
import androidx.core.content.FileProvider
import bmi.demo.application.utils.Constants.PLAY_STORE_PACKAGE_NAME
import bmi.demo.application.utils.Constants.SCREENSHOT_FILE_NAME
import bmi.demo.application.utils.Constants.SCREENSHOT_TITLE
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream



/**
 * Adds a click listener to a View with a debounce mechanism.
 *
 * @param debounceTime time interval (in milliseconds) within which subsequent clicks will be ignored.
 * @param action action to be executed when the View is clicked after the debounce interval.
 */
fun View.clickWithDebounce(debounceTime: Long = 1000, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                action()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

/**
 * Sets the visibility of the view to VISIBLE.
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Captures a screenshot of the view and returns it as a Bitmap.
 */
fun View.takeScreenshot(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

/**
 * Saves a screenshot Bitmap to the app's cache directory.
 *
 * @param bitmap The screenshot to be saved.
 * @Returns: file object representing the saved screenshot file, or null if an error occurs.
 */
fun Context.saveScreenshotToCache(bitmap: Bitmap): File? {
    val cacheDir = this.cacheDir
    val imageFile = File(cacheDir, SCREENSHOT_FILE_NAME)

    return try {
        val fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        imageFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Shares a screenshot file using an Intent.
 * @param screenshotFile screenshot file to be shared.
 */
fun Context.shareScreenshot(screenshotFile: File?) {
    if (screenshotFile != null) {
        val uri = FileProvider.getUriForFile(
            this,
            "${this.packageName}.fileprovider",
            screenshotFile
        )
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        this.startActivity(Intent.createChooser(shareIntent, SCREENSHOT_TITLE))
    }
}

/**
 * Opens the Play Store app or a web link to the specified package.
 */
fun Context.openPlayStoreApp() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PLAY_STORE_PACKAGE_NAME")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$PLAY_STORE_PACKAGE_NAME")
            )
        )
    }
}

/**
 * SpannableString with a different font size for a specific substring within the original string.
 *
 * @param smallFont The substring within the original string that should have a different font size.
 * @param sizeInPixel The size (in pixels) to set for the font of the 'smallFont' substring.
 * @return SpannableString with the specified font size applied to the 'smallFont' substring.
 */
fun String.diffFontSize(smallFont: String, sizeInPixel: Int): SpannableString {
    // Create a new SpannableString with the original string.
    val spannable = SpannableString(this)

    // Find the starting index of the 'smallFont' substring in the original string.
    val startIndexOfPath = spannable.toString().indexOf(smallFont)

    // Apply the AbsoluteSizeSpan to change the font size of the 'smallFont' substring.
    spannable.setSpan(
        AbsoluteSizeSpan(sizeInPixel), // Set the font size to 'sizeInPixel' in pixels.
        startIndexOfPath, // Start index of the 'smallFont' substring.
        startIndexOfPath + smallFont.length, // End index of the 'smallFont' substring.
        0 // Flags, 0 indicates no additional styling flags.
    )

    // Return the modified SpannableString.
    return spannable
}

/**
 * Display a Snackbar message on the current View.
 *
 * @param message message text to display in the Snackbar.
 * @param duration duration for which the Snackbar should be displayed
 */
fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    // Show a Snackbar with the provided 'message' on the current View.
    Snackbar.make(this, message, duration).show()
}