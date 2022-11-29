class PlayGame {
  PlayGame() {}
  public static void main(String[] args) {
    ILoGamePiece bruh = new MtLoGamePiece();
    MyGame realWord = new MyGame(1, bruh, 0, 0);
    realWord.bigBang(500, 300, 1.0 / 28.0);
  }
}