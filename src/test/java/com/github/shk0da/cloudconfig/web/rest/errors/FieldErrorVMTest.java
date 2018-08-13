package com.github.shk0da.cloudconfig.web.rest.errors;

import org.junit.Test;
import com.github.shk0da.cloudconfig.web.rest.errors.FieldErrorVM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FieldErrorVMTest {

    @Test
    public void getObjectNameTest() throws Exception {
        FieldErrorVM vm = new FieldErrorVM(null, null, null);
        assertNull(vm.getObjectName());
        vm = new FieldErrorVM("dto", "field", "message");
        assertEquals("dto", vm.getObjectName());
    }

    @Test
    public void getFieldTest() throws Exception {
        FieldErrorVM vm = new FieldErrorVM(null, null, null);
        assertNull(vm.getField());
        vm = new FieldErrorVM("dto", "field", "message");
        assertEquals("field", vm.getField());
    }

    @Test
    public void getMessageTest() throws Exception {
        FieldErrorVM vm = new FieldErrorVM(null, null, null);
        assertNull(vm.getMessage());
        vm = new FieldErrorVM("dto", "field", "message");
        assertEquals("message", vm.getMessage());
    }

}
