package bruno;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TruthTableGenerator {
    ArrayList<Integer> product = new ArrayList<>();
    ArrayList<Integer> ttOutput = new ArrayList<>();
    ArrayList<Integer> expressionValue = new ArrayList<>();

    ArrayList<Literal> element = new ArrayList<>();
    boolean literalAlreadyExists = false;
    int numOfLiterals = 0;
    String expression;

    private int linhas, colunas;
    private int[][] truthTable;


    public ArrayList<Literal> literalsMapping (){
        for (int i = 0; i < expression.length(); i++) {
            if ((expression.charAt(i) != '*') && (expression.charAt(i) != '+') && (expression.charAt(i) != ' ') && (expression.charAt(i) != '!') && (expression.charAt(i) != '\n') && (expression.charAt(i) != '(' && expression.charAt(i) != ')')) {
                for (Literal literal : element) {
                    if (expression.charAt(i) == literal.getName()) {
                        literalAlreadyExists = true;
                        break;
                    }
                }
                if (!literalAlreadyExists) {
                    element.add(new Literal(expression.charAt(i)));
                    numOfLiterals++;
                }
                literalAlreadyExists = false;
            }
        }
        return element;
    }

    //cria matriz com int 1 em cada endereço
    private int[][] createMatrix(int linhas, int colunas) {
        int[][] truthTable = new int[linhas][colunas];
        for (int col = 0; col < colunas; col++) {
            for (int lin = 0; lin < linhas; lin++) {
                truthTable[lin][col] = 1;
            }
        }
        return truthTable;
    }

    public int[][] createTruthTable (String inputExpression) {
        expression = inputExpression;
        literalsMapping();


        linhas = (int) Math.pow(2, numOfLiterals);
        colunas = numOfLiterals;
        truthTable = createMatrix((int) linhas, (int) colunas);

        int numberOfColumnsInARowEqualsToZero = (int) Math.pow(2, colunas - 1);
        for (int j = 0; j < colunas; j++) {
            for (int i = 0; i < linhas; i++) {
                if (j == numOfLiterals - 1) {//caso especifico para ultima coluna
                    if (i % 2 != 0) {
                        truthTable[i][j] = 1;
                    } else
                        truthTable[i][j] = 0;
                } else if (i % numberOfColumnsInARowEqualsToZero == 0) {
                    for (int k = i, p = i; k < p + numberOfColumnsInARowEqualsToZero; k++) {
                        truthTable[k][j] = 0;
                        i++;
                    }
                }
            }
            numberOfColumnsInARowEqualsToZero /= 2;
        }

        //LiteralsMapping();
        //generate_output();

        return truthTable;
    }

    public ArrayList<Integer> generate_output(boolean outIsInverted){

        literalsMapping();
        StringTokenizer expWithToken = new StringTokenizer(expression, "+");

        //adiciona os literais ao arraylist element
        SortInput sort = new SortInput();
        element = sort.sort(element);


        for (int j = 0; j < numOfLiterals; j++) {
            for (int i = 0; i < this.linhas; i++) {
                element.get(j).setValue(truthTable[i][j]);
            }
        }

        ArrayList<String> allCubes = new ArrayList<>();
        while (expWithToken.hasMoreTokens()) {
            allCubes.add(expWithToken.nextToken());
        }
        int cube;
        for (int line = 0; line < this.linhas; line++) {
            cube = 0;
            for (int k = 0; k < allCubes.size(); k++) {//percorre os cubos
                for (Literal literal : element) { //percorre os literais
                    for (int j = 0; j < allCubes.get(cube).length(); j++) { // percorre o cubo selecionado
                        if (literal.getName() == allCubes.get(cube).charAt(j)) {
                            if (j != 0) {
                                if (allCubes.get(cube).charAt(j - 1) == '!') {
                                    if (literal.getValue(line) == 0) {
                                        expressionValue.add(1);
                                    } else {
                                        expressionValue.add(0);
                                    }
                                } else {
                                    expressionValue.add(literal.getValue(line));
                                }

                                break;
                            } else expressionValue.add(literal.getValue(line));
                        }
                    }
                }
                //aqui, percorreu to.do o cubo selecionado com todos os literais possiveis
                cube++;
                int temp = expressionValue.get(0);
                for (Integer integer : expressionValue) {// faz os produtos (um em cada laço)
                    temp = temp & integer;
                }
                expressionValue.clear();
                product.add(temp);
            }

            int temp = product.get(0);
            for (Integer integer : product) { //faz a soma dos produtos
                temp = temp | integer;
            }
            product.clear();
            ttOutput.add(temp);
        }
        if(outIsInverted){
            ArrayList<Integer> ttOutputInverted = new ArrayList<>();
            for (Integer i : ttOutput) {
                if (i == 0)
                    ttOutputInverted.add(1);
                else if (i == 1)
                    ttOutputInverted.add(0);
            }
            return ttOutputInverted;
        }
        return ttOutput;
    }


    public void printTruthTable() {
        int aux = 0;
        for (int j = 0; j < linhas; j++) {
            for (int i = 0; i < colunas; i++) {
                System.out.print(truthTable[j][i] + " ");
            }
            System.out.print("out " + ttOutput.get(aux));
            aux++;
            System.out.println();
        }
    }
}
