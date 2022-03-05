package com.nadimohammed.currencyconverter.util

import android.content.Context
import android.widget.Toast
import java.net.ConnectException
import java.net.UnknownHostException

//here we are handling exception
fun exceptionHandler(context: Context, exception: Exception) {
    when (exception) {
        is RuntimeException -> Toast.makeText(
            context,
            exception.message.toString(),
            Toast.LENGTH_SHORT
        ).show()

        is ConnectException, is UnknownHostException -> Toast.makeText(
            context,
            "خطاء في الاتصال بالانترنت حاول في وقت لاحق",
            Toast.LENGTH_SHORT
        ).show()

        else -> Toast.makeText(context, "حدث خطاء حاول في وقت لاحق", Toast.LENGTH_SHORT).show()
    }
}


