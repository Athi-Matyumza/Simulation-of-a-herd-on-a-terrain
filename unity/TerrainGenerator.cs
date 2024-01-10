using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using System.IO;
using System.Drawing;


public class TerrainGenerator : MonoBehaviour
{

    // Variables
    private int depth;
    public bool perlinNoise = false;
    private int width, height;
    private float latitude, spacing;
    private float max, min;
    
    
    private float scale = 20f;
    private float offsetX = 100f;
    private float offsetY = 100f;
    
    // public float scale = 20f;

    // Map Variable
    private float[,] heightMap;

    public string terrainFilename = @"resources\large1.elv";
    // Start is called before the first frame update
    void Start()
    {
        readFile();
        Terrain terrain =  GetComponent<Terrain>();
        terrain.terrainData = GenerateTerrain(terrain.terrainData);
        // terrain.transform.position = new Vector3(-width*spacing/2, 0, -height*spacing/2);
    }

    void Update() {
        
    }

    TerrainData GenerateTerrain(TerrainData terrainData)
    {
        terrainData.heightmapResolution = width+1;
        terrainData.size = new Vector3 (width, depth, height);
        terrainData.SetHeights(0, 0, heightMap);
        // terrainData.transform.position = new Vector3(width/2, height/2, height/2);
        return terrainData;
    }

    void readFile(){
        string[] line;
        try
        {
            //Pass the file path and file name to the StreamReader constructor
            StreamReader sr = new StreamReader(terrainFilename);
            //Read the first line of text
            string[] firstLine = sr.ReadLine().Split(" ");
            width = Int32.Parse(firstLine[0]);
            height = Int32.Parse(firstLine[1]);

            spacing = float.Parse(firstLine[2], System.Globalization.CultureInfo.InvariantCulture);
            latitude = float.Parse(firstLine[3], System.Globalization.CultureInfo.InvariantCulture);


            heightMap = new float[width,height];
            //Continue to read until you reach end of file

            if(perlinNoise){
                depth=20;
                for(int x=0; x<width; x++)
                {
                    line = sr.ReadLine().Split(" ");
                    for(int y=0; y<height; y++)
                    {
                        heightMap[x,y] = CalculateHeight(x,y);
                        
                        min = Math.Min(heightMap[x,y], min);
                        max = Math.Max(heightMap[x,y], max);
                    }
                }
            }
            else{
                for(int x=0; x<width; x++)
                {
                    line = sr.ReadLine().Split(" ");
                    for(int y=0; y<height; y++)
                    {
                        heightMap[x,y] = float.Parse(line[y+1], System.Globalization.CultureInfo.InvariantCulture) * 0.3048f;

                        min = Math.Min(heightMap[x,y], min);
                        max = Math.Max(heightMap[x,y], max);
                    }
                }
                depth = (int)(max-min);
                for(int x=0; x<width; x++)
                {
                    for(int y=0; y<height; y++)
                    {
                        heightMap[x,y] = ((heightMap[x,y] - min)/(depth));
                    }
                }
            }
            
            //close the file
            sr.Close();
            // Console.ReadLine();
            
        }
        catch(Exception e)
        {
            Debug.Log("Exception: " + e.Message);
            Debug.Log(e);
        }
        finally
        {
            Debug.Log("Executing finally block.");
            Debug.Log(width+ "  "+ height);
        }
    }


    
    // Perlin noise generator
     float CalculateHeight (int x, int y)
    {
        float xCoord = (float)x / width * scale + offsetX;
        float yCoord = (float)y / height * scale + offsetY;

        return Mathf.PerlinNoise(xCoord, yCoord);
    }
}
