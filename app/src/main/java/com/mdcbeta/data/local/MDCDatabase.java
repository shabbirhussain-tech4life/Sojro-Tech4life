package com.mdcbeta.data.local;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mdcbeta.data.local.Doa.MainAppointmentDetailDoa;
import com.mdcbeta.data.local.Doa.MainAppointmentDetailImageDoa;
import com.mdcbeta.data.local.Doa.MainAppointmentDoa;
import com.mdcbeta.data.model.MainAppointment;
import com.mdcbeta.data.remote.model.MainAppointmentDetail;
import com.mdcbeta.data.remote.model.MainAppointmentDetailImage;

/**
 * Created by Shakil Karim on 7/1/17.
 */
@Database(entities = {
        MainAppointment.class,
        MainAppointmentDetail.class,
        MainAppointmentDetailImage.class
}, version = 1, exportSchema = false)
public abstract class MDCDatabase extends RoomDatabase {

//    private static MDCDatabase INSTANCE;
//
//
//    public static MDCDatabase getInstance(Context context) {
//        if (INSTANCE == null) {
//            synchronized (MDCDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            MDCDatabase.class, "mdc.db")
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }


    public abstract MainAppointmentDoa mainAppointmentDoa();

    public abstract MainAppointmentDetailDoa mainAppointmentDetailDoa();

    public abstract MainAppointmentDetailImageDoa mainAppointmentDetailImageDoa();

}
