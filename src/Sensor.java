import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * represents a Sensor
 */
public class Sensor {

    private Set<Position> area;
    private int weight;


    public Sensor(List<String> playload) throws PositionException {
        String[] sensorInfos = playload.get(1).split(":");
        this.weight = Integer.parseInt(sensorInfos[1]);
        this.area = computeArea(new Position(sensorInfos[0].substring(0,1),sensorInfos[0].substring(1)),weight);
    }

    /**
     * Compute the Sensor area from a position with a certain weight
     * @param sensorPosition The inital position of the Sensor
     * @param weight The weight of the Sensor
     * @return a Set with all the positions managed by Sensor
     */
    private Set<Position> computeArea(Position sensorPosition,int weight) {
        area = new HashSet<Position>();
        for (int i = -weight ; i <= weight ; i++){
            for (int j = -weight ; j <= weight ; j++){
                int rowIndex = sensorPosition.getRow() + i;
                int columnIndex = sensorPosition.getColumn() + j;
                if(checkIndex(rowIndex,columnIndex)) area.add(new Position(rowIndex,columnIndex));
            }
        }
        return area;
    }

    /**
     * @param rowIndex
     * @param columnIndex
     * @return true if the givens index are in the Grid
     */
    private boolean checkIndex(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < 10 && columnIndex >= 0 && columnIndex < 10 ;
    }

    /**
     * Check if Sensor area constain the given Position
     * @param position given position
     * @return true if area contain position
     */
    public Boolean hasPosition(Position position) {
        return area.contains(position);
    }
}
