package com.facilesales.facilesalesapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Invoice.class,SoldProducts.class, AvailProduct.class,CashFlow.class},
        version = 2,
           exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AvailProductsDao availProductsDao();
    public abstract InvoiceWithSoldProductsDao invoiceWithSoldProductsDao();
    public abstract CashFlowDao cashFlowDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class){
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"app_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                CashFlowDao dao = INSTANCE.cashFlowDao();

                CashFlow cashFlow = new CashFlow();
                cashFlow.totalCost = 0;
                cashFlow.totalSales = 0;
                cashFlow.id = 1;

                dao.insert(cashFlow);
            });
        }
    };

}