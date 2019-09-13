package main.java.com.marketsoftcommunity.util;

import java.io.*;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class LoggingUtil {

    public static void logExceptionToFile(Exception e){
        File dir = new File(System.getProperty("user.home") + "\\CMS-Logs");
        File file = new File(dir, Util.date(false) + ".txt");
        try {
            if (!dir.exists())
                System.out.println(dir.mkdirs() ? "Log directory has been successfully created!" :
                        "Log directory has not been successfully created!");
            if (!file.exists()) {
                System.out.println(file.createNewFile() ? "Error log has been successfully created!" :
                        "Error log has not been successfully created!");
            }
        } catch (IOException io){
            io.printStackTrace();
        }
        writeToFile(e, String.valueOf(file));
    }

    private static void writeToFile(Exception ex, String filename){
        try(FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(ex.getMessage() + "\n");
            for (StackTraceElement stackTraceElement : ex.getStackTrace())
                out.println(stackTraceElement);
            out.println("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
