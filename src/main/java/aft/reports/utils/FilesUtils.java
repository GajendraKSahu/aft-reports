package aft.reports.utils;

import org.codehaus.plexus.util.FileUtils;

import java.io.*;

public class FilesUtils {

    public static void copyDirectory(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            //if directory not exists, createBarChart it
            if (!dest.exists()) {
                dest.mkdir();
                /* Uncomment below code to print the copied resource information */
                System.out.println("Directory copied from " + src + "  to " + dest);
            }
            //list all the directory contents
            String files[] = src.list();
            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyDirectory(srcFile, destFile);
            }
        } else {
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            /* Uncomment below code to print the copied resource information */
            //System.out.println("File copied from " + src + " to " + dest);
        }
    }

    public static void directoryCleanUp(File file){
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFiles(File srcDir, String destPath, final String fileNameEndsWith){
        File[] files = srcDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(fileNameEndsWith);
            }
        });
        for (File file : files) {
            try {
                com.google.common.io.Files.copy(file,
                        new File(destPath + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFiles(File src, final String fileNameEndsWith){
        File[] files = src.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(fileNameEndsWith);
            }
        });
        for (File file : files) {
            file.delete();
        }
    }

}
