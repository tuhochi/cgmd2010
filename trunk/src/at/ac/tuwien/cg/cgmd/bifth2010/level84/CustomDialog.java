package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class CustomDialog extends AlertDialog {

	private View view;
	
	public CustomDialog(Context context)
	{
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		setTitle("Info");
		
		//TODO: strings auslagern
		TextView text = (TextView) view.findViewById(R.id.l84_dialogtext);
		text.setText("Hier sollte Text stehen");
		
		ImageView image = (ImageView) view.findViewById(R.id.l84_dialogimage);
		image.setImageResource(R.drawable.l84_tex_gem_oct);
		
		setView(view);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
	}
	
}
