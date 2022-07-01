package orbital.project

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmailValidatorTest {

    @Test
    fun emptyEmail() {
        val emailValidator = EmailValidator()
        assertFalse(emailValidator.isValid(""))
    }

    @Test
    fun invalidEmail1() {
        val emailValidator : EmailValidator = EmailValidator()
        assertFalse(emailValidator.isValid("testemail.com"))
    }

    @Test
    fun invalidEmail2() {
        val emailValidator : EmailValidator = EmailValidator()
        assertFalse(emailValidator.isValid("test@.test.com"))
    }

    @Test
    fun invalidEmail3() {
        val emailValidator : EmailValidator = EmailValidator()
        assertEquals(emailValidator.isValid("test@test"),false)
    }

    @Test
    fun validEmail1() {
        val emailValidator : EmailValidator = EmailValidator()
        assertEquals(true, emailValidator.isValid("test@gmail.com"))
    }

    @Test
    fun validEmail2() {
        val emailValidator : EmailValidator = EmailValidator()
        assertEquals(true, emailValidator.isValid("testing@hotmail.com"))
    }

}