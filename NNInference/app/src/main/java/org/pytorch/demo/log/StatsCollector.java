package org.pytorch.demo.log;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import org.pytorch.demo.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.io.*;


public class StatsCollector {

    private List us;
    private List fps;
    private String netName;

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "Net,us,fps";

    private static final String FORMAT_FPS = "%.2f";
    private static final String FORMAT_US = "%.2f";

    private static final String PATH_TO_STATS = Environment.getExternalStoragePublicDirectory("Download") + "/stats.csv";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public StatsCollector() {
        us = new ArrayList();
        fps = new ArrayList();

    }


    public static void createLog() {
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(PATH_TO_STATS);
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    public void log(String netName, double us, double fps) {

        this.netName = netName;
        this.us.add(us);
        this.fps.add(fps);

    }

    public void writeCSV() {
        FileWriter fileWriter = null;
        if(Files.notExists(Paths.get(PATH_TO_STATS)))
            createLog();

        try {
            fileWriter = new FileWriter(PATH_TO_STATS, true);
            Iterator<Double> it1 = us.iterator();
            Iterator<Double> it2 = fps.iterator();

            while (it1.hasNext() && it2.hasNext()) {


            fileWriter.append(netName);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.format(Locale.US,FORMAT_US,it1.next()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.format(Locale.US,FORMAT_FPS,it2.next()));
            fileWriter.append(NEW_LINE_SEPARATOR);
            }

        } catch (IOException e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
