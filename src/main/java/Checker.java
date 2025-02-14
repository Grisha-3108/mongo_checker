import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Checker {
    public static void main(String[] args) {
        int code = 3;//По умолчанию, ошибка
        try(FileReader fr = new FileReader(args[1])){
            Scanner sc = new Scanner(fr);
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if(line.equals("true")){
                    code = 0;
                    break;
                }
                if(line.equals("false")){
                    code = 1;
                    break;
                }
            }
        }catch(IOException e){
            Runtime.getRuntime().exit(2);
        }
        //Возвращаем код
        Runtime.getRuntime().exit(code);
    }
}
