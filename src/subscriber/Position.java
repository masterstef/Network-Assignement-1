package subscriber;

import java.util.Objects;

public class Position {

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(String row, String column) throws Exception {
        this.row = getRowValue(row);
        this.column = getColumnValue(column);
    }

    private int getColumnValue(String column) throws Exception {
        if(!(Integer.parseInt(column) >= 0 && Integer.parseInt(column) < 10)) throw new Exception("wrong column index");
        return Integer.parseInt(column) - 1;
    }

    private int getRowValue(String row) throws Exception {
        switch (row){
            case "A" : return 0;
            case "B" : return 1;
            case "C" : return 2;
            case "D" : return 3;
            case "E" : return 4;
            case "F" : return 5;
            case "G" : return 6;
            case "H" : return 7;
            case "I" : return 8;
            case "J" : return 9;
            default: throw new Exception("wrong row index");
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
