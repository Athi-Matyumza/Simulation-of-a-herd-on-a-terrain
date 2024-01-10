// package src;


/*
 * SIMHERD7
 * Map data generated from file.
 * To create 2D arrays from file data to use for map drawing.
 */

// imports
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class HeightMaps {
    int[][] slopeColorMap, heightColorMap;
    float[][] heightMap, slopeMap;
    boolean loaded=false;

    float multiplier = 1; //Zoom multipler. Might not be needed

    int width, height;
    float maxLat, minLat;
    float spacing, latitude;
    
    // Constructor requiring filename input
    public HeightMaps(String filename){
        //Read file data
        LoadFile(filename);
    }

    public HeightMaps(){
        this.loaded=false;
    }

    public void LoadFile(String filename){
        long start = System.nanoTime();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            // Vars
            String dataLine = reader.readLine();
            this.width = Integer.parseInt(dataLine.split(" ")[0]);
            this.height = Integer.parseInt(dataLine.split(" ")[1]);
            this.spacing = Float.parseFloat(dataLine.split(" ")[2]);
            this.latitude = Float.parseFloat(dataLine.split(" ")[3]);
            this.maxLat=0;
            this.minLat=Float.POSITIVE_INFINITY;

            // Create sized arrays
            heightMap = new float[this.width][this.height];
            slopeMap = new float[this.width][this.height];
            // Color maps for visualization
            heightColorMap = new int[this.width][this.height];
            slopeColorMap = new int[this.width][this.height];

            // Fill heightmap
            String[] line;
            float maxSlope=0;
            for(int x=0; x<width; x++){
                line = reader.readLine().split(" ");
                for(int y=0; y<height; y++){
                    float val = Float.parseFloat(line[y+1]) * 0.3048f;
                    this.heightMap[x][y] = val;

                    // Check min and max Lat
                    this.maxLat = Math.max(val, this.maxLat);
                    this.minLat = Math.min(val, this.minLat);

                    
                    // Fill slope map
                    float tempMin = Float.POSITIVE_INFINITY;
                    float tempMax = 0;
                    int [] tempMinpos = new int[2];
                    int [] tempMaxpos = new int[2];

                    if(x == 0 ||  x==this.width - 1){
                        slopeMap[x][y] = 0;
                    }
                    else if(y==0 || y==this.height - 1){
                        slopeMap[x][y] = 0;
                    }
                    else{
                        for(int smalx=x-1; smalx<x+1; smalx++){
                            for(int smaly=y-1; smaly<y+1; smaly++){
                                if(heightMap[smalx][smaly] > tempMax){
                                    tempMax = heightMap[smalx][smaly];
                                    tempMaxpos[0] = smalx;
                                    tempMaxpos[1] = smaly;
                                }
                                if(heightMap[smalx][smaly] < tempMin){
                                    tempMin = heightMap[smalx][smaly];
                                    tempMinpos[0] = smalx;
                                    tempMinpos[1] = smaly;
                                }
                            }
                        }
                        slopeMap[x][y] = (tempMax - tempMin)/(this.spacing * (float)Math.hypot(Math.abs(tempMaxpos[0] - tempMinpos[0]), Math.abs(tempMaxpos[1] - tempMinpos[1])));
                    }
                    maxSlope = Math.max(slopeMap[x][y], maxSlope);
                }
            }
            // System.out.println("Maps generated");

            // Fill color maps
            for(int x=0; x<width; x++){
                for(int y=0; y<height; y++){
                    // Fill latitude color map
                    heightColorMap[x][y] = Math.round(255 - 255*( heightMap[x][y] - this.minLat) / (this.maxLat - this.minLat));

                    // Fill slope color map
                    slopeColorMap[x][y] = Math.round(255*( slopeMap[x][y]) / (maxSlope));
                    // System.out.println(slopeColorMap[x][y]);
                }
            }
            // System.out.println("Color Maps generated");
            reader.close(); //Close
        }
        catch(IOException e){
            System.out.println("BRUH NO FILE");
        }
        catch(Exception e){
            System.out.println("BRUH");
        }
        finally{
            this.loaded=true;
        }
        long end = System.nanoTime();   
        System.out.println("Map loaded. Elapsed Time: "+ (end-start)/1e9 + "s"); 
    }
    public int GetWidth(){
        return width;
    }
    public int GetHeight(){
        return height;
    }
    public int[][] GetHeightColors(){
        return heightColorMap;
    }
    public int GetHeightColor(int x, int y){
        return heightColorMap[x][y];
    }
    public float[][] GetHeightMap(){
        return heightMap;
    }
    public float[][] GetSlopeMap(){
        return slopeMap;
    }

    // Finds local points around point. returns 0 at point if not within range.
    public float[][] FindBestSlope(float[] location, float visualRange){
        int[] mapPoint = FindPoint(location);
        int range = FindPointDistance(visualRange);
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        float[][] visualPoints = new float[range][range];
        for(int x= Math.max(mapPoint[0]-range,0); x<=Math.max(mapPoint[0]+range,0); x++){
            for(int y=Math.max(mapPoint[1]-range,0); y<= Math.max(mapPoint[1]+range,0); y++){
                int[] currentPoint = {x,y};
                if(GetDistance(mapPoint,currentPoint)<=visualRange){
                    // Check viability

                    visualPoints[x][y] = slopeMap[x][y];

                    // Print boid sight
                    // if(slopeMap[x][y] <=0.5f){
                    //     System.out.printf("%13.13s", GREEN + Float.toString(slopeMap[x][y]).substring(0, 4) + RESET);
                    // }
                    // else if(slopeMap[x][y] <=1f){
                    //     System.out.printf("%13.13s", YELLOW + Float.toString(slopeMap[x][y]).substring(0, 4) + RESET);
                    // }
                    // else{
                    //     System.out.printf("%13.13s", RED + Float.toString(slopeMap[x][y]).substring(0, 4) + RESET);
                    // }

                    // System.out.printf("%4.4s", slopeMap[x][y]);
                    // System.out.print(" ");
                }
                else{
                    visualPoints[x][y] = 0;
                    // System.out.printf("%4.4s", "");
                    // System.out.print(" ");
                }
                
            }
            System.out.println("");
        }
        return visualPoints;
    }

    // Find relative point from locaiton
    private int[] FindPoint(float[] location){
        int[] point = {FindPointDistance(location[0]),FindPointDistance(location[1])};
        return(point);
    }
    // Convert distance in meters to distance in points
    private int FindPointDistance(float distance){
        return(Math.round(distance/spacing));
    }
    // Convert distance between pixels to distance in meters
    private float GetDistance(int[] start, int[] end){
        return (float)Math.hypot((start[0]-end[0])*spacing, (start[1]-end[1])*spacing);
    }
    // Convert distance between pixels to distance in meters
    private float GetDistance(float[] start, float[] end){
        return (float)Math.hypot(start[0]-end[0], start[1]-end[1]);
    }


    // Test main
    // public static void main(String[] args){
    //     HeightMaps heightMaps = new HeightMaps("unity/Visualizer/resources/large1.elv");
    //     float[] test = {93f,100f};
    //     heightMaps.FindBestSlope(test, 10);
    //     // System.out.println();
    // }
}
         
      