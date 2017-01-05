package com.danilocianfrone.noty.singleton

/**
 * Static class that contains all the constant strings used throughout the application.
 */
internal object Names {
    // Priority key for Controller creations
    const val PRIORITY = "priority"
    // Realm database name
    const val REALM_NAME = "noty"
    // Tag key for named parameters
    const val TAG = "TAG"

    /** Preferences *******************************************************************************/
    const val FIRST_BOOT = "first_boot"     // SharedPrefs first boot key
    const val COL_NUMBER = "column_number"  // Column number for GridLayoutManager
    /**********************************************************************************************/
}