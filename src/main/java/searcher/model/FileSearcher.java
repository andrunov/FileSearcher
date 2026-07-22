package searcher.model;

import searcher.view.ReportTableRow;
import searcher.controller.MainController;
import searcher.view.Skin;
import searcher.util.FileFilter;
import searcher.util.HtmlWriter;
import searcher.util.Sorter;
import javafx.concurrent.Task;

import java.io.File;
import java.util.*;

/**
 * Searches directories for files and directories that resemble a target name.
 */
public class FileSearcher extends Task<List<ReportTableRow>> {

    /**
     * Creates a file search task configured for a single search phrase.
     *
     * @param controller the main controller that launched the search
     * @return a configured search task
     */
    public static FileSearcher createForSearch(MainController controller) {
        tempDictionary = new HashMap<>();
        String searchDirPath = controller.getFirstDirectory().getAbsolutePath();
        boolean isRoot= false;
        if (searchDirPath.split("\\\\").length == 1) {
            isRoot = true;
        }
        FileSearcher comparer = new FileSearcher(isRoot);
        comparer.controller = controller;
        String[] extensions = new String[1];
        String searchPhrase = controller.getSearchPhrase();
        String extension = FileInfo.extractFileExtension(searchPhrase);
        if (extension != null) {
            extensions[0] = extension;
        } else {
            extensions = controller.getSettings().getAllowedExtensions();
        }
        comparer.filter = new FileFilter(extensions);
        comparer.exactWordMatch = controller.getSettings().isExactWordMatch();
        FileInfo fileForSearch = new FileInfo(searchPhrase);
        comparer.setFileToSearch(fileForSearch);
        comparer.setStartDirectoryName(searchDirPath);
        comparer.setResourceBundle(controller.getResourceBundle());
        return comparer;
    }

    /**
     * Creates a file search task configured for directory-to-directory comparison.
     *
     * @param controller the main controller that launched the comparison
     * @return a configured comparison task
     */
    public static FileSearcher createForCompare(MainController controller) {
        tempDictionary = new HashMap<>();
        FileSearcher comparer = new FileSearcher(true);
        comparer.controller = controller;
        String[] extensions = controller.getSettings().getAllowedExtensions();
        comparer.filter = new FileFilter(extensions);
        comparer.exactWordMatch = controller.getSettings().isExactWordMatch();
        return comparer;
    }


    /**
     * Minimum word similarity threshold for matching (in percent).
     */
    private static final int WORD_SIMILARITY_COFF = 75;

    private MainController controller;


    private static Map<String, WordInfo> tempDictionary;

    private final List<WordInfo> dictionary;

    /**
     * The starting directory path for the search.
     */
    private String startDirectoryName;

    /**
     * The target directory path for comparison.
     */
    private String endDirectoryName;

    /**
     * The report file path.
     */
    private String reportName;

    /**
     * Resource bundle for localized messages.
     */
    private ResourceBundle resourceBundle;

    /**
     * Files found in the start directory.
     */
    private List<FileInfo> startDirectory = new ArrayList<>();

    /**
     * Files in the target directory to compare against.
     */
    private List<FileInfo> endDirectory = new ArrayList<>();

    /**
     * Whether to use exact word matching or fuzzy matching.
     */
    private boolean exactWordMatch;


    /**
     * Returns the target file or directory being searched for.
     *
     * @return the target item
     */
    public FileInfo getFileToSearch() {
        return fileToSearch;
    }

    /**
     * Sets the target file or directory being searched for.
     *
     * @param fileToSearch the target item
     */
    public void setFileToSearch(FileInfo fileToSearch) {
        this.fileToSearch = fileToSearch;
    }

    /**
     * The file or directory being searched for.
     */
    private FileInfo fileToSearch;

    /**
     * Files matching by names and size (100% equality).
     */
    private List<FileInfo> fullEquality = new ArrayList<>();

    /**
     * Files matching by names only.
     */
    private List<FileInfo> nameEquality = new ArrayList<>();

    /**
     * Files matching by size only.
     */
    private List<FileInfo> sizeEquality = new ArrayList<>();

    /**
     * Files with highest name similarity.
     */
    private List<FileInfo> nameSimilarityHighest = new ArrayList<>();

    /**
     * Files with high name similarity.
     */
    private List<FileInfo> nameSimilarityHigh = new ArrayList<>();

    /**
     * Files with middle-level name similarity.
     */
    private List<FileInfo> nameSimilarityMiddle = new ArrayList<>();

    /**
     * Files with low name similarity.
     */
    private List<FileInfo> nameSimilarityLow = new ArrayList<>();

    /**
     * Files with no detected similarities.
     */
    private List<FileInfo> noSimilarities = new ArrayList<>();

    private List<ReportTableRow> report = new ArrayList<>();

    /**
     * File type filter.
     */
    private FileFilter filter;

    /**
     * Whether this is a single directory comparison.
     */
    private boolean singleDirCompare;

    /**
     * Whether to show middle-level similarity matches.
     */
    private boolean showSimilarityMiddle;

    /**
     * Whether to show low-level similarity matches.
     */
    private boolean showSimilarityLow;

    public boolean isExactWordMatch() {
        return exactWordMatch;
    }

    private Progress progress;

    private FileSearcher(boolean rootSearch) {
        this.dictionary = new ArrayList<>();
        if (rootSearch) {
            this.progress = new Progress(100000);
        } else {
            this.progress = new Progress(10000);
        }
    }


    @Override
    protected List<ReportTableRow> call() throws Exception {
        this.search();
        return this.getReport();
    }

    /**
     * Configuration and state accessors.
     */

    /**
     * Returns the starting directory path.
     *
     * @return the starting directory path
     */
    public String getStartDirectoryName() {
        return startDirectoryName;
    }

    /**
     * Returns the target directory path.
     *
     * @return the target directory path
     */
    public String getEndDirectoryName() {
        return endDirectoryName;
    }

    /**
     * Sets the starting directory path and updates the report name.
     *
     * @param startDirectoryName the starting directory path
     */
    public void setStartDirectoryName(String startDirectoryName) {
        this.startDirectoryName = startDirectoryName;
        setReportName(startDirectoryName);
    }

    /**
     * Sets the target directory path.
     *
     * @param endDirectoryName the target directory path
     */
    public void setEndDirectoryName(String endDirectoryName) {
        this.endDirectoryName = endDirectoryName;
    }

    /**
     * Sets the resource bundle used for localized messages.
     *
     * @param resourceBundle the resource bundle
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Returns the resource bundle used for localized messages.
     *
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Sets the report file path based on the provided directory.
     *
     * @param startDirectoryName the directory used to build the report path
     */
    public void setReportName(String startDirectoryName) {
        this.reportName = startDirectoryName + "\\report.html";
    }

    /**
     * Returns the report file path.
     *
     * @return the report path
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * Returns the active file filter.
     *
     * @return the file filter
     */
    public FileFilter getFilter() {
        return filter;
    }

    /**
     * Sets the active file filter.
     *
     * @param filter the file filter to use
     */
    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    /**
     * Returns the current search report rows.
     *
     * @return the report rows
     */
    public List<ReportTableRow> getReport() {
        return report;
    }

    /**
     * Returns files that matched exactly.
     *
     * @return the exact-match files
     */
    public List<FileInfo> getFullEquality() {
        return fullEquality;
    }

    /**
     * Returns files that matched by name.
     *
     * @return the name-matched files
     */
    public List<FileInfo> getNameEquality() {
        return nameEquality;
    }

    /**
     * Returns files that matched by size.
     *
     * @return the size-matched files
     */
    public List<FileInfo> getSizeEquality() {
        return sizeEquality;
    }

    /**
     * Returns files with a high similarity score.
     *
     * @return the high-similarity files
     */
    public List<FileInfo> getNameSimilarityHigh() {
        return nameSimilarityHigh;
    }

    /**
     * Returns files with a low similarity score.
     *
     * @return the low-similarity files
     */
    public List<FileInfo> getNameSimilarityLow() {
        return nameSimilarityLow;
    }

    /**
     * Returns files with no detected similarity.
     *
     * @return the files with no similarity
     */
    public List<FileInfo> getNoSimilarity() {
        return noSimilarities;
    }

    /**
     * Returns the files discovered in the start directory.
     *
     * @return the start directory files
     */
    public List<FileInfo> getStartDirectory() {
        return startDirectory;
    }

    /**
     * Returns the files discovered in the end directory.
     *
     * @return the end directory files
     */
    public List<FileInfo> getEndDirectory() {
        return endDirectory;
    }

    /**
     * Indicates whether the search compares only a single directory.
     *
     * @return {@code true} for single-directory comparisons
     */
    public boolean isSingleDirCompare() {
        return singleDirCompare;
    }

    /**
     * Returns files with a middle similarity score.
     *
     * @return the middle-similarity files
     */
    public List<FileInfo> getNameSimilarityMiddle() {
        return nameSimilarityMiddle;
    }

    /**
     * Returns files with the highest similarity score.
     *
     * @return the highest-similarity files
     */
    public List<FileInfo> getNameSimilarityHighest() {
        return nameSimilarityHighest;
    }

    /**
     * Indicates whether middle-range similarity results are shown.
     *
     * @return {@code true} when middle similarity groups are shown
     */
    public boolean isShowSimilarityMiddle() {
        return showSimilarityMiddle;
    }

    /**
     * Enables or disables the display of middle similarity results.
     *
     * @param showSimilarityMiddle {@code true} to show middle similarity results
     */
    public void setShowSimilarityMiddle(boolean showSimilarityMiddle) {
        this.showSimilarityMiddle = showSimilarityMiddle;
    }

    /**
     * Indicates whether low-range similarity results are shown.
     *
     * @return {@code true} when low similarity groups are shown
     */
    public boolean isShowSimilarityLow() {
        return showSimilarityLow;
    }

    /**
     * Enables or disables the display of low similarity results.
     *
     * @param showSimilarityLow {@code true} to show low similarity results
     */
    public void setShowSimilarityLow(boolean showSimilarityLow) {
        this.showSimilarityLow = showSimilarityLow;
    }

    /**
     * Returns the temporary dictionary used during indexing.
     *
     * @return the temporary dictionary
     */
    public static Map<String, WordInfo> getTempDictionary() {
        return tempDictionary;
    }

    /**
     * Sets whether exact word matching is used.
     *
     * @param exactWordMatch {@code true} to require exact word matching
     */
    public void setExactWordMatch(boolean exactWordMatch) {
        this.exactWordMatch = exactWordMatch;
    }

    /**
     * Returns the active skin for the current controller context.
     *
     * @return the active skin
     */
    public Skin getSkin () {
        return this.controller.getSettings().getSkin();
    }


    /**
     * Runs the full directory search and prepares the report.
     */
    public void search(){

        /* memory and performance test
        System.gc();
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        **/
        this.startDirectory = fillDirectory(this.startDirectoryName);
        this.endDirectory.add(this.fileToSearch);
        this.singleDirCompare = false;
        updateMessage(this.resourceBundle.getString("SystWords"));
        this.updateDictionaries();
        updateMessage(this.resourceBundle.getString("SearchFiles"));
        compareDirectories();
        Sorter.sort(this.report);
        if (this.controller.getSettings().isWriteHtmlReport()) {
            updateMessage(this.resourceBundle.getString("WriteReport"));
            HtmlWriter writer = new HtmlWriter(this,"UTF8");
            writer.writeHtmlReport();
        }

        /*
        long finishTime = System.currentTimeMillis();
        long memoryAfter = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        System.out.println("Memory use: " + (memoryAfter - memoryBefore) + " mb");
        System.out.println("Performance: " + (finishTime - startTime) + " ms");
        System.gc();
        **/

    }


    /**
     * Compares files in directories for matching patterns.
     * Full equality comparison is mandatory, as other comparisons depend on it.
     */
    private void compareDirectories(){

        for (FileInfo startFileInfo : startDirectory) {
            for (FileInfo endFileInfo : endDirectory) {

                if (startFileInfo == endFileInfo) {
                    this.progress.increaseWork();
                    this.progress.showProgress();
                    continue;
                }

                if (startFileInfo.nameIsEquals(endFileInfo)) {
                    ReportTableRow reportTableRow = new ReportTableRow(startFileInfo, 100);
                    this.report.add(reportTableRow);
                } else {
                    int similarWords = this.comparePhrases(startFileInfo.getdWords(), endFileInfo.getdWords());
                    if (similarWords > 0) {
                        ReportTableRow reportTableRow = new ReportTableRow(startFileInfo, similarWords);
                        this.report.add(reportTableRow);
                    }
                }

                this.progress.increaseWork();
                this.progress.showProgress();
            }
        }
    }

    /**
     * Compares two lists of words and returns a similarity percentage.
     * Returns 100 for identical lists.
     *
     * @param phrase1 the first word list
     * @param phrase2 the second word list
     * @return similarity score (0-100), where 0 means completely different,
     *         1-99 means similar in that degree, and 100 means identical
     */
    private int comparePhrases(List<WordInfo> phrase1, List<WordInfo> phrase2){
        Difference difference = new Difference(phrase1, phrase2);
        return difference.getСoincidence(this.exactWordMatch);
    }

    private void updateDictionaries() {

        int counter = 0;

        for (Map.Entry<String, WordInfo> entry : tempDictionary.entrySet()) {
            WordInfo wordInfo = entry.getValue();
            wordInfo.setID(counter);
            dictionary.add(wordInfo);
            counter++;
        }

        tempDictionary.clear();
        dictionary.clear();
    }


    /**
     * Populates the dictionary with filenames split by words.
     *
     * @param directoryPath the directory to read files from
     * @return a list of FileInfo objects from the directory
     */
    private List<FileInfo> fillDirectory(String directoryPath){
        String message = String.format("%s: %s",this.resourceBundle.getString("ReadDir"), directoryPath );
        updateMessage(message);
        List<FileInfo> result = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.isDirectory()){

            //only root has slash at end
            if (!directoryPath.endsWith("\\")) {
                directoryPath = String.format("%s\\", directoryPath);
            }

            String[] filePaths = directory.list();
             if (filePaths != null) {
                 this.progress.increaseCompleteWork(directory.list().length);
                 for (String filePath : filePaths) {

                    this.progress.increaseWork();
                    this.progress.showProgress();

                    String absoluteFilePath = String.format("%s%s", directoryPath, filePath);
                    if (this.filter.accept(absoluteFilePath)) {

                        File file = new File(absoluteFilePath);
                        if (file.isFile()) {
                            result.add(new FileInfo(absoluteFilePath, filePath, file.length(), false));
                        } else if (file.isDirectory()) {
                            result.add(new FileInfo(absoluteFilePath, filePath, file.length(), true));
                            result.addAll(fillDirectory(absoluteFilePath));
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Clears the current search state and collected data.
     * (Not needed in multi-threaded versions)
     */
    public void clean() {
        this.startDirectoryName = null;
        this.endDirectoryName = null;
        this.startDirectory.clear();
        this.endDirectory.clear();
        this.report.clear();
    }

    @Override
    protected void succeeded() {
        this.controller.setReportName(this.reportName);
        this.controller.showResult(this.getReport());
        Thread.currentThread().interrupt();
    }

    class Progress {

        int workDone;

        int achievedWork;

        int workToBeDone;

        Progress(int workToBeDone) {
            this.workDone = 0;
            this.workToBeDone = workToBeDone;
        }

        void increaseWork() {
            workDone++;
        }

        void increaseCompleteWork(int value) {
           double achievedProgress = (double) this.workDone  / this.workToBeDone;
           this.workToBeDone = this.workToBeDone + value;
           this.achievedWork = (int) (achievedProgress * this.workToBeDone);
        }

        void showProgress() {
           if (this.workDone > this.achievedWork) {
               updateProgress(workDone, workToBeDone);
           } else {
               updateProgress(this.achievedWork, this.workToBeDone);
           }
        }

    }

}
