package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.viewmodel.SignInControl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignInControlTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignInControl

    @Before
    fun setUp() {
        viewModel = SignInControl()
    }

    // --- Email ---

    @Test
    fun emailValid_noError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("usuario@gmail.com")
        // Assert
        assertNull(viewModel.errorEmail.value)
    }

    @Test
    fun emailNoAt_showsError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("usuariogmail.com")
        // Assert
        assertEquals("El email no es válido", viewModel.errorEmail.value)
    }

    @Test
    fun emailNoDomain_showsError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("usuario@")
        // Assert
        assertEquals("El email no es válido", viewModel.errorEmail.value)
    }

    @Test
    fun emailEmpty_showsError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("")
        // Assert
        assertEquals("El email no puede estar vacío", viewModel.errorEmail.value)
    }

    @Test
    fun emailWithSpaces_showsError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("usuario @gmail.com")
        // Assert
        assertEquals("El email no es válido", viewModel.errorEmail.value)
    }

    @Test
    fun emailTooLong_showsError() {
        // Arrange
        // Act
        viewModel.actualizaEmail("a".repeat(90) + "@g.com")
        // Assert
        assertEquals("El email es demasiado largo", viewModel.errorEmail.value)
    }

    // --- Password ---

    @Test
    fun passwordValid_noError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd!")
        // Assert
        assertNull(viewModel.errorPassword.value)
    }

    @Test
    fun passwordEmpty_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("")
        // Assert
        assertEquals("La contraseña no puede estar vacía", viewModel.errorPassword.value)
    }

    @Test
    fun passwordTooShort_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("abc12")
        // Assert
        assertEquals("La contraseña debe tener al menos 8 caracteres", viewModel.errorPassword.value)
    }

    @Test
    fun passwordNoUppercase_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("passw0rd!")
        // Assert
        assertEquals("La contraseña debe tener al menos una mayúscula", viewModel.errorPassword.value)
    }

    @Test
    fun passwordNoNumber_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Password!")
        // Assert
        assertEquals("La contraseña debe tener al menos un número", viewModel.errorPassword.value)
    }

    @Test
    fun passwordNoSpecialChar_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd")
        // Assert
        assertEquals("La contraseña debe tener al menos un carácter especial", viewModel.errorPassword.value)
    }

    @Test
    fun passwordExactly8_noError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0r!")
        // Assert
        assertNull(viewModel.errorPassword.value)
    }

    @Test
    fun passwordLength100_noError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("P@ssw0rd" + "a".repeat(92))
        // Assert
        assertNull(viewModel.errorPassword.value)
    }

    // --- Confirm password ---

    @Test
    fun passwordsMatch_noError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd!")
        viewModel.actualizaPassword2("Passw0rd!")
        // Assert
        assertNull(viewModel.errorPassword2.value)
    }

    @Test
    fun passwordsDontMatch_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd!")
        viewModel.actualizaPassword2("Passw0rd#")
        // Assert
        assertEquals("Las contraseñas no coinciden", viewModel.errorPassword2.value)
    }

    @Test
    fun confirmEmpty_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd!")
        viewModel.actualizaPassword2("")
        // Assert
        assertEquals("La contraseña no puede estar vacía", viewModel.errorPassword2.value)
    }

    @Test
    fun passwordsDifferentCase_showsError() {
        // Arrange
        // Act
        viewModel.actualizaPassword("Passw0rd!")
        viewModel.actualizaPassword2("passw0rd!")
        // Assert
        assertEquals("Las contraseñas no coinciden", viewModel.errorPassword2.value)
    }

    // --- Birth date ---

    @Test
    fun birthDateValid_noError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("01/01/1990")
        // Assert
        assertNull(viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateMinor_showsError() {
        // Arrange
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -17)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fecha = sdf.format(cal.time)
        // Act
        viewModel.actualizaFechaNacimiento(fecha)
        // Assert
        assertEquals("Debes ser mayor de 18 años", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateEmpty_showsError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("")
        // Assert
        assertEquals("La fecha no puede estar vacía", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateFuture_showsError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("01/01/2099")
        // Assert
        assertEquals("La fecha no es válida", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateInvalidFormat_showsError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("32/13/1990")
        // Assert
        assertEquals("Formato de fecha inválido", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateOver99_showsError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("21/04/1925")
        // Assert
        assertEquals("Límite de edad superado", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateExactly18_noError() {
        // Arrange
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -18)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fecha = sdf.format(cal.time)
        // Act
        viewModel.actualizaFechaNacimiento(fecha)
        // Assert
        assertNull(viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateOneDayBefore18_showsError() {
        // Arrange
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -18)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fecha = sdf.format(cal.time)
        // Act
        viewModel.actualizaFechaNacimiento(fecha)
        // Assert
        assertEquals("Debes ser mayor de 18 años", viewModel.errorFechaNacimiento.value)
    }

    @Test
    fun birthDateNull_showsError() {
        // Arrange
        // Act
        viewModel.actualizaFechaNacimiento("")
        // Assert
        assertEquals("La fecha no puede estar vacía", viewModel.errorFechaNacimiento.value)
    }
}
