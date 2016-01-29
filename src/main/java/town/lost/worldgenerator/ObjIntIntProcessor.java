package town.lost.worldgenerator;

/**
 * Created by peter on 23/01/16.
 */
public interface ObjIntIntProcessor<T> {
    boolean apply(T t, int x, int z);
}
