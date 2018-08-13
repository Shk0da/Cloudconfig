package com.github.shk0da.cloudconfig.web.rest.vm;

import org.junit.Before;
import org.junit.Test;
import com.github.shk0da.cloudconfig.util.TestUtils;
import com.github.shk0da.cloudconfig.web.rest.vm.UserVM;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class UserVMTest {


    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void constructorTest() {
        UserVM vm = new UserVM(null, null);
        assertFalse(validator.validate(vm).isEmpty());
        vm = new UserVM("", null);
        assertFalse(validator.validate(vm).isEmpty());
        vm = new UserVM("badLoginTooLongbadLoginTooLongbadLoginTooLongbadLoginTooLong", null);
        assertFalse(validator.validate(vm).isEmpty());
        vm = new UserVM("goodLogin", null);
        assertTrue(validator.validate(vm).isEmpty());
    }

    @Test
    public void getLoginTest() {
        UserVM vm = new UserVM();
        assertNull(vm.getLogin());

        vm = new UserVM("login", null);
        assertEquals("login", vm.getLogin());
    }

    @Test
    public void getAuthoritiesTest() throws Exception {
        UserVM vm = new UserVM();
        assertNull(vm.getAuthorities());

        Set<String> set = new HashSet<>();
        set.add("authorities1");
        set.add("authorities2");
        vm = new UserVM("login", set);
        assertEquals(set, vm.getAuthorities());
    }

    @Test
    public void toStringTest() throws Exception {
        UserVM vm = new UserVM();

        assertTrue(vm.toString().startsWith(UserVM.class.getSimpleName()));
        String json = vm.toString().replace(UserVM.class.getSimpleName(), "");
        assertTrue(TestUtils.isValid(json));

        Set<String> set = new HashSet<>();
        set.add("authorities1");
        set.add("authorities2");
        vm = new UserVM("fakeLogin", set);
        json = vm.toString().replace(UserVM.class.getSimpleName(), "");
        assertTrue(TestUtils.isValid(json));
    }

}
