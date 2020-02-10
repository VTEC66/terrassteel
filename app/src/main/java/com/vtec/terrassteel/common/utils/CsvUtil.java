package com.vtec.terrassteel.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;
import com.vtec.terrassteel.common.model.Imputation;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;
import com.vtec.terrassteel.database.DatabaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvUtil {

    static FileWriter writer;

    DateFormat dateFormater = new SimpleDateFormat("ddMMyyyy");
    DateFormat timeFormater = new SimpleDateFormat("HHmm");



    public void makeCSV(Context context, WorkOrder workOrder, CsvCallback csvCallback){

        String root = Environment.getExternalStorageDirectory().toString() + "/terrassteel";

        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File gpxfile = new File(root, "export_"+workOrder.getWorkOrderReference()+".csv");

        try
        {
            final CSVWriter writer = new CSVWriter(new FileWriter(gpxfile), ';',  CSVWriter.NO_QUOTE_CHARACTER);

            //String[] headerArray = {"N° AFFAIRE", "N° POINTAGE", "CODE EMPLOYE","DATE (JJMMAAAA)","HEURE DEBUT (HHMM)", "HEURE FIN (HHMM)","OBSERVATION", "RESIDUEL", "NB PIECES", "CODE FRAIS" };
            //writer.writeNext(headerArray);

            DatabaseManager.getInstance(context).getImputationsForWorkorder(workOrder, new DatabaseOperationCallBack<ArrayList<Imputation>>() {
                @Override
                public void onSuccess(ArrayList<Imputation> imputations)  {

                    for(Imputation imputation : imputations){

                        String[] lineArray = {
                                workOrder.getWorkOrderAffaire(),
                                workOrder.getWorkOrderReference(),
                                imputation.getEmployee().getEmployeeCode(),
                                String.valueOf(dateFormater.format(imputation.getImputationStart())),
                                String.valueOf(timeFormater.format(imputation.getImputationStart())),
                                String.valueOf(timeFormater.format(imputation.getImputationEnd())),
                                imputation.getObservation(),
                                "N",
                                String.valueOf(0),
                                "POSE"
                        };

                        writer.writeNext(lineArray);
                    }
                }
            });

            writer.close();
            csvCallback.onSuccess();
        }
        catch (IOException e)
        {
            csvCallback.onFail();
        }

    }



    public interface CsvCallback{

        public void onSuccess();

        public void onFail();
    }
}
