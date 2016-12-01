/**
 * Created by Dallas Sanchez on 5/4/15.
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Dallas Sanchez on 4/15/15.
 */

public class Main
{
    public static void main(String[] args) throws FileNotFoundException {

        //First open and read the input file
        //--------------//

        //open the input file
        Scanner in = new Scanner(new FileReader("input.txt"));

        //initialize a counter for number of points in the input file to 0
        int numOfPoints = 0;

        //While the input file has lines loop through and increment counter by one for each line
        while (in.hasNext())
        {
            in.nextLine();
            numOfPoints +=1;
        }

        in.close(); //Close the input file so that it can be reopened
        in = new Scanner(new FileReader("input.txt")); //reopen the input file to read it from the top again

        //Go through the file again adding every line to an array of strings
        String allLines[] = new String[numOfPoints];
        for (int i = 0; in.hasNext(); i++)
        {
            allLines[i] = in.nextLine();
        }


        in.close();

        //initialize numOfEdges and loop through the fileLines array using the length of each line
        int numOfEdges = 0;

        for (int i = 0; i < allLines.length; i++)
        {
            //Example line "A,5,B,2,C" the algorithm (length - 1) / 4 gives the num of edges in that line
            numOfEdges += (allLines[i].length() - 1) / 4;
        }

        numOfEdges = numOfEdges / 2; //since each connection is listed twice divide the counter by 2
        //----------------//


        //create hashmaps to be used

        //edgesAdded to keep track of edges already used
        HashMap<String, Boolean> edgesAdded = new HashMap<String, Boolean>();

        //parentHM is the final product of this algorithm, it holds the collective weight and parent of each node
        HashMap<String, ParentNode> parentHM = new HashMap<String, ParentNode>();

        //adjacency list is used to store the outgoing edges each node has
        HashMap<String, LinkedList<Node>> adjacencyList = new HashMap<String, LinkedList<Node>>();


        //first loop through each line in the input
        for (int i = 0; i < allLines.length; i ++)
        {
            String currLine[] = allLines[i].split(",");

            //initialize a parent node with max value and itself as the parent for every node in the file
            ParentNode currParentNode = new ParentNode();
            currParentNode.parentNode = currLine[0];
            currParentNode.value = Double.MAX_VALUE;

            parentHM.put(currLine[0], currParentNode);


            //add each Node that the current parent node has connections to too a linked list
            LinkedList<Node> currLinkedList = new LinkedList<Node>();

            for (int j = 0; j < currLine.length - 2; j = j + 2)
            {
                Node currNode = new Node();
                currNode.weight = Double.parseDouble(currLine[j + 1]);
                currNode.nodeID = currLine[j+2];

                currLinkedList.add(currNode);
            }
            //add the linked list of connections to the adjacencyList hashmap
            adjacencyList.put(currLine[0], currLinkedList);

        }

        //prompt the user for the source node
        Scanner userInput = new Scanner(System.in);

        System.out.print("Enter your desired source node: ");
        String source = userInput.nextLine().toUpperCase();

        userInput.close();

        //update the parentHM to have a parentNode with value 0
        ParentNode sourceNode = new ParentNode();
        sourceNode.parentNode = source;
        sourceNode.value = 0;
        parentHM.put(source, sourceNode);

        Heap myHeap = new Heap(numOfEdges); //create a heap to hold the Edges

        //Add the Source nodes edges to the heap
        for(int i = 0; i < adjacencyList.get(source).size(); i++)
        {
            Edge currEdge = new Edge();
            currEdge.parent = source;
            currEdge.node = adjacencyList.get(source).get(i).nodeID;
            currEdge.weight = adjacencyList.get(source).get(i).weight;

            myHeap.addToHeap(currEdge);
            edgesAdded.put(currEdge.node + currEdge.parent, true);
        }


        //while the heap is not empty
        while (myHeap.size > 0)
        {
            Edge minEdge = myHeap.removeMin(); //remove the minimum from the heap

            //Add the edges connected to minEdges toPoint
            for (int i = 0; i < adjacencyList.get(minEdge.node).size(); i++)
            {
                Edge currEdge = new Edge();
                currEdge.parent = minEdge.node;
                currEdge.node = adjacencyList.get(minEdge.node).get(i).nodeID;
                currEdge.weight = adjacencyList.get(minEdge.node).get(i).weight;

                //if the reverse pairing hasn't already been added, add it to the heap
                if (edgesAdded.get(currEdge.parent + currEdge.node) == null)
                {
                    myHeap.addToHeap(currEdge);
                    edgesAdded.put(currEdge.node + currEdge.parent, true);
                }

            }

            //Call the updateHM function to adjust the values in the parentHM if necessary
            updateHM(minEdge, parentHM);
        }


        //print out the results
        System.out.println("Source: " + source);
        System.out.println("Node:   Value:    Parent:");

        //using an enhanced for-loop to loop through the parentHM. entry is like a variable for parentHM.get(i);
        for (Map.Entry<String, ParentNode> entry : parentHM.entrySet())
        {
            System.out.println("    " + entry.getKey() + "      " + entry.getValue().value + "        " + entry.getValue().parentNode);
        }

        System.out.println();
        System.out.println(parentHM);
    }


    //updateHM compares if the weight of a connection, plus it's parents weight is less then a nodes current weight
    //and sets it to whichever is lower
    static void updateHM(Edge _minEdge, HashMap<String, ParentNode> _parentHM)
    {
        if (_minEdge.weight + _parentHM.get(_minEdge.parent).value < _parentHM.get(_minEdge.node).value)
        {
            _parentHM.get(_minEdge.node).value = _minEdge.weight + _parentHM.get(_minEdge.parent).value;
            _parentHM.get(_minEdge.node).parentNode = _minEdge.parent;
        }
    }
}