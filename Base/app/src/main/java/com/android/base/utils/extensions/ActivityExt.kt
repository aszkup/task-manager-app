package com.android.base.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import timber.log.Timber

/**
 * Start activity for result
 *
 * @param requestCode requested code
 * @param f intent lambda
 */
fun Activity.startForResult(requestCode: Int, f: Intent.() -> Unit): Unit =
        Intent().apply(f).run { startActivityForResult(this, requestCode) }

/**
 * Replaces content fragment without backstack
 *
 * @param fragment fragment to be shown
 */
fun AppCompatActivity.changeFragment(@IdRes fragmentId: Int, fragment: Fragment) {
    Timber.d("Changing fragment $fragment")
    supportFragmentManager.beginTransaction()
            .replace(fragmentId, fragment)
            .commit()
    hideSoftKeyboard()
}

/**
 * Find fragment
 *
 * @param id view id
 */
fun Fragment.findFragment(@IdRes id: Int) =
        childFragmentManager.findFragmentById(id)

/**
 * Add  fragment
 *
 * @param fragment fragment to be shown
 */
fun Fragment.addFragment(@IdRes fragmentId: Int, fragment: Fragment) {
    childFragmentManager.beginTransaction()
            .add(fragmentId, fragment)
            .commit()
}

/**
 * Remove  fragment
 *
 * @param fragment fragment to be removed
 */
fun Fragment.removeFragment(fragment: Fragment) {
    childFragmentManager.beginTransaction().remove(fragment).commit()
}

/**
 * Extension method to hide soft keyboard
 */
fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}
