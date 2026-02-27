package consoleui.controls;


import consoleui.utils.ConsoleUtils;
import consoleui.shapes.Rectangle;

public class TextBlock {

  private String label;
  private int x, y;
  private int width; 
  private int height;
  private String color;
  private String content;
  private Rectangle box;
  private boolean drawbox;

  public TextBlock(String label, String content, int x, int y, int width, int height) {
    
    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.content = content;
    this.box = new Rectangle(x, y, width + 1, height + 1);
    this.drawbox = false;

    return;
  }

  public void render() {

    if (color != null) {
      System.out.print(color);
    }

    if(drawbox){

      box.render();

      if (label != null) {
        ConsoleUtils.writeIn(label, x + 2, y);
      }

    }

    int ix = 0;
    int iy = 0;

    for(int i = 0 ; i < content.length() ; i++){
      
      if(iy >= height){ break; }

      if('\n' == content.charAt(i)){
        iy++;
        ix = 0;
        continue;
      }
      
      if(ix < width){
        ConsoleUtils.writeIn(content.charAt(i), x + ix + 1, y + iy + 1);
      }
      
      ix++;
    }


    return;
  }

  public void clearField() {

    ConsoleUtils.hideCursor();

    for (int iy = 0; iy < height; iy++) {
      for (int ix = 0; ix < width; ix++) {
        ConsoleUtils.writeIn(" ", x + 1 + ix, y + 1 + iy);
      }
    }

    ConsoleUtils.setCursorPosition(x + 1, y + 1);
    ConsoleUtils.showCursor();
    return;
  }

  public void setDrawBox(boolean value){
    this.drawbox = value;
  }

  public void setContent(String content){
    this.content = content;
    this.clearField();
    this.render();
  }

  public String getContent() {
    return String.join("", content);
  }

  public String getValue() {
    return String.join("", content);
  }

  public void clear() {
    content = new String();
  }


}
