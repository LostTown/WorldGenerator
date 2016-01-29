package town.lost.worldgenerator;

/**
 * Created by peter on 23/01/16.
 */
public class XZ {
    private static final int M0 = 0x5bc80bad;
    private static final int M1 = 0xea7585d7;

    final int x, z;

    public XZ(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int hashCode() {
        return x * M0 + z * M1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof XZ
                && ((XZ) obj).x == x
                && ((XZ) obj).z == z;
    }

    public XZ adjXZ(int dx, int dz) {
        return new XZ(x + dx, z + dz);
    }

    @Override
    public String toString() {
        return "XZ(" + x + ", " + z + ')';
    }
}
