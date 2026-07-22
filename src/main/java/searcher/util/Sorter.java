package searcher.util;

import searcher.view.ReportTableRow;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides sorting helpers for report rows.
 */
public class Sorter {

    /**
     * Comparator for sorting report rows by similarity and path.
     * Used only for sorting, not for comparing individual FileInfo objects.
     */
    private static Comparator<ReportTableRow> rowTableDataComparator;

    static {
        rowTableDataComparator = new Comparator<ReportTableRow>() {
            @Override
            public int compare(ReportTableRow o1, ReportTableRow o2) {
                int result = o2.getSimilarity() - o1.getSimilarity();
                if (result == 0) {
                    result = o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
                }
                return result;
            }
        };
    }

    /**
     * Sorts the report rows by similarity score and path.
     *
     * @param reportTableRowList the rows to sort
     */
    public static void sort(List<ReportTableRow> reportTableRowList) {
        Collections.sort(reportTableRowList, rowTableDataComparator);
    }
}
