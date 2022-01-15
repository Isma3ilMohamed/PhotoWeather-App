package com.isma3il.core.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.replaceFragment(
    fragment: Fragment,
    @IdRes containerId: Int,
    addToBackStack: Boolean = true,
) {
    beginTransaction()

        .replace(containerId, fragment)
        .apply {
            if (addToBackStack) addToBackStack(null)
        }
        .commit()
}

fun FragmentManager.addFragment(
    fragment: Fragment,
    @IdRes containerId: Int,
    addToBackStack: Boolean = true,

) {
    beginTransaction()
        .add(containerId, fragment)
        .apply {
            if (addToBackStack) addToBackStack(null)
        }
        .commit()
}