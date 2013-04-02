package com.capstone.soundloop;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoopPedal extends Activity {

	private Project project;
	private ProjectsDataSource dataSource;
	private boolean record_created = false;
	private boolean recording = false;
	private boolean playing = false;
	private int record_sec = 0;
	private int max_length = 30; // seconds
	private String max_time = "/00:30";

	/** Required onCreate method called when Activity is started */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loop_pedal);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// initialize audio system
		createEngine();
		createBufferQueueAudioPlayer();

		dataSource = new ProjectsDataSource(this);
		dataSource.open();

		((Button) findViewById(R.id.bRecord))
				.setOnClickListener(new OnClickListener() {
					int tv_id = R.id.tvRecordCounter;
					Timer timer = new Timer();

					public void onClick(View view) {
						if (!record_created) {
							record_created = createAudioRecorder();
						}
						if (record_created) {

							if (!recording && !playing) {
								try {
									startRecording();
									timer.execute(tv_id);
									recording = true;
									updateText("Stop", R.id.bRecord);
								} catch (Exception e) {
									// e.printStackTrace();
									System.err
											.println("COULD NOT START RECORDING");
								}
							} else {
								try {
									stopRecording();
									timer.cancel(true);
									recording = false;
									updateText("Record", R.id.bRecord);
									timer = new Timer();
								} catch (Exception e) {
									// e.printStackTrace();
									System.err
											.println("COULD NOT STOP RECORDING");
								}
							}

						}
					}
				});

		((Button) findViewById(R.id.bPlayback))
				.setOnClickListener(new OnClickListener() {
					int tv_id = R.id.tvPlaybackCounter;
					Timer timer = new Timer();

					public void onClick(View v) {
						if (record_created) {

							if (!playing && !recording) {
								try {
									playback();
									timer.execute(tv_id);
									updateText("Stop", R.id.bPlayback);
									playing = true;
								} catch (Exception e) {
									// e.printStackTrace();
									System.err.println("COULD NOT PLAY");
								}
							} else {
								try {
									stopPlayback();
									timer.cancel(true);
									timer = new Timer();
									updateText("Play", R.id.bPlayback);
									playing = false;
								} catch (Exception e) {
									// e.printStackTrace();
									System.err
											.println("COULD NOT STOP PLAYING");
								}
							}
						}
					}
				});

		// format timer
		String tv_timer = "00:00" + max_time;

		((TextView) findViewById(R.id.tvRecordCounter)).setText(tv_timer);
		((TextView) findViewById(R.id.tvPlaybackCounter)).setText(tv_timer);

		long id = 0;
		try {
			Bundle bundle = getIntent().getExtras();
			id = (int) bundle.getLong("id");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			project = dataSource.getProjectAt(id);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		TextView name = (TextView) findViewById(R.id.project_name);
		name.setText(project.getName() + "  " + project.getId());

	}

	/** Called when the activity is about to be destroyed. */
	@Override
	protected void onPause() {
		// turn off all audio
		selectClip(0, 0);
		super.onPause();
	}

	/** Called when the activity is about to be destroyed. */
	@Override
	protected void onDestroy() {
		shutdown();
		super.onDestroy();
	}

	public void updateText(String text, int id) {
		((TextView) findViewById(id)).setText(text);
	}

	private class Timer extends AsyncTask<Integer, String, Void> {
		String text = "00:00";
		int my_id;

		@Override
		protected Void doInBackground(Integer... params) {

			my_id = params[0];

			for (int i = 0; i < max_length; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (i < 9) {
						text = "00:0" + (i + 1) + max_time;
						publishProgress(text);
					} else if (i < 59) {
						text = "00:" + (i + 1) + max_time;
						publishProgress(text);
					}
				}
				// escape early if cancel() is called
				if (isCancelled()) {
					break;
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... t) {
			updateText(t[0], my_id);
		}

	}

	/** Native methods implemented in jni folder */

	public static native void createEngine();

	public static native void createBufferQueueAudioPlayer();

	public static native boolean selectClip(int which, int count);

	// another function to playback recorded audio
	public static native boolean playback();

	public static native boolean stopPlayback();

	public static native boolean enableReverb(boolean enabled);

	public static native boolean createAudioRecorder();

	// starts a recording for a set amount of time
	public static native void startRecording();

	// another record function to stream recording
	public static native void record();

	public static native void stopRecording();

	public static native void shutdown();

	static {
		System.loadLibrary("sound_loop_jni");
	}

}
