package org.dbforms.util;

import com.bitmechanic.maxq.IScriptAdapter;
import com.bitmechanic.maxq.Utils;
import com.bitmechanic.maxq.generator.AbstractCodeGenerator;
import com.bitmechanic.maxq.generator.GeneratorFactory;
import java.io.File;

/**
 * Generate a java JUnit test case.
 * Take a look at the CodeGenerator Interface to see how to specify which one you want.
 * The important information is that this class generates Java code.
 * @see IScriptGenerator
 * @author Robert Dawson robert@rojotek.com
 * @author Wayne Hearn
 */
public class HttpTestCaseGenerator extends AbstractCodeGenerator {
	private String baseClasspath;

	public HttpTestCaseGenerator(IScriptAdapter adapter) {
		super(adapter);
		File basePath = new File(GeneratorFactory.generatorProperties.getProperty("generator.java.sourcepath", ""));
		this.baseClasspath = basePath.getAbsolutePath();
		System.out.println(this.baseClasspath);
	}

	 public String[] getValidFileExtensions() {
		return new String[] {".java"};
	}


	public void doCreateList() {
		super.getScriptAdapter().append(createStatement("list = new ArrayList()"));
	}

	public void doCallUrl(String url, String method, String data, String contentLength, String list) {
		super.getScriptAdapter().append(createStatement(
				method + "(\"" +
				url + "\"" +
				data +
				contentLength +
				list +
				")"));
	}

	public void doAssertResponse(String respCode) {
	}

	public void doAddParameterToList(String name, String value) {
		super.getScriptAdapter().append( createStatement("list.add(" +
				"new KeyValuePair(\"" + name + "\", \"" + value + "\")" +
				")"));
	}


	public void doTestUrlMessage(String url) {
	}

	public void doSetData(String data) {
		super.getScriptAdapter().append( createStatement("data = '" + data + "'"));
	}

	public void doTidyCode(String url) {
	}

	public void doResponseForStdOut(String url) {
	}

	public void doResponseForFile() {
	}

	public void doStartRecording() {
		if(! scriptContainsTestDeclaration()) {
			getScriptAdapter().insert(getTestDeclarationText(), 0);
		}
	}

	public void doStopRecording() {
		StringBuffer result = new StringBuffer();
		result.append("    }"); //close the method.
		result.append(EOL + EOL);
		result.append("/****************/" + EOL + EOL);
		result.append("}");
		super.getScriptAdapter().append( result.toString());
	}

	public void doSave(String path, String fileName) {
		if(path == null )
			throw new IllegalArgumentException("path cannot be null");
		if(fileName == null )
			throw new IllegalArgumentException("file name cannot be null");

		String name = fileName;
		if(fileName.indexOf(".") > -1)
			name = fileName.substring(0, fileName.indexOf("."));

		setTestName(name);
		setTestPath(path);

		updateTestName();
		updateTestPath();
	}

	private void updateTestPath() {
		String pkg = getPackage();
		System.out.println("package = " + pkg);
		if( getScript() != null) {
			int startpos = getScript().indexOf("package ");
			if(startpos > -1) {
				startpos += 8;
				int end = getScript().indexOf(";", startpos);
				getScriptAdapter().replace(getPackage(), startpos, end);
			}
			else {
				if(!pkg.equals("")) {
					getScriptAdapter().insert("package " + pkg + ";" + EOL + EOL, 0);
				}
			}
		}
	}

	public void doLoad(String script) {
		super.loadScriptAdapter(script);
		String testName = parseTestName();
		System.out.println("testNAme = " + testName);
		setTestName(testName);
		setDefaultTestName(testName);
		String testPath = parseTestPath();
		System.out.println("TestPath = " + testPath);
		setTestPath(parseTestPath());
	}

	public String parseTestName() {
		String testName = "";
		if( getScript() != null) {
			int startpos = getScript().indexOf("public class ");
			if(startpos > -1) {
				startpos += 13;
				int end = getScript().indexOf(" extends", startpos);
				testName = getScript().substring(startpos, end);
			}
		}
		return testName;
	}


	private String parseTestPath() {
		String testPath = null;
		int pos = getPackageNameStartPos();
		if(pos > -1) {
			int end = getPackageNameEndPos();
			testPath = packageToPath(getScript().substring(pos, end).trim());
		}
		return testPath;
	}

	private int getPackageNameEndPos() {
		int pos = -1;
		if( getScript() != null) {
			pos = getScript().indexOf(";", getPackageNameStartPos());
		}
		return pos;
	}

	private int getPackageNameStartPos() {
	   int pos = -1;
		if( getScript() != null) {
			pos = getScript().indexOf("package ");
			if(pos > -1) {
				pos += 7;
			}
		}
		return pos;
	}

	private String packageToPath(String s) {
		return getTestPath() + "\\" + s.replace('.', '\\');
	}

	private String getPackage() {
		String path = "";

		System.out.println("baseclasspath = " + this.baseClasspath);
		System.out.println("testpath = " + getTestPath());

		if ((getTestPath() != null) && (baseClasspath != null)) {
			if(getTestPath().toLowerCase().startsWith(this.baseClasspath.toLowerCase()) &&
			   getTestPath().length() != baseClasspath.length())
			{
				path = getTestPath().substring(this.baseClasspath.length() + 1);
			}
		}
		return path.replace('\\', '.');
	}


	private void updateTestName() {
		//Update Class definition
		updateClassDefinition();
		//update constructor
		updateConstructor();
		//Update default test method
		updateTestMethod();
	}

	private void updateTestMethod() { int startpos = -1;
		if( getScript() != null) {
			startpos = getScript().indexOf("public void test" + getDefaultTestName() + "() throws Exception {");
			if(startpos > -1) {
				startpos += 16;
				getScriptAdapter().replace(getTestName(), startpos, startpos + getDefaultTestName().length());
			}

		}
	}

	private void updateClassDefinition() {
		int startpos = -1;
		if( getScript() != null) {
			startpos = getScript().indexOf("public class " + getDefaultTestName());
			if(startpos > -1) {
				startpos += 13;
				int end = -1;
				if( getScript() != null) {
					end = getScript().indexOf(" extends");
				}
				getScriptAdapter().replace(getTestName(), startpos, end);
			}

		}
	}

	private void updateConstructor() {
		int startpos = -1;
		if( getScript() != null) {
			startpos = getScript().indexOf("public " + getDefaultTestName() + "(String name) {");
			if(startpos > -1) {
				startpos += 7;
				getScriptAdapter().replace(getTestName(), startpos, startpos + getDefaultTestName().length());
			}
		}
	}

	private boolean scriptContainsTestDeclaration() {
		int pos = getScript().indexOf("// This class was generated by MaxQ (maxq.tigris.org)");
		return (pos > -1) ;
	}


	protected String getTestDeclarationText() {
		StringBuffer result = new StringBuffer();
		result.append("// package" + EOL);
		result.append("// This class was generated by MaxQ (maxq.tigris.org)" + EOL);
		result.append(getPackageDeclaration());
		result.append("// imports" + EOL);
		result.append("import java.util.List" + END_STATEMENT);
		result.append("import java.util.ArrayList" + END_STATEMENT);
		result.append("import org.dbforms.util.KeyValuePair" + END_STATEMENT);
		result.append("import org.dbforms.util.HttpTestCase" + END_STATEMENT);

		result.append(EOL + "// definition of test class" + EOL);
		result.append("public class " + getTestName() + " extends HttpTestCase {" + EOL);

		result.append("    // Test method generated from the MaxQ Java generator" + EOL);
		result.append("    public " + getTestName() + "(String name) {" + EOL);
		result.append("        super(name)" + END_STATEMENT);
		result.append("    }" + EOL);


		result.append("    public void test" + getTestName() + "() throws Exception {" + EOL);
		result.append("        List list" + END_STATEMENT);

		return result.toString();

	}

	private String getPackageDeclaration() {
		String pkgDeclaration = "";
		if(! getPackage().equals(""))
			pkgDeclaration =  "package " + getPackage() + END_STATEMENT;

		return pkgDeclaration;
	}

	/**
	 * Returns the supplied string left padded
	 * @param text
	 * @return
	 */
	protected String createStatement(String text) {
		return Utils.lpad(8, text + END_STATEMENT);
	}


}
