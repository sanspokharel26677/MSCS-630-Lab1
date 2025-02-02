/**
 * Multithreaded Word Frequency Counter Program
 *
 * This program reads a text file, partitions it into multiple segments,
 * processes each segment using separate threads, and calculates the frequency of words.
 * The main thread consolidates the results and outputs the final word frequency count.
 *
 * Instructions:
 * - Provide the file path and the number of segments as inputs.
 * - Compile and run the program.
 * - Intermediate and final word frequency results will be displayed.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

// Main class to drive the program
public class WordFrequencyCounter {

    // Entry point of the program
    public static void main(String[] args) {
        // Reading input for file path and number of segments
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the text file: ");
        String filePath = scanner.nextLine();
        System.out.print("Enter the number of segments: ");
        int numSegments = scanner.nextInt();
        scanner.close();

        // Validate input and read the file content
        List<String> fileLines = readFile(filePath);
        if (fileLines == null || fileLines.isEmpty()) {
            System.out.println("File is empty or could not be read. Exiting...");
            return;
        }

        // Segment the file lines for threading
        List<List<String>> segments = createSegments(fileLines, numSegments);

        // Execute threads to process each segment
        ExecutorService executor = Executors.newFixedThreadPool(numSegments);
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();

        for (List<String> segment : segments) {
            // Submit a new thread to process each segment
            futures.add(executor.submit(new WordCountTask(segment)));
        }

        // Shutdown the executor after submitting all tasks
        executor.shutdown();

        // Collect and consolidate results from all threads
        Map<String, Integer> finalWordFrequency = new HashMap<>();
        try {
            for (Future<Map<String, Integer>> future : futures) {
                Map<String, Integer> threadResult = future.get(); // Wait for the thread to finish
                mergeWordCounts(finalWordFrequency, threadResult);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error occurred during thread execution: " + e.getMessage());
        }

        // Output the final word frequency count
        System.out.println("\nFinal Consolidated Word Frequency:");
        finalWordFrequency.forEach((word, count) -> System.out.println(word + ": " + count));
    }

    /**
     * Reads the file and returns the lines as a list of strings.
     *
     * @param filePath The path of the text file to read.
     * @return A list of lines from the file.
     */
    private static List<String> readFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Divides the file lines into N segments.
     *
     * @param lines The lines from the file.
     * @param numSegments The number of segments to create.
     * @return A list of segments, where each segment is a list of lines.
     */
    private static List<List<String>> createSegments(List<String> lines, int numSegments) {
        List<List<String>> segments = new ArrayList<>();
        int segmentSize = (int) Math.ceil((double) lines.size() / numSegments);

        for (int i = 0; i < lines.size(); i += segmentSize) {
            segments.add(lines.subList(i, Math.min(i + segmentSize, lines.size())));
        }

        return segments;
    }

    /**
     * Merges word counts from one map into another.
     *
     * @param mainMap The main map to merge into.
     * @param secondaryMap The map to merge from.
     */
    private static void mergeWordCounts(Map<String, Integer> mainMap, Map<String, Integer> secondaryMap) {
        for (Map.Entry<String, Integer> entry : secondaryMap.entrySet()) {
            mainMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    /**
     * A callable task to process a segment and compute word frequency.
     */
    static class WordCountTask implements Callable<Map<String, Integer>> {
        private final List<String> segment;

        /**
         * Constructor to initialize the segment to be processed.
         *
         * @param segment The segment of lines to process.
         */
        public WordCountTask(List<String> segment) {
            this.segment = segment;
        }

        /**
         * The main method that runs when the thread is executed.
         * Computes word frequency for the assigned segment.
         *
         * @return A map containing word frequencies.
         */
        @Override
        public Map<String, Integer> call() {
            Map<String, Integer> wordFrequency = new HashMap<>();
            for (String line : segment) {
                String[] words = line.split("\\W+"); // Split by non-word characters
                for (String word : words) {
                    if (!word.isEmpty()) {
                        word = word.toLowerCase(); // Convert to lowercase for uniformity
                        wordFrequency.merge(word, 1, Integer::sum);
                    }
                }
            }

            // Output the intermediate result for this segment
            System.out.println("\nIntermediate Word Frequency for Thread:");
            wordFrequency.forEach((word, count) -> System.out.println(word + ": " + count));

            return wordFrequency;
        }
    }
}
