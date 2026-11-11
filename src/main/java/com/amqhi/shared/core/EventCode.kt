package com.amqhi.shared.core

object EventCode {
    // -----------------------------------------------------------------------------
    // System & Auth (1000 ~ 1099)
    // -----------------------------------------------------------------------------
    const val REGISTER_SUCCESS               : Short = 1000
    const val REGISTER_FAILURE               : Short = 1001
    const val LOGIN_SUCCESS                  : Short = 1002
    const val LOGIN_FAILURE                  : Short = 1003
    const val LOGIN_REQUIRED                 : Short = 1004
    const val RE_LOGIN_REQUIRED              : Short = 1005
    const val LOGOUT_SUCCESS                 : Short = 1006
    const val LOGOUT_FAILURE                 : Short = 1007
    const val USER_SWITCHED                  : Short = 1008
    const val REFRESH_SUCCESS                : Short = 1009
    const val REFRESH_FAILURE                : Short = 1010
    const val FETCH_USER_INFO_SUCCESS         : Short = 1011
    const val FETCH_USER_INFO_FAILURE         : Short = 1012

    // -----------------------------------------------------------------------------
    // Item CRUD & Actions (1100 ~ 1199)
    // -----------------------------------------------------------------------------
    const val ITEM_CREATE_SUCCESS            : Short = 1100
    const val ITEM_CREATE_FAILURE            : Short = 1101
    const val ITEM_UPDATE_SUCCESS            : Short = 1102
    const val ITEM_UPDATE_FAILURE            : Short = 1103
    const val ITEM_DELETE_SUCCESS            : Short = 1104
    const val ITEM_DELETE_FAILURE            : Short = 1105
    const val ITEM_SOFT_DELETE_SUCCESS       : Short = 1106
    const val ITEM_SOFT_DELETE_FAILURE       : Short = 1107
    const val ITEM_RESTORE_SUCCESS           : Short = 1108
    const val ITEM_RESTORE_FAILURE           : Short = 1109
    const val ITEM_MOVE_SUCCESS              : Short = 1110
    const val ITEM_MOVE_FAILURE              : Short = 1111
    const val ITEM_MAKE_AVAILABLE_OFFLINE_SUCCESS             : Short = 1112
    const val ITEM_MAKE_AVAILABLE_OFFLINE_FAILURE             : Short = 1113
    const val ITEMS_MOVE_STARTED             : Short = 1114
    const val ITEMS_MOVE_SUCCESS             : Short = 1115
    const val ITEMS_MOVE_FAILURE             : Short = 1116
    const val ITEMS_DELETE_STARTED           : Short = 1117
    const val ITEMS_DELETE_SUCCESS           : Short = 1118
    const val ITEMS_DELETE_FAILURE          : Short = 1119
    const val ITEMS_SOFT_DELETE_STARTED      : Short = 1120
    const val ITEMS_SOFT_DELETE_SUCCESS      : Short = 1121
    const val ITEMS_SOFT_DELETE_FAILURE     : Short = 1122
    const val ITEMS_RESTORE_STARTED          : Short = 1123
    const val ITEMS_RESTORE_SUCCESS          : Short = 1124
    const val ITEMS_RESTORE_FAILURE         : Short = 1125
    const val ITEM_MAKE_ONLINE_ONLY_SUCCESS : Short = 1126
    const val ITEM_MAKE_ONLINE_ONLY_FAILURE : Short = 1127

    // -----------------------------------------------------------------------------
    // File & Transfer (1200 ~ 1299)
    // -----------------------------------------------------------------------------
    const val FILE_DOWNLOAD_SUCCESS          : Short = 1200
    const val FILE_DOWNLOAD_FAILURE          : Short = 1201
    const val FILE_UPLOAD_STARTED            : Short = 1202
    const val FILE_DOWNLOAD_STARTED            : Short = 1203
    const val ITEM_THUMBNAIL_DOWNLOAD_SUCCESS  : Short = 1204
    const val ITEM_THUMBNAIL_DOWNLOAD_FAILURE  : Short = 1205
    const val FETCH_FILE_DOWNLOAD_URL_SUCCESS : Short = 1206
    const val FETCH_FILE_DOWNLOAD_URL_FAILURE : Short = 1207
    const val FETCH_FILES_FAILURE            : Short = 1208
    const val FETCH_FOLDERS_FAILURE          : Short = 1209
    const val FILES_UPLOAD_STARTED            : Short = 1210
    const val FILES_DOWNLOAD_STARTED            : Short = 1211

    // -----------------------------------------------------------------------------
    // Sync (1300 ~ 1399)
    // -----------------------------------------------------------------------------
    const val SYNC_STARTED                   : Short = 1300
    const val SYNC_SUCCESS                   : Short = 1301
    const val SYNC_EVENTS_FETCH_FAILURE      : Short = 1302
    const val SYNC_EVENTS_ACKNOWLEDGE_FAILURE : Short = 1303

    // -----------------------------------------------------------------------------
    // General Errors (1900 ~ 1999)
    // -----------------------------------------------------------------------------
    const val DATABASE_ERROR                 : Short = 1900
    const val NETWORK_CONNECTION_FAILURE    : Short = 1901
    const val RESOURCE_BUSY                  : Short = 1902
    const val FILE_CREATE_FAILURE               : Short = 1903
    const val FILE_READ_FAILURE                 : Short = 1904
    const val FILE_WRITE_FAILURE                 : Short = 1905
}