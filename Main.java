import functions.*;
import functions.basic.*;

import java.io.*;

public class Main {
    public static void main(String[] arg) throws InappropriateFunctionPointException, IOException {
        try {
            System.out.println("== Тестирование Sin и Cos ==");
            Sin testSin = new Sin();
            Cos testCos = new Cos();

            System.out.println("Sin: ");
            for (double x = 0; x <= Math.PI; x += 0.1) {
                System.out.printf("Sin(%.2f) = %.4f \n", x, testSin.getFunctionValue(x));
            }

            System.out.println("\nCos: ");
            for (double x = 0; x <= Math.PI; x += 0.1) {
                System.out.printf("Cos(%.2f) = %.4f \n", x, testCos.getFunctionValue(x));
            }

            System.out.println("\n== Тестирование и сравнение табулированных аналогов Sin и Cos ==");
            TabulatedFunction tabulateSin = TabulatedFunctions.tabulate(testSin, 0, Math.PI, 10);
            TabulatedFunction tabulateCos = TabulatedFunctions.tabulate(testCos, 0, Math.PI, 10);
            for (double x = 0; x < Math.PI; x += 0.1) {
                double tabSin = tabulateSin.getFunctionValue(x);
                double origSin = testSin.getFunctionValue(x);
                System.out.printf("x = %.2f: исходный Sin = %.4f, таб Sin %.4f\n", x, origSin, tabSin);
            }
            System.out.println(" ");
            for (double x = 0; x < Math.PI; x += 0.1) {
                double tabCos = tabulateCos.getFunctionValue(x);
                double origCos = testCos.getFunctionValue(x);
                System.out.printf("x = %.2f: исходный Cos = %.4f, таб Cos %.4f\n", x, origCos, tabCos);
            }

            System.out.println("\n== Тестирование суммы аналогов синуса и косинуса ==");
            Function sin2 = Functions.power(tabulateSin, 2);
            Function cos2 = Functions.power(tabulateCos, 2);
            Function sumOfSquares = Functions.sum(sin2, cos2);
            for (double x = 0; x <= Math.PI; x += 0.1) {
                System.out.printf("x=%.2f: sin^2 + cos^2 = %.4f\n", x, sumOfSquares.getFunctionValue(x));
            }

            System.out.println("\n== Изменение результирующая функции при изменении кол-ва точек ==");
            int[] newPointsCount = {5, 20, 50};
            for (int i : newPointsCount) {
                TabulatedFunction newTabSin = TabulatedFunctions.tabulate(testSin, 0, Math.PI, i);
                TabulatedFunction newTabCos = TabulatedFunctions.tabulate(testCos, 0, Math.PI, i);
                Function sum = Functions.sum(Functions.power(newTabSin, 2), Functions.power(newTabCos, 2));

                double maxError = 0;
                for (double x = 0; x <= Math.PI; x += 0.1)
                    maxError = Math.max(maxError, Math.abs(sum.getFunctionValue(x) - 1.0));
                System.out.printf("Точек: %d, погрешность: %.8f\n", i, maxError);

            }

            System.out.println("\n== Экспонента и работа с файлом ==");
            TabulatedFunction expFunc = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
            FileWriter writer = new FileWriter("exp file.txt");
            TabulatedFunctions.writeTabulatedFunction(expFunc, writer);
            writer.close();

            FileReader reader = new FileReader("exp file.txt");
            TabulatedFunction newExpFunc = TabulatedFunctions.readTabulatedFunction(reader);
            reader.close();

            System.out.println("Сравнение значений исходной и считанной функций");
            for (int i = 0; i < 10; i++) {
                double origY = expFunc.getPointY(i);
                double readY = newExpFunc.getPointY(i);
                System.out.printf("Исходный y = %.2f, прочитанный y = %.2f \n", origY, readY);
            }

            System.out.println("\n== Логарифм и работа с файлом ==");
            TabulatedFunction logFunc = TabulatedFunctions.tabulate(new Log(Math.E), 0.1, 10, 11);

            FileOutputStream out = new FileOutputStream("log file.txt");
            TabulatedFunctions.outputTabulatedFunction(logFunc, out);
            out.close();

            FileInputStream in = new FileInputStream("log file.txt");
            TabulatedFunction newLogFunc = TabulatedFunctions.inputTabulatedFunction(in);
            in.close();

            System.out.println("сравнение");
            for (int i = 0; i < 10; i++) {
                double origY = logFunc.getPointY(i);
                double readY = newLogFunc.getPointY(i);
                System.out.printf("Исходный y = %.2f, прочитанный y = %.2f \n", origY, readY);
            }
            System.out.println("\n== Сериализация ==");

            Function lnExp = Functions.composition(new Exp(), new Log(Math.E));
            TabulatedFunction lnExpFunc = TabulatedFunctions.tabulate(lnExp, 0, 10, 11);

            FileOutputStream serializable = new FileOutputStream("lnExpFunc.ser");
            ObjectOutputStream outS = new ObjectOutputStream(serializable);
            outS.writeObject(lnExpFunc);
            outS.close();
            serializable.close();

            FileInputStream deserializable = new FileInputStream("lnExpFunc.ser");
            ObjectInputStream inD = new ObjectInputStream(deserializable);
            TabulatedFunction deserializebleFunc = (TabulatedFunction) inD.readObject();
            inD.close();
            deserializable.close();

            System.out.println("сравнение исходной и полученной функции");
            for (int i = 0; i <= 10; i++) {
                double origY = lnExpFunc.getFunctionValue(i);
                double desY = deserializebleFunc.getFunctionValue(i);
                System.out.printf("Исходный у = %.2f, полученный y = %.2f\n", origY, desY);
            }

        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

    }
}