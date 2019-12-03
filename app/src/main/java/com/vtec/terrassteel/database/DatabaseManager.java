package com.vtec.terrassteel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vtec.terrassteel.common.model.Construction;
import com.vtec.terrassteel.common.model.ConstructionStatus;
import com.vtec.terrassteel.common.model.Customer;
import com.vtec.terrassteel.common.model.Employee;
import com.vtec.terrassteel.common.model.Job;
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



    // Construction Table Columns
    private static final String KEY_CONSTRUCTION_ID = "id";
    private static final String KEY_CONSTRUCTION_CUSTOMER_ID_FK = "customerId";
    private static final String KEY_CONSTRUCTION_NAME = "constructionName";
    private static final String KEY_CONSTRUCTION_ADDRESS1 = "constructionAddress1";
    private static final String KEY_CONSTRUCTION_ADDRESS2 = "constructionAddress2";
    private static final String KEY_CONSTRUCTION_ZIP = "constructionZip";
    private static final String KEY_CONSTRUCTION_CITY = "constructionCity";
    private static final String KEY_CONSTRUCTION_STATUS = "constructionStatus";


    // Customer Table Columns
    private static final String KEY_CUSTOMER_ID = "id";
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_CUSTOMER_ADDRESS1 = "customerAddress1";
    private static final String KEY_CUSTOMER_ADDRESS2 = "customerAddress2";
    private static final String KEY_CUSTOMER_ZIP = "customerZip";
    private static final String KEY_CUSTOMER_CITY = "customerCity";
    private static final String KEY_CUSTOMER_PHONE = "customerPhone";
    private static final String KEY_CUSTOMER_MAIL = "customerMail";


    // Employee Table Columns
    private static final String KEY_EMPLOYEE_ID = "id";
    private static final String KEY_EMPLOYEE_NAME = "employeeName";
    private static final String KEY_EMPLOYEE_JOB = "employeeJob";
    private static final String KEY_EMPLOYEE_ADDRESS1 = "employeeAddress1";
    private static final String KEY_EMPLOYEE_ADDRESS2 = "employeeAddress2";
    private static final String KEY_EMPLOYEE_ZIP = "employeeZip";
    private static final String KEY_EMPLOYEE_CITY = "employeeCity";
    private static final String KEY_EMPLOYEE_PHONE = "employeePhone";
    private static final String KEY_EMPLOYEE_MAIL = "employeeMail";


    // Work Order Table Columns
    private static final String KEY_WORK_ORDER_ID = "id";
    private static final String KEY_WORK_ORDER_CONSTRUCTION_ID_FK = "constructionId";
    private static final String KEY_WORK_ORDER_REFERENCE = "work_order_reference";
    private static final String KEY_WORK_ORDER_AFFAIRE = "work_order_affaire";
    private static final String KEY_WORK_ORDER_PRODUCT_TYPE = "work_order_product_type";
    private static final String KEY_WORK_ORDER_ALLOCATED_TIME = "work_order_allocated_time";
    private static final String KEY_WORK_ORDER_STATUS = "work_order_Status";


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
                KEY_CONSTRUCTION_ID + " INTEGER PRIMARY KEY," +
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
                KEY_CUSTOMER_ID + " INTEGER PRIMARY KEY," +
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
                KEY_EMPLOYEE_ID + " INTEGER PRIMARY KEY," +
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
                KEY_WORK_ORDER_ID + " INTEGER PRIMARY KEY," +
                KEY_WORK_ORDER_CONSTRUCTION_ID_FK + " INTEGER REFERENCES " + TABLE_CONSTRUCTIONS + "," + // Define a foreign key
                KEY_WORK_ORDER_REFERENCE + " TEXT," +
                KEY_WORK_ORDER_AFFAIRE + " TEXT," +
                KEY_WORK_ORDER_PRODUCT_TYPE + " TEXT," +
                KEY_WORK_ORDER_ALLOCATED_TIME + " TEXT," +
                KEY_WORK_ORDER_STATUS + " TEXT" +
                ")";

        db.execSQL(CREATE_CONSTRUCTIONS_TABLE);
        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_WORK_ORDER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTRUCTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_ORDER);


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
                        KEY_CUSTOMER_ID, TABLE_CUSTOMERS, KEY_CUSTOMER_NAME);

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
                                    .withConstructionId(cursor.getLong(cursor.getColumnIndex(KEY_CONSTRUCTION_ID)))
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
                String.format("SELECT * FROM %s WHERE " + KEY_CUSTOMER_ID + " = %d",
                        TABLE_CUSTOMERS, customerId);

        Cursor cursor = db.rawQuery(CUSTOMERS_SELECT_QUERY, null);
        cursor.moveToFirst();

        Customer newCustomer =
                        new Customer()
                                .withCustomerId(cursor.getLong(cursor.getColumnIndex(KEY_CUSTOMER_ID)))
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
                                    .withCustomerId(cursor.getLong(cursor.getColumnIndex(KEY_CUSTOMER_ID)))
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
                                    .withEmployeeId(cursor.getLong(cursor.getColumnIndex(KEY_EMPLOYEE_ID)))
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
                    , KEY_CUSTOMER_ID + "=" + customerToEdit.getCustomerId()
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
                    , KEY_EMPLOYEE_ID + "=" + employee.getEmployeeId()
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
            db.insertOrThrow(TABLE_WORK_ORDER, null, values);
            db.setTransactionSuccessful();

            Log.d(TAG, "New Work order successfuly added into database");
            callBack.onSuccess(new DefaultResponse());

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add Work order into database : " + e.toString());
            callBack.onError();
        } finally {
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
                                    .withWorkOrderId(cursor.getLong(cursor.getColumnIndex(KEY_WORK_ORDER_ID)))
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
                String.format("SELECT * FROM %s WHERE " + KEY_CONSTRUCTION_ID + " = %d",
                        TABLE_CONSTRUCTIONS, constructionId);

        Cursor cursor = db.rawQuery(CONSTRUCTION_SELECT_QUERY, null);

        cursor.moveToFirst();

        Construction newConstruction =
                new Construction()
                        .withConstructionId(cursor.getLong(cursor.getColumnIndex(KEY_CONSTRUCTION_ID)))
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






}
