import java.util.Scanner;


public class Calculator {
    private double currentValue;

    public Calculator() {
        currentValue = 0.0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        while (true) {
            calculator.printMenu();
            System.out.print("Введите операцию и значение (например, + 5): ");

            String input = scanner.nextLine().trim();
            String[] parts = {
                input.substring(0, 1),
                input.substring(1).trim()
            };

            String operation = parts[0];
            double value;

            try {
                value = Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Пожалуйста, введите правильное число.");
                continue;
            }

            calculator.performOperation(operation, value);
        }
    }

    public void printMenu() {
        System.out.println("Доступные операции:");
        System.out.println(" + : Сложение");
        System.out.println(" - : Вычитание");
        System.out.println(" * : Умножение");
        System.out.println(" / : Деление");
        System.out.println(" C : Сброс");
        System.out.println(" s : Выход");
        System.out.println("Текущее значение: " + trimValue(currentValue));
    }

    public void performOperation(String operation, double value) {
        switch (operation.toLowerCase()) {
            case "+":
                currentValue += value;
                break;
            case "-":
                currentValue -= value;
                break;
            case "*":
                currentValue *= value;
                break;
            case "/":
                if (value == 0) {
                    System.out.println("Ошибка: Деление на ноль невозможно.");
                    return;
                }
                currentValue /= value;
                break;
            case "c":
                currentValue = 0;
                System.out.println("Калькулятор сброшен.");
                return;
            case "s":
                System.out.println("Выход из калькулятора.");
                System.exit(0);
            default:
                System.out.println("Ошибка: Неверная операция.");
                return;
        }
        System.out.println("Текущее значение: " + trimValue(currentValue));
    }

    private String trimValue(double value) {
        return value % 1.0 == 0 ? String.valueOf((int) value) : String.valueOf(value);
    }
}