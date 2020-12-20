package ghost;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class Ambusher extends Ghost {

  public Ambusher(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
                  PImage sprite, PImage frightenedSprite, Waka waka) {
    super(x, y, speed, grid, modeTimes, sprite, frightenedSprite, waka);
    this.xVel = -1 * this.speed; // give an intial velocity
    this.yVel = 0;
  }

  /**
  * Ambusher's tick() method, handles in-game logic, movement and pathfinding.
  */
  public void tick() {
    this.move();
    this.determineDirection(this.waka);
    this.alternateMode();
  }

  /**
  * Ambusher's direction-finding method. Checks if Ambusher can move and makes the required moved
  * based on its current mode.
  * @param waka The waka object that Ambusher bases its chase target off of.
  */
  public void determineDirection(Waka waka) {
    // change directions only if at the center of a grid space
    if ((this.x + 6) % 16 == 0 && (this.y + 6) % 16 == 0) {
      // grid coordinates
      int gridX = (this.x + 6) / 16;
      int gridY = (this.y + 6) / 16;

      // first check if the ghost should change it's direction
      if (!this.checkJunction(gridX, gridY)) {
        // if the current position is not a junction, we don't do anything
        return;
      } else {
        // otherwise, we determine the future velocities based on the ghost's current mode
        this.canTurn = true;
        if (this.frightened) {
          this.frightenedMode(gridX, gridY);
        } else if (this.scatter) { // scatter mode
          this.offenseMode(gridX, gridY, 28, 0);
        } else if (!this.scatter) { // chase mode
          this.offenseMode(gridX, gridY, this.waka.getGridX() + 4, this.waka.getGridY() + 4);
        }
        // making the ghost move
        this.xVel = this.xVelFuture;
        this.yVel = this.yVelFuture;
      }
    }
  }
}
