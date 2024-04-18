package com.cp.compiler.mappers.grpc;

import com.cp.compiler.contract.CompilerProto;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.exceptions.CompilerBadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerExecutionResponse.Language.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompilerResponseMapper {

    @Mapping(source = "execution", target = "execution", qualifiedByName = "toCompilerExecutionResponse")
    @Mapping(source = "error", target = "error", qualifiedByName = "toError")
    CompilerProto.RemoteCodeCompilerResponse ToCompilerResponseProto(RemoteCodeCompilerResponse response);

    @Named("toCompilerExecutionResponse")
    @Mapping(source = "testCasesResult", target = "mutableTestCasesResult", qualifiedByName = "toTestCasesResult")
    @Mapping(target = "testCasesResult", ignore = true) // hack to fix mapstruct issue, see: https://github.com/mapstruct/mapstruct/issues/1343
    @Mapping(source = "language", target = "language", qualifiedByName = "toLanguages")
    CompilerProto.RemoteCodeCompilerExecutionResponse toCompilerExecutionResponse(RemoteCodeCompilerExecutionResponse executionResponse);

    @Named("toError")
    static String toError(String error) {
        if (error == null) {
            return "";
        }
        return error;
    }

    @Named("toLanguages")
    static CompilerProto.RemoteCodeCompilerExecutionResponse.Language toLanguages(Language language) {

        switch (language) {
            case C: return C;
            case CPP: return CPP;
            case CS: return CS;
            case JAVA: return JAVA;
            case KOTLIN: return KOTLIN;
            case SCALA: return SCALA;
            case GO: return GO;
            case HASKELL: return HASKELL;
            case PYTHON: return PYTHON;
            case RUBY: return RUBY;
            case RUST: return RUST;
            default:
                throw new CompilerBadRequestException("Language not supported");
        }
    }

    @Named("toTestCasesResult")
    static Map<String, CompilerProto.RemoteCodeCompilerExecutionResponse.TestCaseResult> toTestCasesResult(
            LinkedHashMap<String, TestCaseResult> testCasesResult) {

        return testCasesResult.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> CompilerProto.RemoteCodeCompilerExecutionResponse.TestCaseResult.newBuilder()
                                            .setOutput(entry.getValue().getOutput())
                                            .setError(entry.getValue().getError())
                                            .setVerdict(entry.getValue().getStatusResponse())
                                            .setVerdictStatusCode(entry.getValue().getVerdictStatusCode())
                                            .setExpectedOutput(entry.getValue().getExpectedOutput())
                                            .setExecutionDuration(entry.getValue().getExecutionDuration())
                                            .build()));
    }
}
