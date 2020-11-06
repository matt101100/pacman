package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import processing.core.PApplet;
import processing.core.PImage;


public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private Map gameMap;
    private Waka waka;

    public App() {
        //Set up your objects
    }

    public void setup() {
        frameRate(60);

        // Load images
        gameMap = new Map(this.loadImage("src/main/resources/horizontal.png"),
                          this.loadImage("src/main/resources/vertical.png"),
                          this.loadImage("src/main/resources/upLeft.png"),
                          this.loadImage("src/main/resources/upRight.png"),
                          this.loadImage("src/main/resources/downLeft.png"),
                          this.loadImage("src/main/resources/downRight.png"),
                          this.loadImage("src/main/resources/fruit.png"),
                          this.loadImage("src/main/resources/ghost.png"));

       waka = new Waka(199, 315, this.loadImage("src/main/resources/playerLeft.png"));
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void draw() {
        background(0, 0, 0);

        this.gameMap.tick();
        this.waka.tick();

        ArrayList<String> configData = this.readConfigFile("config.json");
        this.gameMap.readMapFile(configData.get(0));
        this.gameMap.draw(this);

        this.waka.draw(this);
    }

    public void keyPressed() {
      if (keyCode == LEFT) {
        this.waka.moveLeft();
      } else if (keyCode == RIGHT) {
        this.waka.moveRight();
      } else if (keyCode == UP) {
        this.waka.moveUp();
      } else if (keyCode == DOWN) {
        this.waka.moveDown();
      }
    }

    // reads the config file
    public ArrayList<String> readConfigFile(String filename) {
      if (filename == null) {
        return null;
      }
      JSONParser parser = new JSONParser();
      ArrayList<String> configData = new ArrayList<String>();
      try {
        Object obj = parser.parse(new FileReader("config.json"));
        JSONObject jObj = (JSONObject) obj;
        String mapFileName = (String) jObj.get("map");
        configData.add(mapFileName);
        return configData;
      } catch (FileNotFoundException e) {
        return null;
      } catch (IOException e) {
        return null;
      } catch (ParseException e) {
        return null;
      }
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
