package com.mdcbeta.data.local.Doa;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mdcbeta.data.remote.model.MainAppointmentDetail;

import java.util.List;

/**
 * Created by Shakil Karim on 7/2/17.
 */
@Dao
public interface MainAppointmentDetailDoa {

    @Query("SELECT * FROM MainAppointmentDetail WHERE id = :id")
    List<MainAppointmentDetail> getMainAppointmentDetail(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MainAppointmentDetail> mainAppointments);

    @Query("DELETE FROM MainAppointmentDetail WHERE id = :id")
    void delete(String id);

}
