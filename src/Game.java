import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

//represents the state of the game
class MyGame extends World {

  ILoGamePiece gamePieces;
  int bulletCount;
  int totalScore;
  int currentTick;

  // constructor for the player
  MyGame(int currentTick, ILoGamePiece gamePieces, int bulletCount, int totalScore) {
    this.currentTick = currentTick;
    this.gamePieces = gamePieces;
    this.bulletCount = bulletCount;
    this.totalScore = totalScore;
  }

  @Override
  public WorldScene makeScene() {
    // Make a new scene.
    // WorldScene scene = new WorldScene(this.WIDTH, this.HEIGHT);
    WorldScene scene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

    scene = this.addPiecesToScene(scene);

    scene = this.addInfoToScene(scene);

    return scene;
  }

  // adds info to display on the screen (tick, bullets, score)
  WorldScene addInfoToScene(WorldScene scene) {
    return scene.placeImageXY(new TextImage("       Current tick: " + Integer.toString(this.currentTick)
        + "    Bullets left: " + Integer.toString(10 - this.bulletCount) + "    Total score:  "
        + Integer.toString(this.totalScore), Color.black), 150, 20);
  }

  // places the list of pieces onto the scene
  WorldScene addPiecesToScene(WorldScene scene) {
    return this.gamePieces.placeAll(scene);
  }

  // // gets called every tickrate seconds to update the game
  @Override
  public MyGame onTick() {
    // need a method that checks and removes all collided pieces
    // need to somehow keep track of score for every collision that occurs
    // need to somehow increment when a collision occurs
    return this.removeCollGame().checkShipSpawn().moveGame().updateScore().removeGame()
        .incrementGameTick();
  }

  // increases the game tick by one
  public MyGame incrementGameTick() {
    return new MyGame(this.currentTick + 1, this.gamePieces, this.bulletCount, this.totalScore);
  }

  // did we press the space update the final tick of the game by 10.
  // increment a counter to stop working once you hit the space bar 10 times
  @Override
  public MyGame onKeyEvent(String key) {
    // need to make it so that a bullet is added to the list of gamepieces
    if (key.equals(" ") && this.bulletCount < 10) {
      // return new MyGame(this.currentTick, this.gamePieces,
      // this.bulletCount + 1);
      return new MyGame(this.currentTick,
          new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 250,
              Constants.SCREEN_HEIGHT - Constants.BULLET_START_RADIUS, 270.0,
              Constants.BULLET_SPEED, 1), this.gamePieces),
          this.bulletCount + 1, this.totalScore);
    }
    else {
      return this;
    }
  }

  // if the tick number is a multiple of 28, then add the ships to the game piece
  // list of MyShips
  public MyGame checkShipSpawn() {
    if (this.currentTick % 28 == 0) {
      return new MyGame(currentTick, this.gamePieces.spawn(), this.bulletCount, this.totalScore);
    }
    else {
      return this;
    }
  }

  // moves all the pieces in the list of the game
  public MyGame moveGame() {
    return new MyGame(currentTick, this.gamePieces.moveAll(), this.bulletCount, this.totalScore);
  }

  // removes any pieces that are offscreen in the list of the game
  public MyGame removeGame() {
    return new MyGame(currentTick, this.gamePieces.removeOffScreen(), this.bulletCount,
        this.totalScore);
  }

  // removes all pieces that have collided from this.gamePieces
  public MyGame removeCollGame() {
    return new MyGame(currentTick, this.gamePieces.removeAllCollision(), this.bulletCount,
        this.totalScore);
  }

  // updates the total score
  public MyGame updateScore() {
    return new MyGame(currentTick, this.gamePieces, this.bulletCount,
        this.totalScore + this.gamePieces.addAmount());
  }

  // Check to see if we need to end the game.
  @Override
  public WorldEnd worldEnds() {
    if (this.gamePieces.noBulletsLeft() && this.bulletCount == 10) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // the WorldScene that will display once the game ends
  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    return endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250);

  }
}

//examples
class ExamplesMyWorldProgram {
  AGamePiece bull = new Bullet(Constants.BULLET_START_RADIUS, 50, 50, 270.0, Constants.BULLET_SPEED,
      1);
  AGamePiece s = new Ship(Constants.SHIP_RADIUS, 100, 100, Constants.SHIP_SPEED);
  AGamePiece z = new Ship(Constants.SHIP_RADIUS, 52, 52, Constants.SHIP_SPEED);
  AGamePiece a = new Bullet(Constants.BULLET_START_RADIUS, 100, 100, 45.0, Constants.BULLET_SPEED,
      1);
  ILoGamePiece mt = new MtLoGamePiece();
  ILoGamePiece full = new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.s,
      new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.a, this.mt))));
  AGamePiece bo = new Bullet(Constants.BULLET_START_RADIUS, 1000, 10, 180.0, Constants.BULLET_SPEED,
      1);
  AGamePiece so = new Ship(Constants.SHIP_RADIUS, 104, 1000, Constants.SHIP_SPEED);
  ILoGamePiece mo = new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.bo,
      new ConsLoGamePiece(this.z, new ConsLoGamePiece(this.so, this.mt))));
  MyGame game1 = new MyGame(100, this.full, 0, 0);
  MyGame game2 = new MyGame(1000, this.full, 10, 5);
  MyGame game3 = new MyGame(0, this.mt, 0, 0);
  MyGame game4 = new MyGame(456, this.mt, 10, 1);
  MyGame game5 = new MyGame(100, this.mo, 8, 47);
  MyGame game6 = new MyGame(3, this.mo, 8, 47);
  WorldScene endScene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
  WorldScene scene = new WorldScene(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

  boolean testBigBang(Tester t) {
    ILoGamePiece bruh = new MtLoGamePiece();
    MyGame realWord = new MyGame(1, bruh, 0, 0);
    return realWord.bigBang(500, 300, 1.0 / 28.0);
  }

  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.game3.makeScene(), this.game3.addInfoToScene(this.scene))
        && t.checkExpect(this.game2.makeScene(),
            this.game2.addPiecesToScene(this.game2.addInfoToScene(this.scene)));
  }

  boolean testAddInfoToScene(Tester t) {
    return t
        .checkExpect(this.game3.addInfoToScene(this.scene), this.scene.placeImageXY(
            new TextImage("  Current tick: " + Integer.toString(this.game3.currentTick)
                + "    Bullets left: " + Integer.toString(10 - this.game3.bulletCount)
                + "    Total score:  " + Integer.toString(this.game3.totalScore), Color.black),
            150, 20))
        && t.checkExpect(this.game5.addInfoToScene(this.scene), this.scene.placeImageXY(
            new TextImage("  Current tick: " + Integer.toString(this.game5.currentTick)
                + "    Bullets left: " + Integer.toString(10- this.game5.bulletCount)
                + "    Total score:  " + Integer.toString(this.game5.totalScore), Color.black),
            150, 20));

  }

  boolean testAddPiecesToScene(Tester t) {
    return t.checkExpect(this.game3.addPiecesToScene(this.scene), this.scene)
        && t.checkExpect(this.game2.addPiecesToScene(this.scene),
            this.scene.placeImageXY(this.bull.draw(), 50, 50).placeImageXY(this.s.draw(), 100, 100)
                .placeImageXY(this.z.draw(), 52, 52).placeImageXY(this.a.draw(), 100, 100));

  }

  boolean testOnTick(Tester t) {
    return t.checkExpect(this.game4.onTick(), new MyGame(457, this.mt, 10, 1))
        && t.checkExpect(this.game6.onTick(),
            new MyGame(4,
                new ConsLoGamePiece(new Bullet(4, 58, 50, 360.0, 8, 2),
                    new ConsLoGamePiece(new Bullet(4, 42, 50, 180.0, 8, 2), new MtLoGamePiece())),
                8, 47));
  }

  boolean testIncrementGameTick(Tester t) {
    return t.checkExpect(this.game1.incrementGameTick(), new MyGame(101, this.full, 0, 0))
        && t.checkExpect(this.game2.incrementGameTick(), new MyGame(1001, this.full, 10, 5))
        && t.checkExpect(this.game3.incrementGameTick(), new MyGame(1, this.mt, 0, 0))
        && t.checkExpect(this.game4.incrementGameTick(), new MyGame(457, this.mt, 10, 1))
        && t.checkExpect(this.game5.incrementGameTick(), new MyGame(101, this.mo, 8, 47));
  }

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.game1.onKeyEvent(" "),
        new MyGame(100,
            new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 250, 298, 270.0,
                Constants.BULLET_SPEED, 1), this.full),
            1, 0))
        && t.checkExpect(this.game1.onKeyEvent("a"), this.game1)
        && t.checkExpect(this.game2.onKeyEvent(" "), this.game2)
        && t.checkExpect(this.game2.onKeyEvent("b"), this.game2)
        && t.checkExpect(this.game3.onKeyEvent(" "),
            new MyGame(0,
                new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 250, 298, 270.0,
                    Constants.BULLET_SPEED, 1), this.mt),
                1, 0))
        && t.checkExpect(this.game3.onKeyEvent("c"), this.game3)
        && t.checkExpect(this.game4.onKeyEvent(" "), this.game4)
        && t.checkExpect(this.game4.onKeyEvent("d"), this.game4);
  }

  boolean testMoveGame(Tester t) {
    return t
        .checkExpect(this.game1.moveGame(), new MyGame(100, new ConsLoGamePiece(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 42, 270.0, Constants.BULLET_SPEED, 1),
            new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 104, 100, Constants.SHIP_SPEED),
                new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 56, 52, Constants.SHIP_SPEED),
                    new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 105, 105, 45.0,
                        Constants.BULLET_SPEED, 1), this.mt)))),
            0, 0))
        && t.checkExpect(this.game2.moveGame(), new MyGame(1000, new ConsLoGamePiece(
            new Bullet(Constants.BULLET_START_RADIUS, 50, 42, 270.0, Constants.BULLET_SPEED, 1),
            new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 104, 100, Constants.SHIP_SPEED),
                new ConsLoGamePiece(new Ship(Constants.SHIP_RADIUS, 56, 52, Constants.SHIP_SPEED),
                    new ConsLoGamePiece(new Bullet(Constants.BULLET_START_RADIUS, 105, 105, 45.0,
                        Constants.BULLET_SPEED, 1), this.mt)))),
            10, 5))
        && t.checkExpect(this.game3.moveGame(), this.game3)
        && t.checkExpect(this.game4.moveGame(), this.game4);
  }

  boolean testRemoveGame(Tester t) {
    return t.checkExpect(this.game1.removeGame(), this.game1)
        && t.checkExpect(this.game4.removeGame(), this.game4)
        && t.checkExpect(this.game5.removeGame(), new MyGame(100,
            new ConsLoGamePiece(this.bull, new ConsLoGamePiece(this.z, this.mt)), 8, 47));
  }

  boolean testRemoveCollGame(Tester t) {
    return t.checkExpect(this.game1.removeCollGame(), new MyGame(100,
        new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
            new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
                new ConsLoGamePiece(new Bullet(4, 100, 100, 360.0, 8, 2),
                    new ConsLoGamePiece(new Bullet(4, 100, 100, 180.0, 8, 2), this.mt)))),
        0, 0))
        && t.checkExpect(this.game2.removeCollGame(),
            new MyGame(1000,
                new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
                    new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
                        new ConsLoGamePiece(new Bullet(4, 100, 100, 360.0, 8, 2),
                            new ConsLoGamePiece(new Bullet(4, 100, 100, 180.0, 8, 2), this.mt)))),
                10, 5))
        && t.checkExpect(this.game3.removeCollGame(), new MyGame(0, this.mt, 0, 0))
        && t.checkExpect(this.game4.removeCollGame(), new MyGame(456, this.mt, 10, 1)) && t
            .checkExpect(this.game5.removeCollGame(),
                new MyGame(100,
                    new ConsLoGamePiece(new Bullet(4, 50, 50, 360.0, 8, 2),
                        new ConsLoGamePiece(new Bullet(2, 1000, 10, 180.0, 8, 1),
                            new ConsLoGamePiece(new Bullet(4, 50, 50, 180.0, 8, 2),
                                new ConsLoGamePiece(new Ship(10, 104, 1000, 4), this.mt)))),
                    8, 47));
  }

  boolean testUpdateScore(Tester t) {
    return t.checkExpect(this.game1.updateScore(), new MyGame(100, this.full, 0, 2))
        && t.checkExpect(this.game2.updateScore(), new MyGame(1000, this.full, 10, 7))
        && t.checkExpect(this.game3.updateScore(), this.game3)
        && t.checkExpect(this.game4.updateScore(), this.game4)
        && t.checkExpect(this.game5.updateScore(), new MyGame(100, this.mo, 8, 48));
  }

  boolean testWorldEnds(Tester t) {
    return t.checkExpect(this.game1.worldEnds(),
        new WorldEnd(false,
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250)))
        && t.checkExpect(this.game2.worldEnds(),
            new WorldEnd(false,
                this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250)))
        && t.checkExpect(this.game3.worldEnds(),
            new WorldEnd(false,
                this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250)))
        && t.checkExpect(this.game4.worldEnds(),
            new WorldEnd(true,
                this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250)))
        && t.checkExpect(this.game5.worldEnds(), new WorldEnd(false,
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250)));
  }

  boolean testMakeEndScene(Tester t) {
    return t.checkExpect(this.game1.makeEndScene(),
        this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250))
        && t.checkExpect(this.game2.makeEndScene(),
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250))
        && t.checkExpect(this.game3.makeEndScene(),
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250))
        && t.checkExpect(this.game4.makeEndScene(),
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250))
        && t.checkExpect(this.game5.makeEndScene(),
            this.endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250));
  }

}
