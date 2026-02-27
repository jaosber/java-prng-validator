package consoleui.controls;

import consoleui.utils.ConsoleUtils;
import consoleui.utils.Symbols;
import consoleui.utils.Colors;

public class Label {

  private int x; 
  private int y; 
  private String text; 
  private String color;
  private boolean underline;
  private int width;

  public Label(String text, int x, int y) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.color = Colors.RESET;
    this.width = text.length();
    this.underline = false;
  }

  public Label(String text, int x, int y, int width) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.color = Colors.RESET;
    this.width = width;
    this.underline = false;
  }

  public Label(String text, int x, int y, String color) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.color = color;
  }

  
  public void render() {
    
    ConsoleUtils.hideCursor();

    if (color != null) {
      System.out.print(color);
    }

    int ix = 0;
    int iy = 0;

    for(int i = 0 ; i < text.length() ; i++){
      
      if('\n' == text.charAt(i)){
        iy++;
        ix = 0;
        continue;
      }
      
      if(ix < width){
        ConsoleUtils.writeIn(text.charAt(i), x + ix, y + iy);
      }

      ix++;
    }

    ix = 0;
    iy = iy == 0 ? 1 : iy;

    if(underline){
      for(int i = 0 ; i < width ; i++){
        ConsoleUtils.writeIn(Symbols.HORIZONTAL_LINE, x + i, y + iy);
      }
    }

    System.out.print(Colors.RESET);

    return;
  }

  public void setUnderline(boolean value){
    this.underline = value;
  }

  public void setCoord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setWidth(int value){
    this.width = value;
  }

}