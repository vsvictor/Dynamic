package com.education.dynamic.exceptions

class HTTPException(val error : HTTPCode) : Exception(error.message) {}

