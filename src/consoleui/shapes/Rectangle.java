package consoleui.shapes;

import consoleui.utils.ConsoleUtils;
import consoleui.utils.Symbols;
import consoleui.utils.Colors;

public class Rectangle {
  private int x; 
  private int y; 
  private int width;
  private int height;
  private String color;

  public Rectangle(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = null;
  }

  public Rectangle(int x, int y, int width, int height, String color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
  }

  public void render() {
    // Aplicamos el color si se especificó
    if (color != null) {
      System.out.print(color);
    }

    ConsoleUtils.hideCursor();

    // Dibujar las esquinas
    ConsoleUtils.writeIn(String.valueOf(Symbols.TOP_LEFT_CORNER), x, y);
    ConsoleUtils.writeIn(String.valueOf(Symbols.TOP_RIGHT_CORNER), x + width, y);
    ConsoleUtils.writeIn(String.valueOf(Symbols.BOTTOM_LEFT_CORNER), x, y + height);
    ConsoleUtils.writeIn(String.valueOf(Symbols.BOTTOM_RIGHT_CORNER), x + width, y + height);

    // Dibujar los bordes horizontales
    for (int i = 1; i < width; i++) {
      ConsoleUtils.writeIn(String.valueOf(Symbols.HORIZONTAL_LINE), x + i, y); // Borde superior
      ConsoleUtils.writeIn(String.valueOf(Symbols.HORIZONTAL_LINE), x + i, y + height); // Borde inferior
    }

    // Dibujar los bordes verticales
    for (int i = 1; i < height ; i++) {
      ConsoleUtils.writeIn(String.valueOf(Symbols.VERTICAL_LINE), x, y + i); // Borde izquierdo
      ConsoleUtils.writeIn(String.valueOf(Symbols.VERTICAL_LINE), x + width, y + i); // Borde derecho
    }

    //Restablecer el color al valor predeterminado
    if (color != null) {
      System.out.print(Colors.RESET);
    }

    ConsoleUtils.setCursorPosition(x+1,y+1);

  }

  // Getters y Setters si necesitas manipular el rectángulo después

  public void setCoord(int x, int y){
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

  public int getWidth() {
    return width;
  }

  public void setSize(int width, int height){
    this.width = width;
    this.height = height;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
