package com.example.passportgeneration.db

import androidx.room.*
import com.example.passportgeneration.models.MyPerson


@Dao
interface MyDaoInterface {
    @Insert
    fun addPerson(myPerson: MyPerson)

    @Query("select * from MyPerson")
    fun getAllPerson():List<MyPerson>

    @Query("SELECT * FROM myPerson WHERE seriyaPassport LIKE '%' || :str || '%'")
    fun checkForPassportId(str: String): List<MyPerson>

    @Update
    fun updateUser(myPerson: MyPerson)

    @Delete
    fun delete(myPerson: MyPerson)

    @Query("SELECT MAX(position) FROM myPerson")
    fun getMaxPosition(): Int

}