package orbital.project

import orbital.project.helper_classes.PasswordValidator
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PasswordValidatorTest {

    @Test
    fun passwordValid() {
        val passwordValidator = PasswordValidator()
        assertEquals(true, passwordValidator.isValid("123"))
    }

    @Test
    fun passwordValid2() {
        val passwordValidator = PasswordValidator()
        assertEquals(true, passwordValidator.isValid("A1B2C3"))
    }

    @Test
    fun passwordWithSpecialCharacters() {
        val passwordValidator = PasswordValidator()
        assertEquals(true, passwordValidator.isValid("@#!@(*#(@*"))
    }

    @Test
    fun emptyPassword() {
        val passwordValidator = PasswordValidator()
        assertEquals(false, passwordValidator.isValid(""))
    }

    @Test
    fun whitespacePassword() {
        val passwordValidator = PasswordValidator()
        assertEquals(true, passwordValidator.isValid("      "))
    }

}