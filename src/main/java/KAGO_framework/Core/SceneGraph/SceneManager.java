package KAGO_framework.Core.SceneGraph;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import java.util.ArrayList;
import java.util.Arrays;

public class SceneManager {
    private static ArrayList<Scene> loadedScenes = new ArrayList<>();

    public static void LoadScene(Scene scene) {
        if (loadedScenes.contains(scene)) {
            Debug.Log("The scene you are trying to load is already loaded.", LogType.WARNING);
            return;
        }

        loadedScenes.add(scene);
    }

    public static void UnloadScene(Scene scene){
        if (!loadedScenes.contains(scene)) {
            Debug.Log("The Scene you are trying to unload is not loaded.", LogType.WARNING);
            return;
        }

        loadedScenes.remove(scene);
    }

    public static GameObject[] GetAllActiveGameObjects()  {
        ArrayList<GameObject> gameObjectArrayList = new ArrayList<>();

        // Fill array with gameObjects
        for (Scene scene : loadedScenes)
            gameObjectArrayList.addAll(Arrays.asList(scene.GetAllActiveGameObjects()));

        // Convert ArrayList to Array
        GameObject[] gameObjectArray = new GameObject[gameObjectArrayList.size()];
        gameObjectArrayList.toArray(gameObjectArray);
        return gameObjectArray;
    }
}
