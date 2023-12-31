package KAGO_framework.Core.SceneGraph;

import java.util.ArrayList;

public class Scene {
    private ArrayList<GameObject> gameObjects;

    public Scene() {
        gameObjects = new ArrayList<>();
    }

    public void AddGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void RemoveGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public GameObject[] GetAllActiveGameObjects() {
        GameObject[] gameObjectsArray = new GameObject[gameObjects.size()];
        gameObjects.toArray(gameObjectsArray);

        return gameObjectsArray;
    }
}
