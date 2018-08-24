package plotter;

import java.util.ArrayList;

public class ReachabilityPlot {
    
    private ArrayList<Double> distancesValues;
    private ArrayList<ArrayList<Integer>>  hierarchy;
    private static final int NOISE = 0;
    
    public ReachabilityPlot(ArrayList<Double> eps, ArrayList<ArrayList<Integer>> h)
    {
        hierarchy = h;
        distancesValues = eps;
//        printStructures();
    }
    
public void printStructures()
    {
        System.out.println("Densities");
        System.out.println(this.distancesValues.toString());
        
        System.out.println();
        System.out.println("H Matrix");
        for(int i=0 ; i<this.hierarchy.size() ; i++ )
        {
            System.out.println(this.hierarchy.get(i).toString());
        }
    }      
    
    public double[] getReachabilityDistances()
    {
        //rDist.size() == the number of Objects.
        double[] rDist = new double[hierarchy.size()];
        
        rDist[0] = distancesValues.get(0).doubleValue(); //the first bar is always big (unreachable).
        for(int i = 1; i < hierarchy.size(); ++i) 
        { 
            rDist[i] = distancesValues.get(0).doubleValue(); //if it doesnt receive any other reachability value it will be the highest (unreachable)
            
            //notice that I dont want to compare the the ObjIDs
            //also, the last line is full of noise (0's)
            for(int j = hierarchy.get(0).size() - 2; j >= 1; --j) 
            {
                if((hierarchy.get(i).get(j).intValue() != NOISE) && (hierarchy.get(i).get(j).intValue() == hierarchy.get(i-1).get(j).intValue()) ) 
                {
                    //remembering that the hierarchy matrix has one more line than the distanceValues vector.
                    rDist[i] = distancesValues.get(j-1).doubleValue();
                    break;
                }
            }
        }
        
        return rDist;
    }
   

}