
package org.dbforms.validation;

import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.validator.ValidatorResources;
import org.apache.log4j.Category;

/****
 * <p>
 * This class is use for static reference primarily.
 * </p>
 *
 * @author Eric Beaumier
 */

public class ValidatorConstants {

	// Attribute name in the WEB.XML 
	public static final String VALIDATION = "validation";
	public static final String VALIDATOR_RULES = "validator-rules";

	public static final String RESOURCE_BUNDLE = "resourceBundle";
	
	// Name to store and retrieve ValidatorResources in Application context;
	public static final String VALIDATOR = "Validator";
	
	// Hidden html attribute of <db:form  formValidatorName>
	public static final String FORM_VALIDATOR_NAME = "formValidatorName";

	// javascript boolean variable for doing or bypass validation when submit of <FORM>	
	public static final String JS_CANCEL_VALIDATION = "bValidateForm";
	
	public ValidatorConstants() {
		
	}

}