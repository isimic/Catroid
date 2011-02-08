package at.tugraz.ist.paintroid.test;

import java.io.File;

import com.jayway.android.robotium.solo.Solo;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.ist.paintroid.MainActivity;


public class FileTests extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private MainActivity mainActivity;
	
	final int BRUSH = 3;
	final int FILE = 7;

	public FileTests() {
		super("at.tugraz.ist.paintroid", MainActivity.class);

	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testSavePicturePath() throws Exception{
		solo.clickOnImageButton(FILE);
		solo.clickOnButton("New Drawing");
		
		solo.clickOnImageButton(FILE);
		solo.clickOnButton("Save");
		solo.enterText(0, "test_save");
		solo.clickOnButton("Done");
		
		mainActivity = (MainActivity) solo.getCurrentActivity();
		assertEquals(mainActivity.getSavedFileUriString(), "/mnt/sdcard/Pictures/Paintroid/test_save.png");
		
	}
	
	public void testPictureIsSavedCorrectly() throws Exception{
		/**
		 * ToDo:
		 * Check it if it is possible to test this with Robotium?
		 * Choose Pic from gallery...
		 */
		File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures/Paintroid/test_drawTransparent.png");
		if(dir.exists()) {
		    solo.clickOnMenuItem("Quit");
		}else{
			assertTrue(false);
		}
			
	}
}