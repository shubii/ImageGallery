package com.example.dunzoassignment.presentation

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

/**
 * Add an action which will be invoked after the text changed.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun TextView.doAfterTextChanged(
    crossinline action: (text: Editable?) -> Unit
) = addTextChangedListener(afterTextChanged = action)