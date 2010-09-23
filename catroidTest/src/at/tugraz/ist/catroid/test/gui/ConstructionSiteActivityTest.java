package at.tugraz.ist.catroid.test.gui;

import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.ist.catroid.ConstructionSiteActivity;

public class ConstructionSiteActivityTest extends ActivityInstrumentationTestCase2<ConstructionSiteActivity>{

	private ConstructionSiteActivity mActivity;
	
	public ConstructionSiteActivityTest() {
		super("at.tugraz.ist.catroid", ConstructionSiteActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//you have to turn this off if any of the test methods send key events to the application
		setActivityInitialTouchMode(false); 
		mActivity = getActivity();
        
	}
	
	/**
	 * tests if the scroll bar in the tool box dialog is set to show up as long as expected
	 */
	/* TODO: Failure / Error!
	public void testToolBoxScrollBar(){

		//first approach
		
		InputStream stream = mActivity.getApplicationContext().getResources().openRawResource(R.layout.dialog_toolbox);

		
		String string = "";
		//StringBuffer xmlStringBuffer = null;
		try {
			InputStreamReader in = new InputStreamReader(stream, "utf-8");

			//xmlStringBuffer = new StringBuffer(1024);
			
			BufferedReader reader = new BufferedReader(in);
					
			String tempString = "";
//			while ((tempString = reader.readLine()).length() > 0){
//				string += tempString;
//			}
			tempString = reader.readLine();
			string = tempString;
			
			reader.close();
			
//			char[] chars = new char[1024];
//			int numRead = 0;
//			while( (numRead = reader.read(chars)) > -1){
//				string = string + String.valueOf(chars);	
//			}
//	
//			reader.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
		//String xmlString = xmlStringBuffer.toString();
		
		Log.i("ConstructionSiteActivityTest", "xmlString: "+string);
		
		assertTrue(string.contains("scrollbarDefaultDelayBeforeFade"));

		
		//second approach
		
//		XmlResourceParser parser = mActivity.getApplicationContext().getResources().getXml(R.layout.dialog_toolbox);
//		AttributeSet set = Xml.asAttributeSet(parser);
//		
//		
//		int scrollbarDelay = 0;
//		String namespace = parser.getNamespace();
//		
//		try {
//			while(scrollbarDelay == 0){
//				if (parser.getIdAttribute().equals(R.id.toolboxListView))
//						scrollbarDelay = parser.getAttributeIntValue(parser.getNamespace(), "scrollbarDefaultDelayBeforeFade", 0);
//				parser.next();
//			}
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NullPointerException e) {
//			Log.e("ConstructionSiteActivityTest", "did not find scrollbar attribute");
//		}
//		
//		
//		assertTrue(scrollbarDelay==2000);
	}
	*/	
	

}
