package com.mind.market.aimissioncompose.auth.utils

import android.content.Context
import com.mind.market.aimissioncompose.R

fun AuthenticationValidationErrorStatus.toText(context: Context): String =
    when (this) {
        AuthenticationValidationErrorStatus.NO_EMAIL -> context.getString(R.string.authentication_validation_error_message_no_email)
        AuthenticationValidationErrorStatus.NO_PASSWORD -> context.getString(R.string.authentication_validation_error_message_no_password)
        AuthenticationValidationErrorStatus.LOGIN_FAILED -> context.getString(R.string.authentication_validation_error_message_invalid_login_credentials)
        AuthenticationValidationErrorStatus.INVALID_EMAIL -> context.getString(R.string.authentication_validation_error_message_invalid_email)
    }