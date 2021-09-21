package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException
/*This is the worker file that helps for scheduling background jobs */
class RefreshDataWork(appContext: Context, workerParameters: WorkerParameters) :CoroutineWorker(appContext,workerParameters)
{

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        repository.refreshAsteroids()

        return try{
            repository.refreshAsteroids()
            Result.success()
        }catch (exception: HttpException){
            Result.retry()
        }
    }
}