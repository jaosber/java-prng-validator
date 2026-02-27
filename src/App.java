import consoleui.utils.ConsoleUtils;

public class App{

    public static void main(String[] args) throws Exception {

        int mainoption = 0;
        boolean exit = false;

        while (!exit) {
            
            switch (mainoption) {
                
                case 0:

                    mainoption = Program.MainMenu();
                    break;

                case 1:
                    
                    mainoption = Program.GenerateRandomNumbers();
                    break;

                case 2:
                    
                    mainoption = Program.VerifyRandomNumbers();
                    break;
                
                case 3:
                    
                    exit = true;
                    ConsoleUtils.clearConsole();
                    ConsoleUtils.showCursor();

                    break;
            
                default:
                    mainoption = 0;
                    break;
                
            }

        }

        return;
    }

    
}
