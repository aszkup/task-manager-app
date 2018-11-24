package com.example.protonapp

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.protonapp.repository.AppDatabase
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class TaskRepositoryTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        taskDao = db.taskDao()
    }

    @Test
    fun getSingeInsertedTask() {
        taskDao.insert(testTask1)
        taskDao.taskWithId(testTask1.id)
                .test()
                .assertNoErrors()
                .assertValue(testTask1)
    }

    @Test(expected = AssertionError::class)
    fun getSingleInsertedTaskFailure() {
        taskDao.insert(testTask1)
        taskDao.taskWithId(testTask2.id)
                .test()
                .assertNoErrors()
                .assertValue(testTask1)
    }

    @Test(expected = AssertionError::class)
    fun getNonExistingTaskFailure() {
        taskDao.taskWithId(testTask1.id)
                .test()
                .assertValue(testTask1)
    }

    @Test
    fun getTwoInsertedTasks() {
        taskDao.insert(testTask1)
        taskDao.insert(testTask2)
        taskDao.tasks()
                .test()
                .assertNoErrors()
                .assertValue(listOf(testTask1, testTask2))
    }

    @Test(expected = AssertionError::class)
    fun getTwoNonExistingTasksFailure() {
        taskDao.tasks()
                .test()
                .assertValue(listOf(testTask1, testTask2))
    }

    @Test
    fun insertConflictTask() {
        taskDao.insert(testTask1)
        taskDao.insert(testTask1.copy(name = "changed"))
        taskDao.taskWithId(testTask1.id)
                .test()
                .assertNoErrors()
                .assertValue(testTask1.copy(name = "changed"))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    companion object {
        val testTask1 = Task("1234", "Task1", "Desc1", "uri1")
        val testTask2 = Task("5678", "Task2", "Desc2", "uri2")
    }
}
