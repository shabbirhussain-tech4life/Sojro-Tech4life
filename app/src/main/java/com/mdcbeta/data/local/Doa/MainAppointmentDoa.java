package com.mdcbeta.data.local.Doa;




import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mdcbeta.data.model.MainAppointment;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Shakil Karim on 7/1/17.
 */


@Dao
public interface MainAppointmentDoa {

    @Query("SELECT * FROM MainAppointment WHERE patient_id = :patient_id")
    Maybe<List<MainAppointment>> getMainAppointment(String patient_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MainAppointment> mainAppointments);

    @Query("DELETE FROM MainAppointment WHERE patient_id = :patient_id")
    void delete(String patient_id);



}
