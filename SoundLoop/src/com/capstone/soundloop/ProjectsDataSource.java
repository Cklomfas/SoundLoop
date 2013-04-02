package com.capstone.soundloop;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProjectsDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME };

	public ProjectsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Project createProject(String name) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		long insertID = database.insert(MySQLiteHelper.TABLE_PROJECTS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROJECTS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertID, null,
				null, null, null);
		cursor.moveToFirst();
		Project newProject = cursorToProject(cursor);
		cursor.close();

		return newProject;
	}

	public void deleteProject(Project project) {
		long id = project.getId();
		System.out.println("Project with id : " + id + " deleted");
		database.delete(MySQLiteHelper.TABLE_PROJECTS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Project> getAllProjects() {
		List<Project> projects = new ArrayList<Project>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROJECTS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Project project = cursorToProject(cursor);
			projects.add(project);
			cursor.moveToNext();
		}
		cursor.close();
		return projects;
	}

	public Project getProjectAt(long i) {
		Project project = new Project();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROJECTS,
				allColumns, null, null, null, null, null);
		cursor.moveToPosition((int) i);
		project = cursorToProject(cursor);
		
		return project;
	}

	private Project cursorToProject(Cursor cursor) {
		Project project = new Project();
		project.setId(cursor.getLong(0));
		project.setName(cursor.getString(1));
		return project;
	}

}
