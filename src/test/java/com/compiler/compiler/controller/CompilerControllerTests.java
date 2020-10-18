package com.compiler.compiler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class CompilerControllerTests {
	
	private CompilerController compiler;
	
	@BeforeTestClass
	public void setup() {
		compiler = new CompilerController();
	}
	
	@Test
	public void testPythonCompiler() {
	
	}
	
	@Test
	public void testJavaCompiler() {
	
	}
	
	@Test
	public void testCCompiler() {
	
	}
	
	@Test
	public void testCppCompiler() {
	
	}
}
