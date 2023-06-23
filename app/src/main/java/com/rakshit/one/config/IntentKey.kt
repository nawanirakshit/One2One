package com.test.papers.config

object IntentKey {

    const val RESULT = "RESULT"
    const val PERM_KEY = "perm_key"
    const val TITLE = "title"
    const val BY = "by"
    const val BODY = "body"
    const val IMAGE_URL = "image_url"
    const val STATUS = "status"
    const val CREATED_ON = "created_on"
    const val LOCATION = "location"
    const val AMOUNT = "amount"
    const val IS_REGISTERED = "is_registered"
    const val DATE = "date"
    const val FORUM = "forum"
    const val FRIEND_DATA = "friend_data"

    const val ID = "id"
    const val USER_ID = "user_id"
    const val NAME = "name"
    const val CHAT_KEY = "chat_key"
    const val TAGS = "tags"
    const val TAG_ID = "tag_id"
    const val OPPONENT_ID = "opponent_id"
    const val PROJECT_ID = "project_id"
    const val VENDOR_ID = "vendor_id"
    const val IS_TODAY = "is_today"
    const val SELECT_SINGLE = "SELECT_SINGLE"

    const val REALTIME_CHAT = "chats"
    const val FIRESTORE_USERS = "users"

    const val DATA = "Data"

    const val MATERIAL_TYPE = "MATERIAL TYPE"

    const val BILL_LUMPSUM_TOTAL = "BILL_LUMPSUM_TOTAL"
    const val BILL_DISCOUNT = "BILL_DISCOUNT"
    const val FREIGHT_CHARGE = "FREIGHT_CHARGE"
    const val BILL_CREDIT = "BILL_CREDIT"
    const val DISABLE_FIELDS = "DISABLE_FIELDS"

    const val TASK_NOTE = "TASK_NOTE"
    const val INJURY_DETAILS = "INJURY_DETAILS"
    const val INJURY_HAPPEN = "INJURY_HAPPEN"
}

object AttachmentConst {
    const val TYPE = "TYPE"
    const val CAMERA = "CAMERA"
    const val GALLERY = "GALLERY"
    const val DOCUMENT = "DOCUMENT"

}

object ChatConst {
    const val CREATED_AT = "createdAt"
    const val KEY = "key"
    const val MESSAGE = "message"
    const val RECEIVER_ID = "receiverId"
    const val SENDER_ID = "senderId"
    const val TYPE = "type" //text, attachment
}

object MetadataConst {
    const val LAST_CREATED_AT = "lastCreatedAt"
    const val LAST_MESSAGE = "lastMessage"
    const val STATUS = "status"
    const val RECEIVER_DATA = "receiverData"
    const val SENDER_DATA = "senderData"
}

object RECEIVERDATA {
    const val FULL_NAME = "fullName"
    const val PHOTO_URL = "photoUrl"
    const val USER_ID = "userId"
    const val IS_ADMIN = "is_admin"
    const val USER_TYPE = "user_type"
}

object CHAT_TYPE {
    const val TEXT = "text"
    const val IMAGE = "image"
    const val PDF = "pdf"
}

object NavigationFragment {
    const val SEARCH_RESULT = 876678
    const val FRIEND_PROFILE = 34544
    const val FIND_FRIEND = 15543
    const val CHANGE_PASSWORD = 432232
    const val FORUM_REPLIES = 432231
    const val SEARCH_RESULT_WITH_LIST = 7644321
    const val REQUEST_SENT = 983312
    const val VIEW_FRIENDS = 654444
}

object AlarmType {
    const val Steps = 1
    const val Days = 2
    const val ReminderOne = 3
    const val ReminderTwo = 4
}

object ContactType {
    const val TYPE = "type"
    const val VENDOR = "Vendor"
    const val EMPLOYEE = "Employee"
    const val CONTRACTOR = "Contractor"
    const val MANAGER = "Manager"
    const val SUPERVISOR = "Supervisor"
    const val CUSTOMER = "Customer"
}

object ContactKey {
    const val PEOPLE_INVOLVED = "PEOPLE_INVOLVED"
    const val WITNESS = "WITNESS"
    const val PEOPLE_NOTIFIED = "PEOPLE_NOTIFIED"
    const val EMPLOYEE = "EMPLOYEE"
    const val SUPERVISOR = "SUPERVISOR"
}

object FragmentResult {
    const val LOCATION_PERMISSION = "locationPermission"
    const val NOTIFICATION_PERMISSION = "notificationPermission"
    const val STORAGE_PERMISSION = "storagePermission"
    const val STD_RESULT = "std_result"
    const val PROJECT_RESULT = "project_result"
    const val CONTACT_RESULT = "contact_result"
    const val REFERENCE_PO_RESULT = "reference_po_result"
    const val TAGS_RESULT = "tags_result"
    const val ATTACHMENT_RESULT = "attachment_result"
    const val USER_LIST_RESULT = "user_list_result"
    const val MATERIAL_RESULT = "material_result"
    const val SUBS_ON_JOB_SITE = "SUBS_ON_JOB_SITE"
    const val ACCIDENT = "ACCIDENT"
    const val PEOPLE = "PEOPLE"
    const val BILL_ITEM = "BILL_ITEM"
    const val CHECK_OUT_NOW = "CHECK_OUT_NOW"
}

object MaterialDataObject {
    const val MATERIAL_DELIVERED = "MATERIAL_DELIVERED"
    const val EQUIPMENT_USED = "Equipment"
    const val EQUIPMENT_DELIVERED = "EQUIPMENT_DELIVERED"
    const val MATERIAL_USED = "Material"
}

object AddBillItem {
    const val NAME = "NAME"
    const val TOTAL = "TOTAL"
    const val ITEM_TYPE = "ITEM_TYPE"
    const val AMOUNT = "AMOUNT"
    const val FREIGHT_CHARGE = "FREIGHT_CHARGE"
    const val COST_CODE = "COST_CODE"
}