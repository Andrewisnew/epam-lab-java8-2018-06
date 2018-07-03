package spliterators.exercise;

import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * Сплитератор, оборачивающий прямоугольную матрицу int[][]
 * Обходит элементы слева-направо, сверху-вниз
 * Деление "честное" - по количеству элементов
 */
public class FairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private static final long TRESHHOLD = 7;
    /**
     *  0  1  2  3  4
     *  5  6  / 7  8  9
     * 10 11 12 13 14
     */

    private final int[][] data;
    private int startInclusive;
    private int endExlusive;

    public FairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length * data[0].length);

    }

    private FairRectangleSpliterator(int[][] data, int startInclusive, int endExlusive){
        super(endExlusive - startInclusive, SIZED | ORDERED | NONNULL | IMMUTABLE);
        this.data = data;
        this.startInclusive = startInclusive;
        this.endExlusive = endExlusive;
    }

    @Override
    public OfInt trySplit() {
        if(estimateSize() < TRESHHOLD){
            return null;
        }
        int middle = startInclusive + (int)(estimateSize() / 2);
        return new FairRectangleSpliterator(data, startInclusive, startInclusive = middle);
    }

    @Override
    public long estimateSize() {
        return endExlusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if(startInclusive == endExlusive) {
            return false;
        }
        action.accept(data[startInclusive / data[0].length][startInclusive % data[0].length]);
        startInclusive++;
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (; startInclusive != endExlusive; startInclusive++){
            action.accept(data[startInclusive / data[0].length][startInclusive % data[0].length]);
        }
    }
}