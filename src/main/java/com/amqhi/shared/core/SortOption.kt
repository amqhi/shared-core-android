package com.amqhi.shared.core

object SortOption {
    /// A to Z
    const val NAME_ASC = 'n'
    /// Z to A
    const val NAME_DESC = 'N'

    /// Oldest first
    const val CREATED_AT_ASC = 'c'
    /// Newest first
    const val CREATED_AT_DESC = 'C'

    /// Oldest modified first
    const val UPDATED_AT_ASC = 'u'
    /// Recently modified first
    const val UPDATED_AT_DESC = 'U'

    /// Smallest first
    const val SIZE_ASC = 's'
    /// Largest first
    const val SIZE_DESC = 'S'

    /// Type A to Z
    const val TYPE_ASC = 't'
    /// Type Z to A
    const val TYPE_DESC = 'T'
}