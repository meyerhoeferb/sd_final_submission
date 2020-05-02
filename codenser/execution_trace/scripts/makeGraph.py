import pandas as pd
import numpy as np

'''
VERY IMPORTANT!!!!
this script technically executes from the base codenser folder, therefore all
filepaths go from there
'''

#open trace file
with open('execution_trace/data/agent_output.txt', 'r') as file:
    trace = file.read()

#split into lines, remove blank last line
trace = trace.split('\n')
if trace[-1] == '':
    del trace[-1]

#create mapping of unique methods
method_mapping = list()
for line in trace:
    _, method = line.split()
    if method not in method_mapping:
        method_mapping.append(method)

#create dataframe to hold matrix
matrix = pd.DataFrame(index=method_mapping, columns=method_mapping)
matrix = matrix.fillna(0)

#iterate through trace, pushing to stack on call and popping on return
#creates matrix of how many times each method calls the other
stack = list()
for line in trace:
    action, method = line.split()

    if action == 'CALL':
        #add one to the [callingMethod,method] index (this will fill lower half of matrix)
        #unless this is the first loop
        if len(stack) != 0:
            callingMethod = stack[-1]
            matrix[callingMethod][method] = matrix[callingMethod][method] + 1

        #push called method to stack
        stack.append(method)
    elif action == 'RETURN':
        del stack[-1]

#save matrix graph representation
matrix.to_csv('execution_trace/data/graph_matrix.csv')
print("matrix representation of graph saved to execution_trace/data/graph_matrix.csv")
