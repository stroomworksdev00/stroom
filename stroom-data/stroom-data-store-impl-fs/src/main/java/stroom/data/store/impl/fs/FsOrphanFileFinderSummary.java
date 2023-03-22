package stroom.data.store.impl.fs;

import stroom.data.store.impl.fs.FsPathHelper.DecodedPath;
import stroom.util.logging.AsciiTable;
import stroom.util.logging.AsciiTable.Column;
import stroom.util.logging.LambdaLogger;
import stroom.util.logging.LambdaLoggerFactory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FsOrphanFileFinderSummary {

    private static final LambdaLogger LOGGER = LambdaLoggerFactory.getLogger(FsOrphanFileFinderSummary.class);

    private final Map<SummaryLine, AtomicLong> summaryMap = new HashMap<>();

    public void addPath(final Path path) {
        LOGGER.trace("addPath called for path {}", path);
        DecodedPath decodedPath;
        try {
            decodedPath = FsPathHelper.decodedPath(path);
        } catch (Exception e) {
            LOGGER.error("Unable to decode path {}", path, e);
            decodedPath = null;
        }
        if (decodedPath != null) {
            final SummaryLine summaryLine = new SummaryLine(decodedPath);
            final long count = summaryMap
                    .computeIfAbsent(summaryLine, k -> new AtomicLong())
                    .incrementAndGet();
            LOGGER.trace("Incremented count to {} for summaryLine: {}", count, summaryLine);
        }
    }

    @Override
    public String toString() {
        final StringBuilder summary = new StringBuilder();
        if (summaryMap.size() > 0) {
            summary.append("Summary:\n");
//
            final List<Entry<SummaryLine, AtomicLong>> sortedEntries = summaryMap.entrySet()
                    .stream()
                    .sorted(Entry.comparingByKey(Comparator
                            .comparing(SummaryLine::getType)
                            .thenComparing(SummaryLine::getFeed)
                            .thenComparing(SummaryLine::getDate)))
                    .collect(Collectors.toList());

            summary.append("\n");
            summary.append(AsciiTable.builder(sortedEntries)
                    .withColumn(Column.of("Type", entry2 -> entry2.getKey().getType()))
                    .withColumn(Column.of("File/Directory", entry2 -> entry2.getKey().isDirectory
                            ? "Dir"
                            : "File"))
                    .withColumn(Column.of("Feed (if present)", entry2 -> entry2.getKey().getFeed()))
                    .withColumn(Column.of("Date", entry2 -> entry2.getKey().getDate()))
                    .withColumn(Column.integer("Orphan Count", entry2 -> entry2.getValue().get()))
                    .build());
            summary.append("\n");

        }
        return summary.toString();
    }


    // --------------------------------------------------------------------------------


    private static class SummaryLine {

        private final String type;
        private final String feed;
        private final LocalDate date;
        private final boolean isDirectory;

        public SummaryLine(DecodedPath decodedPath) {
            this.type = decodedPath.getTypeName();
            this.feed = Objects.requireNonNullElse(decodedPath.getFeedName(), "");
            this.date = decodedPath.getDate();
            this.isDirectory = decodedPath.isDirectory();
        }

        public String getType() {
            return type;
        }

        public String getFeed() {
            return feed;
        }

        public LocalDate getDate() {
            return date;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final SummaryLine that = (SummaryLine) o;
            return isDirectory == that.isDirectory && Objects.equals(type,
                    that.type) && Objects.equals(feed, that.feed) && Objects.equals(date, that.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, feed, date, isDirectory);
        }

        @Override
        public String toString() {
            return String.join(":", feed, type, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
