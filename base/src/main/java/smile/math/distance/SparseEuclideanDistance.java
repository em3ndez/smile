/*
 * Copyright (c) 2010-2025 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile. If not, see <https://www.gnu.org/licenses/>.
 */
package smile.math.distance;

import java.io.Serial;
import java.util.Arrays;
import java.util.Iterator;
import smile.util.SparseArray;

/**
 * Euclidean distance on sparse arrays.
 *
 * @author Haifeng Li
 */
public class SparseEuclideanDistance implements Metric<SparseArray> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The weights used in weighted distance.
     */
    private double[] weight = null;

    /**
     * Constructor. Standard (unweighted) Euclidean distance.
     */
    public SparseEuclideanDistance() {
    }

    /**
     * Constructor with a given weight vector.
     * 
     * @param weight the weight vector.
     */
    public SparseEuclideanDistance(double[] weight) {
        for (double w : weight) {
            if (w < 0) {
                throw new IllegalArgumentException(String.format("Weight has to be non-negative: %f", w));
            }
        }

        this.weight = weight;
    }

    @Override
    public String toString() {
        if (weight != null) {
            return String.format("Weighted Euclidean Distance(%s)", Arrays.toString(weight));
        } else {
            return "Euclidean Distance";
        }
    }

    @Override
    public double d(SparseArray x, SparseArray y) {
        if (x.isEmpty()) {
            throw new IllegalArgumentException("SparseArray x is empty.");
        }

        if (y.isEmpty()) {
            throw new IllegalArgumentException("SparseArray y is empty.");
        }

        Iterator<SparseArray.Entry> iterX = x.iterator();
        Iterator<SparseArray.Entry> iterY = y.iterator();

        SparseArray.Entry a = iterX.hasNext() ? iterX.next() : null;
        SparseArray.Entry b = iterY.hasNext() ? iterY.next() : null;

        double dist = 0.0;

        if (weight == null) {
            while (a != null && b != null) {
                if (a.index() < b.index()) {
                    double d = a.value();
                    dist += d * d;

                    a = iterX.hasNext() ? iterX.next() : null;
                } else if (a.index() > b.index()) {
                    double d = b.value();
                    dist += d * d;

                    b = iterY.hasNext() ? iterY.next() : null;
                } else {
                    double d = a.value() - b.value();
                    dist += d * d;

                    a = iterX.hasNext() ? iterX.next() : null;
                    b = iterY.hasNext() ? iterY.next() : null;
                }
            }

            while (a != null) {
                double d = a.value();
                dist += d * d;

                a = iterX.hasNext() ? iterX.next() : null;
            }

            while (b != null) {
                double d = b.value();
                dist += d * d;

                b = iterY.hasNext() ? iterY.next() : null;
            }
        } else {
            while (a != null && b != null) {
                if (a.index() < b.index()) {
                    double d = a.value();
                    dist += weight[a.index()] * d * d;

                    a = iterX.hasNext() ? iterX.next() : null;
                } else if (a.index() > b.index()) {
                    double d = b.value();
                    dist += weight[b.index()] * d * d;

                    b = iterY.hasNext() ? iterY.next() : null;
                } else {
                    double d = a.value() - b.value();
                    dist += weight[a.index()] * d * d;

                    a = iterX.hasNext() ? iterX.next() : null;
                    b = iterY.hasNext() ? iterY.next() : null;
                }
            }

            while (a != null) {
                double d = a.value();
                dist += weight[a.index()] * d * d;

                a = iterX.hasNext() ? iterX.next() : null;
            }

            while (b != null) {
                double d = b.value();
                dist += weight[b.index()] * d * d;

                b = iterY.hasNext() ? iterY.next() : null;
            }
        }

        return Math.sqrt(dist);
    }
}
