package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class CustomDialog {

	public CustomDialog(Context context)
	{
		Dialog dialog = new Dialog(context);
		
		dialog.setContentView(R.layout.l84_customdialog);
		dialog.setTitle("Info");
		
		TextView text = (TextView) dialog.findViewById(R.id.l84_dialogtext);
		text.setText("Hier sollte Text stehen");
		
		ImageView image = (ImageView) dialog.findViewById(R.id.l84_dialogimage);
		image.setImageResource(R.drawable.l84_tex_gem_oct);
		
		dialog.show();
	}
	
	
	
}
