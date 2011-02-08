/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.test.content.brick;

import android.test.AndroidTestCase;
import at.tugraz.ist.catroid.content.brick.HideBrick;
import at.tugraz.ist.catroid.content.sprite.Sprite;

public class HideBrickTest extends AndroidTestCase {
	public void testHide() {
		Sprite sprite = new Sprite("new sprite");
		assertTrue("Unexpected default visibility", sprite.isVisible());
		HideBrick hideBrick = new HideBrick(sprite);
		hideBrick.execute();
		assertFalse("Sprite is still visible after HideBrick executed", sprite.isVisible());
	}
	
	public void testNullSprite() {
		HideBrick hideBrick = new HideBrick(null);
		
		try {
			hideBrick.execute();
			fail("Execution of HideBrick with null Sprite did not cause a NullPointerException to be thrown");
		} catch (NullPointerException e) {
			// expected behavior
		}
	}
}