package com.taboola.rest.api.internal;

import com.taboola.rest.api.annotations.Final;
import com.taboola.rest.api.annotations.ReadOnly;
import com.taboola.rest.api.annotations.Required;
import com.taboola.rest.api.exceptions.RestAPIRequestException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by vladi
 * Date: 1/18/2018
 * Time: 12:07 AM
 * By Taboola
 */
public class FieldsValidatorTest {

    private ExtendedDummyAnnotationTestClass dummyClass;

    private static class DummyAnnotationTestClass {

        @ReadOnly
        protected String id;

        @Final
        protected String name;

        @Required
        protected String cpc;
    }

    private static class ExtendedDummyAnnotationTestClass extends DummyAnnotationTestClass { }

    @Before
    public void beforeTest() {
        dummyClass = new ExtendedDummyAnnotationTestClass();
    }

    @Test
    public void testValidateRequiredFieldsExists() {
        dummyClass.cpc = null;
        dummyClass.name = "notNull";
        try {
            FieldsValidator.validateCreateOperation(dummyClass);
            fail("Did not fail the Required field");
        } catch (RestAPIRequestException e) {
            assertEquals("Invalid error message", "Field 'cpc' is 'Required'", e.getMessage());
        }
    }

    @Test
    public void testValidateNoReadOnlyFields() {
        dummyClass.id = "notNull";
        dummyClass.cpc = "notNull";
        dummyClass.name = "notNull";
        try {
            FieldsValidator.validateCreateOperation(dummyClass);
            fail("Did not fail the Required field");
        } catch (RestAPIRequestException e) {
            assertEquals("Invalid error message", "Field 'id' is 'ReadOnly'", e.getMessage());
        }
    }

    @Test
    public void testValidateNoFinalFields() {
        dummyClass.name = "notNull";
        try {
            FieldsValidator.validateUpdateOperation(dummyClass);
            fail("Did not fail the Required field");
        } catch (RestAPIRequestException e) {
            assertEquals("Invalid error message", "Field 'name' is 'Final'", e.getMessage());
        }
    }

    @Test
    public void testValidateFinalFieldsExists() {
        dummyClass.name = null;
        dummyClass.cpc = "notNull";
        try {
            FieldsValidator.validateCreateOperation(dummyClass);
            fail("Did not fail the Required field");
        } catch (RestAPIRequestException e) {
            assertEquals("Invalid error message", "Field 'name' is 'Final'", e.getMessage());
        }
    }
}
