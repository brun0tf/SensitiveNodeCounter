package general;

import bruno.ReadFile;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class GraphGenerator {
    public Graph<String, Transistor> graphGenerator (ArrayList<Transistor> transistor){
        ArrayList<String> nodeList = new ArrayList<>();


        Graph<String, Transistor> graph = new DirectedMultigraph<>(Transistor.class);

        for(Transistor i : transistor){
            if(!nodeAlreadyExists(nodeList, i.getNode1())){
                nodeList.add(i.getNode1());
            }
            if(!nodeAlreadyExists(nodeList, i.getNode2())){
                nodeList.add(i.getNode2());
            }
        }

        for (String i: nodeList) {
            graph.addVertex(i);
        }

        for(Transistor i : transistor){
            graph.addEdge(i.getNode1(), i.getNode2(), i);
        }

       /* Iterator<String> iter = new DepthFirstIterator<>(graph);
        while (iter.hasNext()) {
            String vertex = iter.next();
            System.out
                    .println(
                            "Vertex " + vertex + " is connected to: "
                                    + graph.edgesOf(vertex).toString());
        }*/

        /*for(Transistor i : transistor){
            System.out.println(i.getBulk());
        }*/
        return graph;
    }

    public boolean nodeAlreadyExists (ArrayList<String> X, String node){
        for (String i : X){
            if(i.equals(node)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Transistor> netlistMapping (String file){
        ArrayList<Transistor> transistor = new ArrayList<>();
        ArrayList<String> netlist = ReadFile.loadFileToArray(file);



        int k = 0;
        for (String line : netlist){
            StringTokenizer lineT = new StringTokenizer(line, " ");
            while(lineT.hasMoreTokens()){
                transistor.add(new Transistor(lineT.nextToken()));
                transistor.get(k).setNode1(lineT.nextToken());
                transistor.get(k).setGate(lineT.nextToken());
                transistor.get(k).setNode2(lineT.nextToken());
                transistor.get(k).setBulk(lineT.nextToken());
                transistor.get(k).setModel(lineT.nextToken());
                k++;
            }
        }
        return transistor;
    }


}
