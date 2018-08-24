package plotter;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antonio
 */
public class ObjectSilhouetteInfo {
    
    private ArrayList<Integer> colorsId;
    private ArrayList<double[]> densities;
        
    public ObjectSilhouetteInfo() {
        this.colorsId = new ArrayList<Integer>();
        this.densities = new ArrayList();
    }

    public void addInfo(int colorId, double[] density) {
        this.colorsId.add(new Integer(colorId));
        this.densities.add(density);
    }
    
    public ArrayList<Integer> getColorsId() {
        return this.colorsId;
    }
    
    public ArrayList<double[]> getDensities() {
        return this.densities;
    }
    
}
