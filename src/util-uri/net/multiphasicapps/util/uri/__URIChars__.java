// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.uri;

/**
 * This is used to specify which characters are valid.
 *
 * @since 2016/06/23
 */
enum __URIChars__
{
	/** The scheme component. */
	SCHEME(true)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/23
		 */
		@Override
		public boolean isValid(int __p, char __c)
		{
			// Letters are always valid
			if (__isAlpha(__c))
				return true;
			
			// Nothing else is valid after the first character
			if (__p != 0)
				return false;
			
			// Second character and up
			return __isDigit(__c) || __c == '+' || __c == '-' || __c == '.';
		}
	},
	
	/** The path component. */
	PATH(false)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/23
		 */
		@Override
		public boolean isValid(int __p, char __c)
		{
			return __isUnreserved(__c) || __isSubDelim(__c) || __c == ':' ||
				__c == '@';
		}
	},
	
	/** The fragment component. */
	FRAGMENT(false)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/23
		 */
		@Override
		public boolean isValid(int __p, char __c)
		{
			return PATH.isValid(__p, __c) || __c == '/' || __c == '?';
		}
	},
	
	/** End. */
	;
	
	/** Should non-hexadecimal alphabetical characters be lowercased? */
	protected boolean lowercase;
	
	/**
	 * Initializes the character group information.
	 *
	 * @param __lc If {@code true} then non-hexadecimal sequences should be
	 * lowercased.
	 * @since 2016/06/23
	 */
	private __URIChars__(boolean __lc)
	{
		this.lowercase = __lc;
	}
	
	/**
	 * Returns {@code true} if the character can exist for the given part
	 * without being encoded.
	 *
	 * @param __p The position of the character.
	 * @param __c The character to check.
	 * @return {@code true} if it does not have to be escaped.
	 * @since 2016/06/23
	 */
	public abstract boolean isValid(int __p, char __c);
	
	/**
	 * Returns {@code true} if the characters that are not part of
	 * hexadecimal sequences should be lowercased.
	 *
	 * @return If characters should be lowercased.
	 * @since 2016/06/23
	 */
	public final boolean doLowerCase()
	{
		return this.lowercase;
	}
	
	/**
	 * Is this an alphabetical character?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is alphabetical.
	 * @since 2016/06/23
	 */
	static boolean __isAlpha(char __c)
	{
		return (__c >= 'A' && __c <= 'Z') || (__c >= 'a' && __c <= 'z');
	}
	
	/**
	 * Is this a digit?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is a digit.
	 * @since 2016/06/23
	 */
	static boolean __isDigit(char __c)
	{
		return (__c >= '0' && __c <= '9');
	}
	
	/**
	 * Is this a sub-delimeter?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is a sub-delimeter.
	 * @since 2016/06/23
	 */
	static boolean __isSubDelim(char __c)
	{
		return __c == '!' || __c == '$' || __c == '&' || __c == '\'' ||
			__c == '(' || __c == ')' || __c == '*' || __c == '+' ||
			__c == ',' || __c == ';' || __c == '=';
	}
	
	/**
	 * Is this an unreserved character?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is unreserved.
	 * @since 2016/06/23
	 */
	static boolean __isUnreserved(char __c)
	{
		return __isAlpha(__c) || __isDigit(__c) || __c == '-' || __c == '.' ||
			__c == '_' || __c == '~';
	}
}

