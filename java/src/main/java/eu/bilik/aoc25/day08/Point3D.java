package eu.bilik.aoc25.day08;

@SuppressWarnings("StringSplitter")
public record Point3D(int x, int y, int z) {
    public static Point3D parse(String s) {
        var parts = s.split(",");
        return new Point3D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    // no need for redundant sqrt, as we only want to compare the distances
    public long euclideanDistance2(Point3D other) {
        int dx = x - other.x;
        int dy = y - other.y;
        int dz = z - other.z;
        return (long) dx * dx + (long) dy * dy + (long) dz * dz;
    }
}