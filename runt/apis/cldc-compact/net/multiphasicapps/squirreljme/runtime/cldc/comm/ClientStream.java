// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.comm;

import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * This class handles the stream on the client side which manages an
 * asynchronous event based system that enables two way communication between
 * client user-space processes and the system-space process.
 *
 * @since 2017/12/08
 */
public final class ClientStream
{
	/** The input stream. */
	protected final DataInputStream input;
	
	/** The output stream. */
	protected final DataOutputStream output;
	
	/**
	 * Initializes the client stream which reads events from the given
	 * input stream (from the server) and writes to the server (from the
	 * client) using the output stream.
	 *
	 * @param __is The stream to read events from.
	 * @param __os The stream to write events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public ClientStream(InputStream __is, OutputStream __os)
		throws NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		this.input = new DataInputStream(__is);
		this.output = new DataOutputStream(__os);
	}
}

