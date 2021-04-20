package bruno;

import java.util.ArrayList;

public class SortInput {
    ArrayList<Literal> output = new ArrayList<>();
    public ArrayList<Literal> sort (ArrayList<Literal> input){
        String[] vector = new String[input.size()];
        for(int i = 0; i < input.size(); i++){
            vector[i] = String.valueOf(input.get(i).getName());
            // System.out.println(vector[i]);
        }

        String temp;
        for(int i = 0; i < input.size(); i++){
            for(int j = i; j < input.size(); j++){
                if(vector[i].compareTo(vector[j]) > 0){
                    temp = vector[i];
                    vector[i] = vector[j];
                    vector[j] = temp;
                }
            }
        }
        for(int i = 0; i < input.size(); i++){
            output.add(new Literal(vector[i].charAt(0)));
        }
        return output;
    }

}