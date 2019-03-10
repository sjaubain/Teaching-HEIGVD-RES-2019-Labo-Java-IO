package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;

import java.io.File;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 *
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

    @Override
    public void explore(File rootDirectory, IFileVisitor visitor) {

        // Pre order
        visitor.visit(rootDirectory);

        // DFS should work if there is no file
        if (!rootDirectory.exists()) return;


        File[] filesList = rootDirectory.listFiles();

        Arrays.sort(filesList);


        // Break condition
        if(filesList == null)
            return;

        for(File f : filesList) {
            // We only visit if f is a file
            if(f.isFile())
                visitor.visit(f);
            else if(f.isDirectory())
                explore(f, visitor);
        }
    }
}
