package fcmclustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Representation d'une matrice
 *
 */
public final class Matrix {

    private final double[][] contents;

    /**
     * @param x
     * @param y
     */
    private Matrix(int x, int y) {
        this.contents = new double[x][y];
    }

    /**
     * @param clone
     */
    private Matrix(Matrix clone) {
        this.contents = new double[clone.getRows()][clone.getColumns()];
        for (int i = 0; i < clone.getRows(); i++) {
            for (int j = 0; j < clone.getColumns(); j++) {
                this.contents[i][j] = clone.valueAt(i, j);
            }
        }
    }

    /**
     * Obtain the current value within the matrix at the provided co-ordinates.
     *
     * @param row The row to lookup, indexed from 0.
     * @param col The column to lookup, indexed from 0.
     * @return The value located at the position {@code [row][col]}.
     */
    public double valueAt(int row, int col) {
        return this.contents[row][col];
    }

    /**
     * Obtain the row vector for the given row, indexed from 0.
     *
     * @param row The row number to obtain.
     * @return A {@code Vector} representing the row.
     */
    public Vector getRow(int row) {
        List<Double> rowList = new ArrayList<Double>(this.contents[row].length);

        for (double d : this.contents[row]) {
            rowList.add(d);
        }

        return new Vector(rowList);
    }

    /**
     * Get the number of rows within the matrix.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return this.contents.length;
    }

    /**
     * Get the number of columns within the matrix.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return this.contents[0].length;
    }

    /**
     *
     */
    public Matrix getClone() {
        Matrix matrix = new Matrix(this.getRows(), this.getColumns());

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                matrix.contents[i][j] = this.contents[i][j];
            }
        }

        return matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Matrix other = (Matrix) obj;
        return Arrays.deepEquals(this.contents, other.contents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getRows(); i++) {
            sb.append(getRow(i).toString() + '\n');
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * Obtain a builder for {@code Matrix} instances.
     *
     * @return A {@link Matrix.Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder object to help with the construction of {@link Matrix}
     * instances.
     */
    public static final class Builder {

        private int rowNumber;
        private int colNumber;
        private List<List<Double>> rows;
        private List<DataPoint> tuples;
        private boolean identity;

        private Builder() {
            reset();
        }

        /**
         * Define the dimensions (rows and columns) that the built up
         * {@code Matrix} will contain.
         *
         * @param rows The number of rows.
         * @param columns The number of columns.
         * @return The current {@code Builder}.
         * @throws IllegalArgumentException if {@code rows} or {@code columns}
         * are less than 1.
         */
        public Builder dimensions(int rows, int columns) {
            this.rowNumber = rows;
            this.colNumber = columns;
            return this;
        }

        /**
         * Add a row vector, for inclusion in the built up {@code Matrix}.
         *
         * @param columnValues The values for the columns.
         * @return the current {@code Builder}.
         * @throws IllegalArgumentException if the {@code columnValues} are not
         * the same dimensions / length that is expected.
         */
        public Builder addRow(Double... columnValues) {
            List<Double> rowVector = Arrays.asList(columnValues);
            this.rows.add(rowVector);
            return this;
        }

        /**
         * Define that the built up {@code Matrix} should be an identity
         * {@code Matrix}. All added row vectors or position values will be
         * discarded if the built up {@code Matrix} is to be an identity
         * {@code Matrix}.
         *
         * @return The current {@code Builder}.
         */
        public Builder identity() {
            this.identity = true;
            return this;
        }

        /**
         * Define that at a specific value for a defined grid point within the
         * built up {@code Matrix}.
         * <p>
         * This operation will override any values specified by a row vector
         * addition.
         *
         * @param row The row number, indexed from 0.
         * @param col The column number, indexed from 0.
         * @param value The value to be set at the defined location.
         * @return The current {@code Builder}.
         */
        public Builder valueAt(int row, int col, double value) {
            this.tuples.add(new DataPoint(row, col, value));
            return this;
        }

        /**
         * Reset the builder into a clean state.
         */
        private void reset() {
            this.rowNumber = 0;
            this.colNumber = 0;
            this.rows = new ArrayList<List<Double>>();
            this.tuples = new ArrayList<DataPoint>();
            this.identity = false;
        }

        /**
         *
         *
         * @return A new immutable {@code Matrix} instance.
         */
        public Matrix build() {
            Matrix matrix = new Matrix(rowNumber, colNumber);

            if (identity) {
                for (int i = 0; i < rowNumber; i++) {
                    for (int j = 0; j < colNumber; j++) {
                        matrix.contents[i][j] = (i == j) ? 1.0 : 0.0;
                    }
                }

                return matrix;
            }

            if (this.rows.size() >= 1) {
                for (int i = 0; i < rowNumber; i++) {
                    for (int j = 0; j < colNumber; j++) {
                        matrix.contents[i][j] = this.rows.get(i).get(j);
                    }
                }
            }

            if (this.tuples.size() >= 0) {
                for (DataPoint tuple : this.tuples) {
                    matrix.contents[tuple.getX()][tuple.getY()] = tuple.getValue();
                }
            }

            reset(); // Reset the builder to the default state, so that it may be used again, if needed.

            return matrix;
        }
    }

    /**
     * Class defining a [row, col, value] tuple for building up of a
     * {@code Matrix}.
     */
    private static final class DataPoint {

        private int x;
        private int y;
        private double value;

        public DataPoint(int x, int y, double value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * @return the contents
     */
    public double[][] getContents() {
        return contents;
    }
}
