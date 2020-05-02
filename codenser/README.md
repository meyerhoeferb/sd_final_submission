Codenser is a CLI development tool to assist understanding of new code bases for developers.
It provides code base visualization, execution tracing, and code summarization.

Developed in tandem by Braden Meyerhoefer, Jiawei Zhang, and David Alejos


#######################################
Dev stuff TO BE DELETED
#######################################

path to test files: other/src/main/java/other
remember all classpaths go from codenser folder because that is where they are technically being called from

#######################################
CLI Instructions
#######################################

CODENSER REQUIRES PYTHON3. To download necessary packages, execute setup_packages.py

Start Codenser by running 'python3 codenser.py' in the command line

Before running make sure 'codenser/other/pom.xml' properly reflects the name of your main class on line 24.

The standard workflow would be to first use the 'run' command, which will do all of the analysis
on your code. From there, use the 'show' command to enter the visualization menu. Select
which visualization you would like to see and it will display for you. Hovering on the nodes will
display information about the node, which will help with further investigation of the code. For
execution tracing, the hottest nodes are the methods that are called the most. For file
interaction, the hottest nodes are the files that have other files call their methods the most.


#######################################
Documentation
#######################################

### EXECUTION TRACE ###

All maven setup for using ASM done with the help of this blog : http://tomsquest.com/blog/2014/01/intro-java-agent-and-bytecode-manipulation/
LICENSE.txt is for this setup.

#Important Note for users
    If having issues with execution trace, make sure 'codenser/other/pom.xml' properly reflects the name of your main class on line 24.
    This is handled on startup but could potentially need to be fixed by hand, luckily it is easy to do so!

    In between <mainClass> and </mainClass> write the name of your main file, without file extension.

    Example: If the main method is in Test.java, line 24 of 'codenser/other/pom.xml'
    should say <mainClass>Test</mainClass>

#A note on modifying the code in execution_trace/scripts
    The CLI runs using a command line backend; these scripts are called using the os package.
    Therefore, all the scripts are technically running from the codenser directory, and all
    filepaths must reflect that.

#Manual build order
    mvn package

    java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar -jar other/target/other-0.1-SNAPSHOT.jar 2> ./agent_output.txt

    python3 makeGraph.py
