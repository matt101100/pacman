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
    private String mapFileName;
    private long lives;
    private long speed;
    private char[][] grid;
    private ArrayList<Long> modeTimes = new ArrayList<Long>();
    private Waka waka;
    private ArrayList<Wall> map;
    private ArrayList<Fruit> fruits;
    private ArrayList<Ghost> ghosts;

    public App() {
        // 28 across, 36 down, (y,x) coordinates due to how information is read in
        this.grid = new char[36][28];
        this.map = new ArrayList<Wall>();
        this.fruits = new ArrayList<Fruit>();
        this.ghosts = new ArrayList<Ghost>();
    }

    /**
    * Set-up method which handles pre-game loading. Sets the framerate, reads the map and config
    * files and stores relevant variables from the file into class variables.
    * Also handles the drawing of Wall, Fruit, superFruit, Waka and Ghost objects based on
    * when their respective chars appear in the grid.
    * Also sets the correct Waka object for Ghosts to utilise.
    */
    public void setup() {
        frameRate(60);

        // file reading
        this.readConfigFile("config.json");
        this.readMapFile(this.mapFileName);
        int intLives = (int) this.lives;
        int intSpeed = (int) this.speed;

        // Load images

       // initializing wall, fruit, ghost and waka objects and adding them to respective arraylists
       int x = 0;
       int y = 0;
       for (int i = 0; i < this.grid.length; i++) {
         for (int j = 0; j < this.grid[i].length; j++) {
           if (this.grid[i][j] == '1') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/horizontal.png")));
             x += 16;
           } else if (this.grid[i][j] == '2') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/vertical.png")));
             x += 16;
           } else if (this.grid[i][j] == '3') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/upLeft.png")));
             x += 16;
           } else if (this.grid[i][j] == '4') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/upRight.png")));
             x += 16;
           } else if (this.grid[i][j] == '5') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/downLeft.png")));
             x += 16;
           } else if (this.grid[i][j] == '6') {
             this.map.add(new Wall(x, y, this.loadImage("src/main/resources/downRight.png")));
             x += 16;
           } else if (this.grid[i][j] == '7') {
             this.fruits.add(new Fruit(x, y, this.loadImage("src/main/resources/fruit.png")));
             x += 16;
           } else if (this.grid[i][j] == '8') {
             this.fruits.add(new SuperFruit(x, y,
                                            this.loadImage("src/main/resources/superFruit.png"),
                                            this.ghosts));
             x += 16;
           } else if (this.grid[i][j] == 'a') {
             this.ghosts.add(new Ambusher(x - 6, y - 6, intSpeed, this.grid, this.modeTimes,
                                          this.loadImage("src/main/resources/ambusher.png"),
                                          this.loadImage("src/main/resources/frightened.png"),
                                          this.waka));
             x += 16;
           } else if (this.grid[i][j] == 'c') {
             this.ghosts.add(new Chaser(x - 6, y - 6, intSpeed, this.grid, this.modeTimes,
                                       this.loadImage("src/main/resources/chaser.png"),
                                       this.loadImage("src/main/resources/frightened.png"),
                                       this.waka));
             x += 16;
           } else if (this.grid[i][j] == 'i') {
             this.ghosts.add(new Ignorant(x - 6, y - 6, intSpeed, this.grid, this.modeTimes,
                                       this.loadImage("src/main/resources/ignorant.png"),
                                       this.loadImage("src/main/resources/frightened.png"),
                                       this.waka));
             x += 16;
           } else if (this.grid[i][j] == 'w') {
             this.ghosts.add(new Whim(x - 6, y - 6, intSpeed, this.grid, this.modeTimes,
                                       this.loadImage("src/main/resources/whim.png"),
                                       this.loadImage("src/main/resources/frightened.png"),
                                       this.waka));
             x += 16;
           } else if (this.grid[i][j] == 'p') {
             this.waka = new Waka(x - 5, y - 5, intLives, intSpeed,
                                                this.loadImage("src/main/resources/playerLeft.png"),
                                                this.loadImage("src/main/resources/playerRight.png"),
                                                this.loadImage("src/main/resources/playerUp.png"),
                                                this.loadImage("src/main/resources/playerDown.png"),
                                                this.loadImage("src/main/resources/playerClosed.png"),
                                                this.grid, this.fruits, this.ghosts);
             x += 16;
           } else {
             x += 16;
           }
         }
         y += 16; // moving down by 16 pixels
         x = 0; // shifting back along the x-axis
       }

       // setting Waka for each ghost
       for (Ghost ghost : this.ghosts) {
         ghost.setWaka(this.waka);
       }
    }

    /**
    * Sets the dimensions of the app window.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
    * Handles drawing object sprites to the screen as well as ensuring each game object runs its
    * tick() method.
    */
    public void draw() {
        background(0, 0, 0);

        // creating tick loops
        this.waka.tick();

        for (Wall wall : this.map) {
          wall.tick();
        }

        for (Fruit fruit : this.fruits) {
          fruit.tick();
        }

        for (Ghost ghost : this.ghosts) {
          ghost.tick();
        }

        // drawing all objects
        this.waka.draw(this);

        for (Wall wall : this.map) {
          wall.draw(this);
        }

        for (Fruit fruit : this.fruits) {
          fruit.draw(this);
        }

        for (Ghost ghost : this.ghosts) {
          ghost.draw(this);
        }
    }

    /**
    * Reads keyboard input and sets Waka's future velocities based on the input.
    * Also allows Waka to turn when available.
    */
    public void keyPressed() {
      if (keyCode == LEFT) {
        this.waka.setVelocity(-1 * (int) this.speed, 0);
        this.waka.setCanTurn(true);
      } else if (keyCode == RIGHT) {
        this.waka.setVelocity(1 * (int) this.speed, 0);
        this.waka.setCanTurn(true);
      } else if (keyCode == UP) {
        this.waka.setVelocity(0, -1 * (int) this.speed);
        this.waka.setCanTurn(true);
      } else if (keyCode == DOWN) {
        this.waka.setVelocity(0, 1 * (int) this.speed);
        this.waka.setCanTurn(true);
      }
    }

    /**
    * Reads the specified config JSON file and stores the information in respective class variables.
    * @param filename The String representing the directory the file is in.
    * @throws FileNotFoundException If the file name cannot be found or cannot be opened.
    * @throws IOException Checked exception that must be thrown.
    * @throws ParseException If the file cannot be parsed.
    */
    public void readConfigFile(String filename) {
      if (filename == null) {
        return;
      }

      JSONParser parser = new JSONParser();
      try {
        // creating JSON file reader
        Object obj = parser.parse(new FileReader("config.json"));
        JSONObject jObj = (JSONObject) obj;

        // getting values at each index
        this.mapFileName = (String) jObj.get("map");
        this.lives = (long) jObj.get("lives");
        this.speed = (long) jObj.get("speed");

        // getting array information
        JSONArray modeLengths = (JSONArray) jObj.get("modeLengths");
        for (int i = 0; i < modeLengths.size(); i++) {
          this.modeTimes.add((Long)modeLengths.get(i));
        }

      } catch (FileNotFoundException e) {
        return;
      } catch (IOException e) {
        return;
      } catch (ParseException e) {
        return;
      }
    }

    /**
    * Reads the specified map file and stores information in a char[][] array
    * @param filename String representing the directory the file is in.
    * @throws FileNotFoundException If the file cannot be found or opened.
    */
    public boolean readMapFile(String filename) {
      if (filename == null) {
        return false;
      }

      try {
        File f = new File(filename);
        Scanner scan = new Scanner(f);
        int i = 0; // keeps track of how many lines we have scanned
        while (scan.hasNextLine()) {
          String mapInfo = scan.nextLine(); // string containing the map information
          char[] charInfo = mapInfo.toCharArray(); // splitting into char array
          this.grid[i] = charInfo; // adding the map info as a char array to the grid
          i++;
        }
        return true;
      } catch (FileNotFoundException e) {
        return false;
      }
    }

    /**
    * Main function.
    */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
