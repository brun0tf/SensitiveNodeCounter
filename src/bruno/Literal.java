package bruno;

import java.util.ArrayList;

public class Literal {
    private final ArrayList<Integer> value= new ArrayList<>();
    private char name;

    public Literal(char name) {
        this.name = name;
    }

    public int getValue(int position) {
        return this.value.get(position);
    }

    public void setValue(int value) {
        this.value.add(value);
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }
}
