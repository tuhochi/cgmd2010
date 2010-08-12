package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class HelpActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l00_generalhelp);

		LinearLayout ll = (LinearLayout) findViewById(R.id.l00_LinearLayoutHelp_AllLevels);
		View.OnClickListener listener = new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				int id = view.getId();
				String sLevel = Constants.LEVELIDS[id];
				String levelString = Constants.getHelpActionString(Integer.parseInt(sLevel));
				Intent levelIntent = new Intent();
				levelIntent.setAction(levelString);
				try{
					startActivity(levelIntent);
				} catch (ActivityNotFoundException e){
					//Toast.makeText(this, "The activity was not found! Did you declare it in the AndroidManifestFile.xml like required?", Toast.LENGTH_LONG).show();
					
				}
				
			}
		};

		for(int i = 0; i<Constants.LEVELIDS.length; i++){
			int iLevel = Integer.parseInt(Constants.LEVELIDS[i]);
			int iIcon = Constants.getLevelIconResource(iLevel);
			LinearLayout lLevel = new LinearLayout(this);
			lLevel.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams lll = new LinearLayout.LayoutParams(200, 80); 
			lll.setMargins(5,5,5,5);

			ImageView v = new ImageView(this);
			v.setBackgroundResource(iIcon);
			v.setOnClickListener(listener);
			v.setId(i);
			TextView t = new TextView(this);
			LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			p.setMargins(2, 2, 2, 2);
			t.setLayoutParams(p);
			
			t.setTextColor(Color.BLACK);
			t.setId(i);
			t.setOnClickListener(listener);
			t.setGravity(Gravity.CENTER_VERTICAL);
			
			int levelTitleResource = Constants.getLevelTitleResource(Integer.parseInt(Constants.LEVELIDS[i]));
			String levelTitle = getResources().getString(levelTitleResource);
			t.setText(levelTitle);

			lLevel.addView(v);			
			lLevel.addView(t);

			ll.addView(lLevel, lll);
		}





	}
}
