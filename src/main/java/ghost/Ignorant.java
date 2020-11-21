package ghost;

import java.util.List;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

public class Ignorant extends Ghost {

  public Ignorant(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
                  PImage sprite, PImage frightenedSprite) {
    super(x, y, speed, grid, modeTimes, sprite, frightenedSprite);
  }

  public void tick() {

  }

  public void scatterMode(int gridX, int gridY) {

  }

  public void chaseMode() {

  }
}
