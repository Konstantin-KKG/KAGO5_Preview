package KAGO_framework.Core.SceneGraph;

import KAGO_framework.Core.Subsystems.Component;
import java.util.ArrayList;
import java.util.Arrays;

public class GameObject {
    public boolean active;
    private Transform transform;
    private ArrayList<Component> components;

    public GameObject(Component[] components) {
        active = true;
        transform = new Transform();
        this.components = new ArrayList<Component>(Arrays.asList(components));
    }

    public void Start(){

    }

    public void Update(){

    }

    public void LateUpdate(){

    }

    public void FixedUpdate(){

    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Transform getTransform() {
        return transform;
    }
}
