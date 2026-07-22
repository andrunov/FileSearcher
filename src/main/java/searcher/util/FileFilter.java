package searcher.util;

import java.io.File;

/**
 * Filters files and directories based on configured extensions.
 */
public class FileFilter {

    /*file extensions*/
    private String[] extensions;

    /**
     * Creates an empty filter that accepts everything.
     */
    public FileFilter() {
    }

    /**
     * Creates a filter for the provided extensions.
     *
     * @param extensions the allowed extensions
     */
    public FileFilter(String[] extensions) {
        this.extensions = extensions;
    }

    /**
     * Returns the configured extensions.
     *
     * @return the extensions array
     */
    public String[] getExtensions() {
        return extensions;
    }

    /**
     * Sets the configured extensions.
     *
     * @param extensions the extensions to allow
     */
    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    /**
     * Determines whether the supplied file path should be accepted.
     *
     * @param absoluteFilePath the absolute path to evaluate
     * @return {@code true} if the path matches the filter or is a directory
     */
    public boolean accept(String absoluteFilePath){

        if ((this.extensions==null)||(this.extensions.length == 0)) return true;

        String path = absoluteFilePath.toLowerCase();
        for (String extension : extensions) {
            if ((path.endsWith(extension) && (path.charAt(path.length()
                    - extension.length() - 1)) == '.')) {
                return true;
            }else if (new File(absoluteFilePath).isDirectory()) return true;
        }

        return false;
    }
}
