package consoleui.utils;


public class KeyCodes {

  public static final char BACKSPACE = 8;
  public static final char TAB = 9;
  public static final char ENTER = 10;
  public static final char RETURN = 13;
  public static final char ESC = 27;
  public static final char DELETE = 127;

  // i.e. if ((key == CODED) && (keyCode == UP))
  public static final int CODED = 0xffff;

  // key will be CODED and keyCode will be this value
  public static final int UP    = 0x0000; // KeyEvent.VK_UP;
  public static final int DOWN  = 0x0000; // KeyEvent.VK_DOWN;
  public static final int LEFT  = 0x0000; // KeyEvent.VK_LEFT;
  public static final int RIGHT = 0x0000; // KeyEvent.VK_RIGHT;

  public static final int SECONDARY_UP = 119; 
  public static final int SECONDARY_DOWN  = 115; 
  public static final int SECONDARY_LEFT  = 97;
  public static final int SECONDARY_RIGHT = 100; 

  // key will be CODED and keyCode will be this value
  static final int ALT     = 0x0000; // KeyEvent.VK_ALT;
  static final int CONTROL = 0x0000; // KeyEvent.VK_CONTROL;
  static final int SHIFT   = 0x0000; // KeyEvent.VK_SHIFT;

}
