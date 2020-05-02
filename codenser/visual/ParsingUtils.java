package P;
import java.io.File;
import java.io.IOException;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.support.compiler.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This file handles configuration and parsing of the Spoon Model's AST
public class ParsingUtils {

    public static CtModel parseAllToModel(final String... baseFilePaths) {
        String currentFilePath = null;
        try {
            // TODO: Implement IncrementalLauncher for improved performance
            final Launcher launcher = new Launcher();
            launcher.getEnvironment().setNoClasspath(true);
            launcher.getEnvironment().setAutoImports(true);
            for(String filePath : baseFilePaths) {
                currentFilePath = filePath;
                // FIXME: potential bug where duplicate files could be present?
                // FIXME: do we need to make a set of file paths to avoid this?
                for (File file : findMatchingJavaFiles(filePath)) {
                    launcher.addInputResource(new FileSystemFile(file));
                }
            }
            return launcher.buildModel();
        }catch(IOException e) {
            System.err.format("Failed to find matching files for baseFilePath=%s", currentFilePath).println();
            return null;
        }
    }

    public static List<File> findMatchingJavaFiles(final String baseFilePath) throws IOException {
        final int MAX_RECURSIVE_DEPTH = 999;
        final String FILE_TYPE_PATTERN = ".*\\.java";
        final Stream<Path> matchingPaths = Files.find(Paths.get(baseFilePath), MAX_RECURSIVE_DEPTH, (path, fileAttributes) -> {
            return path.getFileName().toString().matches(FILE_TYPE_PATTERN) && fileAttributes.isRegularFile();
        });
        return matchingPaths.map(path -> path.toFile()).collect(Collectors.toList());
    }
}
