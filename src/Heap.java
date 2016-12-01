/**
 * Created by Dallas Sanchez on 4/20/15.
 */
public class Heap
{
    //fields
    int size = 0;
    Edge[] allEdges;

    //constructor to initialize Edge[] array to proper max size
    Heap(int _numOfEdges)
    {
        allEdges = new Edge[_numOfEdges];
    }

    //removeMin function removes and returns the minimum and calls heapify to maintain heap properties
    Edge removeMin()
    {
        Edge returnedEdge = allEdges[1];

        allEdges[1] = allEdges[size];
        size = size - 1;

        heapify(1);

        return returnedEdge;
    }

    //heapify ensures that a tree follows heap proprieties by swapping if a root with whichever child is greater than it
    //recursively until no swaps are made
    void heapify(int root)
    {
        double rootWeight = allEdges[root].weight;
        if (root*2 <= size)
        {                                   //If statements to avoid out of bounds errors
            double leftChildWeight = allEdges[root * 2].weight;

            if (root * 2 + 1 <= size)
            {
                double rightChildWeight = allEdges[root * 2 + 1].weight;

                if (rootWeight < leftChildWeight && rootWeight < rightChildWeight)
                    return;

                if (leftChildWeight < rootWeight && leftChildWeight < rightChildWeight) {
                    Edge swappingTemp = allEdges[root * 2];
                    allEdges[root * 2] = allEdges[root];
                    allEdges[root] = swappingTemp;
                    heapify(root * 2);
                }

                if (rightChildWeight < rootWeight && rightChildWeight < leftChildWeight) {
                    Edge swappingTemp = allEdges[root * 2 + 1];
                    allEdges[root * 2 + 1] = allEdges[root];
                    allEdges[root] = swappingTemp;
                    heapify(root * 2 + 1);
                }
            }

            else
            {
                if (leftChildWeight < rootWeight)
                {
                    Edge swappingTemp = allEdges[root * 2];
                    allEdges[root * 2] = allEdges[root];
                    allEdges[root] = swappingTemp;
                    heapify(root * 2);
                }
            }
        }

    }

    //addToHeap adds an Edge to the Heap and then calls the sift up function to ensure heap properties are kept
    void addToHeap(Edge n)
    {
        allEdges[size+1] = n;
        size = size + 1;
        siftUp(size);

    }

    //sift up recursively checks if a leaf is bigger than it's parent, and swaps them if so.
    void siftUp(int leaf)
    {
        if (leaf == 1) return;

        if (allEdges[leaf].weight < allEdges[leaf/2].weight)
        {
            Edge swappingTemp = allEdges[leaf];
            allEdges[leaf] = allEdges[leaf/2];
            allEdges[leaf/2] = swappingTemp;
            siftUp(leaf/2);
        }



        // Swap them, then call the same function for leaf/2
        //
        // if leaf/2 == 1, return
        //
    }
}