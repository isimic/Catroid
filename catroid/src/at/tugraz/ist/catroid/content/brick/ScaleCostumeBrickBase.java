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
package at.tugraz.ist.catroid.content.brick;

import at.tugraz.ist.catroid.content.entities.PrimitiveWrapper;
import at.tugraz.ist.catroid.content.sprite.Sprite;

/**
 * @author Anton Rieder, Ainul Husna
 *
 */
public abstract class ScaleCostumeBrickBase implements BrickBase {
	private static final long serialVersionUID = 1L;
	private Sprite sprite;
	protected PrimitiveWrapper<Double> scale;

	public ScaleCostumeBrickBase(Sprite sprite, double scale) {
		this.sprite = sprite;
		this.scale  = new PrimitiveWrapper<Double>(scale);
	}

	public void execute() {
		sprite.setScale(scale.getValue());
	}

	public Sprite getSprite() {
		return this.sprite;
	}
	
	public double getScale() {
		return scale.getValue();
	}

}