package com.aguerodev.shopp.view.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Calendar


class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(16)
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if ((i + 1) % 4 == 0 && (i + 1) != 16) out += " "
        }
        val offsetTranslator = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val newOffset = offset.coerceAtMost(16)
                val spaces = when {
                    newOffset <= 4 -> 0
                    newOffset <= 8 -> 1
                    newOffset <= 12 -> 2
                    else -> 3 // Para 13, 14, 15, 16
                }

                return newOffset + spaces
            }

            override fun transformedToOriginal(offset: Int): Int {
                val newOffset = offset.coerceAtMost(19)

                val spaces = when {
                    newOffset <= 4 -> 0
                    newOffset <= 9 -> 1 // 4 dígitos + 1 espacio
                    newOffset <= 14 -> 2 // 8 dígitos + 2 espacios
                    else -> 3 // 12 dígitos + 3 espacios (hasta 19)
                }

                return newOffset - spaces
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

fun ExpiryDateValid(date: String) : Boolean {
    if (date.length != 5 || date[2] != '/') {
        return false
    } else {
        val monthString = date.substring(0, 2)
        val yearString = date.substring(3, 5)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100 // Ej: 25
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // Enero es 1

        val inputMonth = monthString.toIntOrNull()
        val inputYear = yearString.toIntOrNull()

        if (inputMonth == null || inputYear == null) {
            return false
        } else if (inputMonth !in 1..12) {
            return false // Mes debe ser 1-12
        } else if (inputYear < currentYear) {
            return  false // Año debe ser el actual o futuro
        } else if (inputYear == currentYear && inputMonth < currentMonth) {
            return false // Si es el año actual, el mes debe ser el actual o futuro
        } else {
            return true // Válida
        }
    }
}

