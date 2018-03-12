package Items;


import java.io.Serializable;

public class Order implements Serializable {

    String name;
    String preisgeld;
    String position;

    public Order(){

    }

    public Order(String[] order){
        position = order[0];
        name = order[1];
        preisgeld = order[2];
    }

    public Order(String position, String name, String preisgeld){
        this.position = position;
        this.name = name;
        this.preisgeld = preisgeld;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getPreisgeld() {
        return preisgeld;
    }
}
