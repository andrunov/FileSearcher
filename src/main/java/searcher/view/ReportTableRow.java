package searcher.view;

import searcher.model.FileInfo;

/**
 * Representation for report table row
 */
public class ReportTableRow {

    /**
     * Creates a view model for a matched file or directory.
     *
     * @param fileInfo the underlying file metadata
     * @param similarity the similarity score for the match
     */
    public ReportTableRow(FileInfo fileInfo, int similarity) {
        this.fileInfo = fileInfo;
        this.similarity = similarity;
    }


    private FileInfo fileInfo;

    int similarity;

    /**
     * Returns the backing file metadata.
     *
     * @return the file information
     */
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * Returns the similarity percentage for the current match.
     *
     * @return similarity score in percent
     */
    public int getSimilarity() {
        return similarity;
    }

    /**
     * Formats the similarity value for display in the table.
     *
     * @return a percentage string for the UI
     */
    public String getPercSimilarity() {
        return String.format("  %s%s" ,similarity,"%");
    }

    /**
     * Returns the parent directory path of the matched item.
     *
     * @return the base folder path
     */
    public String getBaseFolderPath() {
        return this.fileInfo.getBaseFolderPath();
    }

    /**
     * Returns the formatted folder path for UI rendering.
     *
     * @return the folder path prefixed with a folder icon
     */
    public String getBaseFolderPathFormatted() {
        return String.format("\uD83D\uDCC1 %s",this.fileInfo.getBaseFolderPath());
    }

    /**
     * Returns the display name of the matched item.
     *
     * @return the name with an icon based on item type
     */
    public String getName() {
        if (this.isDirectory()) return String.format("\uD83D\uDCC2 %s", this.fileInfo.getName());
        else return String.format("\uD83D\uDCC4 %s", this.fileInfo.getName());
    }

    /**
     * Returns the formatted size for the matched file.
     *
     * @return the size as a UI-friendly string, or an empty string for directories
     */
    public String getSizeFormatted() {
        if (this.isDirectory()) return "";
        else return String.format("%s  ",this.fileInfo.getSizeFormatted());
    }


    /**
     * Returns the absolute path of the matched item.
     *
     * @return the absolute path
     */
    public String getAbsolutePath() {
        return this.fileInfo.getAbsolutePath();
    }

    /**
     * Indicates whether the matched item is a directory.
     *
     * @return {@code true} for directories, otherwise {@code false}
     */
    public boolean isDirectory() {
        return this.fileInfo.isDirectory();
    }


}
