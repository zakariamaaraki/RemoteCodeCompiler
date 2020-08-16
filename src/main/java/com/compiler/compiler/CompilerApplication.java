package com.compiler.compiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SpringBootApplication
public class CompilerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompilerApplication.class, args);
	}

}
