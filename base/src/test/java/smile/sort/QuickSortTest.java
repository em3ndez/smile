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
package smile.sort;

import smile.math.MathEx;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haifeng Li
 */
public class QuickSortTest {

    double[] big = new double[1000000];
    public QuickSortTest() {
        for (int i = 0; i < big.length; i++)
            big[i] = MathEx.random();
    }


    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of sort method, of class QuickSort.
     */
    @Test
    public void testSortInt() {
        System.out.println("sort int");
        int[] data1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] order1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(order1, QuickSort.sort(data1));
        int[] data2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] order2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertArrayEquals(order2, QuickSort.sort(data2));
        int[] data3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        int[] order3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        assertArrayEquals(order3, QuickSort.sort(data3));
        int[] data4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        int[] order4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        assertArrayEquals(order4, QuickSort.sort(data4));
    }

    /**
     * Test of sort method, of class QuickSort.
     */
    @Test
    public void testSortFloat() {
        System.out.println("sort float");
        float[] data1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] order1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(order1, QuickSort.sort(data1));
        float[] data2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] order2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertArrayEquals(order2, QuickSort.sort(data2));
        float[] data3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        int[] order3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        assertArrayEquals(order3, QuickSort.sort(data3));
        float[] data4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        int[] order4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        assertArrayEquals(order4, QuickSort.sort(data4));
    }

    /**
     * Test of sort method, of class QuickSort.
     */
    @Test
    public void testSortDouble() {
        System.out.println("sort double");
        double[] data1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] order1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(order1, QuickSort.sort(data1));
        double[] data2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] order2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertArrayEquals(order2, QuickSort.sort(data2));
        double[] data3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        int[] order3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        assertArrayEquals(order3, QuickSort.sort(data3));
        double[] data4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        int[] order4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        assertArrayEquals(order4, QuickSort.sort(data4));
    }

    /**
     * Test of sort method, of class QuickSort.
     */
    @Test
    public void testSortObject() {
        System.out.println("sort object");
        Integer[] data1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] order1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(order1, QuickSort.sort(data1));
        Integer[] data2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] order2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertArrayEquals(order2, QuickSort.sort(data2));
        Integer[] data3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        int[] order3 = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9};
        assertArrayEquals(order3, QuickSort.sort(data3));
        Integer[] data4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        int[] order4 = {4, 1, 2, 3, 0, 5, 6, 7, 8, 9};
        assertArrayEquals(order4, QuickSort.sort(data4));
    }

    /**
     * Test of sort method, of class QuickSort.
     */
    @Test
    public void testSortBig() {
        System.out.println("sort big array");
        QuickSort.sort(big);
    }
}