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
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Job;
import com.vtec.terrassteel.common.model.Pointing;
import com.vtec.terrassteel.common.model.WorkOrder;
import com.vtec.terrassteel.common.model.WorkOrderStatus;
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
    private static final String TABLE_POINTING = "pointing";



    // Construction Table Columns
    private static final String KEY_CONSTRUCTION_ID_PK = "constructionIdPk";
    private static final String KEY_CONSTRUCTION_CUSTOMER_ID_FK = "customerIdFk";
    private static final String KEY_CONSTRUCTION_NAME = "constructionName";
    private static final String KEY_CONSTRUCTION_ADDRESS1 = "constructionAddress1";
    private static final String KEY_CONSTRUCTION_ADDRESS2 = "constructionAddress2";
    private static final String KEY_CONSTRUCTION_ZIP = "constructionZip";
    private static final String KEY_CONSTRUCTION_CITY = "constructionCity";
    private static final String KEY_CONSTRUCTION_STATUS = "constructionStatus";


    // Customer Table Columns
    private static final String KEY_CUSTOMER_ID_PK = "customerIdPk";
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_CUSTOMER_ADDRESS1 = "customerAddress1";
    private static final String KEY_CUSTOMER_ADDRESS2 = "customerAddress2";
    private static final String KEY_CUSTOMER_ZIP = "customerZip";
    private static final String KEY_CUSTOMER_CITY = "customerCity";
    private static final String KEY_CUSTOMER_PHONE = "customerPhone";
    private static final String KEY_CUSTOMER_MAIL = "customerMail";


    // Employee Table Columns
    private static final String KEY_EMPLOYEE_ID_PK = "employeeIdPk";
    private static final String KEY_EMPLOYEE_NAME = "employeeName";
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


    // Pointing Table
    private static final String KEY_POINTING_ID_PK = "pointingIdPk";
    private static final String KEY_POINTING_ASSIGN_ID = "pointingAssignId";
    private static final String KEY_POINTING_EMPLOYEE_ID = "pointingEmployeeId";
    private static final String KEY_POINTING_WORK_ORDER_ID_FK = "pointingWorkOrderIdFk";
    private static final String KEY_POINTING_TOTAL_TIME = "pointingTotalTime";
    private static final String KEY_POINTING_START = "pointingStart";





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
                KEY_CONSTRUCTION_CUSTOMER_ID_FK + " INTEGER REFERENCES " + TABLE_CUSTOMERS + "," + // Define a foreign key
                KEY_CONSTRUCTION_NAME + " TEXT," +
                KEY_CONSTRUCTION_ADDRESS1 + " TEXT," +
                KEY_CONSTRUCTION_ADDRESS2 + " TEXT," +
                KEY_CONSTRUCTION_ZIP + " TEXT," +
                KEY_CONSTRUCTION_CITY + " TEXT," +
                KEY_CONSTRUCTION_STATUS + " TEXT" +
                ")";

        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS +
                "(" +
                KEY_CUSTOMER_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CUSTOMER_NAME + " TEXT," +
                KEY_CUSTOMER_ADDRESS1 + " TEXT," +
                KEY_CUSTOMER_ADDRESS2 + " TEXT," +
                KEY_CUSTOMER_ZIP + " TEXT," +
                KEY_CUSTOMER_CITY + " TEXT," +
                KEY_CUSTOMER_PHONE + " TEXT," +
                KEY_CUSTOMER_MAIL + " TEXT" +
                ")";

        String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES +
                "(" +
                KEY_EMPLOYEE_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_EMPLOYEE_NAME + " TEXT," +
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

        String CREATE_POINTING_TABLE = "CREATE TABLE " + TABLE_POINTING +
                "(" +
                KEY_POINTING_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_POINTING_ASSIGN_ID + " INTEGER," +
                KEY_POINTING_WORK_ORDER_ID_FK + " INTEGER REFERENCES " + TABLE_WORK_ORDER + "," + // Define a foreign key
                KEY_POINTING_EMPLOYEE_ID + " INTEGER," + // Define a foreign key
                KEY_POINTING_START + " INTEGER," +
                KEY_POINTING_TOTAL_TIME + " INTEGER" +
                ")";

        db.execSQL(CREATE_CONSTRUCTIONS_TABLE);
        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_WORK_ORDER_TABLE);
        db.execSQL(CREATE_ASSIGN_TABLE);
        db.execSQL(CREATE_POINTING_TABLE);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTING);

            onCreate(db);
        }
    }


    public void addConstruction(Construction construction, DatabaseOperationCallBack<DefaultResponse> callBack) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_CONSTRUCTION_CUSTOMER_ID_FK,
                    addOrUpdateCustomer(construction.getCustomer()));

            values.put(KEY_CONSTRUCTION_NAME, construction.getConstructionName());
            values.put(KEY_CONSTRUCTION_ADDRESS1, construction.getConstructionAddress1());
            values.put(KEY_CONSTRUCTION_ADDRESS2, construction.getConstructionAddress2());
            values.put(KEY_CONSTRUCTION_ZIP, construction.getConstructionZip());
            values.put(KEY_CONSTRUCTION_CITY, construction.getConstructionZip());
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

    public long addOrUpdateCustomer(Customer customer) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CUSTOMER_NAME, customer.getCustomerName());
            values.put(KEY_CUSTOMER_ADDRESS1, customer.getCustomerAddress1());
            values.put(KEY_CUSTOMER_ADDRESS2, customer.getCustomerAddress2());
            values.put(KEY_CUSTOMER_ZIP, customer.getCustomerZip());
            values.put(KEY_CUSTOMER_CITY, customer.getCustomerCity());
            values.put(KEY_CUSTOMER_PHONE, customer.getCustomerPhone());
            values.put(KEY_CUSTOMER_MAIL, customer.getCustomerEmail());


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_CUSTOMERS, values, KEY_CUSTOMER_NAME + "= ?", new String[]{customer.getCustomerName()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_CUSTOMER_ID_PK, TABLE_CUSTOMERS, KEY_CUSTOMER_NAME);

                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(customer.getCustomerName())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_CUSTOMERS, null, values);
                db.setTransactionSuccessful();
                Log.d(TAG, "New Customer successfuly added into database");

            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add or update customer : " + e.toString());
        } finally {
            db.endTransaction();
        }
        return userId;
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
                                    .withCustomer(getCustomerWithId(cursor.getInt(cursor.getColumnIndex(KEY_CONSTRUCTION_CUSTOMER_ID_FK))))
                                    .withConstructionAddress1(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ADDRESS1)))
                                    .withConstructionAddress2(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ADDRESS2)))
                                    .withConstructionZip(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ZIP)))
                                    .withConstructionCity(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_CITY)))
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

    private Customer getCustomerWithId(int customerId) throws Exception{
        SQLiteDatabase db = getWritableDatabase();

        String CUSTOMERS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE " + KEY_CUSTOMER_ID_PK + " = %d",
                        TABLE_CUSTOMERS, customerId);

        Cursor cursor = db.rawQuery(CUSTOMERS_SELECT_QUERY, null);
        cursor.moveToFirst();

        Customer newCustomer =
                        new Customer()
                                .withCustomerId(cursor.getLong(cursor.getColumnIndex(KEY_CUSTOMER_ID_PK)))
                                .withCustomerName(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)))
                                .withCustomerAddress1(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ADDRESS1)))
                                .withCustomerAddress2(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ADDRESS2)))
                                .withCustomerZip(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ZIP)))
                                .withCustomerCity(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CITY)))
                                .withCustomerPhone(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_PHONE)))
                                .withCustomerEmail(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_MAIL)));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return newCustomer;

    }

    public void addCustomer(Customer customer, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_CUSTOMER_NAME, customer.getCustomerName());
            values.put(KEY_CUSTOMER_ADDRESS1, customer.getCustomerAddress1());
            values.put(KEY_CUSTOMER_ADDRESS2, customer.getCustomerAddress2());
            values.put(KEY_CUSTOMER_ZIP, customer.getCustomerZip());
            values.put(KEY_CUSTOMER_CITY, customer.getCustomerCity());
            values.put(KEY_CUSTOMER_PHONE, customer.getCustomerPhone());
            values.put(KEY_CUSTOMER_MAIL, customer.getCustomerEmail());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_CUSTOMERS, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New Customer successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add Customer into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public void getAllCustomers(DatabaseOperationCallBack<ArrayList<Customer>> callBack) {
        ArrayList<Customer> customers = new ArrayList<>();

        String CUSTOMERS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_CUSTOMERS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CUSTOMERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Customer newCustomer =
                            new Customer()
                                    .withCustomerId(cursor.getLong(cursor.getColumnIndex(KEY_CUSTOMER_ID_PK)))
                                    .withCustomerName(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)))
                                    .withCustomerAddress1(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ADDRESS1)))
                                    .withCustomerAddress2(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ADDRESS2)))
                                    .withCustomerZip(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_ZIP)))
                                    .withCustomerCity(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CITY)))
                                    .withCustomerPhone(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_PHONE)))
                                    .withCustomerEmail(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_MAIL)));

                    customers.add(newCustomer);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get customers from database : " + e.toString());
            callBack.onError();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Database successfully return " + customers.size() + " customers.");
        callBack.onSuccess(customers);
    }

    public void addEmployee(Employee employee, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_EMPLOYEE_NAME, employee.getEmployeeName());
            values.put(KEY_EMPLOYEE_JOB, employee.getEmployeeJob().name());
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

    public void editCustomer(Customer customerToEdit, DatabaseOperationCallBack<DefaultResponse> callBack) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_CUSTOMER_NAME, customerToEdit.getCustomerName());
            values.put(KEY_CUSTOMER_ADDRESS1, customerToEdit.getCustomerAddress1());
            values.put(KEY_CUSTOMER_ADDRESS2, customerToEdit.getCustomerAddress2());
            values.put(KEY_CUSTOMER_ZIP, customerToEdit.getCustomerZip());
            values.put(KEY_CUSTOMER_CITY, customerToEdit.getCustomerCity());
            values.put(KEY_CUSTOMER_PHONE, customerToEdit.getCustomerPhone());
            values.put(KEY_CUSTOMER_MAIL, customerToEdit.getCustomerEmail());

            db.update(TABLE_CUSTOMERS
                    , values
                    , KEY_CUSTOMER_ID_PK + "=" + customerToEdit.getCustomerId()
                    ,null);

            db.setTransactionSuccessful();

            Log.d(TAG, "Customer have been successfuly edited into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to edit Customer into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }

    }

    public void editEmployee(Employee employee, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_EMPLOYEE_NAME, employee.getEmployeeName());
            values.put(KEY_EMPLOYEE_JOB, employee.getEmployeeJob().name());
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
            values.put(KEY_WORK_ORDER_PRODUCT_TYPE, workOrder.getWorkOrderProductType());
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
                                    .withWorkOrderProductType(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_PRODUCT_TYPE)))
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
                        .withCustomer(getCustomerWithId(cursor.getInt(cursor.getColumnIndex(KEY_CONSTRUCTION_CUSTOMER_ID_FK))))
                        .withConstructionAddress1(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ADDRESS1)))
                        .withConstructionAddress2(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ADDRESS2)))
                        .withConstructionZip(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_ZIP)))
                        .withConstructionCity(cursor.getString(cursor.getColumnIndex(KEY_CONSTRUCTION_CITY)))
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


    public void getAllAssign(DatabaseOperationCallBack<ArrayList<Assign>> callBack) {

        ArrayList<Assign> assigns = new ArrayList<>();

        String ASSIGN_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_ASSIGN);

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
                        .withWorkOrderProductType(cursor.getString(cursor.getColumnIndex(KEY_WORK_ORDER_PRODUCT_TYPE)))
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

            Pointing pointing = getPointingForAssign(assign.getAssignId());

            if(pointing != null){
                pointing
                        .withTotalTime((System.currentTimeMillis()/1000 - pointing.getPointingStart()) + pointing.pointingTotalTime)
                        .withPointingStart(0);

                updatePointing(pointing, new DatabaseOperationCallBack<DefaultResponse>() {
                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {
                        Log.e(TAG, "Pointing successfuly deleted for AssignId : " + assign.getAssignId());
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



    public Pointing getPointingForAssign(long assignId) {

        Pointing pointing = null;

        String POINTING_SELECT_QUERY = "SELECT * " +
                "FROM "+ TABLE_POINTING  +
                " WHERE pointingAssignId = "+  assignId;

        Log.e(TAG, POINTING_SELECT_QUERY);


        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(POINTING_SELECT_QUERY, null);

        try {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                pointing = new Pointing()
                        .withPointingId(cursor.getLong(cursor.getColumnIndex(KEY_POINTING_ID_PK)))
                        .withTotalTime(cursor.getInt(cursor.getColumnIndex(KEY_POINTING_TOTAL_TIME)))
                        .withPointingStart(cursor.getInt(cursor.getColumnIndex(KEY_POINTING_START)));

                Log.e(TAG, "One pointing data have been return for Work order assign with total time : " + pointing.getPointingTotalTime());

            }else{
                Log.e(TAG, "No pointing data for Work order assign");
            }

        }catch (Exception e){
            Log.e(TAG, "Error while trying to get pointing from database : " + e.toString());
        }finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return pointing;
    }

    public void startPointing(Assign assign, DatabaseOperationCallBack<DefaultResponse> callBack) {
        Pointing pointing = getPointingForAssign(assign.getAssignId());

        if(pointing == null){
            pointing = new Pointing()
                    .withAssign(assign)
                    .withWorkOrder(assign.getWorkOrder())
                    .withPointingStart(System.currentTimeMillis()/1000);

            addPointing(pointing, callBack);
        }else{
            pointing
                   .withPointingStart(System.currentTimeMillis()/1000);

            updatePointing(pointing, callBack);
        }
    }


    public void addPointing(Pointing pointing, DatabaseOperationCallBack<DefaultResponse> callBack) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{

            ContentValues values = new ContentValues();

            values.put(KEY_POINTING_ASSIGN_ID, pointing.getAssign().getAssignId());
            values.put(KEY_POINTING_WORK_ORDER_ID_FK, pointing.getWorkOrder().getWorkOrderId());
            values.put(KEY_POINTING_START, pointing.getPointingStart());
            //values.put(KEY_POINTING_EMPLOYEE_ID, pointing.getAssign().getEmployee().getEmployeeId());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_POINTING, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New pointing successfuly added into database");

            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add pointing into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }

    }

    public void updatePointing(Pointing pointing, DatabaseOperationCallBack<DefaultResponse> callBack) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();

            values.put(KEY_POINTING_START, pointing.getPointingStart());
            values.put(KEY_POINTING_TOTAL_TIME, pointing.getPointingTotalTime());


            db.update(TABLE_POINTING
                    , values
                    , KEY_POINTING_ID_PK + "=" + pointing.getPointingId()
                    ,null);

            db.setTransactionSuccessful();

            Log.d(TAG, "Pointing have been successfuly edited into database");

            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to edit Pointing into database : " + e.toString());
            callBack.onError();
        } finally {
            db.endTransaction();
        }
    }

    public int getConsumedTimeForWorkOrder(WorkOrder workOrder) {
        int result = 0;

        //TODO

        return result;
    }

    public void closeWorkOrder(WorkOrder workOrder) {
        //TODO
    }
}
