package ch.zhaw.deeplearningjava.mdm_project_2.djl;

import static java.util.stream.Collectors.toMap;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.util.Progress;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSVDataset extends RandomAccessDataset {

    private static final Logger logger = LoggerFactory.getLogger(CSVDataset.class);

    private static final int FEATURE_LENGTH = 1014;
    private static final String ALL_CHARS =
            "abcdefghijklmnopqrstuvwxyz0123456789-,;.!?:'\"/\\|_@#$%^&*~`+ =<>()[]{}";

    private List<Character> alphabets;
    private Map<Character, Integer> alphabetsIndex;
    private List<CSVRecord> dataset;

    private CSVDataset(Builder builder) {
        super(builder);
        dataset = builder.dataset;
        alphabets = ALL_CHARS.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        alphabetsIndex =
                IntStream.range(0, alphabets.size()).boxed().collect(toMap(alphabets::get, i -> i));
    }

    public static Shape getInitializeShape() {
        return new Shape(1, ALL_CHARS.length(), FEATURE_LENGTH);
    }

    @Override
    public Record get(NDManager manager, long index) {
        NDList datum = new NDList();
        NDList label = new NDList();
        CSVRecord record = dataset.get(Math.toIntExact(index));
        // Get a single data, label pair, encode them using helpers
        datum.add(encodeData(manager, record.get("url")));
        label.add(encodeLabel(manager, record.get("isMalicious")));
        return new Record(datum, label);
    }

    @Override
    protected long availableSize() {
        return dataset.size();
    }

    private NDArray encodeData(NDManager manager, String url) {
        NDArray encoded = manager.zeros(new Shape(alphabets.size(), FEATURE_LENGTH));
        char[] arrayText = url.toCharArray();
        for (int i = 0; i < url.length(); i++) {
            if (i > FEATURE_LENGTH) {
                break;
            }
            if (alphabetsIndex.containsKey(arrayText[i])) {
                encoded.set(new NDIndex(alphabetsIndex.get(arrayText[i]), i), 1);
            }
        }
        return encoded;
    }

    private NDArray encodeLabel(NDManager manager, String isMalicious) {
        return manager.create(Float.parseFloat(isMalicious));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void prepare(Progress progress) {
    }

    public static final class Builder extends BaseBuilder<Builder> {

        List<CSVRecord> dataset;

        protected Builder self() {
            return this;
        }

        CSVDataset build() throws IOException {
            Path path = Paths.get("dataset");
            Files.createDirectories(path);
            Path csvFile = path.resolve("malicious_url_data.csv");
            if (!Files.exists(csvFile)) {
                logger.info("Downloading dataset file ...");
                URL url =
                        new URL(
                                "https://raw.githubusercontent.com/incertum/cyber-matrix-ai/master/Malicious-URL-Detection-Deep-Learning/data/url_data_mega_deep_learning.csv");
                Files.copy(url.openStream(), csvFile);
            }

            CSVFormat format =
                    CSVFormat.Builder.create(CSVFormat.DEFAULT)
                            .setHeader("url", "isMalicious")
                            .setSkipHeaderRecord(true)
                            .setIgnoreHeaderCase(true)
                            .setTrim(true)
                            .build();
            try (Reader reader = Files.newBufferedReader(csvFile);
                 CSVParser csvParser = new CSVParser(reader, format)) {
                dataset = csvParser.getRecords();
                return new CSVDataset(this);
            }
        }
    }
}
