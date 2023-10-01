package KAGO_framework.Core;

import KAGO_framework.Core.Subsystems.Component;
import java.util.ArrayList;

public class GameObject {
    public boolean active;
    public Transform transform;
    public ArrayList<Component> components;

    public GameObject(){
        active = true;
        transform = new Transform();
        components = new ArrayList<>();
    }

    public void Start(){

    }

    public void Update(){

    }

    public void LateUpdate(){

    }

    public void FixedUpdate(){

    }
}
