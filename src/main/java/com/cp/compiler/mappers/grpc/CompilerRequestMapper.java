package com.cp.compiler.mappers.grpc;

import com.cp.compiler.contract.CompilerProto;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.exceptions.CompilerBadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompilerRequestMapper {

    @Mapping(source = "testCases", target = "testCases", qualifiedByName = "toTestCases")
    @Mapping(source = "language", target = "language", qualifiedByName = "toLanguage")
    RemoteCodeCompilerRequest toCompilerRequest(CompilerProto.RemoteCodeCompilerRequest protoRequest);

    @Named("toLanguage")
    static Language toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language language) {

        switch (language) {
            case C: return Language.C;
            case CPP: return Language.CPP;
            case CS: return Language.CS;
            case JAVA: return Language.JAVA;
            case KOTLIN: return Language.KOTLIN;
            case SCALA: return Language.SCALA;
            case GO: return Language.GO;
            case HASKELL: return Language.HASKELL;
            case PYTHON: return Language.PYTHON;
            case RUBY: return Language.RUBY;
            case RUST: return Language.RUST;
            case UNRECOGNIZED:
            default:
                throw new CompilerBadRequestException("Language not supported");
        }
    }

    @Named("toTestCases")
    static LinkedHashMap<String, TestCase> toTestCases(
            Map<String, CompilerProto.RemoteCodeCompilerRequest.TestCase> testCases) {

        return new LinkedHashMap<>(testCases.entrySet()
                                            .stream()
                                            .collect(Collectors.toMap(
                                                        Map.Entry::getKey, entry -> new TestCase(
                                                                entry.getValue().getInput(),
                                                                entry.getValue().getExpectedOutput()))));
    }
}
