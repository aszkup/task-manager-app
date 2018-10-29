package com.android.base.utils

import com.android.base.R
import com.android.base.utils.enums.GENERAL_ERROR
import com.android.base.utils.enums.NETWORK_ERROR
import com.android.base.utils.enums.SERVER_ERROR

/**
 * Base Message class
 */
open class BaseMessage(
        open val messageId: Int? = null,
        open val message: String? = null,
        open val type: Int = GENERAL_ERROR) {

    class MissingLogin : BaseMessage(R.string.error_email_required)
    class MissingPass : BaseMessage(R.string.error_password_required)
    class LoginOrPassIncorrect : BaseMessage(R.string.error_incorrect_credentials)
    class MapDownloadFailed : BaseMessage(R.string.error_cannot_download_map)
    class BackendError : BaseMessage(R.string.error_backend, type = SERVER_ERROR)
    class Timeout : BaseMessage(R.string.error_timeout, type = NETWORK_ERROR)
    class Unauthorized : BaseMessage(R.string.error_unauthorized)

    override fun toString(): String {
        return this::class.java.simpleName
    }
}
