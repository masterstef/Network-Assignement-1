import java.util.ArrayList;
import java.util.List;
/**
 * Represent the Game Grid
 */
public class Grid {

    private List<Sensor> sensors;
    private final String[] ROW_INDEX = new String[]{"A","B","C","D","E","F","G","H","I","J"};

    public Grid() {
        this.sensors = new ArrayList<>();
    }

    /**
     * Function to add Sensor to the Grid
     */
    public void addSensor(Sensor s){
        sensors.add(s);
    }

    /**
     * Function to reset Sensors in the Grid
     */
    public void resetSensors(){
        this.sensors = new ArrayList<>();
    }

    /**
     * Function to display the Grid
     */
    public void print(){
        System.out.println("The monster is situated somewhere on the tiles marked by a cross");
        System.out.println("    1  2  3  4  5  6  7  8  9 10");
        System.out.println("   -- -- -- -- -- -- -- -- -- --");
        for (int i = 0 ; i< 10 ; i++){
            StringBuilder currentLine = new StringBuilder();
            currentLine.append(String.format("%s |", ROW_INDEX[i]));
            for (int j = 0 ; j< 10 ; j++){
                currentLine.append(checkIndex(i,j) ? " X|": "  |");
            }
            System.out.println(currentLine);
            System.out.println("   -- -- -- -- -- -- -- -- -- --");
        }
    }

    /**
     * Function to check a index in the Grid is a potential place where the ghost hides
     * @param i the row index
     * @param j the column index
     * @return true if each sensor has the position in the grid
     */
    public Boolean checkIndex(int i,int j) {
        for(Sensor sensor : sensors){
            if(!sensor.hasPosition(new Position(i,j))){
                return false;
            }
        }
        return true;
    }
}
