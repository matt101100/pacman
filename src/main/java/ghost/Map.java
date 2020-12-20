package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import processing.core.PApplet;
import processing.core.PImage;

public class Map {

  private char[][] grid = new char[36][28]; // 28 across, 36 down
  private PImage horizontalSprite;
  private PImage verticalSprite;
  private PImage upLeftSprite;
  private PImage upRightSprite;
  private PImage downLeftSprite;
  private PImage downRightSprite;
  private PImage fruitSprite;
  private PImage ghostSprite;

  public Map(PImage horizontalSprite, PImage verticalSprite, PImage upLeftSprite,
             PImage upRightSprite, PImage downLeftSprite, PImage downRightSprite,
             PImage fruitSprite, PImage ghostSprite) {
               this.horizontalSprite = horizontalSprite;
               this.verticalSprite = verticalSprite;
               this.upLeftSprite = upLeftSprite;
               this.upRightSprite = upRightSprite;
               this.downLeftSprite = downLeftSprite;
               this.downRightSprite = downRightSprite;
               this.fruitSprite = fruitSprite;
               this.ghostSprite = ghostSprite;
             }

  public void tick() {
  }

  public void draw(PApplet app) {
    int x = 0;
    int y = 0;
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[i].length; j++) {
        if (this.grid[i][j] == '1') {
          app.image(this.horizontalSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '2') {
          app.image(this.verticalSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '3') {
          app.image(this.upLeftSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '4') {
          app.image(this.upRightSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '5') {
          app.image(this.downLeftSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '6') {
          app.image(this.downRightSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == '7') {
          app.image(this.fruitSprite, x, y);
          x += 16;
        } else if (this.grid[i][j] == 'g') {
          app.image(this.ghostSprite, x, y);
          x += 16;
        }
        else {
          x += 16;
        }
      }
      y += 16; // moving down by 16 pixels
      x = 0;
    }
  }

  // just reads the map file and puts the information into a grid (char array)
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
    } catch (NullPointerException e) {
      return false;
    }
  }

}
