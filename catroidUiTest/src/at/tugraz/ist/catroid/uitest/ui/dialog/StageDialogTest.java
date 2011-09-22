package at.tugraz.ist.catroid.uitest.ui.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.common.SoundInfo;
import at.tugraz.ist.catroid.common.Values;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Script;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.StartScript;
import at.tugraz.ist.catroid.content.bricks.PlaySoundBrick;
import at.tugraz.ist.catroid.content.bricks.SetSizeToBrick;
import at.tugraz.ist.catroid.content.bricks.WaitBrick;
import at.tugraz.ist.catroid.io.SoundManager;
import at.tugraz.ist.catroid.io.StorageHandler;
import at.tugraz.ist.catroid.stage.StageActivity;
import at.tugraz.ist.catroid.ui.MainMenuActivity;
import at.tugraz.ist.catroid.uitest.util.UiTestUtils;

import com.jayway.android.robotium.solo.Solo;

public class StageDialogTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private Solo solo;
	private String testProject = UiTestUtils.PROJECTNAME1;
	private StorageHandler storageHandler;

	public StageDialogTest() {
		super("at.tugraz.ist.catroid", MainMenuActivity.class);
		storageHandler = StorageHandler.getInstance();
	}

	@Override
	public void setUp() throws Exception {
		UiTestUtils.clearAllUtilTestProjects();

		solo = new Solo(getInstrumentation(), getActivity());
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
	}

	public void testBackButtonPressedTwice() {

		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);
		solo.sleep(1000);
		solo.goBack();

		solo.goBack();

		solo.assertCurrentActivity("Program is not in stage activity", MainMenuActivity.class);
	}

	public void testBackToPreviousActivity() throws NameNotFoundException, IOException {
		createTestProject(testProject);
		solo.clickOnButton(getActivity().getString(R.string.my_projects));
		solo.clickOnText(testProject);

		Activity previousActivity = getActivity();

		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);

		solo.goBack();
		solo.clickOnButton(getActivity().getString(R.string.back));

		solo.sleep(1000);
		assertEquals("Not equal Activities", previousActivity, getActivity());
	}

	public void testPauseOnBackButton() {
		double scale = 100.0;

		Project project = new Project(getActivity(), testProject);
		Sprite sprite = new Sprite("testSprite");
		Script script = new StartScript("script", sprite);
		WaitBrick waitBrick = new WaitBrick(sprite, 5000);
		SetSizeToBrick scaleCostumeBrick = new SetSizeToBrick(sprite, scale);

		script.getBrickList().add(waitBrick);
		script.getBrickList().add(scaleCostumeBrick);
		sprite.addScript(script);
		project.getSpriteList().add(sprite);

		storageHandler.saveProject(project);
		ProjectManager.getInstance().setProject(project);

		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);

		//		assertEquals("Unexpected sprite size", 100.0, sprite.getSize());
		//		solo.goBack();
		//		solo.sleep(6000);
		//		solo.goBack();
		//		assertEquals("Unexpected sprite size", 100.0, sprite.getSize());
		//		solo.sleep(4000);
		//		assertEquals("Unexpected sprite size", scale, sprite.getSize());
	}

	public void testRestartButtonActivityChain() throws NameNotFoundException, IOException {
		createTestProject(testProject);
		solo.clickOnButton(getActivity().getString(R.string.my_projects));
		solo.clickOnText(testProject);

		Activity currentActivity = solo.getCurrentActivity();

		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);

		solo.sleep(1000);
		solo.goBack();
		solo.sleep(1000);
		solo.clickOnButton(getActivity().getString(R.string.restart_current_project));
		solo.sleep(1000);

		solo.assertCurrentActivity("Program is not in stage activity", StageActivity.class);

		solo.sleep(500);
		solo.goBack();
		solo.sleep(500);
		solo.clickOnButton(getActivity().getString(R.string.back));
		solo.sleep(500);
		assertEquals("Returned to wrong Activity", currentActivity, solo.getCurrentActivity());
	}

	public void testRestartButtonScriptPosition() {
		ArrayList<Integer> scriptPositionsStart = new ArrayList<Integer>();
		ArrayList<Integer> scriptPositionsRestart = new ArrayList<Integer>();
		scriptPositionsStart.clear();
		scriptPositionsRestart.clear();

		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);

		solo.sleep(2000);

		ProjectManager projectManager = ProjectManager.getInstance();
		Project projectStart = projectManager.getCurrentProject();

		String projectNameStart = projectStart.getName();

		//scriptPositions at start
		List<Sprite> spriteList = projectStart.getSpriteList();
		for (int i = 0; i < spriteList.size(); i++) {
			Sprite sprite = spriteList.get(i);
			int size = sprite.getNumberOfScripts();
			for (int j = 0; j < size; j++) {
				scriptPositionsRestart.add(sprite.getScript(j).getExecutingBrickIndex());
			}
		}
		spriteList.clear();

		solo.clickOnScreen(Values.SCREEN_WIDTH / 2, Values.SCREEN_HEIGHT / 2);
		solo.sleep(500);
		solo.goBack();
		solo.sleep(500);
		solo.clickOnButton(getActivity().getString(R.string.restart_current_project));
		solo.sleep(1000);

		//scriptPositions in between
		Project projectRestart = ProjectManager.getInstance().getCurrentProject();
		String projectNameRestart = projectRestart.getName();

		assertEquals("Wrong project after restart", projectNameStart, projectNameRestart);

		spriteList = projectRestart.getSpriteList();
		for (int i = 0; i < spriteList.size(); i++) {
			Sprite sprite = spriteList.get(i);
			int size = sprite.getNumberOfScripts();
			for (int j = 0; j < size; j++) {
				scriptPositionsRestart.add(sprite.getScript(j).getExecutingBrickIndex());
			}
		}

		for (int i = 0; i < scriptPositionsStart.size(); i++) {
			assertEquals(scriptPositionsStart.get(i).intValue(), scriptPositionsRestart.get(i).intValue());
		}
	}

	public void testRestartProjectWithSound() {

		String projectName = UiTestUtils.PROJECTNAME1;
		//creating sprites for project:
		Sprite firstSprite = new Sprite("sprite1");
		Script startScript = new StartScript("startscript", firstSprite);

		PlaySoundBrick playSoundBrick = new PlaySoundBrick(firstSprite);

		startScript.addBrick(playSoundBrick);

		firstSprite.addScript(startScript);

		ArrayList<Sprite> spriteList = new ArrayList<Sprite>();
		spriteList.add(firstSprite);
		Project project = UiTestUtils.createProject(projectName, spriteList, getActivity());

		File soundFile = UiTestUtils.saveFileToProject(projectName, "soundfile.mp3",
				at.tugraz.ist.catroid.uitest.R.raw.longsound, getInstrumentation().getContext(),
				UiTestUtils.TYPE_SOUND_FILE);

		SoundInfo soundInfo = new SoundInfo();
		soundInfo.setSoundFileName(soundFile.getName());
		soundInfo.setTitle(soundFile.getName());
		playSoundBrick.setSoundInfo(soundInfo);

		firstSprite.getSoundList().add(soundInfo);

		storageHandler.saveProject(project);

		MediaPlayer mediaPlayer = SoundManager.getInstance().getMediaPlayer();
		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);
		solo.sleep(1500);
		assertTrue("Sound not playing.", mediaPlayer.isPlaying());
		int positionBeforeRestart = mediaPlayer.getCurrentPosition();
		solo.goBack();
		solo.sleep(500);
		assertFalse("Sound playing but should be paused.", mediaPlayer.isPlaying());
		solo.clickOnButton(getActivity().getString(R.string.restart_current_project));
		solo.sleep(100);
		int positionAfterRestart = mediaPlayer.getCurrentPosition();
		assertTrue("Sound not playing after stage restart.", mediaPlayer.isPlaying());
		assertTrue("Sound did not play from start!", positionBeforeRestart > positionAfterRestart);

	}

	public void createTestProject(String projectName) throws IOException, NameNotFoundException {
		StorageHandler storageHandler = StorageHandler.getInstance();

		Project project = new Project(getActivity(), projectName);
		Sprite firstSprite = new Sprite("cat");
		Sprite secondSprite = new Sprite("dog");
		Sprite thirdSprite = new Sprite("horse");
		Sprite fourthSprite = new Sprite("pig");

		project.addSprite(firstSprite);
		project.addSprite(secondSprite);
		project.addSprite(thirdSprite);
		project.addSprite(fourthSprite);

		storageHandler.saveProject(project);
	}

}
