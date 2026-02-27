package consoleui.utils;

import java.lang.System;
import java.util.ArrayList;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Wincon.SMALL_RECT;
import com.sun.jna.platform.win32.Wincon.CONSOLE_SCREEN_BUFFER_INFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
//import com.sun.jna.platform.win32.WinDef;
//import com.sun.jna.platform.win32.Wincon.COORD;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ConsoleUtils {

  public static <E> void printlnArrayList(ArrayList<E> arr){

    String value = "[";

    for (E item : arr) {
        value += item.toString() + ",";
    }

    if (value.length() > 1) {
        value = value.substring(0, value.length() - 1);
    }

    value += "]";

    System.out.println(value);

    return;
}

  public static void clearConsole() {

    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.flush();
  }

  public static void setCursorPosition(int x, int y) {
    System.out.printf("\033[%d;%dH", y, x);
  }

  public static void writeIn(String s, int x, int y) {
    System.out.printf("\033[%d;%dH", y, x);
    System.out.printf(s);
  }

  public static void writeIn(char c, int x, int y) {
    System.out.printf("\033[%d;%dH", y, x);
    System.out.print(c);
  }

  public static void writeIn(String s) {

    try {

      Terminal terminal = TerminalBuilder.terminal();
      int xCursor = terminal.getCursorPosition(null).getX();
      int yCursor = terminal.getCursorPosition(null).getY();

      System.out.printf("\033[%d;%dH", yCursor, xCursor);
      System.out.printf(s);
      terminal.close();

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

  }

  public static void writeIn(char c) {

    try {

      Terminal terminal = TerminalBuilder.terminal();
      int xCursor = terminal.getCursorPosition(null).getX();
      int yCursor = terminal.getCursorPosition(null).getY();

      System.out.printf("\033[%d;%dH", yCursor, xCursor);
      System.out.printf(String.valueOf(c));
      terminal.close();

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

  }

  public static void writeIn(String s, Terminal terminal) {

    try {

      int xCursor = terminal.getCursorPosition(null).getX();
      int yCursor = terminal.getCursorPosition(null).getY();

      System.out.printf("\033[%d;%dH", yCursor, xCursor);
      System.out.printf(s);
      

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

  }

  public static void writeIn(char c, Terminal terminal) {

    try {

      int xCursor = terminal.getCursorPosition(null).getX();
      int yCursor = terminal.getCursorPosition(null).getY();

      System.out.printf("\033[%d;%dH", yCursor, xCursor);
      System.out.printf(String.valueOf(c));
      

    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

  }


  public static void hideCursor() {
    System.out.print("\033[?25l");
  }

  public static void showCursor() {
    System.out.print("\033[?25h");
  }

  public static void wait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void waitForInput() {
    try {
      System.in.read();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static int[] getConsoleSizeNative() {

    // Windows
    if (System.getProperty("os.name").contains("Windows")) {

      HANDLE hConsole = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
      CONSOLE_SCREEN_BUFFER_INFO info = new CONSOLE_SCREEN_BUFFER_INFO();

      if (!Kernel32.INSTANCE.GetConsoleScreenBufferInfo(hConsole, info)) {
        System.err.println("Windows error getting console information.");
        return new int[] { 0, 0 };
      }

      SMALL_RECT rect = info.srWindow;
      int width = rect.Right - rect.Left + 1;
      int height = rect.Bottom - rect.Top + 1;
      return new int[] { width, height };

    } else {
      System.err.println("Linux error getting console information.");
      return new int[] { 0, 0 };
    }

  }

  public static int[] getConsoleSize() {

    try {

      Terminal terminal = TerminalBuilder.terminal();
      int columns = terminal.getWidth();
      int rows = terminal.getHeight();

      terminal.close();

      return new int[] { columns, rows };

    } catch (Exception e) {

      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

    return new int[] { 0, 0 };

  }

}

