package plotter;

import java.io.IOException;
import java.util.ArrayList;

public class SilhouettePlot {
    
    private ArrayList<Double> distancesValues;
    private ArrayList<ArrayList<Integer>>  hierarchy;
    private double minDistanceValue;

    //author Fernando S. de Aguiar Neto.
    
    public SilhouettePlot(ArrayList<Double> eps, ArrayList<ArrayList<Integer>> h) throws IOException
    {        
        hierarchy = h;
        distancesValues = eps;
        this.discoverMinDistValue();
        //DEBUG
        //this.printStructures();
    }
    
    public double getMinDistanceValue()
    {
        return this.minDistanceValue;
    }
    
    private void discoverMinDistValue()
    {
        this.minDistanceValue = -1;
        for(double d : this.distancesValues)
        {
            this.minDistanceValue = Math.min(this.minDistanceValue, d);
        }
    }
    
    public void printStructures()
    {
        System.out.println("Densities");
        System.out.println(this.distancesValues.toString());
        
        System.out.println();
        System.out.println("H Matrix");
        for(int i=0 ; i< this.hierarchy.size() ; i++ )
        {
            System.out.println(this.hierarchy.get(i).toString());
        }
    }  
    
    public ArrayList<ObjectSilhouetteInfo> getDensities() {
        
        ArrayList<ObjectSilhouetteInfo> silhouetteInfo = 
                                        new ArrayList<ObjectSilhouetteInfo>();
        
        int currentClusterLabel, previousClusterLabel;
        int count;
        double previousDensity;
        
        
        for(int i = 0; i < hierarchy.size(); ++i) {
            
            //remembering I want ignore the first line (Obj ID)
            //also remember that densities.get(n-1) represents the n-esimal level. 
            count = 1;
            previousClusterLabel = hierarchy.get(i).get(count);
            
            previousDensity = 0.0;
            
            ObjectSilhouetteInfo object = new ObjectSilhouetteInfo();
            
            while(previousClusterLabel != 0) {
                
                count++;
                currentClusterLabel = hierarchy.get(i).get(count);
                
                if(previousClusterLabel != currentClusterLabel) {
                    double densities[] = {previousDensity, 1/distancesValues.get(count-1-1)};
                    int colorId = previousClusterLabel;
                    object.addInfo(colorId, densities);
                    previousDensity = 1/distancesValues.get(count-1-1);
                }
                
                previousClusterLabel = currentClusterLabel;
                
            }
            
            silhouetteInfo.add(object);
            
        }
        
        return silhouetteInfo;
        
    }
    
    
    
}
