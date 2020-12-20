package ghost;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class Ghost implements Moveable {

  protected int x;
  protected int y;
  protected int xVel;
  protected int yVel;
  protected int xVelFuture;
  protected int yVelFuture;
  protected int speed;
  protected int modeCounter;
  protected int frameCounter;
  protected char[][] grid;
  protected ArrayList<Long> modeTimes;
  protected long currentTime;
  protected boolean alive;
  protected boolean frightened;
  protected boolean scatter;
  protected boolean eaten;
  protected boolean canTurn;
  protected PImage sprite;
  protected PImage frightenedSprite;
  protected Waka waka;

  public Ghost(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
               PImage sprite, PImage frightenedSprite, Waka waka) {
    this.x = x;
    this.y = y;
    this.xVel = 0;
    this.yVel = 0;
    this.xVelFuture = 0;
    this.yVelFuture = 0;
    this.speed = speed;
    this.modeCounter = 0;
    this.frameCounter = 1;
    this.grid = grid;
    this.modeTimes = modeTimes;
    this.currentTime = this.modeTimes.get(0);
    this.alive = true;
    this.frightened = false;
    this.scatter = true;
    this.eaten = false;
    this.canTurn = true;
    this.sprite = sprite;
    this.frightenedSprite = frightenedSprite;
    this.waka = waka;
  }

  /**
  * Abstract method that ensures tick() is implemented by child classes. Will handle in-game logic
  * movement and path-finding.
  */
  public abstract void tick();

  /**
  * @return Returns the grid x-coordinate.
  */
  public int getGridX() {
    return (this.x + 6) / 16;
  }

  /**
  * @return Returns the grid y-coordinate.
  */
  public int getGridY() {
    return (this.y + 6) / 16;
  }

  /**
  * @return Returns the frightened boolean.
  */
  public boolean isFrightened() {
    return this.frightened;
  }

  /**
  * @return Returns the alive boolean.
  */
  public boolean isAlive() {
    return this.alive;
  }

  /**
  * Setter method, which changes alive if the ghost has been eaten by Waka.
  */
  public void eatGhost() {
    this.alive = false;
  }

  /**
  * Sets the ghost's Waka object.
  */
  public void setWaka(Waka waka) {
    this.waka = waka;
  }

  /**
  * Sets the frightened variable to true if a superFruit has been eaten.
  */
  public void frightenGhost() {
    this.frightened = true;
  }

  /**
  * The draw method, handles drawing ghost sprites to the screen and ensuring the correct
  * sprite is used based on the ghost's current mode.
  * @param app the PApplet object the sprites are drawn to.
  */
  public void draw(PApplet app) {
    if (this.alive && !this.frightened) {
      app.image(this.sprite, this.x, this.y);
    } else if (this.frightened) {
      app.image(this.frightenedSprite, this.x, this.y);
    }
  }

  /**
  * Times the ghost's current mode and switches once the timer reaches 0.
  */
  public void alternateMode() {
    if (this.frameCounter % 60 == 0) { // check once every 60 frames
      this.currentTime--; // timer counts down
      if (this.currentTime == 0) { // time to change modes
        // swap the current modes
        this.scatter = !this.scatter;
        this.modeCounter++;
        currentTime = this.modeTimes.get(this.modeCounter);
      }
    }
  }

  /**
  * Handles the movement when the ghost has a target (i.e. not frightened).
  * First calculates distances from each possible turn a ghost can make to its target position.
  * Then takes turns in the direction of the smallest distance, in order to minimise distance.
  * If the chosen distance is a reverse direction, choose the next best direction.
  * @param gridX the ghost's grid x-coordinate.
  * @param gridY the ghost's grid y-coordinate.
  * @param targetX the target grid x-coordinate.
  * @param targetY the target's grid y-coordinate.
  */
  public void offenseMode(int gridX, int gridY, int targetX, int targetY) {

    // computing distance from all possible directions to target
    ArrayList<Double> distances = new ArrayList<Double>();
    double leftDistance = Math.hypot((gridX - 1) - targetX, gridY - targetY);
    double rightDistance = Math.hypot((gridX + 1) - targetX, gridY - targetY);
    double upDistance = Math.hypot(gridX - targetX, (gridY - 1) - targetY);
    double downDistance = Math.hypot(gridX - targetX, (gridY + 1) - targetY);

    distances.add(leftDistance);
    distances.add(rightDistance);
    distances.add(upDistance);
    distances.add(downDistance);

    Collections.sort(distances); // now distances.get(0) is the smallest distance

    if (distances.get(0) == leftDistance && !this.checkGrid(gridX - 1, gridY)) {
      this.xVelFuture = -1 * this.speed;
      this.yVelFuture = 0;
    } else if (distances.get(0) == rightDistance && !this.checkGrid(gridX + 1, gridY)) {
      this.xVelFuture = 1 * this.speed;
      this.yVelFuture = 0;
    } else if (distances.get(0) == upDistance && !this.checkGrid(gridX, gridY - 1)) {
      this.xVelFuture = 0;
      this.yVelFuture = -1 * this.speed;
    } else if (distances.get(0) == downDistance && !this.checkGrid(gridX, gridY + 1)) {
      this.xVelFuture = 0;
      this.yVelFuture = 1 * this.speed;
    } else {
      if (!this.checkGrid(gridX, gridY - 1)) {
        this.xVelFuture = 0;
        this.yVelFuture = -1 * this.speed;
      } else if (!this.checkGrid(gridX - 1, gridY)) {
        this.xVelFuture = -1 * this.speed;
        this.yVelFuture = 0;
      } else if (!this.checkGrid(gridX, gridY + 1)) {
        this.xVelFuture = 0;
        this.yVelFuture = 1 * this.speed;
      }
    }

    if (this.xVel + (-1 * this.speed) == 0 && this.yVel == 0) {
      distances.remove(leftDistance);
    } else if (this.xVel + (1 * this.speed)== 0 && this.yVel == 0) {
      distances.remove(rightDistance);
    } else if ((this.xVel == 0) && this.yVel + (-1 * this.speed) == 0) {
      distances.remove(upDistance);
    } else if (this.xVel == 0 && this.yVel + (1 * this.speed) == 0) {
      distances.remove(downDistance);
    }

    int i = 0;
    while (this.xVel + this.xVelFuture == 0 && this.yVel + this.yVelFuture == 0) {
      if (distances.get(i) == leftDistance && !this.checkGrid(gridX - 1, gridY)) {
        this.xVelFuture = -1 * this.speed;
        this.yVelFuture = 0;
      } else if (distances.get(i) == rightDistance && !this.checkGrid(gridX + 1, gridY)) {
        this.xVelFuture = 1 * this.speed;
        this.yVelFuture = 0;
      } else if (distances.get(i) == upDistance && !this.checkGrid(gridX, gridY - 1)) {
        this.xVelFuture = 0;
        this.yVelFuture = -1 * this.speed;
      } else if (distances.get(i) == downDistance && !this.checkGrid(gridX, gridY + 1)) {
        this.xVelFuture = 0;
        this.yVelFuture = 1 * this.speed;
      } else {
        i++;
      }
    }

  }

  /**
  * Handles movement when the ghost is frightened.
  * A random direction is picked. If that direction is a reversal of the ghost's current direction
  * another direction is chosen randomly.
  * @param gridX the ghost's grid x-coordinate.
  * @param gridY the ghost's grid y-coordinate.
  */
  public void frightenedMode(int gridX, int gridY) {
    Random rand = new Random();
    int randomDirection = rand.nextInt(4); // randomly choosing a value between 0 and 3
    if (randomDirection == 0) {
      this.xVelFuture = -1 * this.speed;
      this.yVelFuture = 0;
    } else if (randomDirection == 1) {
      this.xVelFuture = 1 * this.speed;
      this.yVelFuture = 0;
    } else if (randomDirection == 2) {
      this.xVelFuture = 0;
      this.yVelFuture = -1 * this.speed;
    } else if (randomDirection == 3) {
      this.xVelFuture = 0;
      this.yVelFuture = 1 * this.speed;
    }

    // if the velocity is into a wall or in the opposite direction, choose another direction
    while (this.checkGrid(gridX + this.xVelFuture, gridY + this.yVelFuture)
           || (this.xVel + this.xVelFuture == 0 && this.yVel + this.yVelFuture == 0)) {
             randomDirection = rand.nextInt(4);
             if (randomDirection == 0) {
               this.xVelFuture = -1 * this.speed;
               this.yVelFuture = 0;
             } else if (randomDirection == 1) {
               this.xVelFuture = 1 * this.speed;
               this.yVelFuture = 0;
             } else if (randomDirection == 2) {
               this.xVelFuture = 0;
               this.yVelFuture = -1 * this.speed;
             } else if (randomDirection == 3) {
               this.xVelFuture = 0;
               this.yVelFuture = 1 * this.speed;
             }
           }
  }

  /**
  * A method which checks if a wall object is at the specified coordinates.
  * @param x x-coordinate to check, in grid form.
  * @param y y-coordinate to check, in grid form.
  * @return Returns a boolean representing whether or not a wall exists at the checked location.
  */
  public boolean checkGrid(int x, int y) {
    if (this.grid[y][x] == '1') {
      return true;
    } else if (this.grid[y][x] == '2') {
      return true;
    } else if (this.grid[y][x] == '3') {
      return true;
    } else if (this.grid[y][x] == '4') {
      return true;
    } else if (this.grid[y][x] == '5') {
      return true;
    } else if (this.grid[y][x] == '6') {
      return true;
    } else {
      return false;
    }
  }

  /**
  * Checks if a ghost is at a junction (a space where it can make a turn).
  * First checks if the space is not a wall, then checks if there is a space up or down
  * and left or right.
  * @param gridX the x-coordinate to check, in grid form.
  * param gridY the y-coordinate to check, in grid from.
  */
  public boolean checkJunction(int gridX, int gridY) {
    if (!this.checkGrid(gridX, gridY)) { // first checking if not already a wall
      if (!this.checkGrid(gridX, gridY - 1) || !this.checkGrid(gridX, gridY + 1)) { // up or down
        if (!this.checkGrid(gridX - 1, gridY) || !this.checkGrid(gridX + 1, gridY)) { // left or right
          return true;
        }
      }
    }
    return false;
  }

  /**
  * Handles ghost movement once the velocity has been set.
  */
  public void move() {
    if (this.canTurn) {
      this.x += this.xVel;
      this.y += this.yVel;
    }
  }
}
