package com.capstone.soundloop;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectProject extends ListActivity implements OnClickListener {

	private ProjectsDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_project);

		dataSource = new ProjectsDataSource(this);
		dataSource.open();

		List<Project> values = dataSource.getAllProjects();

		// Array adapter to show elements in the ListView
		ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Bundle bundle = new Bundle();
		bundle.putLong("id", position);

		Intent i = new Intent(this, LoopPedal.class);
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_project, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.add:
			// start intent to add projects
			Intent i = new Intent("com.capstone.soundloop.ADDPROJECT");
			startActivity(i);
			break;
		case R.id.delete:
			// unimplemented for now
			break;
		}
	}

}
