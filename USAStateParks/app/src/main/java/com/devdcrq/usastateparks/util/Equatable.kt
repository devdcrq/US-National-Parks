package com.devdcrq.usastateparks.util

import java.io.Serializable

interface Equatable : Serializable {
    override fun equals(other: Any?): Boolean
}