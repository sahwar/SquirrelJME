/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"
#include "cc_squirreljme_jvm_mle_UIFormShelf.h"

// The class to forward to
#define SWINGUIFORM_CLASSNAME "cc/squirreljme/emulator/uiform/SwingFormShelf"

// Descriptors for calls
#define SWINGUIFORM_CALLBACK_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/callbacks/UIFormCallback;)V"
#define SWINGUIFORM_DISPLAYS_DESC "()[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;"
#define SWINGUIFORM_DISPLAYCURRENT_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;)Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;"
#define SWINGUIFORM_DISPLAYSHOW_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V"
#define SWINGUIFORM_EQUALSDISPLAY_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;)Z"
#define SWINGUIFORM_EQUALSFORM_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)Z"
#define SWINGUIFORM_EQUALSITEM_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;)Z"
#define SWINGUIFORM_FLUSHEVENTS_DESC "()V"
#define SWINGUIFORM_FORMDELETE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V"
#define SWINGUIFORM_FORMITEMATPOSITION_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_FORMITEMCOUNT_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)I"
#define SWINGUIFORM_FORMITEMPOSITIONGET_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;)I"
#define SWINGUIFORM_FORMITEMPOSITIONSET_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;I)V"
#define SWINGUIFORM_FORMITEMREMOVE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_FORMNEW_DESC "()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;"
#define SWINGUIFORM_INJECTOR_DESC "()Lcc/squirreljme/jvm/mle/callbacks/UIFormCallback;"
#define SWINGUIFORM_ITEMDELETE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;)V"
#define SWINGUIFORM_ITEMNEW_DESC "(I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_ITEMPROPERTY_INT_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;II)V"
#define SWINGUIFORM_ITEMPROPERTY_STR_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;ILjava/lang/String;)V"
#define SWINGUIFORM_METRIC_DESC "(I)I"

JNIEXPORT void JNICALL Impl_mle_FormShelf_callback(JNIEnv* env,
	jclass classy, jobject form, jobject callback)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"callback", SWINGUIFORM_CALLBACK_DESC, form, callback);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_displays(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"displays", SWINGUIFORM_DISPLAYS_DESC);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_displayCurrent(JNIEnv* env,
	jclass classy, jobject display)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"displayCurrent", SWINGUIFORM_DISPLAYCURRENT_DESC,
		display);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_displayShow(JNIEnv* env,
	jclass classy, jobject display, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"displayShow", SWINGUIFORM_DISPLAYSHOW_DESC,
		display, form);
}

JNIEXPORT jboolean JNICALL Impl_mle_FormShelf_equalsDisplay(JNIEnv* env,
	jclass classy, jobject a, jobject b)
{
	return forwardCallStaticBoolean(env, SWINGUIFORM_CLASSNAME,
		"equals", SWINGUIFORM_EQUALSDISPLAY_DESC,
		a, b);
}

JNIEXPORT jboolean JNICALL Impl_mle_FormShelf_equalsForm(JNIEnv* env,
	jclass classy, jobject a, jobject b)
{
	return forwardCallStaticBoolean(env, SWINGUIFORM_CLASSNAME,
		"equals", SWINGUIFORM_EQUALSFORM_DESC,
		a, b);
}

JNIEXPORT jboolean JNICALL Impl_mle_FormShelf_equalsItem(JNIEnv* env,
	jclass classy, jobject a, jobject b)
{
	return forwardCallStaticBoolean(env, SWINGUIFORM_CLASSNAME,
		"equals", SWINGUIFORM_EQUALSITEM_DESC,
		a, b);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_flushEvents(JNIEnv* env,
	jclass classy)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"flushEvents", SWINGUIFORM_FLUSHEVENTS_DESC);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_formDelete(JNIEnv* env,
	jclass classy, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"formDelete", SWINGUIFORM_FORMDELETE_DESC,
		form);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formItemAtPosition(JNIEnv* env,
	jclass classy, jobject form, jint position)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formItemAtPosition", SWINGUIFORM_FORMITEMATPOSITION_DESC,
		form, position);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_formItemCount(JNIEnv* env,
	jclass classy, jobject form)
{
	return forwardCallStaticInteger(env, SWINGUIFORM_CLASSNAME,
		"formItemCount", SWINGUIFORM_FORMITEMCOUNT_DESC,
		form);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_formItemPositionGet(JNIEnv* env,
	jclass classy, jobject form, jobject item)
{
	return forwardCallStaticInteger(env, SWINGUIFORM_CLASSNAME,
		"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONGET_DESC,
		form, item);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_formItemPositionSet(JNIEnv* env,
	jclass classy, jobject form, jobject item, jint position)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONSET_DESC,
		form, item, position);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formItemRemove(JNIEnv* env,
	jclass classy, jobject form, jint position)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formItemRemove", SWINGUIFORM_FORMITEMREMOVE_DESC,
		form, position);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formNew(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formNew", SWINGUIFORM_FORMNEW_DESC);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_injector(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"injector", SWINGUIFORM_INJECTOR_DESC);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_itemDelete(JNIEnv* env,
	jclass classy, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"itemDelete", SWINGUIFORM_ITEMDELETE_DESC,
		form);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_itemNew(JNIEnv* env,
	jclass classy, jint type)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"itemNew", SWINGUIFORM_ITEMNEW_DESC,
		type);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_itemPropertyInt(JNIEnv* env,
	jclass classy, jobject item, jint property, jint newValue)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"itemProperty", SWINGUIFORM_ITEMPROPERTY_INT_DESC,
		item, property, newValue);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_itemPropertyStr(JNIEnv* env,
	jclass classy, jobject item, jint property, jobject newValue)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"itemProperty", SWINGUIFORM_ITEMPROPERTY_STR_DESC,
		item, property, newValue);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_metric(JNIEnv* env, jclass classy,
	jint metricId)
{
	return forwardCallStaticInteger(env, SWINGUIFORM_CLASSNAME,
		"metric", SWINGUIFORM_METRIC_DESC,
		metricId);
}

static const JNINativeMethod mleFormMethods[] =
{
	{"callback", SWINGUIFORM_CALLBACK_DESC, (void*)Impl_mle_FormShelf_callback},
	{"displays", SWINGUIFORM_DISPLAYS_DESC, (void*)Impl_mle_FormShelf_displays},
	{"displayCurrent", SWINGUIFORM_DISPLAYCURRENT_DESC, (void*)Impl_mle_FormShelf_displayCurrent},
	{"displayShow", SWINGUIFORM_DISPLAYSHOW_DESC, (void*)Impl_mle_FormShelf_displayShow},
	{"equals", SWINGUIFORM_EQUALSDISPLAY_DESC, (void*)Impl_mle_FormShelf_equalsDisplay},
	{"equals", SWINGUIFORM_EQUALSFORM_DESC, (void*)Impl_mle_FormShelf_equalsForm},
	{"equals", SWINGUIFORM_EQUALSITEM_DESC, (void*)Impl_mle_FormShelf_equalsItem},
	{"flushEvents", SWINGUIFORM_FLUSHEVENTS_DESC, (void*)Impl_mle_FormShelf_flushEvents},
	{"formDelete", SWINGUIFORM_FORMDELETE_DESC, (void*)Impl_mle_FormShelf_formDelete},
	{"formItemAtPosition", SWINGUIFORM_FORMITEMATPOSITION_DESC, (void*)Impl_mle_FormShelf_formItemAtPosition},
	{"formItemCount", SWINGUIFORM_FORMITEMCOUNT_DESC, (void*)Impl_mle_FormShelf_formItemCount},
	{"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONGET_DESC, (void*)Impl_mle_FormShelf_formItemPositionGet},
	{"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONSET_DESC, (void*)Impl_mle_FormShelf_formItemPositionSet},
	{"formItemRemove", SWINGUIFORM_FORMITEMREMOVE_DESC, (void*)Impl_mle_FormShelf_formItemRemove},
	{"formNew", SWINGUIFORM_FORMNEW_DESC, (void*)Impl_mle_FormShelf_formNew},
	{"injector", SWINGUIFORM_INJECTOR_DESC, (void*)Impl_mle_FormShelf_injector},
	{"itemDelete", SWINGUIFORM_ITEMDELETE_DESC, (void*)Impl_mle_FormShelf_itemDelete},
	{"itemNew", SWINGUIFORM_ITEMNEW_DESC, (void*)Impl_mle_FormShelf_itemNew},
	{"itemProperty", SWINGUIFORM_ITEMPROPERTY_INT_DESC, (void*)Impl_mle_FormShelf_itemPropertyInt},
	{"itemProperty", SWINGUIFORM_ITEMPROPERTY_STR_DESC, (void*)Impl_mle_FormShelf_itemPropertyStr},
	{"metric", SWINGUIFORM_METRIC_DESC, (void*)Impl_mle_FormShelf_metric},
};

jint JNICALL mleFormInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/UIFormShelf"),
		mleFormMethods, sizeof(mleFormMethods) / sizeof(JNINativeMethod));
}
