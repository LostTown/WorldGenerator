package town.lost.worldgenerator;

/**
 * Created by peter on 23/01/16.
 */
public enum Direction {
    WENS(+1, 0, 0, +1),
    WESN(+1, 0, 0, -1),
    EWNS(-1, 0, 0, +1),
    EWSN(-1, 0, 0, -1),
    NSEW(0, +1, +1, 0),
    NSWE(0, +1, -1, 0),
    SNEW(0, -1, +1, 0),
    SNWE(0, -1, -1, 0);

    static final Direction[] ALL = values();
    final int x1, z1, x2, z2;

    Direction(int x1, int z1, int x2, int z2) {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    static int min(int v, int indent) {
        if (v == +1)
            return indent;
        if (v == -1)
            return 15 - indent;
        throw new AssertionError();
    }

    static int max(int v, int indent) {
        if (v == +1)
            return 16 - indent;
        if (v == -1)
            return indent - 1;
        throw new AssertionError();
    }

    public <T> void forEach(T t, int indent, ObjIntIntProcessor consumer) {
        if (x1 == 0) {
            for (int z = min(z1, indent), mz = max(z1, indent); z != mz; z += z1) {
                for (int x = min(x2, indent), mx = max(x2, indent); x != mx; x += x2) {
                    if (consumer.apply(t, x, z))
                        break;
                }
            }
        } else {
            for (int x = min(x1, indent), mx = max(x1, indent); x != mx; x += x1) {
                for (int z = min(z2, indent), mz = max(z2, indent); z != mz; z += z2) {
                    if (consumer.apply(t, x, z))
                        break;
                }
            }
        }
    }
}
