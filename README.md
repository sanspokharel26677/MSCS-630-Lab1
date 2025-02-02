# MSCS-630-Lab1

# Multithreaded Word Frequency Counter

## Overview
This program reads a text file, partitions it into multiple segments, processes each segment concurrently using separate threads, and computes word frequency counts. Once all threads complete execution, the main process consolidates the results and outputs the final word-frequency count.

This program demonstrates the use of multithreading to enhance performance by processing multiple segments of a file simultaneously.

## Program Design

The program follows these main steps:
1. **Input Handling:**
   - The program prompts the user to enter the file path and the number of segments (`N`).

2. **File Segmentation:**
   - The file is divided into `N` segments. Each segment consists of approximately equal portions of the file's lines.

3. **Thread Processing:**
   - Each thread processes its assigned segment by computing word frequencies. Intermediate results are stored in a word-frequency map.

4. **Result Consolidation:**
   - The main process waits for all threads to finish execution.
   - It then merges the intermediate results to generate the final word frequency count.

5. **Output:**
   - The program displays intermediate word-frequency results for each thread and the final consolidated word frequency.

## Instructions to Run the Program

### **Requirements**
- Java Development Kit (JDK) 17 or later.
- A text file for input.

### **Compilation and Execution**
1. **Compile the program:**
   ```sh
   javac WordFrequencyCounter.java
2. Run the program
   java WordFrequencyCounter
3. Provide input when prompted
 * Enter the full path of the text file
 * Entr the number of segments (N) to divide the file
 
#Notes
1. Ensure that the text file exists and is readable before running the program.
2. The number of segments should be appropriate for the file size to achieve optimal performance.
3. The program handles case-insensitive word counting and ignores empty words.

