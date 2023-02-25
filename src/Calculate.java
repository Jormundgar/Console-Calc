import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Calculate {

    public static void main(String[] args) {
        String[] arrFromLine = mismatchValue();

        Check start = new Check();

        boolean isRoman = !start.numberCheck(arrFromLine[0]) && !start.numberCheck(arrFromLine[2]);

        int x;
        int y;

        if (isRoman) {
            x = romanToNumber(arrFromLine[0]);
            y = romanToNumber(arrFromLine[2]);
        } else {
            x = Integer.decode(arrFromLine[0]);
            y = Integer.decode(arrFromLine[2]);
        }

        int result = switch (arrFromLine[1]) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            default -> x / y;
        };

        if (isRoman) {
            if (result < 0) {
                System.out.println("Результат = " + "-" + numberToRoman(Math.abs(result)));
            } else if (result > 0) {
                System.out.println("Результат = " + numberToRoman(result));
            } else System.out.println("Результат = " + result);
        } else {
            System.out.println("Результат = " + result);
        }
    }

    public static String[] enterValue() {

        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();
        String[] arrFromLine = value.split(" ");
        if (arrFromLine.length != 3 || !checkValue(arrFromLine)) throw new InputMismatchException();
        return arrFromLine;
    }

    public static boolean checkValue(String[] s) {

        boolean resultOfCheck = true;

        Check start = new Check();

        if (!start.numberCheck(s[0]) && start.romanCheck(s[0])) {
            resultOfCheck = false;
        }
        if (start.actionCheck(s[1])) {
            resultOfCheck = false;
        }
        if (!start.numberCheck(s[2]) && start.romanCheck(s[2])) {
            resultOfCheck = false;
        }
        if (!start.bothOneType(s)) {
            resultOfCheck = false;
        }
        if (start.devideByZero(s)) throw new ArithmeticException();

        return resultOfCheck;
    }

    public static String[] mismatchValue() {
        boolean count;
        String[] value = null;
        do {
            try {
                value = enterValue();
                count = false;
            } catch (InputMismatchException e) {
                System.out.println("Вводите только арабские числа (оба) от 0 до 10 или римские числа (оба) от I до X в формате [Число №1]пробел[Знак действия]пробел[Число №2]");
                count = true;
            } catch (ArithmeticException e) {
                System.out.println("Делить на ноль нельзя");
                count = true;
            }
        } while (count);
        System.out.print("\r");
        return value;
    }

    public static String numberToRoman(int number) {

        List<Roman> romanNumerals = Roman.reverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            Roman currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static int romanToNumber(String input) {

        List<Roman> romanNumerals = Roman.reverseSortedValues();

        int result = 0;
        int i = 0;

        while ((input.length() > 0) && (i < romanNumerals.size())) {
            Roman symbol = romanNumerals.get(i);
            if (input.startsWith(symbol.name())) {
                result += symbol.getValue();
                input = input.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        return result;
    }
}

enum Roman {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100),
    CD(400), D(500), CM(900), M(1000);

    private int value;

    Roman(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<Roman> reverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((Roman e) -> e.value).
                        reversed()).collect(Collectors.toList());
    }
}

class Check {

    String[] arrSymbol = {"+", "-", "*", "/"};
    String numberPattern = "^(?:10|1|[02-9])$";
    String[] arrOfRoman = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public boolean numberCheck(String s) {
        return Pattern.matches(numberPattern, s);
    }

    public boolean romanCheck(String s) {
        return Arrays.stream(arrOfRoman).noneMatch(a -> a.equals(s));
    }

    public boolean devideByZero(String[] s) {return s[1].equals("/") && s[2].equals("0");}

    public boolean actionCheck(String s) {
        return Arrays.stream(arrSymbol).noneMatch(a -> a.equals(s));
    }

    public boolean bothOneType(String[] s) {
        return ((numberCheck(s[0]) && numberCheck(s[2])) || (!romanCheck(s[0]) && !romanCheck(s[2])));
    }

}
