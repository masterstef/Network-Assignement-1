package subscriber;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    List<Sensor> sensors;

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
        System.out.println(" The monster is situated somewhere on the tiles marked by a cross");
        System.out.println("     1  2  3  4  5  6  7  8  9 10");
        System.out.println("    -- -- -- -- -- -- -- -- -- --");
        for (int i = 0 ; i< 10 ; i++){
            StringBuilder currentLine = new StringBuilder();
            currentLine.append(String.format(" %s |", Constants.ROW_INDEX[i]));
            for (int j = 0 ; j< 10 ; j++){
                currentLine.append(checkIndex(i,j) ? " X|": "  |");
            }
            System.out.println(currentLine);
            System.out.println("    -- -- -- -- -- -- -- -- -- --");
        }
    }

    public Boolean checkIndex(int i,int j) {
        for(Sensor sensor : sensors){
            if(!sensor.hasPosition(new Position(i,j))){
                return false;
            }
        }
        return true;
    }
}
