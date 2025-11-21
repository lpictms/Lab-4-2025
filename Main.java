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

            Function lnExp = Functions.composition(new Log(Math.E), new Exp());
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
            for (int i = 0; i<=10; i++){
                double origY = lnExpFunc.getFunctionValue(i);
                double desY = deserializebleFunc.getFunctionValue(i);
                System.out.printf("Исходный у = %.2f, полученный y = %.2f\n", origY, desY);
            }

        }catch (Exception e){
            System.out.println("Error"+e.getMessage());
        }


//
//        System.out.println("==Тестирование двусвязного списка==");
//        TabulatedFunction func = new LinkedListTabulatedFunction(1, 5.6, 8);
//        for (int i =0; i < func.getPointsCount(); i++)
//            func.setPointY(i, func.getPointX(i)*1.2);
//
//        System.out.println("Функиця: y=1.2x");
//        func.outFunction();
//
//        System.out.println("Проверка удаления точки с индексом 3");
//        func.deletePoint(3);
//        func.outFunction();
//
//        System.out.println("Проверка добавления точки (3, 4)");
//        func.addPoint(new FunctionPoint(3.0, 4.0));
//        func.outFunction();
//
//        System.out.println("Проверка установки точки (2.2, 5) на позицию с индексом 2");
//        func.setPoint(2, new FunctionPoint(2.2, 5));
//        func.outFunction();
//
//        System.out.println("Проверка getFunctionValue");
//        for (double i = -1; i < 3; i += 0.8) {
//            System.out.printf("(%.2f, %.2f) ", i, func.getFunctionValue(i));
//            System.out.println();
//        }
//
//        System.out.println("==Тестирование исключений==");
//
//        System.out.println("Некорректное создание (левая граница больше правой)");
//        try {
//            TabulatedFunction testFunc = new ArrayTabulatedFunction(34, 3, 8);
//            System.out.println("без ошибок");
//        }catch (IllegalArgumentException e){
//            System.out.println("Error " + e.getMessage());
//        }
//
//        System.out.println("обращение к точке с несуществующим индексом");
//        try {
//            func.getPoint(100);
//            System.out.println("Без ошибок");
//        }catch (FunctionPointIndexOutOfBoundsException e){
//            System.out.println("Error " + e.getMessage());
//        }
//
//        System.out.println("точка нарушает порядок возрастания");
//        try {
//            func.setPoint(2, new FunctionPoint(30,2));
//            System.out.println("без ошибок");
//        }catch (InappropriateFunctionPointException e){
//            System.out.println("Error " + e.getMessage());
//        }
//
//        try{
//            TabulatedFunction testFunc2 = new ArrayTabulatedFunction(0,2,3);
//            testFunc2.deletePoint(0);
//            testFunc2.deletePoint(0);
//            testFunc2.deletePoint(0);
//            System.out.println("без ошибок");
//        }catch (IllegalStateException e){
//            System.out.println("Error " + e.getMessage());
//        }
//
//
    }
}
