
import os

#CLI for codenser
#TODO: setup method that runs on startup, performs movement of files and specifying of main in 'codenser/other/pom.xml'
#TODO: option that moves files to proper place for user, reset option that deletes files??
#TODO: write README documentation for how to use CLI
def CLI():
    print("   ___|   _ \   __ \   ____|   \  |   ___|   ____|   _ \  \n  |      |   |  |   |  __|      \ | \___ \   __|    |   | \n  |      |   |  |   |  |      |\  |       |  |      __ <  \n \____| \___/  ____/  _____| _| \_| _____/  _____| _| \_\ ")
    print("Welcome to Codenser")
    print("Please type one of the following commands")
    help()
    while(True):
        proc = input("\n>>> ")
        if(proc == "run"):
            run()
        elif(proc == "show"):
            show()
        elif(proc == "help"):
            help()
        elif(proc == "exit"):
            break
        elif(proc == "file"):
            fileInteract()
        elif(proc == "sum"):
            lines()
        elif(proc == "trace"):
            trace()
        else:
            print("\n\n" + proc + " is not a recognized command\n\n")

    print("Exiting Codenser")

#print menu options with descriptions of what they do
def help():
    print()
    print("run: run full analysis")
    print("show: access visualization options, only works after running analysis")
    print("help: reprint this menu")
    print("trace: run execution trace")
    print("file: process file interactions")
    print("sum: summarize methods inside the codebase")
    print("exit: exit codenser")
    print()
    #TODO: make sure all options have adequate descriptions

#run all analysis processes on the files
#this will be what is actually used in the user workflow, running them all
#one at a time would be annoying, maybe leave them as an option if need to redo a single
#part for whatever reason
def run():
    trace()
    summarize()
    fileInteract()

#visualization functionality
#can show either execution trace or file interaction
def show():
    print("\n\nWhat would you like to visualize\n\n")
    print("trace: execution trace graph")
    print("file: graph of files calling methods from each other")
    print("help: reprint this menu")
    print("exit: exit visualization")
    proc = ""
    while(True):
        proc = input("\nvisualization >>> ")
        if(proc == "trace"):
            os.system('python3 execution_trace/scripts/visualizeTrace.py')
        elif(proc == "file"):
            os.system('python3 visual/visualizeFile.py')
        elif(proc == "exit"):
            break
        elif(proc == "help"):
            print("trace: execution trace graph")
            print("file: file interaction graph")
            print("help: reprint this menu")
            print("exit: exit visualization")
        else:
            print("\n\n" + proc + " is not a recognized command\n\n")

#trace functionality
def trace():
    #delete previous compilation if it exists
    if os.path.exists("agent/target/agent-0.1-SNAPSHOT.jar"):
        os.remove("agent/target/agent-0.1-SNAPSHOT.jar")

    #compile agent and supplied files to be analyzed
    os.system('mvn package')

    #run program being analyzed with agent
    os.system('java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar -jar other/target/other-0.1-SNAPSHOT.jar 2> execution_trace/data/agent_output.txt')

    #process output of agent
    #logic in makeGraph.py
    os.system('python3 execution_trace/scripts/makeGraph.py')

    #FIXME: simple visualization, something for David to build off of
    # os.system('python3 execution_trace/scripts/visualizeTrace.py')

    print('\n\n\nFinished Trace\n\n\n')

#process codebase
def fileInteract():
    print('\n\n\nBeginning File Interactions\n\n\n')

    #delete previous data if it exists
    if os.path.exists("visual/graph_data.txt"):
        os.remove("visual/graph_data.txt")

    if os.path.exists("visual/Graph_Matrix.txt"):
        os.remove("visual/Graph_Matrix.txt")

    os.system('javac -d . -cp .:visual/spoon-core-7.3.0-jar-with-dependencies.jar visual/ParsingUtils.java')
    os.system('javac -d . -cp .:visual/commons-io-2.6.jar:visual/spoon-core-7.3.0-jar-with-dependencies.jar:visual/ParsingUtils visual/Method_Visualization.java')
    os.system('java -cp .:visual/commons-io-2.6.jar:visual/spoon-core-7.3.0-jar-with-dependencies.jar:visual/ParsingUtils V.Method_Visualization')

    #remove package folders because they annoy me
    os.system('rm -r V P')

    print('\n\n\nFinished File Interactions\n\n\n')

#get line numbers and files for methods
def lines():
    os.system('python3 execution_trace/scripts/fileAnalysis.py')

#summarization functionality
def summarize():
    print('\n\n\nBeginning Summarization\n\n\n')

    #check if file exists, delete if does
    if os.path.exists("summarization/sum_output.csv"):
        os.remove("summarization/sum_output.csv")

    #then create file again
    f = open("summarization/sum_output.csv", 'x')

    os.system('javac -d . -cp .:summarization/commons-collections-3.2.1-1.0.0.jar:summarization/commons-lang3-3.9.jar summarization/FileUtil.java')
    os.system('javac -d . summarization/MainPrograme.java')
    #iterate through all files in our test directory
    for subdir, dirs, files in os.walk("other/src/main/java/other"):
        for file in files:
            #print os.path.join(subdir, file)
            filepath = subdir + os.sep + file

            if filepath.endswith(".java"):
                print (filepath)
                os.system('java S.MainPrograme ' + filepath)

    #remove package folders because they annoy me
    os.system('rm -r F S')

    print('\n\n\nFinished Summarization\n\n\n')

if __name__ == '__main__':
    CLI()
