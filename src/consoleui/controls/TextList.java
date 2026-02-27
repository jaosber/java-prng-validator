package consoleui.controls;

import consoleui.utils.ConsoleTerminal;
import consoleui.utils.ConsoleUtils;
import consoleui.utils.KeyCodes;
import consoleui.shapes.Rectangle;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import consoleui.utils.Colors;
import java.util.ArrayList;

public class TextList {

  private String label;
  private int x, y;
  private int width, height;
  private ArrayList<String> items;
  private int visibleStartIndex;
  private boolean focus;
  private Rectangle box;
  private String boxColorFocus;
  private ConsoleTerminal consoleTerminal;
  private Thread threadReader;

  // Constructor
  public TextList(ConsoleTerminal ct, String label, ArrayList<String> items, int x, int y, int width, int height) {
    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.items = new ArrayList<>();
    for (String item : items) {
      this.items.add(item);
    }
    this.visibleStartIndex = 0; // Empieza mostrando desde el primer elemento
    this.focus = false;
    this.box = new Rectangle(x, y, width + 1, height + 1);
    this.consoleTerminal = ct;
    this.threadReader = new Thread(() -> {
      readInput();
    });

    this.boxColorFocus = Colors.GREEN;

  }

  public TextList(ConsoleTerminal ct, String label, String content, int x, int y, int width, int height) {

    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.items = stringToItems(content);
    this.visibleStartIndex = 0; // Empieza mostrando desde el primer elemento
    this.focus = false;
    this.box = new Rectangle(x, y, width + 1, height + 1);
    this.consoleTerminal = ct;
    this.threadReader = new Thread(() -> {
      readInput();
    });

  }

  public void render() {

    if(focus){
      box.setColor(boxColorFocus);
    }else{
      box.setColor(Colors.WHITE);
    }
    
    clearField();

    box.render();

    if (label != null) {
      ConsoleUtils.writeIn(label, x + 2, y);
    }

    for (int i = 0; i < Math.min(height, items.size()); i++) {

      String item = items.get(i);

      for (int j = 0; j < Math.min(item.length(), width); j++) {
        ConsoleUtils.writeIn(item.charAt(j), x + 1 + j, y + 1 + i);
      }

      // ConsoleUtils.writeIn(item, x + 1, y + 1 + i);
    }

    if (focus && (threadReader == null || !threadReader.isAlive())) {
      threadReader = new Thread(() -> {
        readInput();
      });
      threadReader.start();
    }

  }

  public void readInput() {

    try {

      Terminal terminal = consoleTerminal.getTerminal();
      NonBlockingReader reader = terminal.reader();

      ConsoleUtils.setCursorPosition(x + 1, y + 1);

      boolean exit = false;

      while (!exit) {

        int key = reader.read(10);

        if (key < 0) {
          continue;
        }

        switch (key) {
          case KeyCodes.RETURN: // Si se presiona Enter, salir del foco
            exit = true;
            focus = false;
            break;

          case KeyCodes.SECONDARY_UP: // Desplazarse hacia arriba
            if (visibleStartIndex > 0) {
              visibleStartIndex--;
            }
            break;

          case KeyCodes.SECONDARY_DOWN: // Desplazarse hacia abajo
            if (visibleStartIndex < items.size()) {
              visibleStartIndex++;
            }
            break;

          default:
            break;
        }

        renderVisibleItems();
      }

      clearField();
      renderNoFocus();

      Thread.currentThread().interrupt();

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

    ConsoleUtils.hideCursor();
  }

  private void renderNoFocus() {

    if(focus){
      box.setColor(boxColorFocus);
    }else{
      box.setColor(Colors.WHITE);
    }

    box.render(); // Renderizar el cuadro

    if (label != null) {
      ConsoleUtils.writeIn(label, x + 2, y);
    }

    for (int i = 0; i < Math.min(height, items.size()); i++) {

      String item = items.get(i);

      for (int j = 0; j < Math.min(item.length(), width); j++) {
        ConsoleUtils.writeIn(item.charAt(j), x + 1 + j, y + 1 + i);
      }

    }

    return;
  }

  private void renderVisibleItems() {

    clearField();

    ConsoleUtils.showCursor();

    for (int i = 0; i < Math.min(height, items.size()); i++) {

      int index = visibleStartIndex + i < items.size() ? visibleStartIndex + i : items.size() - 1;

      String item = items.get(index);
      // ConsoleUtils.writeIn(item, x + 1, y + 1 + i);

      for (int j = 0; j < Math.min(item.length(), width); j++) {
        ConsoleUtils.writeIn(item.charAt(j), x + 1 + j, y + 1 + i);
      }

      if (index == items.size() - 1) {
        break;
      }
    }

  }

  private void clearField() {

    ConsoleUtils.hideCursor();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ConsoleUtils.writeIn(' ', x + 1 + j, y + 1 + i);
      }
    }

  }

  public void setFocus(boolean focus) {
    this.focus = focus;
  }

  public boolean getFocus(){
    return this.focus;
  }

  public ArrayList<String> getItems() {
    return items;
  }

  public void setItems(String[] newItems) {
    items.clear();
    for (String item : newItems) {
      items.add(item);
    }
    visibleStartIndex = 0; // Reiniciar el desplazamiento
  }

  public void setItems(ArrayList<String> newItems) {
    items.clear();
    for (String item : newItems) {
      items.add(item);
    }
    visibleStartIndex = 0; // Reiniciar el desplazamiento
  }

  public void setContent(String content) {
    
    this.items = stringToItems(content);

    visibleStartIndex = 0;
  }

  public int getVisibleStartIndex() {
    return visibleStartIndex;
  }

  // Cambiar el índice visible
  public void setVisibleStartIndex(int index) {
    if (index >= 0 && index < items.size()) {
      visibleStartIndex = index;
    }
  }

  private ArrayList<String> stringToItems(String text) {

    ArrayList<String> listItems = new ArrayList<>();
    String item = "";

    for (int i = 0; i < text.length(); i++) {

      if ('\n' == text.charAt(i)) {
        listItems.add(item);
        item = "";
      }

      item += String.valueOf(text.charAt(i));
    }

    listItems.add(item);

    return listItems;
  }

}
