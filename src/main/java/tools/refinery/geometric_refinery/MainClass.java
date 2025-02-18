package tools.refinery.geometric_refinery;

import tools.refinery.generator.standalone.StandaloneRefinery;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        var problem = StandaloneRefinery.getProblemLoader().loadString("""
            class Filesystem {
                contains Directory[1] root
            }

            class File.

            class Directory extends File {
                contains Directory[] children
            }

            scope Filesystem = 1, File = 20.
            """);
        var generator = StandaloneRefinery.getGeneratorFactory().createGenerator(problem);
        generator.generate();
        var trace = generator.getProblemTrace();
        var childrenRelation = trace.getPartialRelation("Directory::children");
        var childrenInterpretation = generator.getPartialInterpretation(childrenRelation);
        var cursor = childrenInterpretation.getAll();
        while (cursor.move()) {
            System.out.printf("%s: %s%n", cursor.getKey(), cursor.getValue());
        }
    }
}