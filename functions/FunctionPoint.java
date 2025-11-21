package functions;

public class FunctionPoint {
    private double x; // координата абсциссы
    private double y; // координата ординаты

    // конструктор объекта точки с заданными координатами
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    // конструктор объекта точки с коор что у указанной точки
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    // конструктор точки (0, 0)
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    // геттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // сеттеры
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}

