package orbital.project

import orbital.project.helper_classes.McrValidator
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class McrValidatorTest {

    @Test
    fun emptyMcrString() {
        val mcrValidator = McrValidator()
        assertEquals(false,mcrValidator.isValid(""))
    }

    @Test
    fun mcrNumberWithNoStartingM() {
        val mcrValidator = McrValidator()
        assertEquals(false,mcrValidator.isValid("2928301"))
    }

    @Test
    fun mcrNumberMoreThan7Char() {
        val mcrValidator = McrValidator()
        assertEquals(false,mcrValidator.isValid("M102933P"))
    }

    @Test
    fun mcrNumberLessThan7Char() {
        val mcrValidator = McrValidator()
        assertEquals(false,mcrValidator.isValid("M1023P"))
    }

    @Test
    fun mcrNumberDoesNotEndWithCapitalLetter() {
        val mcrValidator = McrValidator()
        assertEquals(false,mcrValidator.isValid("M10234p"))
    }

    @Test
    fun validMCR() {
        val mcrValidator = McrValidator()
        assertEquals(true,mcrValidator.isValid("M65432J"))
    }


}