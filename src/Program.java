import java.util.ArrayList;

import consoleui.controls.ComboBox;
import consoleui.controls.Label;
import consoleui.controls.TextBlock;
import consoleui.controls.TextField;
import consoleui.controls.TextList;
import consoleui.shapes.Rectangle;
import consoleui.utils.ConsoleUtils;
import numerics.RandomGenerator;
import consoleui.utils.Colors;
import consoleui.utils.ConsoleTerminal;
import numerics.RandomnessValidation;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {

  public static int MainMenu() throws Exception {

    int option = 0;

    ConsoleTerminal consoleTerminal = new ConsoleTerminal();

    ConsoleUtils.clearConsole();
    ConsoleUtils.hideCursor();

    int initsize[] = consoleTerminal.getConsoleSize();
    Rectangle rectangle = new Rectangle(1, 1, initsize[0] - 1, initsize[1] - 1, Colors.YELLOW);
    rectangle.render();

    String menu = """
        ■
        │
        ├────── Option 1. Generate Random Numbers
        │
        ├────── Option 2. Verify Random Numbers
        │
        └────── Option 3. Exit
                """;

    Label label = new Label("RANDOM NUMBERS", 3, 3, initsize[0] - 3);
    Label labelControls = new Label("[basics keys input: a,w,s,d,enter,backspace]", 3, initsize[1] - 1);
    TextBlock textBlock = new TextBlock("Text Block", menu, 2, 5, 45, 9);
    TextField textField = new TextField(consoleTerminal, "Input", 3, 16, 20);
    textField.setFocus(true);

    label.setUnderline(true);
    label.render();
    labelControls.render();
    textBlock.render();
    textField.render();

    boolean exit = false;

    while (!exit) {

      // long time = consoleTerminal.getTime();
      // double t = ((double) time) / 1000.0;

      int currentsize[] = consoleTerminal.getConsoleSize();
      label.setText("RANDOM NUMBERS");

      if (consoleTerminal.isConsoleResized()) {

        ConsoleUtils.clearConsole();

        rectangle.setSize(currentsize[0] - 1, currentsize[1] - 1);
        label.setWidth(currentsize[0] - 3);
        labelControls.setCoord(2, initsize[1] - 1);

        // label.setText("RANDOM NUMBERS | Time: " + String.valueOf(t) + "s");

        rectangle.render();
        textBlock.render();
        textField.render();
        label.render();
        labelControls.render();

        initsize[0] = currentsize[0];
        initsize[1] = currentsize[1];
      }

      if (!textField.getFocus()) {

        try {
          option = Integer.valueOf(textField.getValue());
        } catch (Exception e) {
          option = 0;
        }

        exit = true;
      }

    }

    consoleTerminal.stopThreadUpdater();
    consoleTerminal.getTerminal().close();

    return option;
  }

  public static int GenerateRandomNumbers() throws Exception {

    String METHOD = "";
    int AMOUNT = 0;
    long SEED = 0;
    int OPTION = 0;

    int TRIGGER = 0;

    ConsoleTerminal consoleTerminal = new ConsoleTerminal();

    ConsoleUtils.clearConsole();
    ConsoleUtils.hideCursor();

    int initsize[] = consoleTerminal.getConsoleSize();
    Rectangle rectangle = new Rectangle(1, 1, initsize[0] - 1, initsize[1] - 1, Colors.YELLOW);

    String blankValidationOptions = """
         ■
        ├─── Means    []
        │
        ├─── Variance []
        │
        └─── Corrida  []""";

    String menu = "1.Copy 2.Export 3.LookNumbers 4.LookTest 5.New 6.MainMenu 7.Exit";
    String[] methodsOptions = { "Java Random", "Lehmer", "Middle Square" };

    ArrayList<String> listNumbers = new ArrayList<>();

    Label label = new Label("GENERATE RANDOM NUMBERS", 3, 3, initsize[0] - 3);
    TextList textListValidation = new TextList(consoleTerminal, "Validation Test", blankValidationOptions, 30, 5, 40,
        7);

    ComboBox comboBoxMethods = new ComboBox(consoleTerminal, "Method", methodsOptions, 3, 5, 20);
    TextField textFieldAmount = new TextField(consoleTerminal, "Amount", "50", 3, 9, 20);
    TextField textFieldSeed = new TextField(consoleTerminal, "Seed", 3, 13, 20);
    TextBlock textBlockMenu = new TextBlock("Menu", menu, 3, 17, 64, 1);
    TextField textFieldInput = new TextField(consoleTerminal, "Enter Option", 3, 21, 20);
    TextBlock textBlockLog = new TextBlock("Log", "", 26, 21, 42, 1);
    TextList textListRandomNumbers = new TextList(consoleTerminal, "Random Numbers", listNumbers, 32 + 24 + 17, 5, 18,
        17);

    label.setUnderline(true);
    textBlockMenu.setDrawBox(true);
    textBlockLog.setDrawBox(true);

    comboBoxMethods.setFocus(true);

    rectangle.render();
    label.render();
    comboBoxMethods.render();
    textFieldAmount.render();
    textFieldSeed.render();
    textBlockMenu.render();
    textFieldInput.render();
    textBlockLog.render();
    textListRandomNumbers.render();
    textListValidation.render();

    boolean EXIT = false;

    while (!EXIT) {

      int currentsize[] = consoleTerminal.getConsoleSize();
      label.setText("GENERATE RANDOM NUMBERS");
      if (consoleTerminal.isConsoleResized()) {

        ConsoleUtils.clearConsole();

        rectangle.setSize(currentsize[0] - 1, currentsize[1] - 1);
        label.setWidth(currentsize[0] - 3);

        rectangle.render();
        label.render();
        textListValidation.render();
        comboBoxMethods.render();
        textFieldAmount.render();
        textFieldSeed.render();
        textBlockMenu.render();
        textFieldInput.render();
        textBlockLog.render();
        textListRandomNumbers.render();

        initsize[0] = currentsize[0];
        initsize[1] = currentsize[1];
      }

      if (!comboBoxMethods.getFocus() && TRIGGER == 0) {

        METHOD = comboBoxMethods.getValue();
        textBlockLog.setContent("Method to use: " + METHOD);

        comboBoxMethods.setFocus(false);
        textFieldAmount.setFocus(true);
        textFieldAmount.render();
        TRIGGER++;
      }

      if (!textFieldAmount.getFocus() && TRIGGER == 1) {

        if (isNumeric(textFieldAmount)) {

          AMOUNT = Integer.parseInt(textFieldAmount.getValue());
          textFieldAmount.setFocus(false);
          textFieldSeed.setFocus(true);
          textFieldSeed.render();
          TRIGGER++;

          textBlockLog.setContent("Number of random numbers to generate: " + textFieldAmount.getValue());

        } else {
          textFieldAmount.setFocus(true);
          textFieldAmount.render();

          textBlockLog.setContent("Error: You must enter an integer value.");
        }

      }

      if (!textFieldSeed.getFocus() && TRIGGER == 2) {

        if (isNumeric(textFieldSeed)) {
          SEED = Long.parseLong(textFieldSeed.getValue());
          textFieldAmount.setFocus(false);
          textFieldInput.setFocus(true);
          textFieldInput.render();
          TRIGGER++;

          textBlockLog.setContent("Seed saved.");

          textBlockLog.setContent("Calculating...");

          generateRandomNumbers(textListRandomNumbers, textListValidation, METHOD, SEED, AMOUNT);

          textListRandomNumbers.render();
          textListValidation.render();

          textBlockLog.setContent("Random numbers generated.");

        } else {
          textFieldSeed.setFocus(true);
          textFieldSeed.render();
          textBlockLog.setContent("Error: You must enter an integer value.");
        }

      }

      if (!textFieldInput.getFocus() && TRIGGER == 3) {

        if (isNumeric(textFieldInput)) {

          OPTION = Integer.parseInt(textFieldInput.getValue());

          switch (OPTION) {

            // 1.Copy 2.Export 3.LookNumbers 4.LookTest 5.New 6.MainMenu 7.Exit

            case 1:

              textBlockLog.setContent("Getting the numbers...");
              String text = new String("");
              for (String num : textListRandomNumbers.getItems()) {
                text += num + "\n";
              }

              textBlockLog.setContent("Copying to clipboard...");

              boolean isCopied = copyToClipboard(text);

              if (isCopied) {
                textBlockLog.setContent("Numbers copied to clipboard!");
              } else {
                textBlockLog.setContent("Clipboard not supported in this environment.");
              }

              textFieldInput.clear();
              textFieldInput.setFocus(true);
              textFieldInput.render();

              break;

            case 2:

              textBlockLog.setContent("Getting the numbers...");
              String _text = new String("");
              for (String num : textListRandomNumbers.getItems()) {
                _text += num + "\n";
              }

              textBlockLog.setContent("Saving file...");

              boolean isSaved = saveToDownloads(
                  "random_numbers_" + METHOD.replaceAll("\\s+", "_").toLowerCase() + ".txt", _text);

              if (isSaved) {
                textBlockLog.setContent("File was saved in downloads!");
              } else {
                textBlockLog.setContent("Error trying to save file...");
              }

              textFieldInput.clear();
              textFieldInput.setFocus(true);
              textFieldInput.render();

              break;

            case 3:

              textListRandomNumbers.setFocus(true);
              textListRandomNumbers.render();
              TRIGGER = 4;

              textFieldInput.clear();
              textBlockLog.setContent("Looking at list of numbers.");

              break;

            case 4:

              textListValidation.setFocus(true);
              textListValidation.render();
              TRIGGER = 5;

              textFieldInput.clear();
              textBlockLog.setContent("Looking at validations.");

              break;

            case 5:

              OPTION = 1;
              EXIT = true;

              break;

            case 6:

              OPTION = 0;
              EXIT = true;
              break;

            case 7:

              OPTION = 3;
              EXIT = true;
              break;

            default:

              textFieldInput.setFocus(true);
              textFieldInput.render();
              textBlockLog.setContent("Warning: Please enter another option.");

              break;
          }

        } else {
          textFieldInput.setFocus(true);
          textFieldInput.render();
        }

      }

      if (textListRandomNumbers.getFocus() == false && TRIGGER == 4) {

        TRIGGER--;
        textFieldInput.setFocus(true);
        textFieldInput.render();
        textBlockLog.setContent("Enter an option.");
      }

      if (textListValidation.getFocus() == false && TRIGGER == 5) {

        TRIGGER--;
        textFieldInput.setFocus(true);
        textFieldInput.render();
        textBlockLog.setContent("Enter an option.");
      }

    }

    consoleTerminal.stopThreadUpdater();
    consoleTerminal.getTerminal().close();

    return OPTION;

  }

  public static int VerifyRandomNumbers() throws Exception {

    int OPTION = 0;
    int TRIGGER = 0;

    ConsoleTerminal consoleTerminal = new ConsoleTerminal();

    ConsoleUtils.clearConsole();
    ConsoleUtils.hideCursor();

    int initsize[] = consoleTerminal.getConsoleSize();
    Rectangle rectangle = new Rectangle(1, 1, initsize[0] - 1, initsize[1] - 1, Colors.YELLOW);

    String blankValidationOptions = """
         ■
        ├─── Means    []
        │
        ├─── Variance []
        │
        └─── Corrida  []""";

    String menu = "[1]Paste [2]Copy Validation [3]Look Validation [4]Main Menu [5]Exit";
    String[] inputOptions = { "Paste", "Copy Validation", "Look Validation", "Main Menu", "Exit" };

    ArrayList<String> listNumbers = new ArrayList<>();

    Label label = new Label("VERIFY RANDOM NUMBERS", 3, 3, initsize[0] - 3);
    TextBlock textBlockMenu = new TextBlock("Menu", menu, 3, 5, 70, 1);

    ComboBox comboBoxInputOptions = new ComboBox(consoleTerminal, "Option", inputOptions, 3, 9, 20);

    TextBlock textBlockLog = new TextBlock("Log", "", 3, 13, 45, 1);
    TextList textListRandomNumbers = new TextList(consoleTerminal, "Random Numbers", listNumbers, 50, 8, 50, 6);
    TextList textListValidation = new TextList(consoleTerminal, "Validation Test", blankValidationOptions, 3, 8 + 6 + 3,
        97, 9);

    label.setUnderline(true);
    textBlockMenu.setDrawBox(true);
    textBlockLog.setDrawBox(true);
    comboBoxInputOptions.setFocus(true);

    rectangle.render();
    label.render();
    comboBoxInputOptions.render();
    textBlockMenu.render();
    textBlockLog.render();
    textListRandomNumbers.render();
    textListValidation.render();

    boolean EXIT = false;

    while (!EXIT) {

      int currentsize[] = consoleTerminal.getConsoleSize();
      label.setText("VERIFY RANDOM NUMBERS");
      if (consoleTerminal.isConsoleResized()) {

        ConsoleUtils.clearConsole();

        rectangle.setSize(currentsize[0] - 1, currentsize[1] - 1);
        label.setWidth(currentsize[0] - 3);

        rectangle.render();
        label.render();
        textListValidation.render();
        comboBoxInputOptions.render();
        textBlockMenu.render();
        textBlockLog.render();
        textListRandomNumbers.render();

        initsize[0] = currentsize[0];
        initsize[1] = currentsize[1];
      }

      if (TRIGGER == 0 && comboBoxInputOptions.getFocus() == false) {

        String option = comboBoxInputOptions.getValue();

        switch (option) {

          // "Paste", "Copy Validation", Look Validation "Main Menu", "Exit"

          case "Paste":

            textBlockLog.setContent("Reading the clipboard...");
            String text = pasteFromClipboard();
            textBlockLog.setContent("Parsing the numbers...");
            ArrayList<Double> numbers = transformToNumberList(text);

            if (numbers.size() > 0) {

              textBlockLog.setContent("Calculating statistics...");
              verifyRandomNumbers(textListRandomNumbers, textListValidation, numbers);

              textListRandomNumbers.render();
              textListValidation.render();

              textBlockLog.setContent("Validations calculated correctly!");
            }else{
              textBlockLog.setContent("Warning: No numbers found in the text.");
            }

            comboBoxInputOptions.setFocus(true);
            comboBoxInputOptions.render();

            break;

          case "Copy Validation":

            textBlockLog.setContent("Getting information...");
            String _text = new String("");
            for (String token : textListValidation.getItems()) {
              _text += token + " ";
            }

            textBlockLog.setContent("Copying to clipboard...");

            boolean isCopied = copyToClipboard(_text);

            if (isCopied) {
              textBlockLog.setContent("Validations copied to clipboard!");
            } else {
              textBlockLog.setContent("Clipboard not supported in this environment.");
            }

            comboBoxInputOptions.setFocus(true);
            comboBoxInputOptions.render();

            break;

          case "Look Validation":

            TRIGGER = 1;
            textListValidation.setFocus(true);
            textListValidation.render();

            comboBoxInputOptions.setFocus(false);
            comboBoxInputOptions.render();

            break;

          case "Main Menu":

            OPTION = 0;
            EXIT = true;

            break;

          case "Exit":

            OPTION = 3;
            EXIT = true;

            break;

          default:
            break;
        }




      }

      if (TRIGGER == 1 && textListValidation.getFocus() == false){

        TRIGGER = 0;

        comboBoxInputOptions.setFocus(true);
        comboBoxInputOptions.render();

      }

    }

    consoleTerminal.stopThreadUpdater();
    consoleTerminal.getTerminal().close();

    return OPTION;
  }

  public static boolean isNumeric(TextField textField) {

    String input = textField.getValue();

    if (input == null || input.isEmpty()) {
      return false;
    }

    if (!input.matches("-?\\d+(\\.\\d+)?") || input.length() <= 0) {
      return false;
    }

    try {

      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }

  }

  public static void generateRandomNumbers(TextList tl, TextList infovalidation, String method, long seed,
      int amount) {

    RandomGenerator randomGenerator = new RandomGenerator(seed, amount);
    ArrayList<Double> numbers = new ArrayList<>();

    // Generar números según el método seleccionado
    switch (method) {
      case "Java Random":
        numbers = randomGenerator.generateWithJavaRandom();
        break;

      case "Lehmer":
        numbers = randomGenerator.generateWithLehmerLCG(seed, amount);
        break;

      case "Middle Square":
        numbers = randomGenerator.generateWithMiddleSquare();
        break;

      default:
        return;
    }

    ArrayList<String> numberStrings = new ArrayList<>();
    for (Double num : numbers) {
      numberStrings.add(String.format("%.10f", num));
    }
    tl.setItems(numberStrings); // Mostrar los números en la lista
    tl.render();

    // Realizar las pruebas de validación (Mean Test, Variance Test y Runs Test)
    boolean meansValid = RandomnessValidation.meanTestValidation(numbers);
    boolean varianceValid = RandomnessValidation.varianceTestValidation(numbers);
    boolean runsValid = RandomnessValidation.corridaTestValidation(numbers);

    // Actualizar la información de validación según los resultados
    String validationOptions = String.format("""
         ■
        ├─── Means    [%s]
        │
        ├─── Variance [%s]
        │
        └─── Corrida  [%s] \n""",
        meansValid ? "Accepted" : "Rejected",
        varianceValid ? "Accepted" : "Rejected",
        runsValid ? "Accepted" : "Rejected");

    String info1 = RandomnessValidation.getInfoMeanTestValidation(numbers);
    String info2 = RandomnessValidation.getInfoVarianceTestValidation(numbers);
    String info3 = RandomnessValidation.getInfoCorridaTestValidation(numbers);

    validationOptions += " \n" + info1 + " \n" + info2 + " \n" + info3 + " ";

    infovalidation.setContent(validationOptions);

    return;
  }

  public static void verifyRandomNumbers(TextList textListNumbers, TextList infovalidation, ArrayList<Double> numbers) {

    ArrayList<String> numberStrings = new ArrayList<>();
    for (Double num : numbers) {
      numberStrings.add(String.format("%.10f", num));
    }

    textListNumbers.setItems(numberStrings); // Mostrar los números en la lista
    textListNumbers.render();

    // realizar las pruebas de validación (Mean Test, Variance Test y Runs Test)
    boolean meansValid = RandomnessValidation.meanTestValidation(numbers);
    boolean varianceValid = RandomnessValidation.varianceTestValidation(numbers);
    boolean runsValid = RandomnessValidation.corridaTestValidation(numbers);

    // Actualizar la información de validación según los resultados
    String validationOptions = String.format("""
         ■
        ├─── Means    [%s]
        │
        ├─── Variance [%s]
        │
        └─── Corrida  [%s] \n""",
        meansValid ? "Accepted" : "Rejected",
        varianceValid ? "Accepted" : "Rejected",
        runsValid ? "Accepted" : "Rejected");

    String info1 = RandomnessValidation.getInfoMeanTestValidation(numbers);
    String info2 = RandomnessValidation.getInfoVarianceTestValidation(numbers);
    String info3 = RandomnessValidation.getInfoCorridaTestValidation(numbers);

    validationOptions += " \n" + info1 + " \n" + info2 + " \n" + info3 + " ";

    infovalidation.setContent(validationOptions);

    return;
  }

  public static boolean copyToClipboard(String text) {

    if (!GraphicsEnvironment.isHeadless()) { // Verifica si hay entorno gráfico disponible

      StringSelection stringSelection = new StringSelection(text);
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(stringSelection, null);
      // System.out.println("Text copied to clipboard!");

      return true;
    }

    return false;
  }

  public static String pasteFromClipboard() {

    if (!GraphicsEnvironment.isHeadless()) {
      try {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable content = clipboard.getContents(null);

        // verificar si el contenido es texto
        if (content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
          return (String) content.getTransferData(DataFlavor.stringFlavor);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return null; // devuelve null si no hay texto o no se puede obtener el portapapeles
  }

  public static boolean saveToDownloads(String filename, String content) throws IOException {

    String userHome = System.getProperty("user.home");
    String downloadsPath = userHome + File.separator + "Downloads";

    File file = new File(downloadsPath, filename);

    int counter = 1;
    while (file.exists()) {
      String newFilename = filename.replaceFirst("(\\.txt)$", "(" + counter + ").txt");
      file = new File(downloadsPath, newFilename);
      counter++;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(content);

      return true;
    } catch (Exception e) {

      return false;
    }

    // System.out.println("File saved in: " + file.getAbsolutePath());
  }

  public static ArrayList<Double> transformToNumberList(String input) {

    ArrayList<Double> numbers = new ArrayList<>();

    if (input == null || input.isEmpty()) {
      // System.out.println("Warning: There is no content to process.");
      return numbers;
    }

    // eliminar corchetes, llaves o paréntesis si están presentes
    input = input.replaceAll("[\\[\\]{}()]", "");

    // separar los números usando coma, punto y coma, espacio o salto de línea como
    // delimitadores
    String[] tokens = input.split("[,;\\s]+");

    // filtrar números válidos
    Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    for (String token : tokens) {

      // limpiar el token y verificar si es un número válido
      Matcher matcher = numberPattern.matcher(token.trim());
      if (matcher.matches()) {
        try {
          numbers.add(Double.parseDouble(token.trim()));
        } catch (NumberFormatException e) {
          // System.out.println("No se pudo convertir a número: " + token);
        }
      }
    }

    // si no se encontraron números, dar aviso
    if (numbers.isEmpty()) {
      //System.out.println("Warning: No numbers found in the text.");
    }

    return numbers;
  }

}
