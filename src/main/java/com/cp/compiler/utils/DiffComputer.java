package com.cp.compiler.utils;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public abstract class DiffComputer {

    private static final DiffMatchPatch diffMatchPatch = new DiffMatchPatch();

    private DiffComputer() {}

    public static String diff(String output, String expectedOutput) {

        if (expectedOutput == null) {
            throw new IllegalArgumentException("expectedOutput can not be null");
        }

        if (output == null) {
            throw new IllegalArgumentException("output can not be null");
        }

        LinkedList<DiffMatchPatch.Diff> computedDiff =
                diffMatchPatch.diffMain(CmdUtils.trimText(output), CmdUtils.trimText(expectedOutput));

        var sb = new StringBuilder();

        for (DiffMatchPatch.Diff diff: computedDiff) {
            switch (diff.operation) {
                case EQUAL:
                    sb.append(diff.text.trim()); // remove leading and trailing spaces.
                    break;
                case DELETE:
                    sb.append("<delete>");
                    sb.append(diff.text.trim());
                    sb.append("</delete>");
                    break;
                case INSERT:
                    sb.append("<add>");
                    sb.append(diff.text.trim());
                    sb.append("</add>");
                    break;
            }
        }

        return sb.toString();
    }
}
