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
package smile.classification;

import smile.datasets.*;
import smile.io.Read;
import smile.io.Write;
import smile.math.MathEx;
import smile.validation.*;
import smile.validation.metric.Error;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haifeng Li
 */
public class KNNTest {

    public KNNTest() {
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

    @Test
    public void testWeather() throws Exception {
        System.out.println("Weather");
        var weather = new WeatherNominal();
        ClassificationMetrics metrics = LOOCV.classification(weather.onehot(), weather.y(), KNN::fit);
        System.out.println("1-NN Error: " + metrics);
        assertEquals(8, metrics.error());

        metrics = LOOCV.classification(weather.onehot(), weather.y(), (x, y) -> KNN.fit(x, y, 3));
        System.out.println("3-NN Error: " + metrics);
        assertEquals(7, metrics.error());

        metrics = LOOCV.classification(weather.onehot(), weather.y(), (x, y) -> KNN.fit(x, y, 5));
        System.out.println("5-NN Error: " + metrics);
        assertEquals(8, metrics.error());

        metrics = LOOCV.classification(weather.onehot(), weather.y(), (x, y) -> KNN.fit(x, y,7));
        System.out.println("7-NN Error: " + metrics);
        assertEquals(5, metrics.error());
    }

    @Test
    public void testIris() throws Exception {
        System.out.println("Iris");
        var iris = new Iris();
        ClassificationMetrics metrics = LOOCV.classification(iris.x(), iris.y(), (x, y) -> KNN.fit(x, y,1));
        System.out.println("1-NN Error: " + metrics);
        assertEquals(0.96, metrics.accuracy(), 1E-4);

        metrics = LOOCV.classification(iris.x(), iris.y(), (x, y) -> KNN.fit(x, y,3));
        System.out.println("3-NN Error: " + metrics);
        assertEquals(0.96, metrics.accuracy(), 1E-4);

        metrics = LOOCV.classification(iris.x(), iris.y(), (x, y) -> KNN.fit(x, y,5));
        System.out.println("5-NN Error: " + metrics);
        assertEquals(0.9667, metrics.accuracy(), 1E-4);

        metrics = LOOCV.classification(iris.x(), iris.y(), (x, y) -> KNN.fit(x, y,7));
        System.out.println("7-NN Error: " + metrics);
        assertEquals(0.9667, metrics.accuracy(), 1E-4);
    }

    @Test
    public void testPenDigits() throws Exception {
        System.out.println("Pen Digits");
        MathEx.setSeed(19650218); // to get repeatable results.
        var pen = new PenDigits();
        var result = CrossValidation.classification(10, pen.x(), pen.y(),
                (x, y) -> KNN.fit(x, y, 3));

        System.out.println(result);
        assertEquals(0.9947, result.avg().accuracy(), 1E-4);
    }

    @Test
    public void testBreastCancer() throws Exception {
        System.out.println("Breast Cancer");

        MathEx.setSeed(19650218); // to get repeatable results.
        var cancer = new BreastCancer();
        var result = CrossValidation.classification(10, cancer.x(), cancer.y(),
                (x, y) -> KNN.fit(x, y, 3));

        System.out.println(result);
        assertEquals(0.9232, result.avg().accuracy(), 1E-4);
    }

    @Test
    public void testSegment() throws Exception {
        System.out.println("Segment");
        var segment = new ImageSegmentation();
        KNN<double[]> model = KNN.fit(segment.x(), segment.y(), 1);

        int[] prediction = model.predict(segment.testx());
        int error = Error.of(segment.testy(), prediction);

        System.out.println("Error = " + error);
        assertEquals(39, error);
    }

    @Test
    public void testUSPS() throws Exception {
        System.out.println("USPS");
        var usps = new USPS();
        KNN<double[]> model = KNN.fit(usps.x(), usps.y());

        int[] prediction = model.predict(usps.testx());
        int error = Error.of(usps.testy(), prediction);

        System.out.println("Error = " + error);
        assertEquals(113, error);

        java.nio.file.Path temp = Write.object(model);
        Read.object(temp);
    }
}
