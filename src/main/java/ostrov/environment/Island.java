package ostrov.environment;

import java.util.ArrayList;
import java.util.List;

public class Island {
    public int width, height;
    public Location[][] grid;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Location[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Location(x, y);
            }
        }
    }

    public Location getLocation(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height) ? grid[y][x] : null;
    }

    public List<Location> getNeighbors(int x, int y) {
        List<Location> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            Location neighbor = getLocation(x + dir[0], y + dir[1]);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}