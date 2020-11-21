package ghost;

import java.lang.Math;
import java.util.ArrayList;
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
  protected boolean alive;
  protected boolean frightened;
  protected boolean scatter;
  protected boolean eaten;
  protected boolean canTurn;
  protected PImage sprite;
  protected PImage frightenedSprite;

  public Ghost(int x, int y, int speed, char[][] grid, ArrayList<Long> modeTimes,
               PImage sprite, PImage frightenedSprite) {
    this.x = x;
    this.y = y;
    this.xVel = 0;
    this.yVel = 0;
    this.xVelFuture = 0;
    this.yVelFuture = 0;
    this.speed = speed;
    this.modeCounter = 0;
    this.frameCounter = 0;
    this.modeTimes = modeTimes;
    this.alive = true;
    this.frightened = false;
    this.scatter = true;
    this.eaten = false;
    this.canTurn = true;
    this.grid = grid;
    this.sprite = sprite;
    this.frightenedSprite = frightenedSprite;
  }

  public abstract void tick();

  public abstract void scatterMode(int gridX, int gridY);

  public abstract void chaseMode();

  public int getGridX() {
    return (this.x + 6) / 16;
  }

  public int getGridY() {
    return (this.y + 6) / 16;
  }

  public void draw(PApplet app) {
    if (this.alive && !this.frightened) {
      app.image(this.sprite, this.x, this.y);
    } else if (this.frightened) {
      app.image(this.frightenedSprite, this.x, this.y);
    }
  }

  // times ghost's mode and switches the mode when necessary
  public void alternateMode() {
    long currentTime = this.modeTimes.get(this.modeCounter);
    if (this.frameCounter % 60 == 0) { // check once every 60 frames
      currentTime--; // timer counts down
      if (currentTime == 0) { // time to change modes
        // swap the current modes
        this.scatter = !this.scatter;
        this.modeCounter++;
        currentTime = this.modeTimes.get(this.modeCounter);
      }
    }
  }

  public void determineDirection() {
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
          this.scatterMode(gridX, gridY);
        } else if (!this.scatter) { // chase mode

        }
        // making the ghost move
        this.xVel = this.xVelFuture;
        this.yVel = this.yVelFuture;
      }
    }
  }

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

  // checks if the ghost is at a junction (a position where it can make a turn)
  public boolean checkJunction(int gridX, int gridY) {
    // the position is a junction if there is a space up or down and left or right
    if (!this.checkGrid(gridX, gridY)) {
      if (!this.checkGrid(gridX, gridY - 1) || !this.checkGrid(gridX, gridY + 1)) {
        if (!this.checkGrid(gridX - 1, gridY) || !this.checkGrid(gridX + 1, gridY)) {
          return true;
        }
      }
    }
    return false;
  }

  public void move() {
    if (this.canTurn) {
      this.x += this.xVel;
      this.y += this.yVel;
    }
  }

  public void frightenGhost() {
    this.frightened = true;
  }
}
