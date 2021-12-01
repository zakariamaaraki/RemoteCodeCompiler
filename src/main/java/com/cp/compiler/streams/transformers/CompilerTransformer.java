package com.cp.compiler.streams.transformers;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.cp.compiler.services.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * The type Compiler transformer.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public class CompilerTransformer implements ValueTransformer<String, String> {
	
	private CompilerService compilerService;
	
	/**
	 * Instantiates a new Compiler transformer.
	 *
	 * @param compilerService the compiler service
	 */
	public CompilerTransformer(CompilerService compilerService) {
		this.compilerService = compilerService;
	}
	
	@Override
	public void init(ProcessorContext processorContext) {
		// empty
	}
	
	@Override
	public String transform(String jsonRequest) {
		try {
			return JsonMapper.transform(jsonRequest, compilerService);
		} catch (Exception e) {
			log.error("Error : ", e);
			return null;
		}
	}
	
	@Override
	public void close() {
		// empty
	}
}
