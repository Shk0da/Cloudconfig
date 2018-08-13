package com.github.shk0da.cloudconfig.web.rest.errors;

import com.github.shk0da.cloudconfig.util.TestUtils;
import org.junit.Test;
import com.github.shk0da.cloudconfig.web.rest.errors.ParameterizedErrorVM;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ParameterizedErrorVMTest {

    @Test
    public void getMessageTest() {
        ParameterizedErrorVM vm = new ParameterizedErrorVM(null, null);
        assertNull(vm.getMessage());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("param1", "param1");
        paramMap.put("param2", "param2");
        vm = new ParameterizedErrorVM("message", paramMap);
        assertEquals("message", vm.getMessage());
    }

    @Test
    public void getParamsTest() {
        ParameterizedErrorVM vm = new ParameterizedErrorVM(null, null);
        assertNull(vm.getMessage());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("param1", "param1");
        paramMap.put("param2", "param2");
        vm = new ParameterizedErrorVM("message", paramMap);
        assertTrue(vm.getParams().size() == 2);
    }

    @Test
    public void isSerializable() {
        assertTrue(TestUtils.isSerializable(new ParameterizedErrorVM(null, null)));
    }

}
