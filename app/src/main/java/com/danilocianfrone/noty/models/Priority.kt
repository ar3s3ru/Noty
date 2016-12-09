package com.danilocianfrone.noty.models

import android.support.annotation.ColorRes

import com.danilocianfrone.noty.R

/**
 *
 */
enum class Priority {
    VERY_HIGH {
        override fun Value()  = 0
        override fun String() = "Very High"
        @ColorRes override fun ColorBody() = R.color.colorPriorityVeryHigh
        @ColorRes override fun ColorTop()  = R.color.colorPriorityVeryHighLight
    },
    HIGH {
        override fun Value()  = 1
        override fun String() = "High"
        @ColorRes override fun ColorBody() = R.color.colorPriorityHigh
        @ColorRes override fun ColorTop()  = R.color.colorPriorityHighLight
    },
    MEDIUM {
        override fun Value()  = 2
        override fun String() = "Medium"
        @ColorRes override fun ColorBody() = R.color.colorPriorityMedium
        @ColorRes override fun ColorTop()  = R.color.colorPriorityMediumLight
    },
    LOW {
        override fun Value()  = 3
        override fun String() = "Low"
        @ColorRes override fun ColorBody() = R.color.colorPriorityLow
        @ColorRes override fun ColorTop()  = R.color.colorPriorityLowLight
    },
    VERY_LOW {
        override fun Value()  = 4
        override fun String() = "Very Low"
        @ColorRes override fun ColorBody() = R.color.colorPriorityVeryLow
        @ColorRes override fun ColorTop()  = R.color.colorPriorityVeryLowLight
    };

    /**
     * Returns the integer Value of Priority instance
     *
     * @return Priority integer representation
     */
    abstract fun Value(): Int

    /**
     * Returns the name of the particular Priority Value
     *
     * @return Priority name
     */
    abstract fun String(): String

    /**
     * Returns the color resource ID of the corresponding priority Value
     *
     * @return Priority color resource ID
     */
    @ColorRes abstract fun ColorBody(): Int

    /**
     * Returns the light color resource ID of the corresponding priority Value
     *
     * @return Priority light color resource ID
     */
    @ColorRes abstract fun ColorTop(): Int

    /**
     * Specifies the color resource ID to use in TextViews for text displaying
     *
     * @return Text displaying color resource ID
     */
    @ColorRes fun TextColor(): Int {
        return when (this) {
            // TODO: finish this
            MEDIUM -> 0
            else   -> 0
        }
    }

    companion object {
        /**
         * Retrieve Priority object from its integer representation
         *
         * @param i Priority integer representation
         * @return Associated Priority object
         */
        fun FromValue(i: Int): Priority {
            return when (i % 5) {
                0 -> VERY_HIGH; 1 -> HIGH; 2 -> MEDIUM; 3 -> LOW; 4 -> VERY_LOW
                // Won't actually happen
                else -> VERY_LOW
            }
        }
    }
}
