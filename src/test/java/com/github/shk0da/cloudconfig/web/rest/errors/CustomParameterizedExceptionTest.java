package com.github.shk0da.cloudconfig.web.rest.errors;

import org.junit.Test;
import com.github.shk0da.cloudconfig.web.rest.errors.CustomParameterizedException;

import static org.junit.Assert.*;

public class CustomParameterizedExceptionTest {

    @Test
    public void getErrorVMTest() throws Exception {
        CustomParameterizedException exc = new CustomParameterizedException("Test");

        try {
            throw exc;
        } catch (Exception exception) {
            assertTrue(exception instanceof CustomParameterizedException);

            CustomParameterizedException exceptionCast = (CustomParameterizedException) exception;
            assertNotNull(exceptionCast.getErrorVM());
            assertNotNull(exceptionCast.getErrorVM().getMessage());
            assertEquals("Test", exceptionCast.getErrorVM().getMessage());
        }

        exc = new CustomParameterizedException(null);

        try {
            throw exc;
        } catch (Exception exception) {
            assertTrue(exception instanceof CustomParameterizedException);

            CustomParameterizedException exceptionCast = (CustomParameterizedException) exception;
            assertNotNull(exceptionCast.getErrorVM());
            assertNull(exceptionCast.getErrorVM().getMessage());
        }
    }

}
