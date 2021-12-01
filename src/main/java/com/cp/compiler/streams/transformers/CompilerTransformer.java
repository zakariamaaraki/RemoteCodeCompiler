package com.cp.compiler.streams.transformers;

import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.cp.compiler.services.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.http.ResponseEntity;

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
			Request request = JsonMapper.toRequest(jsonRequest);
			ResponseEntity<Object> responseEntity = compilerService.compile(request.getExpectedOutput(),
					request.getSourceCode(), request.getInput(), request.getTimeLimit(), request.getMemoryLimit(),
					request.getLanguage());
			Object body = responseEntity.getBody();
			return body instanceof Response ? JsonMapper.toJson((Response) body) : null;
		} catch(Exception exception) {
			log.error("Error : ", exception);
			return null;
		}
	}
	
	@Override
	public void close() {
		// empty
	}
}
