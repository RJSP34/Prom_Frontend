package edu.ufp.pam2022.project.main.login.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val UsernameError: Int? = null,
    val EmailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)