package com.vtec.terrassteel.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.vtec.terrassteel.common.model.Pointing;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.database.DatabaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvUtil {

    static FileWriter writer;

    File root = Environment.getExternalStorageDirectory();
    private File gpxfile = new File(root, "mydata.csv");

    public void makeCSV(Context context, WorkOrder workOrder){

        try {
            writer = new FileWriter(gpxfile);

            String[] headerArray = {"N° AFFAIRE", "N° POINTAGE", "CODE EMPLOYE","DATE (JJMMAAAA)","HEURE DEBUT (HHMM)", "HEURE FIN (HHMM)","OBSERVATION", "RESIDUEL", "NB PIECES", "CODE FRAIS" };

            writeCsvLine(Arrays.asList(headerArray));

            DatabaseManager.getInstance(context).getImputationsForWorkorder(workOrder, new DatabaseOperationCallBack<ArrayList<Pointing>>() {
                @Override
                public void onSuccess(ArrayList<Pointing> pointings)  {

                    for(Pointing pointing : pointings){

                        ArrayList<String> line = new ArrayList<>();

                        line.add(workOrder.getWorkOrderAffaire());
                        line.add(workOrder.getWorkOrderReference());
                        line.add(String.valueOf(pointing.getEmployee().getEmployeeId()));
                        line.add(String.valueOf(pointing.getPointingStart())); //Get Only Date JJMMAAA
                        line.add(String.valueOf(pointing.getPointingStart())); //Get Only Date HHMM
                        line.add(String.valueOf(pointing.getPointingEnd())); //Get Only Date HHMM
                        line.add(pointing.getObservation());
                        line.add("N");
                        line.add(String.valueOf(0));
                        line.add("POSE");

                        writeCsvLine(line);

                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeCsvLine(List<String> headerArray)  {

        try {

            StringBuilder line = new StringBuilder();

            for(String string : headerArray){
                line.append(string);
                line.append(",");
            }

            line.append("\n");
            writer.write(line.toString());

            Log.d(CsvUtil.class.getSimpleName(), "Success writing line : " + line.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
