// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
public abstract class Displayable
{
	/** The display this is currently associated with. */
	volatile Display _current;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the height of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current height of this displayable in pixels, if it is not
	 * visible then the default height is returned.
	 * @since 2017/02/08
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the width of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current width of this displayable in pixels, if it is not
	 * visible then the default width is returned.
	 * @since 2017/02/08
	 */
	public abstract int getWidth();
	
	public void addCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public Command getCommand(int __p)
	{
		throw new todo.TODO();
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new todo.TODO();
	}
	
	public Command[] getCommands()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display.
	 * @since 2016/10/08
	 */
	public Display getCurrentDisplay()
	{
		return this._current;
	}
	
	public Menu getMenu(int __p)
	{
		throw new todo.TODO();
	}
	
	public Ticker getTicker()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the title of this displayable.
	 *
	 * @return The title of this displayable.
	 * @since 2016/10/08
	 */
	public String getTitle()
	{
		throw new todo.TODO();
	}
	
	public void invalidateCommandLayout()
	{
		throw new todo.TODO();
	}
	
	public boolean isShown()
	{
		throw new todo.TODO();
	}
	
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void removeCommandOrMenu(int __p)
	{
		throw new todo.TODO();
	}
	
	public void setCommand(Command __c, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setCommandLayoutPolicy(CommandLayoutPolicy __p)
	{
		throw new todo.TODO();
	}
	
	public void setCommandListener(CommandListener __a)
	{
		throw new todo.TODO();
	}
	
	public void setMenu(Menu __m, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setTicker(Ticker __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __a The title to use.
	 * @since 2016/10/08
	 */
	public void setTitle(String __a)
	{
		throw new todo.TODO();
		/*// Set
		this._title = __a;
		
		// Set the title to use
		DisplayInstance instance = this._instance;
		if (instance != null)
			instance.setTitle(__a);*/
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new heigh of the displayable.
	 * @since 2016/10/10
	 */
	@__SerializedEvent__
	protected void sizeChanged(int __w, int __h)
	{
		// Implemented by sub-classes
	}
	
	/**
	 * Returns the height if the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable height or the maximum height of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getHeight()
	{
		Display d = getCurrentDisplay();
		return (d != null ? d.getHeight() :
			Display.getDisplays(0)[0].getHeight());
	}
	
	/**
	 * Returns the width if the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable width or the maximum width of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getWidth()
	{
		Display d = getCurrentDisplay();
		return (d != null ? d.getWidth() :
			Display.getDisplays(0)[0].getWidth());
	}
}


