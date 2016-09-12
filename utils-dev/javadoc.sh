#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# DESCRIPTION: Build and update the JavaDoc.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

# Build the markdown doclet
if ! "$__exedir/../build.sh" build doclet-markdown
then
	echo "Failed to build doclet-markdown." 1>&2
	exit 1
fi

# Generate documentation for all projects
for __dir in "$__exedir/../src/"*
do
	# If not a directory and does not contain a manifest, ignore
	if [ ! -d "$__dir" ] || [ ! -f "$__dir/META-INF/MANIFEST.MF" ]
	then
		continue
	fi
	
	# Note
	__base="$(basename "$__dir")"
	echo "Running for $__base..." 1>&2
	
	# No source code will cause it to fail
	__sf="$(find "$__dir" -type f | grep '\.java$')"
	if [ -z "$__sf" ]
	then
		echo "No sources." 1>&2
		continue
	fi
	
	# Run JavaDoc
	javadoc -doclet net.multiphasicapps.doclet.markdown.DocletMain \
		-docletpath "doclet-markdown.jar:." -subpackages \
		$__sf
done 

