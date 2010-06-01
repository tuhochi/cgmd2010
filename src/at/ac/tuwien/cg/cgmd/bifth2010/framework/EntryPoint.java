package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.CommonFunctions;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class EntryPoint extends Activity {
	private static final String CLASS_TAG = EntryPoint.class.getName();

	private static final String PREFERENCE_HELP = "help_id";
	private static final String PREFERENCE_LEVEL = "level_id";
	
	public static final String PREFERENCE_LEVEL_01 = "level01_id";
	public static final String PREFERENCE_LEVEL_02 = "level02_id";
	public static final String PREFERENCE_LEVEL_03 = "level03_id";
	public static final String PREFERENCE_LEVEL_04 = "level04_id";
	public static final String PREFERENCE_LEVEL_05 = "level05_id";
	public static final String PREFERENCE_LEVEL_06 = "level06_id";
	
	private static final String  PREFERENCE_DEBUG_ALWAYSSTARTLEVEL = "alwaysstart";

	public static final String SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS = "l00_debug_settings";

	public static final String PREFERENCE_DEBUG_ENABLED = "debug_enabled";

	/** Called when the activity is first created.
	 * Shows a splash screen 
	 * Starts the Menu
	 * */

	private TextView mTextView = null;
	//EditText mEditTextHelpId = null;
	//EditText mEditTextLevelId = null;
	
	private ArrayAdapter<String> mLevelAdapter;
	
	Spinner mSpinnerLevel = null;
	Spinner mSpinnerHelp = null;
	
	EditText mEditTextLevelId01 = null;
	EditText mEditTextLevelId02 = null;
	EditText mEditTextLevelId03 = null;
	EditText mEditTextLevelId04 = null;
	EditText mEditTextLevelId05 = null;
	EditText mEditTextLevelId06 = null;
	CheckBox mCheckBoxAlwasStartLevel = null;

	private Handler uiHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mTextView.setText("Result: "+msg.what);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l00_debug);
		
		mLevelAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);        
		mLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for(int i=0; i<Constants.LEVELIDS.length; i++)
		{
			mLevelAdapter.add(Constants.LEVELIDS[i]);
		}
			
		
		mSpinnerLevel = (Spinner)findViewById(R.id.l00_SpinnerDebug); 
		mSpinnerHelp = (Spinner)findViewById(R.id.l00_SpinnerHelp);
		
		mSpinnerLevel.setAdapter(mLevelAdapter);
		mSpinnerHelp.setAdapter(mLevelAdapter);
		
		
		
		//mEditTextHelpId = (EditText) findViewById(R.id.l00_EditViewDebugHelpActivity);
		//mEditTextLevelId = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity);
		mEditTextLevelId01 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity01);
		mEditTextLevelId02 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity02);
		mEditTextLevelId03 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity03);
		mEditTextLevelId04 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity04);
		mEditTextLevelId05 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity05);
		mEditTextLevelId06 = (EditText) findViewById(R.id.l00_EditViewDebugLevelActivity06);

		mCheckBoxAlwasStartLevel = (CheckBox) findViewById(R.id.l00_CheckBoxAlwaysStartLevel);
		mTextView = (TextView) findViewById(R.id.l00_TextViewLevelResult);

		Button help = (Button) findViewById(R.id.l00_ButtonStartHelp);
		help.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int iHelp = -1; 
				//String sHelp = mEditTextHelpId.getText().toString();
				long lId = mSpinnerHelp.getSelectedItemId();
				String sHelp = "";
				if(mLevelAdapter.getCount() > (int)lId) 
				{
					sHelp = mLevelAdapter.getItem((int)lId);
				}
				 
				try{
					iHelp = Integer.parseInt(sHelp);
					if(iHelp<0){
						Toast.makeText(EntryPoint.this, "Not a valid id! Enter a level id!", Toast.LENGTH_LONG).show();
					} else {
						SessionState s = new SessionState();
						s.setLevel(0);
						s.setProgress(0);
						String sAction = Constants.getHelpActionString(iHelp);
						Intent levelIntent = new Intent();
						levelIntent.setAction(sAction);
						levelIntent.putExtras(s.asBundle());
						try{
							startActivity(levelIntent);
						} catch (ActivityNotFoundException e){
							Toast.makeText(EntryPoint.this, "The activity was not found! Did you declare it in the AndroidManifestFile.xml like required?", Toast.LENGTH_LONG).show();
						}
					}

				} catch (NumberFormatException e){
					Toast.makeText(EntryPoint.this, "Not a valid id! Enter a level id!", Toast.LENGTH_LONG).show();
				}


			}

		});
		Button level = (Button) findViewById(R.id.l00_ButtonStartLevel);

		level.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int iLevel = -1; 
				//String sLevel = mEditTextLevelId.getText().toString();
				long lId = mSpinnerLevel.getSelectedItemId();
				String sLevel = "";
				if(mLevelAdapter.getCount() > (int)lId) 
				{
					sLevel = mLevelAdapter.getItem((int)lId);
				}
				try{
					iLevel = Integer.parseInt(sLevel);
					if(iLevel<0){
						Toast.makeText(EntryPoint.this, "Not a valid id! Enter a level id!", Toast.LENGTH_LONG).show();
					} else {
						SessionState s = new SessionState();
						s.setLevel(0);
						s.setProgress(0);
						String sAction = Constants.getLevelActionString(iLevel);
						Intent levelIntent = new Intent();
						levelIntent.setAction(sAction);
						levelIntent.putExtras(s.asBundle());
						try{
							startActivityForResult(levelIntent, 1);
						} catch (ActivityNotFoundException e){
							Toast.makeText(EntryPoint.this, "The activity was not found! Did you declare it in the AndroidManifestFile.xml like required?", Toast.LENGTH_LONG).show();
						}
					}
				} catch (NumberFormatException e){
					Toast.makeText(EntryPoint.this, "Not a valid id! Enter a level id!", Toast.LENGTH_LONG).show();
				}
			}

		});

		Button menu = (Button) findViewById(R.id.l00_ButtonStartMenu);

		menu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EntryPoint.this, MenuActivity.class);
				startActivity(intent);					
			}

		});


		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS, 0);
		boolean bDebug = state.getBoolean(PREFERENCE_DEBUG_ENABLED, false);

		boolean bAlwaysStartLevel = state.getBoolean(PREFERENCE_DEBUG_ALWAYSSTARTLEVEL, false);
		mCheckBoxAlwasStartLevel.setChecked(bAlwaysStartLevel);

		if(!bDebug){
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);

		} else {
			if(bAlwaysStartLevel){
				int iLevel = state.getInt(PREFERENCE_LEVEL, -1);
				if(iLevel<0){
					Toast.makeText(EntryPoint.this, "Not a valid level id! Cannot autostart it! Enter a valid level id!", Toast.LENGTH_LONG).show();
				} else {
					SessionState s = new SessionState();
					s.setLevel(0);
					s.setProgress(0);
					String sAction = Constants.getLevelActionString(iLevel);
					Intent levelIntent = new Intent();
					levelIntent.setAction(sAction);
					levelIntent.putExtras(s.asBundle());
					try{
						startActivityForResult(levelIntent, 1);
					} catch (ActivityNotFoundException e){
						Toast.makeText(EntryPoint.this, "The activity was not found! Did you declare it in the AndroidManifestFile.xml like required?", Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		//TODO: showing the splash screen
		//TODO: starting the service
		//TODO: starting the menu activity
		//Intent intent = new Intent(this, MenuActivity.class);
		//Intent intent = new Intent(this, AboutActivity.class);
		//Intent intent = new Intent(this, TextureTest.class);
		//Intent intent = new Intent(this, MapActivity.class);
		//Intent intent = new Intent(this, HelpActivity.class);
		//Intent intent = new Intent(this, CreditsActivity.class);


		//startActivity(intent);
		//finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS, 0);
		SharedPreferences.Editor editor = state.edit();

		boolean bAlwaysStart = mCheckBoxAlwasStartLevel.isChecked();
		editor.putBoolean(PREFERENCE_DEBUG_ALWAYSSTARTLEVEL, bAlwaysStart);

		int iHelp = -1;
		int iLevel = -1;
		int iLevel1 = -1;
		int iLevel2 = -1;
		int iLevel3 = -1;
		int iLevel4 = -1;
		int iLevel5 = -1;
		int iLevel6 = -1;

	

		long lLevelIndex = mSpinnerLevel.getSelectedItemId();
		long lHelpIndex = mSpinnerHelp.getSelectedItemId();
		
		String sLevel = "00" ;			
		if(lLevelIndex<mLevelAdapter.getCount()){
			sLevel = mLevelAdapter.getItem((int)lLevelIndex);
		}
		
		try{
			iLevel = Integer.parseInt(sLevel);
		} catch (NumberFormatException e){

		}
		
		String sHelp = "00" ;			
		if(lHelpIndex<mLevelAdapter.getCount()){
			sHelp = mLevelAdapter.getItem((int)lHelpIndex);
		}
		
		try{
			iHelp = Integer.parseInt(sHelp);
		} catch (NumberFormatException e){

		}

		/*String sHelp = mEditTextHelpId.getText().toString();
		try{
			iHelp = Integer.parseInt(sHelp);
		} catch (NumberFormatException e){

		}*/

		/*String sLevel = mEditTextLevelId.getText().toString();
		try{
			iLevel = Integer.parseInt(sLevel);
		} catch (NumberFormatException e){

		}*/
		String sLevel1 = mEditTextLevelId01.getText().toString();
		try{
			iLevel1 = Integer.parseInt(sLevel1);
		} catch (NumberFormatException e){

		}
		String sLevel2 = mEditTextLevelId02.getText().toString();
		try{
			iLevel2 = Integer.parseInt(sLevel2);
		} catch (NumberFormatException e){

		}
		String sLevel3 = mEditTextLevelId03.getText().toString();
		try{
			iLevel3 = Integer.parseInt(sLevel3);
		} catch (NumberFormatException e){

		}
		String sLevel4 = mEditTextLevelId04.getText().toString();
		try{
			iLevel4 = Integer.parseInt(sLevel4);
		} catch (NumberFormatException e){

		}
		String sLevel5 = mEditTextLevelId05.getText().toString();
		try{
			iLevel5 = Integer.parseInt(sLevel5);
		} catch (NumberFormatException e){

		}
		String sLevel6 = mEditTextLevelId06.getText().toString();
		try{
			iLevel6 = Integer.parseInt(sLevel6);
		} catch (NumberFormatException e){

		}



		editor.putInt(PREFERENCE_HELP, iHelp);
		editor.putInt(PREFERENCE_LEVEL, iLevel);
		editor.putInt(PREFERENCE_LEVEL_01, iLevel1);
		editor.putInt(PREFERENCE_LEVEL_02, iLevel2);
		editor.putInt(PREFERENCE_LEVEL_03, iLevel3);
		editor.putInt(PREFERENCE_LEVEL_04, iLevel4);
		editor.putInt(PREFERENCE_LEVEL_05, iLevel5);
		editor.putInt(PREFERENCE_LEVEL_06, iLevel6);

		editor.commit();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode==Activity.RESULT_OK){
			int iProgress = -1;
			if(data!=null){
				SessionState state = new SessionState(data.getExtras());
				iProgress = state.getProgress();
			}
			uiHandler.sendEmptyMessage(iProgress);
			if(iProgress<=0){
				Toast.makeText(this, "Did you forget to set the progress-result of your activity, or was it just 0 in this case?", Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(this, "You set the result of your ativity correctly! This will be used to increase the progress of the user.", Toast.LENGTH_LONG).show();
			}
		}else {
			Toast.makeText(this, "Either you didn't set any result for your activity, or the resultCode wasn't set to Activity.RESULT_OK.", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS, 0);
		int iHelp = settings.getInt(PREFERENCE_HELP, -1);
		int iLevel = settings.getInt(PREFERENCE_LEVEL, -1);
		int iLevel01 = settings.getInt(PREFERENCE_LEVEL_01, -1);
		int iLevel02 = settings.getInt(PREFERENCE_LEVEL_02, -1);
		int iLevel03 = settings.getInt(PREFERENCE_LEVEL_03, -1);
		int iLevel04 = settings.getInt(PREFERENCE_LEVEL_04, -1);
		int iLevel05 = settings.getInt(PREFERENCE_LEVEL_05, -1);
		int iLevel06 = settings.getInt(PREFERENCE_LEVEL_06, -1);

		if(iHelp>=0) {
			String sHelp = String.valueOf(iHelp);
			int iIndex = (int) CommonFunctions.getIndexOfLevel(sHelp);
			if((iIndex>0) && (iIndex<mLevelAdapter.getCount())) {
				mSpinnerHelp.setSelection(iIndex);
			} else {
				mSpinnerHelp.setSelection(0);
			}
			//mEditTextHelpId.setText();
		}
		if(iLevel>=0) {
			String sLevel = String.valueOf(iLevel);
			int iIndex = (int) CommonFunctions.getIndexOfLevel(sLevel);
			if((iIndex>0) && (iIndex<mLevelAdapter.getCount())) {
				mSpinnerLevel.setSelection(iIndex);
			} else {
				mSpinnerLevel.setSelection(0);
			}
			//mEditTextLevelId.setText(String.valueOf(iLevel));
		}
		if(iLevel01>=0) {
			mEditTextLevelId01.setText(String.valueOf(iLevel01));
		}
		if(iLevel02>=0) {
			mEditTextLevelId02.setText(String.valueOf(iLevel02));
		}
		if(iLevel03>=0) {
			mEditTextLevelId03.setText(String.valueOf(iLevel03));
		}
		if(iLevel04>=0) {
			mEditTextLevelId04.setText(String.valueOf(iLevel04));
		}
		if(iLevel05>=0) {
			mEditTextLevelId05.setText(String.valueOf(iLevel05));
		}
		if(iLevel06>=0) {
			mEditTextLevelId06.setText(String.valueOf(iLevel06));
		}


	}

}