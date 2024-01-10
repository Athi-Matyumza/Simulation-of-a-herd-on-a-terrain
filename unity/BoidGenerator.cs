using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Drawing;
using System;
using UnityEngine;

public class BoidGenerator : MonoBehaviour
{

    public string pathName = @"resources\Data\";

    private bool deadlink=false;
    public GameObject spawnee;
    public Terrain terrain;
    public int framerate = 15;
    private GameObject[] boids;
    public int num=1;
    private int skips=0;
    // Start is called before the first frame update
    void Start()
    {
        Application.targetFrameRate = framerate;
        bool initFound=false;
        while(!deadlink && !initFound){
            try{
                StreamReader sr = new StreamReader(pathName+"boid_positions_"+num+".txt");
                string[] firstLine = sr.ReadLine().Split(" ");
                boids = new GameObject[int.Parse(firstLine[0])];
                for(int x=0; x<boids.Length; x++){
                    string[] line = sr.ReadLine().Split(",");
                    boids[x] = GameObject.Instantiate(spawnee);
                    boids[x].transform.position = new Vector3(float.Parse(line[0],System.Globalization.CultureInfo.InvariantCulture),500,float.Parse(line[1],System.Globalization.CultureInfo.InvariantCulture));
                    Vector3 pos = boids[x].transform.position;
                    pos.y = terrain.SampleHeight(boids[x].transform.position)+ 1.5f;
                    boids[x].transform.position =  pos;
                }
                
                initFound=true;
                skips=0;
            }
            catch(Exception e)
            {
                Debug.Log("Exception: " + e.Message);
                Debug.Log(e);
                skips++;
                if(skips>=150){
                    setDead(true);
                }
            }
            finally
            {
                Debug.Log(num);
                num++;
            }
        }
        
    }

    // Update is called once per frame
    void Update()
    {
        if(!deadlink){
            try{
                StreamReader sr = new StreamReader(pathName+"boid_positions_"+num+".txt");
                string[] firstLine = sr.ReadLine().Split(" ");
                if(int.Parse(firstLine[0]) > boids.Length){
                    GameObject[] newArr = new GameObject[int.Parse(firstLine[0])];
                    for(int x=0; x<boids.Length; x++){
                        newArr[x] = boids[x];
                    }
                    for(int i = boids.Length; i<int.Parse(firstLine[0]); i++){
                        newArr[i] = GameObject.Instantiate(spawnee);
                    }
                    boids = newArr;
                }
                for(int x=0; x<boids.Length; x++){
                    string[] line = sr.ReadLine().Split(",");
                    boids[x].transform.position = new Vector3(
                        float.Parse(line[0],System.Globalization.CultureInfo.InvariantCulture),
                        500,
                        float.Parse(line[1],System.Globalization.CultureInfo.InvariantCulture)
                    );
                    Vector3 pos = boids[x].transform.position;
                    pos.y = terrain.SampleHeight(boids[x].transform.position) + 1.5f;
                    boids[x].transform.position =  pos;
                }
                skips=0;
            }
            catch(Exception e)
            {
                // Debug.Log("Exception: " + e.Message);
                // Debug.Log(e);
                skips++;
                if(skips>=10){
                    setDead(true);
                }
            }
            finally
            {
                num++;
            }
        }
        else{
            Debug.Log("Done: " + num);
            // foreach (GameObject boid in boids)
            // {
            //     boid.constraints = RigidbodyConstraints.FreezeAll;
            // }
        }
        
    }

    void setDead(bool val){
        deadlink = val;
    }
}
