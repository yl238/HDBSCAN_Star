/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Fernando Soares de Aguiar Neto
 */
public class Plotter extends javax.swing.JFrame {

    /**
     * Creates new form Plotter
     */
    
    private final String L_NO_FILE_LOADED = "None";
    public static String dir;
    
    //Hierarchy Matrix. See Strutures.txt
    protected ArrayList<Double> densities;              //Density Vector
    protected ArrayList<ArrayList<Integer>> matrix;     //Matrix with objectIds and clusterIDs

    protected Color[] colors; //color scheme
    
    protected Integer maxCluster;
    protected Integer levels;
    protected Double rShave;
    
    public Plotter() {
        initComponents();
        this.dir  = ".";
        this.matrix = new ArrayList<ArrayList<Integer>>();
        this.densities = new ArrayList<Double>();
        this.maxCluster = -1;
        this.rShave = -1.0;
    }
    
    public Plotter(String dir) {
        initComponents();
        this.dir = dir;
        this.t_filepath.setText(dir);
        this.matrix = new ArrayList<ArrayList<Integer>>();
        this.densities = new ArrayList<Double>();
        this.maxCluster = -1;        
        this.rShave = -1.0;
    }
    
    public Double getRShave()
    {
        return this.rShave;
    }
    
    public void printStructures()
    {
        System.out.println("Densities");
        System.out.println(this.densities.toString());
        
        System.out.println();
        System.out.println("H Matrix");
        for(int i=0 ; i<this.matrix.size() ; i++ )
        {
            System.out.println(this.matrix.get(i).toString());
        }
    }
    
    public int loadVisualization(FileReader fr)
    {
        try(BufferedReader br = new BufferedReader(fr);)
        {
            //firstLine is always composed entirelly of ClusterID 1
            Integer flag = Integer.parseInt(br.readLine());
            if(flag == 1)
            {
                this.levels = Integer.parseInt(br.readLine());
            }
            else
            {
                this.l_filename.setText(this.L_NO_FILE_LOADED);
                JOptionPane.showMessageDialog(this, "You must use the complete hierarchy in order to generate the graphics. Plase run HDBSCAN* setting compact=false");
                return 0;
            }
            
            return 1;
        }
        catch(IOException ioe)
        {
            this.l_filename.setText(this.L_NO_FILE_LOADED);
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please be sure that the structure of the file is correct, it must have two lines, the first one is a flag (0 or 1) and the second line is a integer number.\n"
                    + "For Example; \t\t\t1\n\t\t\t256");
            return 0;
        }
        catch(Exception e)
        {
            this.l_filename.setText(this.L_NO_FILE_LOADED);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Couldn't read the visualization file. Please be sure that the file exists and has the _visualization.vis sufix");
            return 0;
        }
    }
    
    public int loadFile(FileReader fr)
	{
		String line="";

		try(BufferedReader br = new BufferedReader(fr);)
		{
                    //this assures that even if the user loads many files, only the struture of the last one will be stored.
                    this.matrix.clear();
                    this.densities.clear();

                    //First read
                    //The first read generates the arrays of collumns, also adds the ObjID.
                    if((line = br.readLine()) != null)
                    {
                        
                        String[] parts = line.split(",");

                        //First column is always density
                        densities.add(Double.parseDouble(parts[0]));
                        
                        //On the first line there are only clusterID 1, so I am sure that for now the max Cluster size is 1.
                        this.maxCluster = 1;
                        
                        //For other each object, generates a new ArrayList<Integer> and adds ObjID and ClusterID.
                        for(int i = 1; i < parts.length; i++)
                        {
                            ArrayList<Integer> newCollumn = new ArrayList<Integer>();
                            newCollumn.add(i-1); //adding Obj ID.

                            int clusterID = Integer.parseInt(parts[i]);

                            newCollumn.add(clusterID);

                            matrix.add(newCollumn);
                        }  
                    }

                    //Reading subsequent lines.
                    while((line = br.readLine()) != null)
                    {
                        String[] parts = line.split(",");

                        densities.add(Double.parseDouble(parts[0]));
                        
                        //adds ClusterID to the respective ArrayList, also updates maxClusterID if needed.
                        for(int i = 1; i < parts.length; i++)
                        {
                            int clusterID = Integer.parseInt(parts[i]);
                            if(this.maxCluster <= clusterID)
                            {
                                this.maxCluster = clusterID;
                            }

                            this.matrix.get(i-1).add(clusterID);
                        }  
                    }
                    
                    //FOR DEBUG
                    //this.printStructures();
                
                }
                catch(OutOfMemoryError m)
                {
                    this.clearStructures();
                    //this.l_filename.setText(this.L_NO_FILE_LOADED);
                    //                                                                                                                     this -1 below excludes the line with the objID.
                    JOptionPane.showMessageDialog(this, "Your computer run out of memory, please try to use the shaving load, the number of lines read was "+(this.matrix.size()-1));
            
                        //failure
			return 0;
                }
		catch(Exception e)
		{
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error when reading the hierarchy matrix. Verify the structure, each line must be on the following format: '<density(decimal using period)> , <many integers split by a coma>\\n"
                            + "                           \nExample: 4.458,4,5,5,0,0,2,2,2"
                            + "                           \n         2.152,4,6,7,0,0,8,8,9");
            
                        //failure
			return 0;
		}
		
		//success
		return 1;
		
	}

    public int loadFileShaving(FileReader fr, double rShave)
	{
            String line="";
            int n = this.levels;
            int skip = Math.max(1,(int)Math.floor(n*(rShave)));

            try(BufferedReader br = new BufferedReader(fr);)
            {
                this.matrix.clear();
                this.densities.clear();

                //First read
                //The first read generates the arrays of collumns, also adds the ObjID.

                //firstRead sets the objIDs
                if((line = br.readLine()) != null)
                {
                    String[] parts = line.split(",");

                    //On the first line there are only clusterID 1, so I am sure that for now the max Cluster size is 1.
                    this.maxCluster = 1;

                    densities.add(Double.parseDouble(parts[0]));
                    //For other each object, generates a new ArrayList<Integer> and adds ObjID.
                    for(int i = 1; i < parts.length; i++)
                    {
                        ArrayList<Integer> newCollumn = new ArrayList<Integer>();
                        newCollumn.add(i-1); //adding Obj ID.
                        newCollumn.add(1); //firstLine is full of 1's
                        matrix.add(newCollumn);
                    }

                    while(n >= 1)
                    {
                        //skipping to the line we want to read
                        for(int i = 0; i<skip; i++)
                        {
                            line = br.readLine();
                            
                        }

                        if(line == null)
                        {
                            break;
                        }
                        
                        //spliting lines.
                        parts = line.split(",");
                        //adding density
                        densities.add(Double.parseDouble(parts[0]));

                        //DEBUG
                        //System.out.println(line);


                        //adds ClusterID to the respective ArrayList, also updates maxClusterID if needed.
                        for(int i = 1; i < parts.length; i++)
                        {
                            int clusterID = Integer.parseInt(parts[i]);
                            if(this.maxCluster <= clusterID)
                            {
                                this.maxCluster = clusterID;
                            }

                            this.matrix.get(i-1).add(clusterID);
                        }

                        n = n-skip;
                        skip = Math.max(1,(int)Math.floor(n*(rShave)));
                    }

                    //if the line full of zeroes wasnt read, generate it.
                    if(Double.parseDouble(parts[0]) != 0.0)
                    {
                        densities.add(0.0);
                        for(int i = 1; i < parts.length; i++)
                        {
                            this.matrix.get(i-1).add(0);
                        }
                    }
                }

            }
            catch(OutOfMemoryError m)
            {
                this.clearStructures();
                //this.l_filename.setText(this.L_NO_FILE_LOADED);
                //                                                                                                                     this -1 below excludes the line with the objID.
                JOptionPane.showMessageDialog(this, "Your computer run out of memory, please try to use the shaving load, the number of lines read was "+(this.matrix.size()-1));

                    //failure
                    return 0;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("Skip="+skip+"|n="+n+"this.matrix.size()="+this.matrix.size());
                //this.l_filename.setText(this.L_NO_FILE_LOADED);
                JOptionPane.showMessageDialog(this, "Error when reading the hierarchy matrix. Verify the structure, each line must be on the following format: '<density(decimal using period)> , <many integers split by a coma>\\n"
                        + "                           \nExample: 4.458,4,5,5,0,0,2,2,2"
                        + "                           \n         2.152,4,6,7,0,0,8,8,9");

                    //failure
                    return 0;
            }

           
            //success
            return 1;
		
	}
    
        //Loads _tree.csv file and generates the color scheme.
        public void loadTreeFile(FileReader fr)
        {
            //Some information and also an example can be found in ColorAlg.txt. And also on ColorSolution.ods
            final int BORN =  1;
            final int DEATH = 2; 
            
            ArrayList<Double> colorAUX = new ArrayList<Double>(); //responsible for the time-to-live values of the colors
            int[] colorRNA = new int[this.maxCluster+1];          //responsible by assign the colorID to a cluster. The +1 is the noise value.
             
            String line="";

            try(BufferedReader br = new BufferedReader(fr);)
            {
                //firstLine is always composed entirelly of ClusterID 1
                line = br.readLine();
                String[] parts = line.split(",");
                
                colorAUX.add(Double.parseDouble(parts[DEATH]));
                //colorRNA[0] is noise, so we start from index 1.
                colorRNA[1] = 0;
                
                //the file has one line for each clusterID counting from 1
                for(int i = 2; i <= this.maxCluster; i++)
                {
                    line = br.readLine();
                    parts = line.split(",");
                    
                    double bornVal = Double.parseDouble(parts[BORN]);
                    double deathVal = Double.parseDouble(parts[DEATH]);
                   
                    colorRNA[i] = -1;
                    //Given that exists a color to be reutilized, searches for the first one that can be reutilized.
                    for(int j = 0; j < colorAUX.size(); j++)
                    {
                           if(colorAUX.get(j) > bornVal)
                           {
                               colorAUX.set(j, deathVal);
                               colorRNA[i] = j;
                               break;
                           }    
                    }
                    
                    //If no color could be reutilized. Assigns to a new color.
                    if(colorRNA[i] == -1)
                    {
                        colorAUX.add(deathVal);
                        colorRNA[i] = colorAUX.size()-1;
                    }
                    
                }
                
                //converting integers to actual colors.
                
                //0 is always noise and always white.
                this.colors[0] = (new Color(255,255,255));
                //dividing the space between the colors
                double step = (double)1/(colorAUX.size()+1);
                
                for(int i = 1 ; i < colorRNA.length; i++)
                {
                    this.colors[i] = (WaveLenght.toRGB(step*colorRNA[i] + 0.5));
                }
                
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            //DEBUG
            //this.printStructures();

        }
    
        //Responsible for generating the window for file choosing and calling the functions that will read the important files into memory.
	public int chooseFile(boolean shaveLoad, Plotter parent)
	{
                ArrayList<Double> c = new ArrayList<Double>();
		
                
                
                //LOADING VISUALIZATION FILE
                
                try(FileReader reader = new FileReader(new File(this.t_filepath.getText())))
                {
                   //If the file couldn't be read
                   if((this.loadVisualization(reader)) == 0)
                   {
                       //stops file reading.
                       //The other measures, like this.l_filename.setText(this.L_NO_FILE_LOADED); and showing mesages will be handled by loadVisualization().
                       return 0;
                   }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "Couldn't read the visualization file. Please be sure that the file exists and has the _visualization.vis sufix");
                    return 0;
                }
                
                //LOADING HIERARCHY
                String[] HierarchyFilenameSplit = this.t_filepath.getText().split("_");

                //changing the sufix _visualization.vis for a _hierarchy.csv
                String HierarchyFilename = "";
                for(int i =0 ; i<HierarchyFilenameSplit.length-1;i++)
                {
                    HierarchyFilename = HierarchyFilename+HierarchyFilenameSplit[i]+"_";
                }
                HierarchyFilename = HierarchyFilename+"hierarchy.csv";

                try(FileReader reader = new FileReader(new File(HierarchyFilename));)
                {
                        int ret;
                        if(shaveLoad)
                        {
                            ret = this.loadFileShaving(reader, this.rShave);
                        }
                        else
                        {
                            ret = this.loadFile(reader);
                        }
                        
                        if (ret == 0)
                        {
                            return 0;
                        }
                        //Ordering Hierarchy.      Since every plotter uses the sorted matrix, I will sort it only once.
                        //Notice that the first line is not ordered, because this line shows the ObjID.
                        for(int i = this.matrix.get(0).size()-1; i >= 1; --i) {
                            final int k = i;
                            Collections.sort(this.matrix, new Comparator<ArrayList<Integer>>() {

                                @Override
                                public int compare(ArrayList<Integer> t1, ArrayList<Integer> t2) {
                                    return Integer.compare(t2.get(k), t1.get(k));
                                }
                            });
                        }
                        
                        JOptionPane.showMessageDialog(this, "Hierarchy matrix was successfully read!");
                }
                catch(Exception e)
                {
                        //this.l_filename.setText(this.L_NO_FILE_LOADED);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parent, "Couldn't read the hierarchy matrix file. Please be sure that the file exists and has the _hierarchy.csv sufix");
                        return 0;
                }

                //Colorize the shaved hierarchy might cause some trouble, namely the same colour can be atributed to a clusterID and to the one right above that cluster.
                if(!shaveLoad)
                {
                    //LOADING TREE (used only for color scheme.)
                    String[] treeFilenameSplit = this.t_filepath.getText().split("_");

                    //changing the sufix  _hierarchy.csv for a _tree.csv
                    String treeFilename = "";
                    for(int i =0 ; i<treeFilenameSplit.length-1;i++)
                    {
                        treeFilename = treeFilename+treeFilenameSplit[i]+"_";
                    }
                    treeFilename = treeFilename+"tree.csv";
                    //DEBUG
                    //System.out.println(treeFilename);

                    try(FileReader fr = new FileReader(new File(treeFilename))){

                        //the color scheme is reset, in order to garantee that if any problem happen there will be no old value.
                        this.colors = new Color[this.maxCluster+1];
                        this.loadTreeFile(fr);
                    }
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(parent, "Couldn't read the _tree.csv file. Colour scheme will be default.");
                        this.createDefaultColorScale();
                        e.printStackTrace();
                    }
                }
                else
                {
                    this.createDefaultColorScale();
                }
                
                return 1;

	}

    //This is the default colorScale it uses many colors (one color for two clusters). But is used only if the _tree.csv file was not read.
    public void createDefaultColorScale()
    {
        int qtdColors = (int)Math.ceil(this.maxCluster/2);
        this.colors = new Color[qtdColors+1];
        //Color of index 0 is aways noise, and aways white.
        this.colors[0] = (new Color(255,255,255));
        
        double step = (double)1/(qtdColors);
        
        for(int i = 1; i <= qtdColors; i++)
        {
            this.colors[i] = (WaveLenght.toRGB(step*i + 0.5));
        }
    }    
        
    public void clearStructures()
    {
        this.matrix.clear();
        this.densities.clear();
    }
    
    public Color[] getColors()
    {
        return this.colors;
    }
    
    public ArrayList<Double> getDensities()
    {
        return this.densities;
    }
    
    public ArrayList<ArrayList<Integer>> getMatrix()
    {
        return this.matrix;
    }
    
    public int getMaxCluster()
    {
        return this.maxCluster;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg_Load = new javax.swing.ButtonGroup();
        p_Loading = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        p_top = new javax.swing.JPanel();
        b_openFile = new javax.swing.JButton();
        b_Browse = new javax.swing.JButton();
        t_filepath = new javax.swing.JTextField();
        l_filename = new javax.swing.JLabel();
        l_help = new javax.swing.JLabel();
        rb_Normal = new javax.swing.JRadioButton();
        rb_Shaved = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        p_Loading.setMaximumSize(new java.awt.Dimension(550, 200));
        p_Loading.setMinimumSize(new java.awt.Dimension(550, 200));
        p_Loading.setPreferredSize(new java.awt.Dimension(550, 200));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/482.GIF"))); // NOI18N
        jLabel3.setText("\"Loading Hierarchy, it may take a while, please wait...\"");

        javax.swing.GroupLayout p_LoadingLayout = new javax.swing.GroupLayout(p_Loading);
        p_Loading.setLayout(p_LoadingLayout);
        p_LoadingLayout.setHorizontalGroup(
            p_LoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
            .addGroup(p_LoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(p_LoadingLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        p_LoadingLayout.setVerticalGroup(
            p_LoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
            .addGroup(p_LoadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(p_LoadingLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Plotter");
        setMinimumSize(new java.awt.Dimension(340, 175));
        setName("Plotter"); // NOI18N
        setResizable(false);

        javax.swing.GroupLayout p_topLayout = new javax.swing.GroupLayout(p_top);
        p_top.setLayout(p_topLayout);
        p_topLayout.setHorizontalGroup(
            p_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
        );
        p_topLayout.setVerticalGroup(
            p_topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        b_openFile.setMnemonic('l');
        b_openFile.setText("Load");
        b_openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_openFileActionPerformed(evt);
            }
        });

        b_Browse.setText("Browse..");
        b_Browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_BrowseActionPerformed(evt);
            }
        });

        t_filepath.setMaximumSize(new java.awt.Dimension(40, 30));
        t_filepath.setMinimumSize(new java.awt.Dimension(10, 30));
        t_filepath.setPreferredSize(new java.awt.Dimension(10, 30));

        l_filename.setText("Load file (.vis)");
        l_filename.setToolTipText("");

        l_help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/Info.png"))); // NOI18N
        l_help.setText("Help");
        l_help.setToolTipText("More Information");
        l_help.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        l_help.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 1, 1));
        l_help.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_helpMouseClicked(evt);
            }
        });

        bg_Load.add(rb_Normal);
        rb_Normal.setSelected(true);
        rb_Normal.setText("Normal");
        rb_Normal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_NormalActionPerformed(evt);
            }
        });

        bg_Load.add(rb_Shaved);
        rb_Shaved.setText("Shaved");

        jLabel1.setText("Choose load method:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(b_openFile))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(rb_Normal)
                        .addGap(18, 18, 18)
                        .addComponent(rb_Shaved))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(l_filename)
                            .addGap(18, 18, 18)
                            .addComponent(p_top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(l_help))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(t_filepath, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(b_Browse))))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(p_top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l_filename))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b_Browse)
                            .addComponent(t_filepath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(b_openFile))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rb_Normal)
                                    .addComponent(rb_Shaved))
                                .addGap(0, 54, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(l_help)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_openFileActionPerformed
        
        int extIdx = t_filepath.getText().lastIndexOf('.');
        if(extIdx == -1)
        {
            JOptionPane.showMessageDialog(this, "Please be sure to be reading a .vis file");
            return;
        }
        else
        {
            String extension = t_filepath.getText().substring(extIdx);
            if(extension.equals(".vis"))
            {
                this.setVisible(false);
                
                if(this.rb_Shaved.isSelected())
                {
                    this.rShave = -1.0;
                
                    boolean ok = false;
                    
                    while(!ok)
                    {
                        try
                        {
                            String ret = JOptionPane.showInputDialog(this, "Percentage to be shaved in each step (0 < rShave < 100):" ,"Shaving" , JOptionPane.QUESTION_MESSAGE);
                            
                            if(ret == null)
                            {
                                this.setVisible(true);
                                return;
                            }
                            
                            this.rShave = (double)Double.parseDouble(ret);
                            if(this.rShave<=0 || this.rShave >=100)
                            {
                                JOptionPane.showMessageDialog(this, "Please be assure that your input is a number greater than 0 and smaller than 100");
                            }
                            else
                            {
                                this.rShave = (double)this.rShave/100.0;
                                ok = true;
                            }
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(this, "Please be assure that your input is a number greater than 0 and smaller than 100");
                        }
                    }
                }
        
                
                //JOptionPane.showOptionDialog(this, "Reading hierarchy matrix, it may take a while, please wait..", "Loading Matrix", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("C:\\Users\\fsan\\Desktop\\482.GIF"), new Object[]{} ,null);
                //Creating LoadingDialog
                JOptionPane pane = new JOptionPane();
                JDialog dialog = pane.createDialog("Loading...");
                pane.removeAll();    
                           
                dialog.addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        e.consume();
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        e.consume();
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        e.consume();
                    }
                   });                                       
                
                Plotter2 plotter2 = new Plotter2(this.rb_Normal.isSelected(), this, t_filepath.getText());
                try
                {
                    File f = new File(t_filepath.getText());
                    plotter2.setFileLabel(f.getName());
                }
                catch(Exception e)
                {
                    plotter2.setFileLabel(t_filepath.getText());
                }
                dialog.setMinimumSize(new Dimension(550,200));
                dialog.setPreferredSize(new Dimension(550,200));
                dialog.setMaximumSize(new Dimension(550,200));
                
                dialog.setContentPane(p_Loading);
                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                
                //Showing Dialog meanwhile the file is loaded.
                Plotter PlotterPtr = this;
                
               
                SwingWorker<?,?> worker = new SwingWorker<Object,Object>(){

                    @Override
                    protected Object doInBackground() throws Exception {
                        int fileRead = chooseFile(rb_Shaved.isSelected(), PlotterPtr);

                        if(fileRead == 1)
                        {
                            //showing next window
                            plotter2.enableAllButtons();

                            plotter2.setVisible(true);
                            plotter2.requestFocus();
                        }
                        else
                        {
                            plotter2.setVisible(false);
                            PlotterPtr.setVisible(true);
                            PlotterPtr.requestFocus();
                            
                        }
                        return null;
                    }

                    @Override
                    protected void done()
                    {
                        dialog.setVisible(false);
                    }

                };

                worker.execute();

                dialog.setVisible(true);


                //waiting end of the loading
                while(dialog.isVisible())
                {
                    try
                    {
                        Thread.sleep(1000);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                //printStructures();
                               
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Couldn't read the visualization file. Please be sure that the file exists and has the _visualization.vis sufix");
            }
        }
        
        
        
        
    }//GEN-LAST:event_b_openFileActionPerformed

    private void l_helpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_helpMouseClicked
        JOptionPane.showMessageDialog(this, "Select a .vis file to be loaded, then click load to load a hierarchy file. You also may choose between normal and shaved options.\n"+
                "The normal one loads the entire matrix into memory.\n" +
                "The shaved option will shave (remove) some lines of the hierarchy matrix in order to have a compacted version of the matrix.\n"+
                "For more information about shaved load refer to: \n"+
                "GUPTA G.; LIU, A.; GHOSH, J.; Automated Hierarchical Density Shaving: A Robust Automated Clustering and Visualization Framework for Large Biological Data Sets.\n"
                +"IEEE/ACM Transactions on Computational Biology and Bioinformatics. 2010.\n"
                + "DOI: http://dx.doi.org/10.1109/TCBB.2008.32");
    }//GEN-LAST:event_l_helpMouseClicked

    private void b_BrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_BrowseActionPerformed
        JFileChooser fc = new JFileChooser(t_filepath.getText());
        FileFilter filter = new FileNameExtensionFilter("HDBScan Visualization File","vis");
        fc.addChoosableFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        int ret = fc.showOpenDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                t_filepath.setText(fc.getSelectedFile().getCanonicalPath());
        
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_b_BrowseActionPerformed

    private void rb_NormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_NormalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_NormalActionPerformed

    /**
     * @param args the command line arguments
     * 
     *  args[0] Directory used for the fileChooser. if null the javaProject directory will be used.
     */
    public static void main(String args[]) {
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Plotter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            if(args.length != 0)
            {
                new Plotter(args[0]).setVisible(true);
            }
            else
            {
                new Plotter().setVisible(true);
            }
        });
    }

    public String getTFilepathText()
    {
        return this.t_filepath.getText();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_Browse;
    private javax.swing.JButton b_openFile;
    private javax.swing.ButtonGroup bg_Load;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel l_filename;
    private javax.swing.JLabel l_help;
    private javax.swing.JPanel p_Loading;
    private javax.swing.JPanel p_top;
    public javax.swing.JRadioButton rb_Normal;
    public javax.swing.JRadioButton rb_Shaved;
    private javax.swing.JTextField t_filepath;
    // End of variables declaration//GEN-END:variables

}
