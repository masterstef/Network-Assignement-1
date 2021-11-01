package subscriber;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    List<Sensor> sensors;
    private final String[] rowIndex = new String[]{"A","B","C","D","E","F","G","H","I","J"};

    public Grid() {
        this.sensors = new ArrayList<>();
    }

    public void addSensor(Sensor s){
        sensors.add(s);
    }

    public void resetSensors(){
        this.sensors = new ArrayList<>();
    }

    public void printGrid(){

        Boolean[][] grid = new Boolean[10][10];

        System.out.println("     1  2  3  4  5  6  7  8  9 10");
        System.out.println("    -- -- -- -- -- -- -- -- -- --");
        for (int i = 0 ; i< grid.length ; i++){
            StringBuilder currentLine = new StringBuilder();
            currentLine.append(String.format(" %s |", rowIndex[i]));
            for (int j = 0 ; j< grid[i].length ; j++){
                currentLine.append(checkIndex(i,j) ? " X|": "  |");
            }
            System.out.println(currentLine);
            System.out.println("    -- -- -- -- -- -- -- -- -- --");
        }
    }

    private Boolean checkIndex(int i,int j) {
        for(Sensor sensor : sensors){
            if(!sensor.hasPosition(new Position(i,j))){
                return false;
            }
        }
        return true;
    }
}
