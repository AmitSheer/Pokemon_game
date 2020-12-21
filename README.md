# Pokemon_game
Catch all Pokemon in given graph. Assignment number 2 in OOP class.
Helping Professor Boaz catch all of the pokemon on the graph

## Usage
There are 2 options in order to run the program. One option is to run the program normaly and then input the ID and Scenario. The second option is to run the program through the
cmd and input the ID and Scenario in the number after the JAR name, that way the jar opened window will close when the scenario time runs out.

### Open application to run multiple scenarios
  1. Go to folder `E:\Git\Pokemon_game\out\artifacts\Ex2_jar\`
  2. Run `java -jar Ex2.jar`
  3. In the window you have the option to input the:
    * ID to run with
    * Scenario to use
  4. Then press the start button\
![panel](https://user-images.githubusercontent.com/26150015/102694529-56f6b300-422a-11eb-80f9-60cdcf167939.PNG)
  5. View the game run on the left side of the divider\
  ![gamePanel](https://user-images.githubusercontent.com/26150015/102694688-60344f80-422b-11eb-98f7-4d1e593d8227.PNG)
  6. The score for the scenario will be shown top left corner where the timer was\
  ![Score](https://user-images.githubusercontent.com/26150015/102696238-85c65680-4235-11eb-9698-23a7bb2cb1b1.PNG)


  
  
### Open from application to run one scenario
  1. Go to folder `E:\Git\Pokemon_game\out\artifacts\Ex2_jar\`
  2. Run `java -jar Ex2.jar {USER_ID} {SCENARIO_NUMBER}`
  3. View the game run on the left side of the divider\
  ![gamePanel](https://user-images.githubusercontent.com/26150015/102694688-60344f80-422b-11eb-98f7-4d1e593d8227.PNG)
  4. The scenario will finish running and will save the result in the server.
 
# Structure
This platforme is based on a directed connected graph, the algorithm's used to check the graph for three things:
  1. SCC, strongly connected components
  2. Shortest Path Distance, the shortest path from node to node
  3. Shortest Path, the path to take to get from one node to the other
## Algorithms
Two algorithms are used in the program.
1. Tarjan - check SCC
2. Dijkstra - find the shrtest path by distance

### Dijkstra
This algorithm check the graph for shortest path from one node to the other, by putting them into a priority queue ordered according to distance, or 'weight', of the path to the node. Then moving on the graph according to the order of the queue.

### Tarjan
This algorithm uses Depth-First Search , but with a quirk. When a algorithm visits a node it adds it to  a stack, when it visits the starting node it unloads the stack and counts it as an SCC, and adds all of the unloaded nodes into a list of lists of SCC nodes.

For the inner workings visit the wiki.
