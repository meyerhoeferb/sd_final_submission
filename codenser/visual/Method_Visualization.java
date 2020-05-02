package V;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;
import java.io.*;
import P.ParsingUtils;

// Compile
// javac -cp .;commons-io-2.6.jar;spoon-core-7.3.0-jar-with-dependencies.jar Method_Visualization.java
// javac -cp .:visual/commons-io-2.6.jar:visual/spoon-core-7.3.0-jar-with-dependencies.jar visual/Method_Visualization.java

// Run
// java -cp .;commons-io-2.6.jar;spoon-core-7.3.0-jar-with-dependencies.jar Method_Visualization


public class Method_Visualization {

    public static void main(String[] args) throws IOException {

		// Find all files using apache commons FileUtils
		// test1 is the name of the directory used for testing

		final String dir = "other/src/main/java/other";

		File directory = new File("other/src/main/java/other");
		String[] extensions = {"java"};
		//Collection<File> files = FileUtils.listFiles(directory,extensions,true);
		//test
		List<File> files = ParsingUtils.findMatchingJavaFiles(dir);
		//endtest
		LinkedList<String> file_paths = new LinkedList<String>();
		LinkedList<String> file_names = new LinkedList<String>();
		BufferedWriter writer = new BufferedWriter(new FileWriter("visual/graph_data.txt"));


		//Debug line:
		//System.out.println("\nThe java files are:\n");
		for (File f : files) {
			// Print statement for debugging
			//System.out.println(f.getName());
			writer.write(f.getName() + "\n");
			file_names.add(f.getName());
			file_paths.add(f.getPath());
        }
		writer.write("\n\n");

		// Parse files to get method calls using Spoon

		String[] pathsToFiles = file_paths.toArray(new String[file_paths.size()]);
		CtModel model = ParsingUtils.parseAllToModel(pathsToFiles);

		//adj matrix declaration
		String[] filesArr = file_names.toArray(new String[file_names.size()]);
		int size = filesArr.length;
		int[][] adjMatrix = new int[size][size];
		//finish declaration

		//System.out.println(pathsToFiles[3]);

		/*

		Test lines:

		if(model.getAllTypes().stream().count() == 0)
			System.out.println("c is empty");

		if(model.getAllTypes().stream().map(type -> type.getAllMethods()).findFirst().get().isEmpty())
			System.out.println("empty");

		long stream_size = model.getAllTypes().stream().count();
		System.out.println(stream_size);

		Object[] allTypes = model.getAllTypes().stream().toArray();
		System.out.println(allTypes.length);

		*/


        // locate the main method (this will throw a NPE if none exists)
        //CtMethod mainMethod = model.getAllTypes().stream().map(type -> type.getMethodsByName("main")).findFirst().get().get(0);

		// Test line

		List<CtMethod> allMethods = model.getElements(new TypeFilter<>(CtMethod.class));


		//System.out.println("\nThe method calls are:\n");

		// Start loop

		for(int n = 0; n < allMethods.size(); n++){

		CtMethod mainMethod = allMethods.get(n);


        // find all method calls within the main method
        List<CtInvocation> statementList = mainMethod.filterChildren(statement -> statement instanceof CtInvocation).list();

		/*
		if(statementList.isEmpty())
			System.out.println("No methods called in main method");
		*/

        // for each method, extract info and print it


        for(CtInvocation methodCall : statementList) {

            // get the name, signature, and whether or not the method call is to a static method
            CtExecutableReference executableReference = methodCall.getExecutable();
            final boolean isStatic = executableReference.isStatic();
            final String methodName = executableReference.getSimpleName();
            final String methodSignature = executableReference.getSignature();

            // get the declaring type of the method (the class in which it is declared)
            CtTypeReference declaringType = executableReference.getDeclaringType();

            // get the AST node of the declaring type
            // if this is not in our parsed file types (ex. a built in java class or third party file we have not parsed), type will be null
            CtType type = declaringType.getDeclaration();
            CtMethod methodASTNode = null;
            if(type != null) {
                // find the AST node of the method with the matching signature in the declaring type
                @SuppressWarnings("unchecked")
                Optional<CtMethod> optionalCtMethod = type.getAllMethods().stream().filter(m -> methodSignature.equals(((CtMethod) m).getSignature())).findFirst();

                // deal with the optional
                if(optionalCtMethod.isPresent()) {
                    methodASTNode = optionalCtMethod.get();
                }
            }

            // for non static methods, gather some information about the calling variable
            CtVariableReference callingVariable = null;
            if(!isStatic && methodCall.getTarget() instanceof CtVariableRead) {
                // object method call, extract data about calling object
                CtVariableRead variable = (CtVariableRead) methodCall.getTarget();
                callingVariable = variable.getVariable();
            }


            // print out data
            //System.out.println(isStatic ? "STATIC METHOD CALL" : "OBJECT METHOD CALL");
            //System.out.format("LOCATION: %s %d:%d", methodCall.getPosition().getFile().getName(), methodCall.getPosition().getLine(), methodCall.getPosition().getColumn()).println();
            //System.out.println("CALL: " + methodCall);
            //System.out.println("SIGNATURE: " + methodSignature);
            //System.out.println("METHOD NAME: " + methodName);
            //System.out.println("CALLING VARIABLE: " + callingVariable);
            //System.out.println("DECLARING TYPE: " + declaringType);
            //System.out.println("METHOD AST NODE: " + methodASTNode);
            //System.out.println();



			// Check if matrix needs to be updated

			String fileName = methodCall.getPosition().getFile().getName();

			if((file_names.contains(declaringType + ".java")) && !(fileName.equals(declaringType + ".java"))){
				writer.write(methodCall.getPosition().getFile().getName() + " to " + declaringType + "\n");



			// update adj matrix

			int index1 = 0;     // The calling class (the row in the matrix)
			int index2 = 0;		// The class being called  (the column in the matrix)

			// Reading the matrix: Row class calls method in column class


			for(int i = 0; i < size; i++){
				if(filesArr[i].equals(fileName)){
					index1 = i;
					break;
				}
			}

			for(int i = 0; i < size; i++){
				if(filesArr[i].equals(declaringType + ".java")){
					index2 = i;
					break;
				}
			}

			if(index1 != index2)
				adjMatrix[index1][index2]++;
			// finish updating matrix
			}

        }

      writer.flush();


	  // finish loop

		}

      writer.close();

		// Write matrix to csv
	try
    {
      FileWriter csvwriter = new FileWriter("visual/Graph_Matrix.csv");
		// csvwriter.append("Matrix");
		csvwriter.append(',');
		for(int i = 0; i < size; i++){
			csvwriter.append(filesArr[i]);
			if(i != (size - 1)){
				csvwriter.append(',');
			}
		}
        csvwriter.append('\n');
        csvwriter.flush();

         for(int i = 0; i < size; i++)
         {
			csvwriter.append(filesArr[i]);
			csvwriter.append(',');
			int j;
            for (j = 0; j<(size-1); j++)
             {
                 csvwriter.append("" + adjMatrix[i][j]);
                 csvwriter.append(',');
             }
               csvwriter.append("" + adjMatrix[i][j]);
               csvwriter.append('\n');
               csvwriter.flush();
         }
         csvwriter.close();
      }
    catch(Exception e)
    {
      e.printStackTrace();
    }
	// finish writing matrix to csv
    }
}
