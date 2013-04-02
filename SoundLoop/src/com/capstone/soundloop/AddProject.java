package com.capstone.soundloop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddProject extends Activity {

	ProjectsDataSource db;
	Project project;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_project);

		db = new ProjectsDataSource(this);
		db.open();

		OnClickListener l = new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean good = submitProject();
				if (good) {
					Intent i = new Intent("com.capstone.soundloop.LOOPPEDAL");
					Bundle bundle = new Bundle();
					bundle.putLong("id", project.getId());
					i.putExtras(bundle);
					startActivity(i);
				}
			}
		};

		((EditText) findViewById(R.id.etName))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean good = submitProject();
						if (good) {
							Intent i = new Intent(
									"com.capstone.soundloop.LOOPPEDAL");
							Bundle bundle = new Bundle();
							bundle.putLong("id", project.getId());
							i.putExtras(bundle);
							startActivity(i);
						}
					}
				});
		((Button) findViewById(R.id.bSubmit))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean good = submitProject();
						if (good) {
							Intent i = new Intent(
									"com.capstone.soundloop.LOOPPEDAL");
							Bundle bundle = new Bundle();
							bundle.putLong("id", project.getId());
							i.putExtras(bundle);
							startActivity(i);
						}
					}
				});

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_project, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean submitProject() {

		try {
			String name = ((EditText) findViewById(R.id.etName)).getText()
					.toString();
			System.out.println(name);
			project = db.createProject(name);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ya fucked up... no value added");
			return false;
		}
	}

}
