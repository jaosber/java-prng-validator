package consoleui.utils;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ConsoleTerminal {

  public static Terminal terminal;
  private long startTime;
  public static long time;
  public static int consoleWidth;
  public static int consoleHeight;
  private boolean consoleResize;
  private Thread threadUpdater;
  private boolean killThread;

  private long resizeStartTime = 0;
  private static final long RESIZE_DURATION_MS = 350;

  public ConsoleTerminal() {
    try {
      terminal = TerminalBuilder.terminal();
      startTime = System.currentTimeMillis();
      consoleWidth = terminal.getWidth();
      consoleHeight = terminal.getHeight();

      consoleResize = false;
      killThread = false;
    } catch (Exception e) {
      System.err.println("Error getting console information.");
      e.printStackTrace();
    }

    threadUpdater = new Thread(() -> {
      loopUpdate();
    });

    threadUpdater.start();
  }

  public void update() {

    time = System.currentTimeMillis() - startTime;

    int[] currentSize = { terminal.getWidth(), terminal.getHeight() };

    if (consoleWidth != currentSize[0] || consoleHeight != currentSize[1]) {
      consoleResize = true;
      resizeStartTime = System.currentTimeMillis();
    } else {
      
      if (System.currentTimeMillis() - resizeStartTime >= RESIZE_DURATION_MS) {
        consoleResize = false; //después de 50 ms, vuelve a false
      }
    }

    consoleWidth = currentSize[0];
    consoleHeight = currentSize[1];
  }

  private void loopUpdate() {
    while (!killThread) {
      update();
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        System.err.println("The thread was interrupted.");
      }
    }
  }

  public void stopThreadUpdater() {
    killThread = true;
    try {
      threadUpdater.join();

    } catch (InterruptedException e) {
      System.err.println("Error waiting for thread to terminate.");
    }

  }

  public long getTime() {
    return time;
  }

  public boolean isConsoleResized() {
    return consoleResize;
  }

  public int[] getConsoleSize() {
    return new int[] { consoleWidth, consoleHeight };
  }

  public int getConsoleWidth() {
    return consoleWidth;
  }

  public int getConsoleHeight() {
    return consoleHeight;
  }

  public Terminal getTerminal() {
    return terminal;
  }

}
