package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class CreditsActivity extends Activity {


	private class Credit {
		public String mType = null; 
		public String mName = null; 
		public String mAuthor = null;
		public String mUrl = null;
		public String mLicense = null; 
		public String mAcknowledgement = null;
		public String mUsage = null;
	};


	private class CreditsParser extends DefaultHandler {
		private final String CREDITSPARSER = "CreditsParser";
		private String mText = "";
		private Credit mCredit = null;
		private ArrayList<Credit> mCredits = new ArrayList<Credit>(); 

		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if(localName.equalsIgnoreCase("credit")) {
				mCredit = new Credit();
				mCredit.mType = attributes.getValue("type");
				mCredit.mAcknowledgement = attributes.getValue("acknowledgement");
				mCredit.mAuthor = attributes.getValue("author");
				mCredit.mLicense = attributes.getValue("license");
				mCredit.mName = attributes.getValue("name");
				mCredit.mUrl = attributes.getValue("url");
				mCredit.mUsage  = attributes.getValue("usage");

			} else {
				Log.e(CREDITSPARSER, "unknown opening tag in credits file: "+localName);
			}

		}

		public void endElement(String uri, String localName, String qName) {
			if(localName.equalsIgnoreCase("credit")) {
				if(mCredit!=null){
					mCredits.add(mCredit);
				}
				mCredit = null;
			}

		}

		public void characters(char[] ch, int start, int length) {
			this.mText  += new String(ch, start, length);
		}

		public ArrayList<Credit> getCreditsList() {
			return mCredits;
		}


	};

	CreditsParser mCreditsParser = new CreditsParser(); 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AssetManager a = getAssets();
		try {
			InputStream is = a.open("credits.xml");
			SAXParserFactory spf = null;
			SAXParser sp = null;
			spf = SAXParserFactory.newInstance();
			if (spf != null) {
				sp = spf.newSAXParser();
				sp.parse(is, mCreditsParser);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setContentView(R.layout.l00_credits);
		TextView t = (TextView)findViewById(R.id.l00_TextViewCredits);
		ArrayList<Credit> list = mCreditsParser.getCreditsList();

		HashSet<String> authors = new HashSet<String>();
		
		ArrayList<String> artworks = new ArrayList<String>();
		ArrayList<String> textures = new ArrayList<String>();
		ArrayList<String> sounds = new ArrayList<String>();
		ArrayList<String> models = new ArrayList<String>();
		ArrayList<String> photos = new ArrayList<String>();
		ArrayList<String> music = new ArrayList<String>();
		ArrayList<String> texts = new ArrayList<String>();
		ArrayList<String> others = new ArrayList<String>();


		for(Credit credit : list){
			if(credit.mAuthor!=null) {
				authors.add(credit.mAuthor);
				if(credit.mType.equalsIgnoreCase("texture")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+=getResources().getString(R.string.l00_credits_01)+"\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02)+"\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					textures.add(text);
				} else if(credit.mType.equalsIgnoreCase("artwork")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+= getResources().getString(R.string.l00_credits_03)+"\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+= getResources().getString(R.string.l00_credits_02)+"\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					artworks.add(text);
				} else if(credit.mType.equalsIgnoreCase("sound")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+=  getResources().getString(R.string.l00_credits_04)+"\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02)+"\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					sounds.add(text);
				} else if(credit.mType.equalsIgnoreCase("music")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+=getResources().getString(R.string.l00_credits_05) + "\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02)+ "\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					music.add(text);
				} else if(credit.mType.equalsIgnoreCase("3D model")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+= getResources().getString(R.string.l00_credits_06) +"\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02) + "\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					models.add(text);
				} else if(credit.mType.equalsIgnoreCase("photo")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+= getResources().getString(R.string.l00_credits_07) + "\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02) + "\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					photos.add(text);
				} else if(credit.mType.equalsIgnoreCase("text")) {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+= getResources().getString(R.string.l00_credits_08)+ "\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02) + "\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					texts.add(text);
				} else {
					String text = credit.mAuthor;
					if(credit.mName!=null) {
						text+= getResources().getString(R.string.l00_credits_09) + "\""+ credit.mName + "\". ";
					} 
					if(credit.mLicense!=null) {
						text+=getResources().getString(R.string.l00_credits_02) + "\""+ credit.mLicense + "\". ";
					}
					if(credit.mAcknowledgement!=null) {
						text+="(\""+ credit.mAcknowledgement + "\").";
					}
					others.add(text);
				}


			}
		}

		String alltext = getResources().getString(R.string.l00_credits_10) + " \n";

		alltext += "\n";
		for(String m : authors) {
			alltext+= m +", ";
		}
		alltext+= getResources().getString(R.string.l00_credits_11) +"\n\n\n";

		if(!music.isEmpty()){
			alltext += "\n"+getResources().getString(R.string.l00_credits_12)+"\n\n";
			for(String m : music) {
				alltext+= m +"\n";
			}
		}


		if(!sounds.isEmpty()){
			alltext += "\n"+getResources().getString(R.string.l00_credits_13)+"\n\n";
			for(String s : sounds) {
				alltext+=s+"\n";
			}
		}

		if(!models.isEmpty()){
			alltext += "\n" +getResources().getString(R.string.l00_credits_14) + "\n\n";
			for(String m : models) {
				alltext+=m+"\n";
			}
		}

		if(!textures.isEmpty()){
			alltext += "\n"+ getResources().getString(R.string.l00_credits_15)+"\n\n";
			for(String t0 : textures) {
				alltext+=t0+"\n";
			}
		}

		if(!artworks.isEmpty()){
			alltext += "\n" + getResources().getString(R.string.l00_credits_16) + "\n\n";
			for(String a1 : artworks) {
				alltext+=a1+"\n";
			}
		}

		if(!texts.isEmpty()){
			alltext += "\n" +getResources().getString(R.string.l00_credits_17) + "\n\n";
			for(String t1 : texts) {
				alltext+=t1+"\n";
			}
		}

		if(!photos.isEmpty()){
			alltext += "\n" +getResources().getString(R.string.l00_credits_18)  + "\n\n";
			for(String p : photos) {
				alltext+=p+"\n";
			}
		}

		if(!others.isEmpty()){
			alltext += "\n" + getResources().getString(R.string.l00_credits_19) + "\n\n";
			for(String o : others) {
				alltext+=o+"\n";
			}
		}

		t.setText(alltext);

	}
}
