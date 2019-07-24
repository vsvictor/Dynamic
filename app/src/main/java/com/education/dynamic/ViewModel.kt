package com.education.dynamic

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel(){
    val items = DataLoader.items
}