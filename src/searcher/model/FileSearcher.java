package searcher.model;

import searcher.RowTableData;
import searcher.controller.MainController;
import searcher.style.Skin;
import searcher.util.FileFilter;
import searcher.util.HtmlWriter;
import searcher.util.Sorter;
import javafx.concurrent.Task;

import java.io.File;
import java.util.*;

/**
 * Program for find duplicate files in two different directories
 */
public class FileSearcher extends Task<List<RowTableData>> {

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

    public static FileSearcher createForCompare(MainController controller) {
        tempDictionary = new HashMap<>();
        FileSearcher comparer = new FileSearcher(true);
        /*constructor. if extensions undefined filter no use*/
        comparer.controller = controller;
        String[] extensions = controller.getSettings().getAllowedExtensions();
        comparer.filter = new FileFilter(extensions);
        comparer.exactWordMatch = controller.getSettings().isExactWordMatch();
        return comparer;
    }


        /*
    * minimal percent of equal letters in two words
    * that allow considering that words are similar*/
    private static final int WORD_SIMILARITY_COFF = 75;

    private MainController controller;


    private static Map<String, WordInfo> tempDictionary;

    private final List<WordInfo> dictionary;

    /*first directory path*/
    private String startDirectoryName;

    /*second directory path*/
    private String endDirectoryName;

    /*report path*/
    private String reportName;

    /*Localization*/
    private ResourceBundle resourceBundle;

    /*first directory with files which we want to check for duplicate */
    private List<FileInfo> startDirectory = new ArrayList<>();

    /*another directory where need to find duplicates files */
    private List<FileInfo> endDirectory = new ArrayList<>();

    /*show analyze by letters*/
    private boolean exactWordMatch;


    public FileInfo getFileToSearch() {
        return fileToSearch;
    }

    public void setFileToSearch(FileInfo fileToSearch) {
        this.fileToSearch = fileToSearch;
    }

    /*  file that needs to de search
    */
    private FileInfo fileToSearch;

    /*list for files matching by names and size, expect full equality*/
    private List<FileInfo> fullEquality = new ArrayList<>();

    /*list for files matching by names only*/
    private List<FileInfo> nameEquality = new ArrayList<>();

    /*list for files matching by sizes*/
    private List<FileInfo> sizeEquality = new ArrayList<>();

    /*list for files similar by names with highest similarity */
    private List<FileInfo> nameSimilarityHighest = new ArrayList<>();

    /*list for files similar by names with high similarity */
    private List<FileInfo> nameSimilarityHigh = new ArrayList<>();

    /*list for files similar by names with middle similarity */
    private List<FileInfo> nameSimilarityMiddle = new ArrayList<>();

    /*list for files similar by names with low similarity */
    private List<FileInfo> nameSimilarityLow = new ArrayList<>();

    /*list for files which no has similarities */
    private List<FileInfo> noSimilarities = new ArrayList<>();

    private List<RowTableData> report = new ArrayList<>();

    /*filter of file types*/
    private FileFilter filter;

    /*indicate that compares files in single directory*/
    private boolean singleDirCompare;

    /*show middle similarity if true*/
    private boolean showSimilarityMiddle;

    /*show low similarity if true*/
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
    protected List<RowTableData> call() throws Exception {
        this.search();
        return this.getReport();
    }

    /*getters and setters*/

    public String getStartDirectoryName() {
        return startDirectoryName;
    }

    public String getEndDirectoryName() {
        return endDirectoryName;
    }

    public void setStartDirectoryName(String startDirectoryName) {
        this.startDirectoryName = startDirectoryName;
        setReportName(startDirectoryName);
    }

    public void setEndDirectoryName(String endDirectoryName) {
        this.endDirectoryName = endDirectoryName;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setReportName(String startDirectoryName) {
        this.reportName = startDirectoryName + "\\report.html";
    }

    public String getReportName() {
        return reportName;
    }

    public FileFilter getFilter() {
        return filter;
    }

    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    public List<RowTableData> getReport() {
        return report;
    }

    public List<FileInfo> getFullEquality() {
        return fullEquality;
    }

    public List<FileInfo> getNameEquality() {
        return nameEquality;
    }

    public List<FileInfo> getSizeEquality() {
        return sizeEquality;
    }

    public List<FileInfo> getNameSimilarityHigh() {
        return nameSimilarityHigh;
    }

    public List<FileInfo> getNameSimilarityLow() {
        return nameSimilarityLow;
    }

    public List<FileInfo> getNoSimilarity() {
        return noSimilarities;
    }

    public List<FileInfo> getStartDirectory() {
        return startDirectory;
    }

    public List<FileInfo> getEndDirectory() {
        return endDirectory;
    }

    public boolean isSingleDirCompare() {
        return singleDirCompare;
    }

    public List<FileInfo> getNameSimilarityMiddle() {
        return nameSimilarityMiddle;
    }

    public List<FileInfo> getNameSimilarityHighest() {
        return nameSimilarityHighest;
    }


    public boolean isShowSimilarityMiddle() {
        return showSimilarityMiddle;
    }

    public void setShowSimilarityMiddle(boolean showSimilarityMiddle) {
        this.showSimilarityMiddle = showSimilarityMiddle;
    }

    public boolean isShowSimilarityLow() {
        return showSimilarityLow;
    }

    public void setShowSimilarityLow(boolean showSimilarityLow) {
        this.showSimilarityLow = showSimilarityLow;
    }

    public static Map<String, WordInfo> getTempDictionary() {
        return tempDictionary;
    }

    public void setExactWordMatch(boolean exactWordMatch) {
        this.exactWordMatch = exactWordMatch;
    }

    public Skin getSkin () {
        return this.controller.getSettings().getSkin();
    }


    /*this method contains main logic of comparing*/
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


    /*comparing files in directories
    * comparing for full equality is mandatory
    * in other case rest comparings will not works properly*/
    private void compareDirectories(){

        int compareWork = startDirectory.size() * endDirectory.size();

        for (FileInfo startFileInfo : startDirectory) {
            for (FileInfo endFileInfo : endDirectory) {

                if (startFileInfo == endFileInfo) {
                    this.progress.increaseWork();
                    this.progress.showProgress();
                    continue;
                }

                if (startFileInfo.nameIsEquals(endFileInfo)) {
                    RowTableData rowTableData = new RowTableData(startFileInfo, 100);
                    this.report.add(rowTableData);
                } else {
                    int similarWords = this.comparePhrases(startFileInfo.getdWords(), endFileInfo.getdWords());
                    if (similarWords > 0) {
                        RowTableData rowTableData = new RowTableData(startFileInfo, similarWords);
                        this.report.add(rowTableData);
                    }
                }

                this.progress.increaseWork();
                this.progress.showProgress();
            }
        }
    }

    /*find quantity of similar words in two List<String>, return 100 means equality */

    /*
     * find quantity of similar words in two phrases,
     * return 100 NOT means phrases equality (phrases contains equal words,
     * however the order of words may be different)
     * return 0 means that phrases are definitely indifferent
     * return value in range from 1 nj 99 means that phrases are similar in that degree */
    private int comparePhrases(List<WordInfo> phrase1, List<WordInfo> phrase2){
        Difference difference = new Difference(phrase1, phrase2);
        return difference.getСoincidence(this.exactWordMatch);
    }

    private void updateDictionaries() {

        int counter = 0;
        int sumQuantity = 0;

        for (Map.Entry<String, WordInfo> entry : tempDictionary.entrySet()) {
            WordInfo wordInfo = entry.getValue();
            wordInfo.setID(counter);
            dictionary.add(wordInfo);
            sumQuantity = sumQuantity + entry.getValue().getQuantity();
            counter++;
        }

        tempDictionary.clear();
        dictionary.clear();
    }


    /*fill map with filenames and their split names by the words */
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

    /* clear fields and collections
    * no need in multi-thread version
    * */
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
