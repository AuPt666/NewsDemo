package com.jinbo.newsdemo.ui.island

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.IslandResponse


/***********小岛ViewModel**************/
class IslandViewModel: ViewModel(){

    private val islandResponseViewModel = MutableLiveData<String>()

    val islandList = ArrayList<IslandResponse.Detail>()

    val islandLiveData = Transformations.switchMap(islandResponseViewModel) { sort ->
        Repository.getIsLand(sort, 10)
    }

    fun getIsland(sort: String){
        islandResponseViewModel.value = sort
    }

}