package com.revolut.revolutandroidtest.utils

import android.content.Context

/**
 * ResourceProvider helper class use make access to Android Resource without need to deal with context direct
 * That will bw help inside ViewModel classes should not, though, hold a reference to Activities, Fragments, or Contexts
 */
class ResourceProvider(val context: Context) {

	/**
	 * Fetch the String Resource that select by resId then return the equivalent String
	 * @param resId
	 */
	fun getString(resId: Int): String {
		return context.getString(resId)
	}

	/**
	 * Fetch the String Resource that select by resId and the pass value then return the equivalent String
	 * @param resId
	 * @param value
	 */
	fun getString(resId: Int, value: String): String {
		return context.getString(resId, value)
	}
    /**
     * Fetch the String Resource that select by resId and the pass value then return the equivalent String
     * @param resId
     * @param value
     */
    fun getString(resId: Int, vararg value: Any): String {
        return context.getString(resId, value)
    }

	/**
	 * Fetch the String Resource that select by resId and the pass value then return the equivalent String
	 * @param resId
	 * @param value1
	 * @param value2
	 */
	fun getString(resId: Int, value1: String, value2: String): String {
		return context.getString(resId, value1, value2)
	}

	/**
	 * Fetch the String Resource that select by resId and the pass value then return the equivalent String
	 * @param resId
	 * @param value
	 */
	fun getString(resId: Int, value: Int): String {
		return context.getString(resId, value)
	}

	/**
	 * Fetch the String Resource that select by resId and the pass value1 then return the equivalent String
	 * @param resId
	 * @param value1
	 * @param value2
	 */
	fun getString(resId: Int, value1: Int, value2: Int): String {
		return context.getString(resId, value1, value2)
	}


}