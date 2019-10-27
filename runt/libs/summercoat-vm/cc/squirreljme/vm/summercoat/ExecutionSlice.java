// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.nncc.ArgumentFormat;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import java.io.PrintStream;

/**
 * This represents a single slice of execution.
 *
 * @since 2019/10/27
 */
public final class ExecutionSlice
{
	/**
	 * Prints the information about this slice.
	 *
	 * @since 2019/10/27
	 */
	public final void print()
	{
		this.print(System.err);
	}
	
	/**
	 * Prints the information about this slice.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/27
	 */
	public final void print(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		
		/*
		PrintStream out = System.err;
		
		// Limit class name
		CallTraceElement trace = this.trace(__nf);
		String cname = "" + trace.className();
		int nl;
		if ((nl = cname.length()) > 20)
			cname = cname.substring(nl - 20, nl);
		
		// Print Header (with location info)
		out.printf("***** @%08x %-19.19s/%10.10s | L%-4d/J%-3d %20.20s::%s %n",
			__nf._pc,
			NativeInstruction.mnemonic(__op),
			InstructionMnemonics.toString(trace.byteCodeInstruction()),
			trace.line(),
			trace.byteCodeAddress(),
			cname,
			trace.methodName() + ":" + trace.methodDescriptor());
		
		// Is this an invoke?
		boolean isinvoke = (__op == NativeInstructionType.INVOKE ||
			__op == NativeInstructionType.SYSTEM_CALL);
		
		// Arguments to print, invocations get 1 (pc) + register list
		int naf = (isinvoke ? 1 + __reglist.length:
			__af.length);
		
		// Used to modify some calls
		int encoding = NativeInstruction.encoding(__op);
		
		// Print out arguments to the call
		out.printf("  A:[");
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
			
			// Comma
			if (i > 0)
				out.print(", ");
			
			// Can be special?
			boolean canspec = true;
			if (encoding == NativeInstructionType.DEBUG_ENTRY ||
				encoding == NativeInstructionType.DEBUG_POINT ||
				(encoding == NativeInstructionType.IF_ICMP &&
					i == 2) ||
				(encoding == NativeInstructionType.MATH_CONST_INT &&
					i == 1) ||
				(encoding == NativeInstructionType.IFEQ_CONST &&
					i == 1) ||
				(encoding == NativeInstructionType.ATOMIC_INT_INCREMENT &&
					i == 1) ||
				(encoding == NativeInstructionType.
					ATOMIC_INT_DECREMENT_AND_GET && i == 2) ||
				(encoding == NativeInstructionType.MEMORY_OFF_ICONST &&
					i == 2) ||
				(encoding == NativeInstructionType.LOAD_POOL && i == 0))
				canspec = false;
			
			// Is this a special register?
			String spec = null;
			if (canspec)
				switch (iv)
				{
					case NativeCode.ZERO_REGISTER:
						spec = "zero";
						break;
					
					case NativeCode.RETURN_REGISTER:
						spec = "return1";
						break;
					
					case NativeCode.RETURN_REGISTER + 1:
						spec = "return2";
						break;
					
					case NativeCode.EXCEPTION_REGISTER:
						spec = "exception";
						break;
					
					case NativeCode.STATIC_FIELD_REGISTER:
						spec = "sfieldptr";
						break;
					
					case NativeCode.THREAD_REGISTER:
						spec = "thread";
						break;
					
					case NativeCode.POOL_REGISTER:
						spec = "pool";
						break;
					
					case NativeCode.NEXT_POOL_REGISTER:
						spec = "nextpool";
						break;
					
					case NativeCode.ARGUMENT_REGISTER_BASE:
						spec = "a0/this";
						break;
				}
			
			// Print special register
			if (spec != null)
				out.printf("%10.10s", spec);
			else
				out.printf("%10d", iv);
		}
		out.print("] | ");
		
		// And register value
		out.printf("V:[");
		int[] registers = __nf._registers;
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
				
			if (i > 0)
				out.print(", ");
			
			// Load register value
			if (iv < 0 || iv >= registers.length)
				out.print("----------");
			else
				out.printf("%+10d", registers[iv]);
		}
		out.println("]");
		*/
	}
	
	/**
	 * Creates an execution slice of the given frame information.
	 *
	 * @param __nf The native frame.
	 * @param __op The operand.
	 * @param __args The arguments.
	 * @param __reglist The register list for method calls.
	 * @return The execution slice.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/27
	 */
	public static final ExecutionSlice of(NativeCPU.Frame __nf, int __op,
		int[] __args, int[] __reglist)
		throws NullPointerException
	{
		if (__nf == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		
		/*
		PrintStream out = System.err;
		
		// Limit class name
		CallTraceElement trace = this.trace(__nf);
		String cname = "" + trace.className();
		int nl;
		if ((nl = cname.length()) > 20)
			cname = cname.substring(nl - 20, nl);
		
		// Print Header (with location info)
		out.printf("***** @%08x %-19.19s/%10.10s | L%-4d/J%-3d %20.20s::%s %n",
			__nf._pc,
			NativeInstruction.mnemonic(__op),
			InstructionMnemonics.toString(trace.byteCodeInstruction()),
			trace.line(),
			trace.byteCodeAddress(),
			cname,
			trace.methodName() + ":" + trace.methodDescriptor());
		
		// Is this an invoke?
		boolean isinvoke = (__op == NativeInstructionType.INVOKE ||
			__op == NativeInstructionType.SYSTEM_CALL);
		
		// Arguments to print, invocations get 1 (pc) + register list
		int naf = (isinvoke ? 1 + __reglist.length:
			__af.length);
		
		// Used to modify some calls
		int encoding = NativeInstruction.encoding(__op);
		
		// Print out arguments to the call
		out.printf("  A:[");
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
			
			// Comma
			if (i > 0)
				out.print(", ");
			
			// Can be special?
			boolean canspec = true;
			if (encoding == NativeInstructionType.DEBUG_ENTRY ||
				encoding == NativeInstructionType.DEBUG_POINT ||
				(encoding == NativeInstructionType.IF_ICMP &&
					i == 2) ||
				(encoding == NativeInstructionType.MATH_CONST_INT &&
					i == 1) ||
				(encoding == NativeInstructionType.IFEQ_CONST &&
					i == 1) ||
				(encoding == NativeInstructionType.ATOMIC_INT_INCREMENT &&
					i == 1) ||
				(encoding == NativeInstructionType.
					ATOMIC_INT_DECREMENT_AND_GET && i == 2) ||
				(encoding == NativeInstructionType.MEMORY_OFF_ICONST &&
					i == 2) ||
				(encoding == NativeInstructionType.LOAD_POOL && i == 0))
				canspec = false;
			
			// Is this a special register?
			String spec = null;
			if (canspec)
				switch (iv)
				{
					case NativeCode.ZERO_REGISTER:
						spec = "zero";
						break;
					
					case NativeCode.RETURN_REGISTER:
						spec = "return1";
						break;
					
					case NativeCode.RETURN_REGISTER + 1:
						spec = "return2";
						break;
					
					case NativeCode.EXCEPTION_REGISTER:
						spec = "exception";
						break;
					
					case NativeCode.STATIC_FIELD_REGISTER:
						spec = "sfieldptr";
						break;
					
					case NativeCode.THREAD_REGISTER:
						spec = "thread";
						break;
					
					case NativeCode.POOL_REGISTER:
						spec = "pool";
						break;
					
					case NativeCode.NEXT_POOL_REGISTER:
						spec = "nextpool";
						break;
					
					case NativeCode.ARGUMENT_REGISTER_BASE:
						spec = "a0/this";
						break;
				}
			
			// Print special register
			if (spec != null)
				out.printf("%10.10s", spec);
			else
				out.printf("%10d", iv);
		}
		out.print("] | ");
		
		// And register value
		out.printf("V:[");
		int[] registers = __nf._registers;
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
				
			if (i > 0)
				out.print(", ");
			
			// Load register value
			if (iv < 0 || iv >= registers.length)
				out.print("----------");
			else
				out.printf("%+10d", registers[iv]);
		}
		out.println("]");
		*/
	}
}
