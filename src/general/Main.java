package general;

import org.jgrapht.*;
import org.jgrapht.graph.*;


import java.io.*;
import java.net.*;
import java.util.*;
import bruno.*;

public class Main {

    public static void main(String[] args) {
        int[][] aux1;
        ArrayList<Literal> aux0;
        ArrayList<Integer> aux3;
        ArrayList<Transistor> aux4;
        Graph<String, Transistor> aux2;
        boolean outIsInverted = false;

        GraphGenerator graph = new GraphGenerator();
        aux4 = graph.netlistMapping("src\\general\\SPICEnetlist.txt");
        aux2= graph.graphGenerator(aux4);

        for (Transistor i : aux4){
            if (i.getName().contains("not_out")) {
                outIsInverted = true;
                break;
            }
        }

        String fileExpression = ReadFile.LoadFileToString("src\\general\\input.txt");
        //System.out.println(fileExpression);

        TruthTableGenerator truthTable = new TruthTableGenerator();
        aux1 = truthTable.createTruthTable(fileExpression);
        aux3 = truthTable.generate_output(outIsInverted);
        aux0 = truthTable.literalsMapping();





        SensitiveNodeFinder X = new SensitiveNodeFinder(aux2, aux1, aux3, aux0, aux4);
        X.sensitiveNode();

    }
}
