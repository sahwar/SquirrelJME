// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This is a map which cannot be modified.
 *
 * @param <K> Key type to use.
 * @param <V> Value type to store.
 * @since 2016/02/29
 */
final class __UnmodifiableMap__<K, V>
	extends AbstractMap<K, V>
{
	/** The existing map to wrap. */
	protected final Map<K, V> wrapped;
	
	/** Entry set cache, since it is not always needed. */
	private volatile Reference<Set<Map.Entry<K, V>>> _escache;
	
	/**
	 * Initializes the unmodifiable wrapped map.
	 *
	 * @param __w The map to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/29
	 */
	__UnmodifiableMap__(Map<K, V> __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Set
		wrapped = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		return wrapped.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		// Get reference to the map
		Reference<Set<Map.Entry<K, V>>> ref = _escache;
		Set<Map.Entry<K, V>> rv = null;
		
		// In reference?
		if (ref != null)
			rv = ref.get();
		
		// Needs initialization?
		if (rv == null)
			_escache = new WeakReference<>((rv = new __SetView__()));
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public V get(Object __k)
	{
		return wrapped.get(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public int size()
	{
		return wrapped.size();
	}
	
	/**
	 * This is the iterator over the entry set of the map.
	 *
	 * @since 2016/02/29
	 */
	private final class __SetIterator__
		implements Iterator<Map.Entry<K, V>>
	{
		/** The base iterator. */
		protected final Iterator<Map.Entry<K, V>> from =
			wrapped.entrySet().iterator();
		
		/**
		 * Initializes the iterator.
		 *
		 * @since 2016/02/29
		 */
		private __SetIterator__()
		{
		}
	
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public boolean hasNext()
		{
			return from.hasNext();
		}
	
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public Map.Entry<K, V> next()
		{
			// Get the next entry
			Map.Entry<K, V> ent = from.next();
			
			// If null, this is a bad map but possibly might be valid
			if (ent == null)
				return null;
			
			// Wrap it
			return new __SetEntry__<K, V>(ent);
		}
		
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
	
	/**
	 * This implements the unmodifiable set view.
	 *
	 * @since 2016/02/29
	 */
	private final class __SetView__
		extends AbstractSet<Map.Entry<K, V>>
	{
		/**
		 * Initializes the set view.
		 *
		 * @since 2016/02/29
		 */
		private __SetView__()
		{
		}
	
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public Iterator<Map.Entry<K, V>> iterator()
		{
			return new __SetIterator__();
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public int size()
		{
			return wrapped.size();
		}
	}
	
	/**
	 * This is a single entry in the set.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @since 2016/02/29
	 */
	private static final class __SetEntry__<K, V>
		implements Map.Entry<K, V>
	{
		/** The base entry. */
		protected final Map.Entry<K, V> base;
		
		/**
		 * Initializes the set entry.
		 *
		 * @param __e The entry to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/02/29
		 */
		private __SetEntry__(Map.Entry<K, V> __e)
			throws NullPointerException
		{
			// Check
			if (__e == null)
				throw new NullPointerException("NARG");
			
			// Set
			base = __e;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public boolean equals(Object __a)
		{
			return base.equals(__a);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public K getKey()
		{
			return base.getKey();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public V getValue()
		{
			return base.getValue();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public int hashCode()
		{
			return base.hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public V setValue(V __a)
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

