using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraMovement : MonoBehaviour
{
    
    // private Vector3 cameraPostition;
    // [Header{"Camera Settings"}];
    // public float cameraSpeed;

    // Start is called before the first frame updat

    public GameObject player;
    public float sensitivity;
    
    void FixedUpdate (){
        float rotateHorizontal = Input.GetAxis ("Mouse X");
        float rotateVertical = Input.GetAxis ("Mouse Y");
        transform.RotateAround (player.transform.position, -Vector3.up, rotateHorizontal * sensitivity);
        transform.RotateAround (Vector3.zero, transform.right, rotateVertical * sensitivity);
    }
}
