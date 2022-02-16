package work.racka.util

object Constants {
    const val API_VERSION = "/v1"
    const val USERS = "$API_VERSION/users"
    const val REGISTER_REQUEST = "$USERS/register"
    const val LOGIN_REQUEST = "$USERS/login"
    const val NOTES = "$API_VERSION/notes"
    const val CREATE_NOTE ="$NOTES/create"
    const val UPDATE_NOTE = "$NOTES/update"
    const val DELETE_NOTE = "$NOTES/delete"

    // Errors
    const val ERROR_MISSING_FIELDS = "Missing Some Fields"
    const val ERROR_GENERIC = "Some Problem Occurred"
    const val ERROR_BAD_EMAIL = "Wrong e-mail id!"
    const val ERROR_BAD_PASSWORD = "Password Incorrect!"
    const val ERROR_PARAMETER_ID_NOT_PRESENT = "QueryParameter: id is not present"
    // Success
    const val SUCCESS_NOTE_ADDED = "Note Added Successfully"
    const val SUCCESS_NOTE_UPDATED = "Note Updated Successfully"
    const val SUCCESS_NOTE_DELETED = "Note Deleted Successfully"
}