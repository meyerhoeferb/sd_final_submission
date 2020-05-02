import networkx as nx
import matplotlib.pyplot as plt
import pandas as pd
import plotly.graph_objects as go
from random import randint

#digest graph_matrix.csv, aka the matrix representation of the execution tree, and visualize
#columns are calling method, row is what they call, number is how many times
matrix = pd.read_csv('visual/Graph_Matrix.csv')

files = matrix[matrix.columns[0]]

#create graph and add nodes
#give each node a random x,y coordinate for visualizaiton
G = nx.DiGraph()
taken = []
for n in files:
    pos = (randint(1,50),randint(1,25))
    while pos in taken:
        pos = (randint(1,50),randint(1,25))
    taken.append(pos)
    G.add_node(n, pos=pos)

#add edges and calculate incoming weight of each method
inWeight = {}
for i in range(0, files.size):
    inWeight[files[i]] = 0

for i in range(0,files.size):
    calledFile = files[i]
    for j in range(0, files.size):
        callingFile = files[j]
        weight = matrix.loc[j,calledFile]
        if weight != 0:
            G.add_edge(callingFile,calledFile, weight=weight)
            inWeight[calledFile] = inWeight[calledFile] + weight

#setup graph for plotly visualization
edge_x = []
edge_y = []
for edge in G.edges():
    x0, y0 = G.nodes[edge[0]]['pos']
    x1, y1 = G.nodes[edge[1]]['pos']
    edge_x.append(x0)
    edge_x.append(x1)
    edge_x.append(None)
    edge_y.append(y0)
    edge_y.append(y1)
    edge_y.append(None)

edge_trace = go.Scatter(
    x=edge_x, y=edge_y,
    line=dict(width=0.5, color='#888'),
    hoverinfo='text',
    mode='lines')

node_x = []
node_y = []
for node in G.nodes():
    x, y = G.nodes[node]['pos']
    node_x.append(x)
    node_y.append(y)

node_trace = go.Scatter(
    x=node_x, y=node_y,
    mode='markers',
    hoverinfo='text',
    marker=dict(
        showscale=True,
        # colorscale options
        #'Greys' | 'YlGnBu' | 'Greens' | 'YlOrRd' | 'Bluered' | 'RdBu' |
        #'Reds' | 'Blues' | 'Picnic' | 'Rainbow' | 'Portland' | 'Jet' |
        #'Hot' | 'Blackbody' | 'Earth' | 'Electric' | 'Viridis' |
        colorscale='Hot',
        reversescale=True,
        color=[],
        size=50,
        colorbar=dict(
            thickness=15,
            title='Incoming Weight',
            xanchor='left',
            titleside='right'
        ),
        line_width=2))

#add coloring and node text
node_degrees = []
node_text = []
for node in G.nodes():
    node_degrees.append(inWeight[node])
    node_text.append("File: " + node)

#edge text
edge_text = []
for edge in G.edges():
    edge_text.append(edge[0] + " to " + edge[1] + " weight: ")

node_trace.marker.color = node_degrees
node_trace.text = node_text

#display
fig = go.Figure(data=[edge_trace, node_trace],
             layout=go.Layout(
                showlegend=False,
                hovermode='closest',
                margin=dict(b=20,l=5,r=5,t=40),
                xaxis=dict(showgrid=False, zeroline=False, showticklabels=False),
                yaxis=dict(showgrid=False, zeroline=False, showticklabels=False))
                )
fig.show()
#
# #display visualization
# # nx.draw_shell(G,with_labels=True, node_color=range(files.size), cmap=plt.cm.Blues)
# # plt.show()
