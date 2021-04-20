package general;

import bruno.Literal;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class SensitiveNodeFinder {
    Graph<String, Transistor> graph;
    ArrayList<Transistor> transistor;
    int[][] truthTable;
    ArrayList<Integer> ttOut;
    ArrayList<Literal> element;

    public SensitiveNodeFinder(Graph<String, Transistor> graph, int[][] truthTable, ArrayList<Integer> ttOut, ArrayList<Literal> literal, ArrayList<Transistor> transistor) {
        this.graph = graph;
        this.truthTable = truthTable;
        this.ttOut = ttOut;
        this.element = literal;
        this.transistor = transistor;
    }
    public void atribuiValorAosTransistores (int line){
        for (Literal i : element) {
            for (Transistor j : transistor) {
                if (j.getGate().equals(Character.toString(i.getName()))) {
                    j.value = i.getValue(line);
                }
                else if (j.getGate().equals("out")){
                    j.value = ttOut.get(line);
                }
            }
        }

    }
    public void sensitiveNode(){
        int numOflines = ttOut.size();
        AllDirectedPaths<String, Transistor> allPaths = new AllDirectedPaths<>(graph);

        List<GraphPath<String, Transistor>> x = allPaths.getAllPaths("out", "GND", true, null);
        List<GraphPath<String, Transistor>> VDDtoOut = allPaths.getAllPaths("out", "VDD", true, null);

        ArrayList<String> sensitiveNodes = new ArrayList<>();

        for(int line = 0; line < numOflines; line++){//percorre linhas da tabela
            atribuiValorAosTransistores(line);
            if(ttOut.get(line) == 1){
                for(int i = 0; i < x.size(); i++){//percorre todos os vertices do caminho entre GND e out
                    if(x.get(i).getVertexList().size() == 2){ //caso GND esteja ligado apenas em out
                        if(!nodeAlreadyExistsInSensitiveNodesList(x.get(i).getStartVertex(), sensitiveNodes)){
                            sensitiveNodes.add(x.get(i).getStartVertex());
                        }
                    }
                    else if(x.get(i).getVertexList().size() > 2){//tenha outros vertices entre gnd e out
                        for(int j = 0; j < x.get(i).getVertexList().size(); j++){
                            if(!nodeAlreadyExistsInSensitiveNodesList(x.get(i).getVertexList().get(j), sensitiveNodes)){//node ainda nao existe na lista de nos sensitivos
                                if(!x.get(i).getVertexList().get(j).equals("GND")){//nao é gnd
                                    if(pullDownPathAnalysis(x.get(i).getEdgeList().toString())){
                                        sensitiveNodes.add(x.get(i).getVertexList().get(j));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (ttOut.get(line) == 0){
                for(int i = 0; i < VDDtoOut.size(); i++){//percorre todos os vertices do caminho entre GND e out
                    if(VDDtoOut.get(i).getVertexList().size() == 2){ //caso GND esteja ligado apenas em out
                      if(!nodeAlreadyExistsInSensitiveNodesList(VDDtoOut.get(i).getStartVertex(), sensitiveNodes)){
                          sensitiveNodes.add(VDDtoOut.get(i).getStartVertex());
                      }
                    }
                    else if(VDDtoOut.get(i).getVertexList().size() > 2){//caso tenha outros vertices entre gnd e out
                        for(int j = 0; j < VDDtoOut.get(i).getVertexList().size(); j++){//percorre o caminho selecionado
                            if(!nodeAlreadyExistsInSensitiveNodesList(VDDtoOut.get(i).getVertexList().get(j), sensitiveNodes)){//node ainda nao existe na lista de nos sensitivos
                              if(!VDDtoOut.get(i).getVertexList().get(j).equals("VDD")){//nao é vdd
                                 if(pullUpPathAnalysis(VDDtoOut.get(i).getEdgeList().toString(), j)){
                                sensitiveNodes.add(VDDtoOut.get(i).getVertexList().get(j));
                                 }
                              }
                            }
                        }
                    }
                }
            }
            System.out.print("sensitive nodes from line " + line + ": ");
            for (String node : sensitiveNodes){
                System.out.print(node + " ");
            }
            System.out.println();
            sensitiveNodes.clear();
        }

       /*while (iter.hasNext()) {
            String vertex = iter.next();
            System.out
                    .println(
                            "Vertex " + vertex + " is connected to: "
                                    + graph.edgesOf(vertex).toString());
        }*/

        for (String node : sensitiveNodes){
            System.out.println(node);
        }
    }

    public boolean pullDownPathAnalysis (String allEdges){
        StringTokenizer edge = new StringTokenizer(allEdges, ",");
        while(edge.hasMoreElements()){
            if(edge.nextToken().contains("0")){
                if(edge.hasMoreElements())
                    return false;
            }
        }
        return true;
    }
    public boolean pullUpPathAnalysis (String allEdges, int numOfVertex){
        StringTokenizer edge = new StringTokenizer(allEdges, ",");
        for (int i = 0; i < numOfVertex; i++){
            if(edge.nextToken().contains("1")){
                return false;
            }
        }

        return true;
    }
    public boolean nodeAlreadyExistsInSensitiveNodesList(String node, ArrayList<String> list){
        int cont = 0;
        if(list.isEmpty()){
            return false;
        }else{
            for(String element : list){
                if(element.equals(node)){
                    cont++;
                }
            }
            if(cont == 0){
                return false;
            }
            cont = 0;
        }
        return true;
    }

}
