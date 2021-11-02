package subscriber;

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

    public Set<Position> computeArea(Position sensorPosition,int weight) {
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

    private boolean checkIndex(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < 10 && columnIndex >= 0 && columnIndex < 10 ;
    }

    public Boolean hasPosition(Position position) {
        return area.contains(position);
    }
}
