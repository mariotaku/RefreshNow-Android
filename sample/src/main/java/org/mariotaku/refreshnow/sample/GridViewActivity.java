package org.mariotaku.refreshnow.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.mariotaku.refreshnow.widget.OnRefreshListener;
import org.mariotaku.refreshnow.widget.RefreshMode;
import org.mariotaku.refreshnow.widget.RefreshNowGridView;
import org.mariotaku.refreshnow.widget.RefreshNowListView;

import java.util.ArrayList;

public class GridViewActivity extends Activity implements OnRefreshListener {

	private RefreshNowGridView mListView;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mListView = (RefreshNowGridView) findViewById(android.R.id.list);
	}

	@Override
	public void onRefreshComplete() {
		Toast.makeText(this, String.format("onRefreshComplete"), Toast.LENGTH_SHORT).show();
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onRefreshStart(final RefreshMode mode) {
		Toast.makeText(this, String.format("onRefreshStart: %s", mode), Toast.LENGTH_SHORT).show();
		setProgressBarIndeterminateVisibility(true);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mListView.setRefreshComplete();
			}
		}, 5000L);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_grid);
		final ArrayList<String> objects = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			objects.add(String.format("Item %d", i));
		}
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, objects));
		mListView.setOnRefreshListener(this);
		mListView.setRefreshIndicatorView(findViewById(android.R.id.progress));
	}

}
