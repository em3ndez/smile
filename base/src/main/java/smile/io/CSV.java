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
package smile.io;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.type.DataType;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;

/**
 * Reads and writes files in variations of the Comma Separated Value
 * (CSV) format.
 *
 * @author Haifeng Li
 */
public class CSV {
    /** The schema of data structure. */
    private StructType schema;
    /** The CSV file format. */
    private final CSVFormat format;
    /** Charset of file. */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Constructor.
     * Standard Comma Separated Value format,
     * as for RFC4180 but allowing empty lines.
     */
    public CSV() {
        this(CSVFormat.DEFAULT);
    }

    /**
     * Constructor.
     * @param format the CSV file format.
     */
    public CSV(CSVFormat format) {
        this.format = format;
    }

    /**
     * Sets the schema.
     * @param schema the data schema.
     * @return this object.
     */
    public CSV schema(StructType schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Sets the charset.
     * @param charset the charset of file.
     * @return this object.
     */
    public CSV charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Reads a CSV file.
     * @param path the input file path.
     * @throws IOException when fails to read the file.
     * @throws URISyntaxException when the file path syntax is wrong.
     * @return the data frame.
     */
    public DataFrame read(String path) throws IOException, URISyntaxException {
        return read(path, Integer.MAX_VALUE);
    }

    /**
     * Reads a limited number of records from a CSV file.
     * @param path the input file path.
     * @param limit the number of records to read.
     * @throws IOException when fails to read the file.
     * @throws URISyntaxException when the file path syntax is wrong.
     * @return the data frame.
     */
    public DataFrame read(String path, int limit) throws IOException, URISyntaxException {
        if (schema == null) {
            // infer the schema from top 1000 rows.
            schema = inferSchema(Input.reader(path, charset), Math.min(1000, limit));
        }

        return read(Input.reader(path, charset), limit);
    }

    /**
     * Reads a CSV file.
     * @param path the input file path.
     * @throws IOException when fails to read the file.
     * @return the data frame.
     */
    public DataFrame read(Path path) throws IOException {
        return read(path, Integer.MAX_VALUE);
    }

    /**
     * Reads a limited number of records from a CSV file.
     * @param path the input file path.
     * @param limit the number of records to read.
     * @throws IOException when fails to read the file.
     * @return the data frame.
     */
    public DataFrame read(Path path, int limit) throws IOException {
        if (schema == null) {
            // infer the schema from top 1000 rows.
            schema = inferSchema(Files.newBufferedReader(path, charset), Math.min(1000, limit));
        }

        return read(Files.newBufferedReader(path, charset), limit);
    }

    private DataFrame read(Reader reader, int limit) throws IOException {
        if (schema == null) {
            // infer the schema from top 1000 rows.
            throw new IllegalStateException("The schema is not set or inferred.");
        }

        var fields = schema.fields();
        List<Function<String, Object>> parser = schema.parser();

        try (CSVParser csv = CSVParser.parse(reader, format)) {
            List<Tuple> rows = new ArrayList<>();
            boolean[] missing = new boolean[schema.length()];

            for (CSVRecord record : csv) {
                Object[] row = new Object[fields.size()];
                for (int i = 0; i < fields.size(); i++) {
                    String s = record.get(i).trim();
                    if (!s.isEmpty()) {
                        row[i] = parser.get(i).apply(s);
                    }
                }

                // Check any missing values
                for (int j = 0; j < fields.size(); j++) {
                    if (row[j] == null) {
                        missing[j] = true;
                    }
                }

                rows.add(Tuple.of(schema, row));
                if (rows.size() >= limit) break;
            }

            // Set a field to nullable if any missing value in the column.
            for (int j = 0; j < missing.length; j++) {
                if (missing[j] && fields.get(j).dtype().isPrimitive()) {
                    var field = fields.get(j);
                    if (field.dtype() == DataTypes.IntType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableIntType, field.measure()));
                    } else if (field.dtype() == DataTypes.LongType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableLongType, field.measure()));
                    } else if (field.dtype() == DataTypes.FloatType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableFloatType, field.measure()));
                    } else if (field.dtype() == DataTypes.DoubleType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableDoubleType, field.measure()));
                    } else if (field.dtype() == DataTypes.BooleanType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableBooleanType, field.measure()));
                    } else if (field.dtype() == DataTypes.ByteType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableByteType, field.measure()));
                    } else if (field.dtype() == DataTypes.ShortType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableShortType, field.measure()));
                    } else if (field.dtype() == DataTypes.CharType) {
                        fields.set(j, new StructField(field.name(), DataTypes.NullableCharType, field.measure()));
                    }
                }
            }
            schema = new StructType(fields);
            return DataFrame.of(schema, rows);
        }
    }

    /**
     * Infer the schema from the top n rows.
     * <ol>
     *  <li>Infer type of each row.</li>
     *  <li>Merge row types to find common type</li>
     *  <li>String type by default.</li>
     * </ol>
     *
     * @param reader the file reader.
     * @param limit the number of records to read.
     * @throws IOException when fails to read the file.
     * @return the data frame.
     */
    public StructType inferSchema(Reader reader, int limit) throws IOException {
        try (CSVParser parser = CSVParser.parse(reader, format)) {
            String[] names;
            DataType[] types;

            Map<String, Integer> header = parser.getHeaderMap();
            if (header != null) {
                names = new String[header.size()];
                types = new DataType[header.size()];
                for (Map.Entry<String, Integer> column : header.entrySet()) {
                    names[column.getValue()] = column.getKey();
                }
            } else {
                Iterator<CSVRecord> iter = parser.iterator();
                if (!iter.hasNext()) {
                    throw new IOException("Empty file");
                }

                CSVRecord record = iter.next();
                names = new String[record.size()];
                types = new DataType[record.size()];
                for (int i = 0; i < names.length; i++) {
                    names[i] = String.format("V%d", i+1);
                    types[i] = DataType.infer(record.get(i).trim());
                }
            }

            int k = 0;
            for (CSVRecord record : parser) {
                for (int i = 0; i < names.length; i++) {
                    types[i] = DataType.coerce(types[i], DataType.infer(record.get(i).trim()));
                }

                if (++k >= limit) break;
            }

            StructField[] fields = new StructField[names.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = new StructField(names[i], types[i] == null ? DataTypes.StringType : types[i]);
            }
            return new StructType(fields);
        }
    }
    
    /**
     * Writes the data frame to a csv file with UTF-8 encoding.
     *
     * @param data the data frame.
     * @param path the output file path.
     * @throws IOException when fails to write the file.
     */
    public void write(DataFrame data, Path path) throws IOException {
        int p = data.schema().length();
        String[] header = new String[p];
        for (int i = 0; i < p; i++) {
            header[i] = data.schema().field(i).name();
        }

        List<String> record = new ArrayList<>(p);
        try (CSVPrinter printer = format.print(path, charset)) {
            printer.printRecord((Object[]) header);
            for (int i = 0; i < data.size(); i++) {
                Tuple row = data.get(i);
                for (int j = 0; j < p; j++) record.add(row.getString(j));
                printer.printRecord(record);
                record.clear();
            }
        }
    }
}
