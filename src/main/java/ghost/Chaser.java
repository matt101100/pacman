package ghost;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class Chaser extends Ghost {

  private final int topLeftX = 0;
  private final int topLeftY = 48;

  public Chaser(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
                PImage sprite, PImage frightenedSprite) {
    super(x, y, speed, grid, modeTimes, sprite, frightenedSprite);
    this.speed = 2;
  }

  public void tick() {
    this.move();
    this.determineDirection();
    this.alternateMode();
  }

  public void scatterMode(int gridX, int gridY) {
    // computing target (x,y) coordinates i.e: the top left corner
    int targetX = 0;
    int targetY = 0;

    // computing distance from all possible directions to target
    ArrayList<Double> distances = new ArrayList<Double>();
    double leftDistance = Math.hypot((gridX - 1) - targetX, gridY - targetY);
    double rightDistance = Math.hypot((gridX + 1) - targetX, gridY - targetY);
    double upDistance = Math.hypot(gridX - targetX, (gridY - 1) - targetY);
    double downDistance = Math.hypot(gridX - targetX, (gridY + 1) - targetY);

    /* We add distances in the following fixed order to reflect the priority of each direction
    * in this way, if two or more distances are equal, we take the highest priority direction
    * up > left > down > right
    */
    distances.add(leftDistance);
    distances.add(rightDistance);
    distances.add(upDistance);
    distances.add(downDistance);

    Collections.sort(distances); // now distances.get(0) is the smallest distance

    if (distances.get(0) == leftDistance && !this.checkGrid(gridX - 1, gridY)) {
      if (!(this.xVel + (1 * this.speed) == 0 && this.yVel == 0)) {
        this.xVelFuture = -1 * this.speed;
        this.yVelFuture = 0;
      }
    } else if (distances.get(0) == rightDistance && !this.checkGrid(gridX + 1, gridY)) {
      if (!(this.xVel + (-1 * this.speed) == 0 && this.yVel == 0)) {
        this.xVelFuture = 1 * this.speed;
        this.yVelFuture = 0;
      }
    } else if (distances.get(0) == upDistance && !this.checkGrid(gridX, gridY - 1)) {
      if (!(this.xVel == 0 && this.yVel + (1 * this.speed) == 0)) {
        this.xVelFuture = 0;
        this.yVelFuture = -1 * this.speed;
      }
    } else if (distances.get(0) == downDistance && !this.checkGrid(gridX, gridY + 1)) {
      if (!(this.xVel == 0 && this.yVel + (-1 * this.speed) == 0)) {
        this.xVelFuture = 0;
        this.yVelFuture = 1 * this.speed;
      }
    } else {
      if (!this.checkGrid(gridX, gridY - 1)) {
        if (!(this.xVel == 0 && this.yVel + (1 * this.speed) == 0)) {
          this.xVelFuture = 0;
          this.yVelFuture = -1 * this.speed;
        }
      } else if (!this.checkGrid(gridX - 1, gridY)) {
        if (!(this.xVel + (1 * this.speed) == 0 && this.yVel == 0)) {
          this.xVelFuture = -1 * this.speed;
          this.yVelFuture = 0;
        }
      } else if (!this.checkGrid(gridX, gridY + 1)) {
        if (!(this.xVel == 0 && this.yVel + (-1 * this.speed) == 0)) {
          this.xVelFuture = 0;
          this.yVelFuture = 1 * this.speed;
        }
      } else if (!this.checkGrid(gridX + 1, gridY)) {
        if (!(this.xVel + (-1 * this.speed) == 0 && this.yVel == 0)) {
          this.xVelFuture = 1 * this.speed;
          this.yVelFuture = 0;
        }
      }
    }
  }

  public void chaseMode() {

  }
}
