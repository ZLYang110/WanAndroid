/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.zlyandroid.wanandroid.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.util.log.KLog;

/**测试用Log,不要使用android.util.Log
 * @modifier Lemon
 */
public class LogUtil {

	private static boolean isInit = false;//是否初始化
	public static boolean isDebug = false;//是否输出日志
	/**
	 * 初始化日志打印
	 *
	 * @param TAG     日志输出标签
	 * @param isDebug 是否输出日志
	 */
	public static void init(@NonNull String TAG, @NonNull boolean isDebug) {
		KLog.init(isDebug, TAG);
		LogUtil.isDebug = isDebug;
		isInit = true;
	}

	public static void d(Object objectMsg) {
		if (!isInit) throwException();
		if (isDebug) KLog.d(objectMsg);
	}

	public static void e(Object objectMsg) {
		if (!isInit) throwException();
		if (isDebug) KLog.e(objectMsg);
	}

	public static void w(Object objectMsg) {
		if (!isInit) throwException();
		if (isDebug) KLog.w(objectMsg);
	}

	public static void i(Object objectMsg) {
		if (!isInit) throwException();
		if (isDebug) KLog.i(objectMsg);
	}

	private static void throwException() {
		throw new NullPointerException("日志未初始化,请调用init()方法初始化后再试!");
	}
}
