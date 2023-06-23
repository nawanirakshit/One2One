package com.test.papers.kotlin

import androidx.annotation.LayoutRes
import com.test.papers.kotlin.KotlinBaseFragment

open class KotlinBaseOverlayFragment(@LayoutRes val layout: Int = 0) : KotlinBaseFragment(layout)