package socketHW1;

public class BasicCalculator implements CalculatorProtocol {
    @Override
    public int ADD(int num1, int num2) {
        return num1 + num2;
    }

    @Override
    public int SUB(int num1, int num2) {
        return num1 - num2;
    }

    @Override
    public int MUL(int num1, int num2) {
        return num1 * num2;
    }

    @Override
    public int DIV(int num1, int num2) {
        if (num2 == 0) {
        	System.out.println("Incorrect: Divided by zero");
        }
        return num1 / num2;
    }
}
