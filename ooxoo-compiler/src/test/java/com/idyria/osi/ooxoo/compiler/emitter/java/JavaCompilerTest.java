/**
 * 
 */
package com.idyria.osi.ooxoo.compiler.emitter.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import org.junit.Test;

/**
 * @author rleys
 *
 */
public class JavaCompilerTest {

	/**
	 * 
	 */
	public JavaCompilerTest() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Test
	public void schemaToXGen() {
		
		
		// Compile To XGEN
		//------------------------
		JavaCompiler compiler = new JavaCompiler();
		try {
			compiler.setOutputStream(new FileOutputStream("res.xgen.xml"));
			
			compiler.compile(getClass().getResource("extoll-validation-db.xsd"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// XGEN to JAVA
		//----------------------
		File jout = new File("./xjava");
		jout.mkdir();
		
		
		XGenScala xgenjava = new XGenScala();
		try {
			xgenjava.compile(new File("res.xgen.xml").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

}
