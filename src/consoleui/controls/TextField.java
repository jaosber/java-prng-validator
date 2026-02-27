package consoleui.controls;

import consoleui.utils.ConsoleUtils;
import consoleui.utils.KeyCodes;
import consoleui.shapes.Rectangle;
import consoleui.utils.ConsoleTerminal;
import consoleui.utils.Symbols;
import java.util.ArrayList;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class TextField {

  private String label;
  private int x, y; 
  private int width; 
  private int height;
  private boolean focus; 
  private ArrayList<String> content; 
  private int cursorPosition; 
  private Rectangle box;
  private ConsoleTerminal consoleTerminal;
  private Thread threadReader;

  public TextField(ConsoleTerminal ct, String label, int x, int y, int width) {

    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = 1;
    this.focus = false; 
    this.content = new ArrayList<>(); 
    this.cursorPosition = 0;
    this.box = new Rectangle(x, y, width + 1, height + 1);
    this.consoleTerminal = ct;

    threadReader = new Thread(() -> { read(); });

  }

  public TextField(ConsoleTerminal ct, String label, String content, int x, int y, int width) {

    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = 1;
    this.focus = false;
    this.content = new ArrayList<>();
    for (int i = 0; i < content.length(); i++) {
      this.content.add(String.valueOf(content.charAt(i)));
    }

    this.cursorPosition = content.length();
    this.box = new Rectangle(x, y, width + 1, height + 1);
    this.consoleTerminal = ct;

    threadReader = new Thread(() -> { read(); });

  }

  public void render() {

    box.render();

    renderClearField();

    if (label != null) {
      ConsoleUtils.writeIn(label, x + 2, y);
    }

    for (int i = 0; i < Math.min(content.size(), width); i++) {
      ConsoleUtils.writeIn(content.get(i), x + 1 + i, y + 1);
    }

    if (focus && (threadReader == null || !threadReader.isAlive())) {
      threadReader = new Thread(() -> {
        read();
      });
      threadReader.start();
    }

  }

  public void renderFocus() {

    ConsoleUtils.showCursor();

    if (content.size() == 0) {
      clearField();
      return;
    }

    int j = content.size() - width >= 0 ? content.size() - width : 0;
    
    for (int i = 0; i < Math.min(content.size(), width); i++) {
      ConsoleUtils.writeIn(content.get(i + j), x + 1 + i, y + 1);
    }

    return;
  }

  public void renderNoFocus() {

    box.render();

    if (label != null) {
      ConsoleUtils.writeIn(label, x + 2, y);
    }

    for (int i = 0; i < Math.min(content.size(), width); i++) {
      ConsoleUtils.writeIn(content.get(i), x + 1 + i, y + 1);
    }

    return;
  }

  public void read() {

    try{

      Terminal terminal = consoleTerminal.getTerminal();
      NonBlockingReader reader = terminal.reader();

      ConsoleUtils.setCursorPosition(x + 1, y + 1);
      
      boolean exit = false;

      while (!exit && focus) {

        ConsoleUtils.showCursor();

        int key = reader.read(500);

        if (key < 0) { renderFocus(); continue; }

        switch (key) {
          case KeyCodes.RETURN:
            exit = true;
            focus = false;
            break;

          case KeyCodes.BACKSPACE:

            if (cursorPosition >= 0) {
              backspaceField();
            }

            break;

          default:

            content.add(String.valueOf((char) key));
            cursorPosition++;
            
            //xcursor = terminal.getCursorPosition(null).getX();

            break;
        }

        renderFocus();

      }

      renderNoFocus();

      //reader.close();
      
      Thread.currentThread().interrupt();

      //terminal.close();

    } catch (Exception e) {

      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

    ConsoleUtils.hideCursor();

    return;
  }

  public void backspaceField() {

    if (content.size() == 0) {
      clearField();
      return;
    }

    ConsoleUtils.hideCursor();

    int d = (content.size() > 0) ? content.size() - 1 : 0;

    ConsoleUtils.writeIn(Symbols.SPACE, x + 1 + d, y + 1);
    ConsoleUtils.showCursor();

    content.removeLast();
    cursorPosition--;

    return;
  }

  private void renderClearField() {

    ConsoleUtils.hideCursor();
    for (int i = 0; i < content.size(); i++) {
      ConsoleUtils.writeIn(" ", x + 1 + i, y + 1);
    }

    ConsoleUtils.setCursorPosition(x + 1, y + 1);
    
    return;
  }

  private void clearField() {

    ConsoleUtils.hideCursor();
    for (int i = 0; i < content.size(); i++) {
      ConsoleUtils.writeIn(" ", x + 1 + i, y + 1);
    }

    ConsoleUtils.setCursorPosition(x + 1, y + 1);

    content.clear();
    cursorPosition = -1;

    return;
  }

  public void setFocus(boolean focus) {
    this.focus = focus;  
  }

  public boolean getFocus(){
    return this.focus;
  }

  public String getContent() {
    return String.join("", content);
  }

  public String getValue() {
    return String.join("", content);
  }

  public boolean isEmpty(){
    return content.size() == 0;
  }
  
  public void clear() {
    this.clearField();
    content = new ArrayList<>();
    //content.add("");
    cursorPosition = 0;
  }

}


