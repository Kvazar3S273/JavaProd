package program;

import java.math.BigDecimal;
import java.util.Scanner;

public class Helper {

    static Scanner s = new Scanner(System.in);

    // Провіряємо чи введений символ - ціле число
    public static int readInputInt() {
        int result;
        while (true) {
            if (s.hasNextInt()) {
                result = s.nextInt();
                return result;
            } else {
                s.next();
                System.out.println("це не цифра, введіть ще раз:");
            }
        }
    }

    // Провіряємо чи введений символ - десятковий дріб
    public static double readInputDouble() {
        double result;
        while (true) {
            if (s.hasNextBigDecimal()) {
                result = s.nextDouble();
                return result;
            } else {
                s.next();
                System.out.println("це не цифра, введіть ще раз:");
            }
        }
    }
}
