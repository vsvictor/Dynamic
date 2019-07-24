package com.education.dynamic.interfaces

import com.education.dynamic.exceptions.HTTPException
import java.lang.Exception

interface OnFail{
    fun onFail(ex : Exception)
}