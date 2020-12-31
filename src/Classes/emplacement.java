/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author vange
 */
public class emplacement {

    @Override
    public String toString() {
        return "("+x+";"+y+")";
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public emplacement(String x, String y) {
        this.x = x;
        this.y = y;
    }
    private String x;
    private String y;
    
    
}
