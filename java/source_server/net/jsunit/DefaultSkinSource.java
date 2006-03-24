package net.jsunit;

import net.jsunit.results.Skin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class DefaultSkinSource implements SkinSource {

    private Comparator<File> fileComparator = new Comparator<File>() {
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public List<Skin> getSkins() {
        List<File> skinXslFiles = skinFiles();
        Collections.sort(skinXslFiles, fileComparator);
        List<Skin> result = new ArrayList<Skin>();
        for (File skinXslFile : skinXslFiles) {
            int id = skinXslFiles.indexOf(skinXslFile);
            result.add(new Skin(id, skinXslFile));
        }
        return result;
    }

    private List<File> skinFiles() {
        return Arrays.asList(
                new File("skins").listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toUpperCase().endsWith("XSL");
                    }
                }));
    }
}
