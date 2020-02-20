package com.vtec.terrassteel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vtec.terrassteel.common.model.Assign;
import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Job;
import com.vtec.terrassteel.common.model.Imputation;
import com.vtec.terrassteel.common.model.Order;
import com.vtec.terrassteel.common.model.OrderStatus;
import com.vtec.terrassteel.common.model.Picture;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
import com.vtec.terrassteel.core.manager.SessionManager;
import com.vtec.terrassteel.core.model.DefaultResponse;
import com.vtec.terrassteel.core.task.DatabaseOperationCallBack;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = DatabaseManager.class.getSimpleName();

    // Database Info
    private static final String DATABASE_NAME = "mainDatabase";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_CONSTRUCTIONS = "constructions";
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_WORK_ORDER = "work_order";
    private static final String TABLE_ASSIGN = "assign";
    private static final String TABLE_IMPUTATION = "imputation";
    private static final String TABLE_ORDER = "_order";
    private static final String TABLE_PICTURE = "picture";


    // Construction Table Columns
    private static final String KEY_CONSTRUCTION_ID_PK = "constructionIdPk";
    private static final String KEY_CONSTRUCTION_CUSTOMER = "customerIdFk";
    private static final String KEY_CONSTRUCTION_NAME = "constructionName";
    private static final String KEY_CONSTRUCTION_STATUS = "constructionStatus";


    // Employee Table Columns
    private static final String KEY_EMPLOYEE_ID_PK = "employeeIdPk";
    private static final String KEY_EMPLOYEE_NAME = "employeeName";
    private static final String KEY_EMPLOYEE_CODE = "employeeCode";
    private static final String KEY_EMPLOYEE_JOB = "employeeJob";
    private static final String KEY_EMPLOYEE_ADDRESS1 = "employeeAddress1";
    private static final String KEY_EMPLOYEE_ADDRESS2 = "employeeAddress2";
    private static final String KEY_EMPLOYEE_ZIP = "employeeZip";
    private static final String KEY_EMPLOYEE_CITY = "employeeCity";
    private static final String KEY_EMPLOYEE_PHONE = "employeePhone";
    private static final String KEY_EMPLOYEE_MAIL = "employeeMail";


    // Work Order Table Columns
    private static final String KEY_WORK_ORDER_ID_PK = "workOrderIdPk";
    private static final String KEY_WORK_ORDER_CONSTRUCTION_ID_FK = "constructionIdFk";
    private static final String KEY_WORK_ORDER_REFERENCE = "workOrderReference";
    private static final String KEY_WORK_ORDER_AFFAIRE = "workOrderAffaire";
    private static final String KEY_WORK_ORDER_PRODUCT_TYPE = "workOrderProductType";
    private static final String KEY_WORK_ORDER_ALLOCATED_TIME = "workOrderAllocatedTime";
    private static final String KEY_WORK_ORDER_STATUS = "workOrderStatus";


    // Assign Table
    private static final String KEY_ASSIGN_ID_PK = "assignIdPk";
    private static final String KEY_ASSIGN_WORKORDER_ID_FK = "workOrderIdFk";
    private static final String KEY_ASSIGN_EMPLOYEE_ID_FK = "employeeIdFk";
    private static final String KEY_ASSIGN_IS_WORKING = "isWorking";


    // Imputation Table
    private static final String KEY_IMPUTATION_ID_PK = "imputationIdPk";
    private static final String KEY_IMPUTATION_ASSIGN_ID = "imputationAssignId";
    private static final String KEY_IMPUTATION_EMPLOYEE_ID = "imputationEmployeeId";
    private static final String KEY_IMPUTATION_WORK_ORDER_ID_FK = "imputationWorkOrderIdFk";
    private static final String KEY_IMPUTATION_START_TIME = "imputationStart";
    private static final String KEY_IMPUTATION_END_TIME = "imputationEnd";


    // Order Table Columns
    private static final String KEY_ORDER_ID_PK = "orderIdPk";
    private static final String KEY_ORDER_CODE = "orderCode";
    private static final String KEY_ORDER_CUSTOMER  = "orderCustomer";
    private static final String KEY_ORDER_STATUS = "orderStatus";

    // Order Table Columns
    private static final String KEY_PICTURE_ID_PK = "pictureIdPk";
    private static final String KEY_PICTURE_NAME = "pictureNmae";
    private static final String KEY_ORDER_ID_FK = "orderIdFk";




    private static DatabaseManager sInstance;


    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONSTRUCTIONS_TABLE = "CREATE TABLE " + TABLE_CONSTRUCTIONS +
                "(" +
                KEY_CONSTRUCTION_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CONSTRUCTION_CUSTOMER + " TEXT," + // Define a foreign key
                KEY_CONSTRUCTION_NAME + " TEXT," +
                KEY_CONSTRUCTION_STATUS + " TEXT" +
                ")";


        String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES +
                "(" +
                KEY_EMPLOYEE_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_EMPLOYEE_NAME + " TEXT," +
                KEY_EMPLOYEE_CODE + " TEXT," +
                KEY_EMPLOYEE_JOB + " TEXT," +
                KEY_EMPLOYEE_ADDRESS1 + " TEXT," +
                KEY_EMPLOYEE_ADDRESS2 + " TEXT," +
                KEY_EMPLOYEE_ZIP + " TEXT," +
                KEY_EMPLOYEE_CITY + " TEXT," +
                KEY_EMPLOYEE_PHONE + " TEXT," +
                KEY_EMPLOYEE_MAIL + " TEXT" +
                ")";

        String CREATE_WORK_ORDER_TABLE = "CREATE TABLE " + TABLE_WORK_ORDER +
                "(" +
                KEY_WORK_ORDER_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_WORK_ORDER_CONSTRUCTION_ID_FK + " INTEGER REFERENCES " + TABLE_CONSTRUCTIONS + "," + // Define a foreign key
                KEY_WORK_ORDER_REFERENCE + " TEXT," +
                KEY_WORK_ORDER_AFFAIRE + " TEXT," +
                KEY_WORK_ORDER_PRODUCT_TYPE + " TEXT," +
                KEY_WORK_ORDER_ALLOCATED_TIME + " TEXT," +
                KEY_WORK_ORDER_STATUS + " TEXT" +
                ")";

        String CREATE_ASSIGN_TABLE = "CREATE TABLE " + TABLE_ASSIGN +
                "(" +
                KEY_ASSIGN_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ASSIGN_WORKORDER_ID_FK + " INTEGER REFERENCES " + TABLE_WORK_ORDER + "," + // Define a foreign key
                KEY_ASSIGN_EMPLOYEE_ID_FK + " INTEGER REFERENCES " + TABLE_EMPLOYEES + "," + // Define a foreign key
                KEY_ASSIGN_IS_WORKING + " INTEGER" +
                ")";

        String CREATE_IMPUTATION_TABLE = "CREATE TABLE " + TABLE_IMPUTATION +
                "(" +
                KEY_IMPUTATION_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_IMPUTATION_ASSIGN_ID + " INTEGER," +
                KEY_IMPUTATION_WORK_ORDER_ID_FK + " INTEGER REFERENCES " + TABLE_WORK_ORDER + "," + // Define a foreign key
                KEY_IMPUTATION_EMPLOYEE_ID + " INTEGER," + // Define a foreign key
                KEY_IMPUTATION_START_TIME + " INTEGER," +
                KEY_IMPUTATION_END_TIME + " INTEGER" +
                ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER +
                "(" +
                KEY_ORDER_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ORDER_CODE + " TEXT," +
                KEY_ORDER_CUSTOMER + " TEXT," + // Define a foreign key
                KEY_ORDER_STATUS + " TEXT " +
                ")";

        String CREATE_PICTURE_TABLE = "CREATE TABLE " + TABLE_PICTURE +
                "(" +
                KEY_PICTURE_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PICTURE_NAME + " TEXT," +
                KEY_ORDER_ID_FK + " INTEGER REFERENCES " + TABLE_ORDER + //define a foreign key
                ")";

        db.execSQL(CREATE_CONSTRUCTIONS_TABLE);
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_WORK_ORDER_TABLE);
        db.execSQL(CREATE_ASSIGN_TABLE);
        db.execSQL(CREATE_IMPUTATION_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_PICTURE_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTRUCTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMPUTATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);

            onCreate(db);
        }
    }


    public void addConstruction(Construction construction, DatabaseOperationCallBack<DefaultResponse> callBack) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_CONSTRUCTION_CUSTOMER, construction.getCustomer());
            values.put(KEY_CONSTRUCTION_NAME, construction.getConstructionName());
            values.put(KEY_CONSTRUCTION_CUSTOMER, construction.getCustomer());
            values.put(KEY_CONSTRUCTION_STATUS, construction.getConstructionStatus().name());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_CONSTRUCTIONS, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New construction successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add construction into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }


    public void getAllConstructions(DatabaseOperationCallBack<ArrayList<Construction>> callBack) {
        ArrayList<Construction> constructions = new ArrayList<>();

        String CONSTRUCTIONS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_CONSTRUCTIONS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CONSTRUCTIONS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Construction newConstruction =
                            new Construction()
                                    .withConstructionId(cursor.getLong(cursor.getColumnIndex(KEY_CONSTRUCTION_ID_PK)))
                                    .withConstructionName(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_NAME)))
                                    .withCustomer(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_CUSTOMER)))
                                    .withConstructionStatus(ConstructionStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_STATUS))));

                    constructions.add(newConstruction);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get constructions from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + constructions.size() + " constructions.");

        callBack.onSuccess(constructions);
    }




    public void addEmployee(Employee employee, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_EMPLOYEE_NAME, employee.getEmployeeName());
            values.put(KEY_EMPLOYEE_JOB, employee.getEmployeeJob().name());
            values.put(KEY_EMPLOYEE_CODE, employee.getEmployeeCode());
            values.put(KEY_EMPLOYEE_ADDRESS1, employee.getEmployeeAddress1());
            values.put(KEY_EMPLOYEE_ADDRESS2, employee.getEmployeeAddress2());
            values.put(KEY_EMPLOYEE_ZIP, employee.getEmployeeZip());
            values.put(KEY_EMPLOYEE_CITY, employee.getEmployeeCity());
            values.put(KEY_EMPLOYEE_PHONE, employee.getEmployeePhone());
            values.put(KEY_EMPLOYEE_MAIL, employee.getEmployeeEmail());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_EMPLOYEES, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New Employee successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add Employee into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void getAllEmployees(DatabaseOperationCallBack<ArrayList<Employee>> callBack) {
        ArrayList<Employee> employees = new ArrayList<>();

        String EMPLOYEES_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_EMPLOYEES);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(EMPLOYEES_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Employee employee =
                            new Employee()
                                    .withEmployeeId(cursor.getLong(cursor.getColumnIndex(KEY_EMPLOYEE_ID_PK)))
                                    .withEmployeeName(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME)))
                                    .withEmployeeCode(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CODE)))
                                    .withEmployeeJob(Job.valueOf(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_JOB))))
                                    .withEmployeeAddress1(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS1)))
                                    .withEmployeeAddress2(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS2)))
                                    .withEmployeeZip(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ZIP)))
                                    .withEmployeeCity(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CITY)))
                                    .withEmployeePhone(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_PHONE)))
                                    .withEmployeeEmail(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_MAIL)));

                    employees.add(employee);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get employees from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + employees.size() + " employees.");
        callBack.onSuccess(employees);
    }

    public void editEmployee(Employee employee, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_EMPLOYEE_NAME, employee.getEmployeeName());
            values.put(KEY_EMPLOYEE_JOB, employee.getEmployeeJob().name());
            values.put(KEY_EMPLOYEE_CODE, employee.getEmployeeCode());
            values.put(KEY_EMPLOYEE_ADDRESS1, employee.getEmployeeAddress1());
            values.put(KEY_EMPLOYEE_ADDRESS2, employee.getEmployeeAddress2());
            values.put(KEY_EMPLOYEE_ZIP, employee.getEmployeeZip());
            values.put(KEY_EMPLOYEE_CITY, employee.getEmployeeCity());
            values.put(KEY_EMPLOYEE_PHONE, employee.getEmployeePhone());
            values.put(KEY_EMPLOYEE_MAIL, employee.getEmployeeEmail());

            db.update(TABLE_EMPLOYEES
                    , values
                    , KEY_EMPLOYEE_ID_PK + "=" + employee.getEmployeeId()
                    ,null);

            db.setTransactionSuccessful();

            Log.d(TAG, "Employee have been successfuly edited into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to edit Employe into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }


    public void addWorkOrder(WorkOrder workOrder, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_WORK_ORDER_REFERENCE, workOrder.getWorkOrderReference());
            values.put(KEY_WORK_ORDER_AFFAIRE, workOrder.getWorkOrderAffaire());
            values.put(KEY_WORK_ORDER_ALLOCATED_TIME, workOrder.getWorkOrderAllocatedHour());
            values.put(KEY_WORK_ORDER_STATUS, workOrder.getWorkOrderStatus().name());
            values.put(KEY_WORK_ORDER_CONSTRUCTION_ID_FK, workOrder.getConstruction().getConstructionId());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            workOrder.workOrderId = db.insertOrThrow(TABLE_WORK_ORDER, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New Work order successfuly added into database");
            db.endTransaction();

            addAssign(new Assign(workOrder, null), callBack);
            //callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add Work order into database : " + e.toString());
            callBack.onError();
            db.endTransaction();
        }
    }


    public void getAllWorkOrderForConstruction(Construction construction, DatabaseOperationCallBack<ArrayList<WorkOrder>> callBack) {

        ArrayList<WorkOrder> workOrders = new ArrayList<>();

        String WORK_ORDER_SELECT_QUERY =
             String.format("SELECT * FROM %s WHERE %s = '%d'",
                        TABLE_WORK_ORDER, KEY_WORK_ORDER_CONSTRUCTION_ID_FK, construction.getConstructionId());

        Log.e(TAG, WORK_ORDER_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WORK_ORDER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    WorkOrder workOrder =
                            new WorkOrder()
                                    .withWorkOrderId(cursor.getLong(cursor.getColumnIndex(KEY_WORK_ORDER_ID_PK)))
                                    .withWorkOrderReference(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_REFERENCE)))
                                    .withWorkOrderAffaire(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_AFFAIRE)))
                                    .withWorkOrderAllocatedHour(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ORDER_ALLOCATED_TIME)))
                                    .withworkOrderStatus(WorkOrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_STATUS))))
                                    .withConstruction(getConstructionWithId(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ORDER_CONSTRUCTION_ID_FK))));


                    workOrders.add(workOrder);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get work order from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + workOrders.size() + " work orders.");
        callBack.onSuccess(workOrders);
    }



    private Construction getConstructionWithId(int constructionId) throws Exception {
        SQLiteDatabase db = getWritableDatabase();

        String CONSTRUCTION_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE " + KEY_CONSTRUCTION_ID_PK + " = %d",
                        TABLE_CONSTRUCTIONS, constructionId);

        Cursor cursor = db.rawQuery(CONSTRUCTION_SELECT_QUERY, null);

        cursor.moveToFirst();

        Construction newConstruction =
                new Construction()
                        .withConstructionId(cursor.getLong(cursor.getColumnIndex(KEY_CONSTRUCTION_ID_PK)))
                        .withConstructionName(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_NAME)))
                        .withCustomer(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_CUSTOMER)))
                        .withConstructionStatus(ConstructionStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_STATUS))));


        if (!cursor.isClosed()) {
            cursor.close();
        }

        return newConstruction;
    }


    public void addAssign(Assign assign, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_ASSIGN_WORKORDER_ID_FK, assign.withWorkOrder.getWorkOrderId());
            if(assign.getEmployee() != null){
                values.put(KEY_ASSIGN_EMPLOYEE_ID_FK, assign.employee.getEmployeeId());
            }
            values.put(KEY_ASSIGN_IS_WORKING, 0);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ASSIGN, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New assign successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add assign into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }


    public void getAllAssignForConstruction(Construction consrtrucion, DatabaseOperationCallBack<ArrayList<Assign>> callBack) {

        ArrayList<Assign> assigns = new ArrayList<>();

        String ASSIGN_SELECT_QUERY =
                String.format("SELECT * FROM "+ TABLE_ASSIGN + " assign " +
                                "INNER JOIN "+ TABLE_WORK_ORDER + " wo " +
                                "ON wo.workOrderIdPk = assign.workOrderIdFk " +
                                "WHERE wo.workOrderStatus LIKE '"+ WorkOrderStatus.IN_PROGRESS.name()+"' " +
                                "AND wo.constructionIdFk = " + consrtrucion.getConstructionId() +
                                " ORDER BY wo.workOrderIdPk ASC"
                            );

        Log.e(TAG, ASSIGN_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ASSIGN_SELECT_QUERY, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Assign assign =
                            new Assign()
                                    .withAssignId(cursor.getLong(cursor.getColumnIndex(KEY_ASSIGN_ID_PK)))
                                    .withWorkOrder(getWorkOrderWithId(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_WORKORDER_ID_FK))))
                                    .withEmployee(getEmployeeWithId(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_EMPLOYEE_ID_FK))))
                                    .isWorking(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_IS_WORKING)) > 0);


                    assigns.add(assign);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get assign from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + assigns.size() + " assign.");
        callBack.onSuccess(assigns);
    }

    private Employee getEmployeeWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();

        Employee employee = null;

        String EMPLOYEE_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE " + KEY_EMPLOYEE_ID_PK + " = %d",
                        TABLE_EMPLOYEES, id);

        Cursor cursor = db.rawQuery(EMPLOYEE_SELECT_QUERY, null);

        if(cursor != null && cursor.moveToFirst()){

            employee  =
                    new Employee()
                            .withEmployeeId(cursor.getLong(cursor.getColumnIndex(KEY_EMPLOYEE_ID_PK)))
                            .withEmployeeName(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME)))
                            .withEmployeeJob(Job.valueOf(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_JOB))))
                            .withEmployeeCode(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CODE)))
                            .withEmployeeAddress1(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS1)))
                            .withEmployeeAddress2(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS2)))
                            .withEmployeeZip(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ZIP)))
                            .withEmployeeCity(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CITY)))
                            .withEmployeePhone(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_PHONE)))
                            .withEmployeeEmail(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_MAIL)));

        }


        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return employee;
    }

    private WorkOrder getWorkOrderWithId(int id) throws Exception {
        SQLiteDatabase db = getWritableDatabase();

        String WORKORDER_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE " + KEY_WORK_ORDER_ID_PK + " = %d",
                        TABLE_WORK_ORDER, id);

        Cursor cursor = db.rawQuery(WORKORDER_SELECT_QUERY, null);

        cursor.moveToFirst();

        WorkOrder workOrder =
                new WorkOrder()
                        .withWorkOrderId(cursor.getLong(cursor.getColumnIndex(KEY_WORK_ORDER_ID_PK)))
                        .withWorkOrderReference(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_REFERENCE)))
                        .withWorkOrderAffaire(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_AFFAIRE)))
                        .withWorkOrderAllocatedHour(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ORDER_ALLOCATED_TIME)))
                        .withworkOrderStatus(WorkOrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_STATUS))))
                        .withConstruction(getConstructionWithId(cursor.getInt(cursor.getColumnIndex(KEY_WORK_ORDER_CONSTRUCTION_ID_FK))));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return workOrder;
    }



    public void getAvailableEmployee(DatabaseOperationCallBack<ArrayList<Employee>> callBack) {

        ArrayList<Employee> employees = new ArrayList<>();

        String EMPLOYEE_SELECT_QUERY = "SELECT * " +
                                "FROM "+ TABLE_EMPLOYEES + " employee " +
                                "WHERE employee.employeeIdPk NOT IN " +
                                    "(SELECT emp.employeeIdPk " +
                                    "FROM "+ TABLE_EMPLOYEES + " emp " +
                                    "INNER JOIN "+ TABLE_ASSIGN + " assign " +
                                    "ON emp.employeeIdPk = assign.employeeIdFk " +
                                    "INNER JOIN "+ TABLE_WORK_ORDER + " wo " +
                                    "ON wo.workOrderIdPk = assign.workOrderIdFk " +
                                    "WHERE wo.workOrderStatus LIKE '"+ WorkOrderStatus.IN_PROGRESS.name()+"')";


        Log.e(TAG, EMPLOYEE_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(EMPLOYEE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Employee employee =
                            new Employee()
                                    .withEmployeeId(cursor.getLong(cursor.getColumnIndex(KEY_EMPLOYEE_ID_PK)))
                                    .withEmployeeName(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME)))
                                    .withEmployeeJob(Job.valueOf(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_JOB))))
                                    .withEmployeeAddress1(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS1)))
                                    .withEmployeeAddress2(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS2)))
                                    .withEmployeeZip(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ZIP)))
                                    .withEmployeeCity(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CITY)))
                                    .withEmployeePhone(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_PHONE)))
                                    .withEmployeeEmail(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_MAIL)));

                    employees.add(employee);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get employees from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Available : ");
        for(Employee emp : employees){
            Log.d(TAG, emp.employeeName + " = id"+ emp.getEmployeeId());
        }

        Log.d(TAG, "Database successfully return " + employees.size() + " employees.");
        callBack.onSuccess(employees);
    }

    public void getAssignedEmployee(WorkOrder workOrder, DatabaseOperationCallBack<ArrayList<Employee>> callBack) {

        ArrayList<Employee> employees = new ArrayList<>();

        String EMPLOYEE_SELECT_QUERY = "SELECT * " +
                "FROM "+ TABLE_EMPLOYEES + " emp " +
                "INNER JOIN "+ TABLE_ASSIGN + " assign " +
                "ON emp.employeeIdPk = assign.employeeIdFk " +
                "WHERE assign.workOrderIdFk = "+  workOrder.workOrderId ;


        Log.e(TAG, EMPLOYEE_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(EMPLOYEE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Employee employee =
                            new Employee()
                                    .withEmployeeId(cursor.getLong(cursor.getColumnIndex(KEY_EMPLOYEE_ID_PK)))
                                    .withEmployeeName(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME)))
                                    .withEmployeeJob(Job.valueOf(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_JOB))))
                                    .withEmployeeAddress1(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS1)))
                                    .withEmployeeAddress2(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ADDRESS2)))
                                    .withEmployeeZip(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_ZIP)))
                                    .withEmployeeCity(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_CITY)))
                                    .withEmployeePhone(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_PHONE)))
                                    .withEmployeeEmail(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_MAIL)));

                    employees.add(employee);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get employees from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + employees.size() + " employees.");

        Log.d(TAG, "Assigned : ");
        for(Employee emp : employees){
            Log.d(TAG, emp.employeeName + " = id"+ emp.getEmployeeId());
        }


        callBack.onSuccess(employees);
    }

    public Assign getAssign(WorkOrder workOrder, Employee employee){

        Assign assign = null;

        String SELECT_ASSIGN_QUERY = "SELECT * " +
                "FROM "+ TABLE_ASSIGN +
                " WHERE employeeIdFk = " + employee.getEmployeeId() +
                " AND workOrderIdFk = " + workOrder.getWorkOrderId();

        Log.e(TAG, SELECT_ASSIGN_QUERY);

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(SELECT_ASSIGN_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                assign = new Assign()
                                .withAssignId(cursor.getLong(cursor.getColumnIndex(KEY_ASSIGN_ID_PK)))
                                .withWorkOrder(getWorkOrderWithId(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_WORKORDER_ID_FK))))
                                .withEmployee(getEmployeeWithId(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_EMPLOYEE_ID_FK))))
                                .isWorking(cursor.getInt(cursor.getColumnIndex(KEY_ASSIGN_IS_WORKING)) > 0);

                Log.e(TAG, "Database successfuly return an Assign");
            }
        }catch (Exception e){
            Log.e(TAG, "Database Fail to return an Assign");
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return assign;
    }

    public void deleteAssign(WorkOrder workOrder, Employee employee, DatabaseOperationCallBack<DefaultResponse> callBack) {

        Assign assign = getAssign(workOrder, employee);

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            String DELETE_ASSIGN_QUERY = "DELETE " +
                    "FROM "+ TABLE_ASSIGN +
                    " WHERE assignIdPk = " + assign.getAssignId();

            Log.e(TAG, DELETE_ASSIGN_QUERY);

            db.execSQL(DELETE_ASSIGN_QUERY);

            Imputation imputation = getLastImputationForAssign(assign.getAssignId());

            if(imputation.getImputationEnd() == 0) {

                stopImputation(imputation, new DatabaseOperationCallBack<DefaultResponse>() {
                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {
                        Log.e(TAG, "Stop imputation : " + assign.getAssignId());
                    }
                });
            }


        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete assign from database : " + e.toString());
            callBack.onError();
        } finally {
            db.close();
        }

        callBack.onSuccess(new DefaultResponse());
    }



    public Imputation getLastImputationForAssign(long assignId) {

        Imputation imputation = null;

        String IMPUTATION_SELECT_QUERY = "SELECT * " +
                "FROM "+ TABLE_IMPUTATION  +
                " WHERE imputationAssignId = "+  assignId +
                " AND imputationStart = (" +
                " SELECT MAX(imputationStart)" +
                        " FROM "+ TABLE_IMPUTATION  +
                        " WHERE imputationAssignId = "+  assignId + ")";

        Log.e(TAG, IMPUTATION_SELECT_QUERY);

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(IMPUTATION_SELECT_QUERY, null);

        try {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                imputation = new Imputation()
                        .withImputationId(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_ID_PK)))
                        .withImputationStart(cursor.getInt(cursor.getColumnIndex(KEY_IMPUTATION_START_TIME)))
                        .withImputationEnd(cursor.getInt(cursor.getColumnIndex(KEY_IMPUTATION_END_TIME)));


                Log.e(TAG, "The last imputation have been return for assign ");

            }else{
                Log.e(TAG, "No imputation data for Work order assign");
            }

        }catch (Exception e){
            Log.e(TAG, "Error while trying to get imputation from database : " + e.toString());
        }finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return imputation;
    }

    public void startImputation(Assign assign, DatabaseOperationCallBack<DefaultResponse> callBack) {

        Imputation imputation = new Imputation()
                    .withAssign(assign)
                    .withWorkOrder(assign.getWorkOrder())
                    .withImputationStart(System.currentTimeMillis());

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{

            ContentValues values = new ContentValues();

            values.put(KEY_IMPUTATION_ASSIGN_ID, imputation.getAssign().getAssignId());
            values.put(KEY_IMPUTATION_WORK_ORDER_ID_FK, imputation.getWorkOrder().getWorkOrderId());
            values.put(KEY_IMPUTATION_START_TIME, imputation.getImputationStart());
            values.put(KEY_IMPUTATION_EMPLOYEE_ID, imputation.getAssign().getEmployee().getEmployeeId());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_IMPUTATION, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New imputation successfuly added into database");

            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add imputation into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }




    public void stopImputation(Imputation imputation, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_IMPUTATION_END_TIME, System.currentTimeMillis());

            db.update(TABLE_IMPUTATION
                    , values
                    , KEY_IMPUTATION_ID_PK + "=" + imputation.getImputationId()
                    ,null);

            db.setTransactionSuccessful();

            Log.d(TAG, "Imputation have been successfuly stopped into database");

            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to stop Imputation into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public int getConsumedTimeForWorkOrder(WorkOrder workOrder) {
        int result = 0;

        ArrayList<Imputation> imputations = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY = "SELECT * " +
                "FROM "+ TABLE_IMPUTATION  + " imputation " +
                "INNER JOIN "+ TABLE_WORK_ORDER + " wo " +
                "ON wo.workOrderIdPk = imputation.imputationWorkOrderIdFk " +
                "WHERE wo.workOrderIdPk = " + workOrder.getWorkOrderId();

        Log.e(TAG, SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY);


        Cursor cursor = db.rawQuery(SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Imputation imputation =
                            new Imputation()
                                    .withImputationEnd(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_END_TIME)))
                                    .withImputationStart(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_START_TIME)));

                    imputations.add(imputation);


                } while (cursor.moveToNext());
            }
        }catch (Exception e){
                Log.e(TAG, "Error while trying to make Sum of time consumed from database : " + e.toString());
        }finally {
            if (!cursor.isClosed()) {
                    cursor.close();
            }
        }

        Log.e(TAG, "Returning  : " + result);

        return processTotalTimeConsumed(imputations);
    }

    private int processTotalTimeConsumed(ArrayList<Imputation> imputations) {
        int result = 0;

        for(Imputation imputation : imputations){
            if(imputation.getImputationEnd() != 0){
                result +=  imputation.getImputationEnd() - imputation.getImputationStart();
            }else{
                result +=  System.currentTimeMillis() - imputation.getImputationStart();
            }
        }

        Log.d(TAG, "Time consummed have been processed : " + result + " , in " + imputations.size() + " imputations");

        return result;
    }

    public void closeWorkOrder(WorkOrder workOrder, DatabaseOperationCallBack<WorkOrder> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_WORK_ORDER_STATUS, WorkOrderStatus.FINISHED.name());

            db.update(TABLE_WORK_ORDER
                    , values
                    , KEY_WORK_ORDER_ID_PK + "=" + workOrder.getWorkOrderId()
                    ,null);

            db.setTransactionSuccessful();

            workOrder.withworkOrderStatus(WorkOrderStatus.FINISHED);

            Log.d(TAG, "Workorder have been successfuly closed");
            callBack.onSuccess(workOrder);

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to close Workorder : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void getImputationsForWorkorder(WorkOrder workOrder, DatabaseOperationCallBack<ArrayList<Imputation>> callBack) {
        ArrayList<Imputation> imputations = new ArrayList<>();

        String IMPUTATION_SELECT_QUERY = "SELECT * " +
                "FROM "+ TABLE_IMPUTATION + " imputation " +
                "INNER JOIN "+ TABLE_WORK_ORDER + " wo " +
                "ON wo.workOrderIdPk = imputation.imputationWorkOrderIdFk " +
                "WHERE wo.workOrderIdPk = "+  workOrder.workOrderId ;


        Log.e(TAG, IMPUTATION_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(IMPUTATION_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Imputation imputation =
                            new Imputation()
                                    .withImputationStart(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_START_TIME)))
                                    .withImputationEnd(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_END_TIME)))
                                    .withEmployee(getEmployeeWithId(cursor.getInt(cursor.getColumnIndex(KEY_IMPUTATION_EMPLOYEE_ID))));

                    imputations.add(imputation);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get employees from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + imputations.size() + " imputation.");


        callBack.onSuccess(imputations);
    }

    public int getConsumedTimeForAssign(Assign assign) {
        int result = 0;

        ArrayList<Imputation> imputations = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY = "SELECT * " +
                "FROM "+ TABLE_IMPUTATION  + " imputation " +
                "WHERE imputation.imputationAssignId = " + assign.getAssignId();

        Log.e(TAG, SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY);


        Cursor cursor = db.rawQuery(SELECT_ALL_IMPUTATION_FOR_WORK_ORDER_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Imputation imputation =
                            new Imputation()
                                    .withImputationEnd(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_END_TIME)))
                                    .withImputationStart(cursor.getLong(cursor.getColumnIndex(KEY_IMPUTATION_START_TIME)));

                    imputations.add(imputation);


                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG, "Error while trying to make Sum of time consumed from database : " + e.toString());
        }finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.e(TAG, "Returning  : " + result);

        return processTotalTimeConsumed(imputations);
    }


    public void stopAllActiveImputation(Construction construction, DatabaseOperationCallBack<DefaultResponse> callBack){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        String STOP_ALL_IMPUTATION_QUERY = "UPDATE " + TABLE_IMPUTATION +
                    " SET imputationEnd  = "  + System.currentTimeMillis() +
                    " WHERE imputationIdPk IN " +
                    "(SELECT imp.imputationIdPk " +
                    "FROM " + TABLE_IMPUTATION + " imp " +
                    "INNER JOIN "+ TABLE_WORK_ORDER + " wo " +
                    "ON wo.workOrderIdPk = imp.imputationWorkOrderIdFk " +
                    "WHERE wo.constructionIdFk = " +  construction.getConstructionId() +
                    " AND imp.imputationEnd IS NULL)" ;


        Log.e(TAG, STOP_ALL_IMPUTATION_QUERY);

        try {

            db.execSQL(STOP_ALL_IMPUTATION_QUERY);

            db.setTransactionSuccessful();

            Log.d(TAG, "imputations have been successfuly stopped into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to stop Imputation into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void getAllOrders(DatabaseOperationCallBack<ArrayList<Order>> callBack) {
        ArrayList<Order> orders = new ArrayList<>();

        String GET_ALL_ORDER_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_ORDER);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_ORDER_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Order newOrder =
                            new Order()
                                    .withId(cursor.getLong(cursor.getColumnIndex(KEY_ORDER_ID_PK)))
                                    .withOrderCode(cursor.getString(cursor.getColumnIndex(KEY_ORDER_CODE)))
                                    .withCustomer(cursor.getString(cursor.getColumnIndex(KEY_ORDER_CUSTOMER)))
                                    .withStatus(OrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_ORDER_STATUS))));

                    orders.add(newOrder);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get orders from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + orders.size() + " orders.");

        callBack.onSuccess(orders);
    }

    public void addOrder(Order newOrder, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_ORDER_CUSTOMER, newOrder.getCustomer());
            values.put(KEY_ORDER_CODE, newOrder.getOrderCode());
            values.put(KEY_ORDER_STATUS, newOrder.getStatus().name());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ORDER, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New order successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add order into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void closeOrder(Order order, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_ORDER_STATUS, OrderStatus.FINISHED.name());

            db.update(TABLE_ORDER
                    , values
                    , KEY_ORDER_ID_PK + "=" + order.getOrderId()
                    ,null);

            db.setTransactionSuccessful();

            Log.d(TAG, "Workorder have been successfuly closed");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to close Workorder : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void addPicture(Picture newPicture, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_PICTURE_NAME, newPicture.getPictureName());
            values.put(KEY_ORDER_ID_FK, newPicture.getOrder().getOrderId());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_PICTURE, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New picture successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add picture into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void getAllPictureForOrder(Order order, DatabaseOperationCallBack<ArrayList<Picture>> callBack) {
        ArrayList<Picture> pictures = new ArrayList<>();

        String PICTURE_SELECT_QUERY = "SELECT * " +
                "FROM "+ TABLE_PICTURE + " picture " +
                "WHERE picture.orderIdFk = "+  order.getOrderId() ;


        Log.e(TAG, PICTURE_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PICTURE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Picture picture =
                            new Picture()
                                    .withId(cursor.getLong(cursor.getColumnIndex(KEY_PICTURE_ID_PK)))
                                    .withPictureName(cursor.getString(cursor.getColumnIndex(KEY_PICTURE_NAME)));

                    pictures.add(picture);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get pictures from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + pictures.size() + " pictures.");


        callBack.onSuccess(pictures);
    }

    public void deletePicture(Picture picture, DatabaseOperationCallBack<DefaultResponse> callBack) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            String DELETE_PICTURE_QUERY = "DELETE " +
                    "FROM "+ TABLE_PICTURE +
                    " WHERE pictureIdPk = " + picture.getPictureId();

            Log.e(TAG, DELETE_PICTURE_QUERY);

            db.execSQL(DELETE_PICTURE_QUERY);

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete picture from database : " + e.toString());
            callBack.onError();
        } finally {
            db.close();
        }

        callBack.onSuccess(new DefaultResponse());
    }
}
