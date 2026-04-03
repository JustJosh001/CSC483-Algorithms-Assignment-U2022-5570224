package com.csc483.assignment.sorting;

/**
 * Holds counters for comparisons and swaps made during a sorting run.
 *
 * @author CSC483 Student
 */
public class SortMetrics {
    /** Number of element comparisons performed */
    public long comparisons = 0;

    /** Number of element swaps (or assignments) performed */
    public long swaps = 0;

    /** Resets all counters to zero */
    public void reset() {
        comparisons = 0;
        swaps       = 0;
    }

    @Override
    public String toString() {
        return String.format("comparisons=%,d  swaps=%,d", comparisons, swaps);
    }
}
