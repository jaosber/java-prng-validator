package consoleui.controls;

import consoleui.utils.ConsoleTerminal;
import consoleui.utils.ConsoleUtils;
import consoleui.utils.KeyCodes;
import consoleui.utils.Symbols;
import consoleui.shapes.Rectangle;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.util.ArrayList;

public class ComboBox {

  private String label;
  private int x, y;
  private int width;
  private ArrayList<String> options;
  private int selectedIndex;
  private boolean focus;
  private Rectangle box;
  private ConsoleTerminal consoleTerminal;
  private Thread threadReader;

  public ComboBox(ConsoleTerminal ct, String label, String[] options, int x, int y, int width) {
    this.label = label;
    this.x = x;
    this.y = y;
    this.width = width;
    this.options = new ArrayList<>();
    for (String option : options) {
      this.options.add(option);
    }
    this.selectedIndex = 0;
    this.focus = false;
    this.box = new Rectangle(x, y, width + 1, 2);

    this.consoleTerminal = ct;

    threadReader = new Thread(() -> {
      readInput();
    });

  }

  public void render() {

    box.render();

    if (label != null) {
      ConsoleUtils.writeIn(label, x + 2, y);
    }

    String selectedOption = options.get(selectedIndex);

    for (int i = 0; i < Math.min(selectedOption.length(), width); i++) {
      ConsoleUtils.writeIn(selectedOption.charAt(i), x + 1 + i, y + 1);
    }

    if (focus && (threadReader == null || !threadReader.isAlive())) {
      threadReader = new Thread(() -> {
        readInput();
      });
      threadReader.start();
    }

  }

  public void renderSelectedOption() {

    // box.render();
    // if (label != null) { ConsoleUtils.writeIn(label, x + 2, y); }

    clearField();

    ConsoleUtils.showCursor();
    String selectedOption = options.get(selectedIndex);
    for (int i = 0; i < Math.min(selectedOption.length(), width); i++) {
      ConsoleUtils.writeIn(selectedOption.charAt(i), x + 1 + i, y + 1);
    }

    return;
  }

  public void clearField() {

    ConsoleUtils.hideCursor();

    for (int i = 0; i < width; i++) {
      ConsoleUtils.writeIn(Symbols.SPACE, x + 1 + i, y + 1);
    }

    return;
  }

  private void readInput() {

    try {

      Terminal terminal = consoleTerminal.getTerminal();
      NonBlockingReader reader = terminal.reader();
      ConsoleUtils.setCursorPosition(x + 1, y + 1);
      ConsoleUtils.showCursor();

      boolean exit = false;

      while (!exit && focus) {

        int key = reader.read(256);

        if (key < 0) {
          renderSelectedOption();
          continue;
        }

        switch (key) {

          case KeyCodes.RETURN:
            exit = true;
            focus = false;
            break;

          case KeyCodes.SECONDARY_UP:
            if (selectedIndex > 0) {
              selectedIndex--;
            } else {
              selectedIndex = options.size() - 1;
            }
            break;

          case KeyCodes.SECONDARY_LEFT:
            if (selectedIndex > 0) {
              selectedIndex--;
            } else {
              selectedIndex = options.size() - 1;
            }
            break;

          case KeyCodes.SECONDARY_DOWN:
            if (selectedIndex < options.size() - 1) {
              selectedIndex++;
            } else {
              selectedIndex = 0;
            }
            break;

          case KeyCodes.SECONDARY_RIGHT:
            if (selectedIndex < options.size() - 1) {
              selectedIndex++;
            } else {
              selectedIndex = 0;
            }
            break;

          default:
            break;
        }

        renderSelectedOption();
      }

      // terminal.close();

      // reader.close();
      Thread.currentThread().interrupt();

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

    ConsoleUtils.hideCursor();
    Thread.currentThread().interrupt();

    return;
  }

  public void setFocus(boolean focus) {
    this.focus = focus;
  }

  public boolean getFocus() {
    return this.focus;
  }

  public String getSelectedOption() {
    return options.get(selectedIndex);
  }

  public String getValue() {
    return options.get(selectedIndex);
  }

  public void setOptions(String[] newOptions) {
    options.clear();
    for (String option : newOptions) {
      options.add(option);
    }
    selectedIndex = 0; // Resetear la selección
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int index) {
    if (index >= 0 && index < options.size()) {
      selectedIndex = index;
    }
  }

}
