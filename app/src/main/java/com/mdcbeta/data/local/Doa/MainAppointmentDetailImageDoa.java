package com.mdcbeta.data.local.Doa;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mdcbeta.data.remote.model.MainAppointmentDetailImage;

import java.util.List;

/**
 * Created by Shakil Karim on 7/2/17.
 */
@Dao
public interface MainAppointmentDetailImageDoa {

    @Query("SELECT * FROM MainAppointmentDetailImage WHERE id = :id")
    List<MainAppointmentDetailImage> getImageDetails(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MainAppointmentDetailImage> mainAppointments);

    @Query("DELETE FROM MainAppointmentDetailImage WHERE id = :id")
    void delete(String id);
}
