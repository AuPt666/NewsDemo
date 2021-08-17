package com.jinbo.newsdemo.ui.island

import android.view.animation.Transformation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.logic.model.IslandResponse


class IslandViewModel: ViewModel(){

    private val islandResponseViewModel = MutableLiveData<String>()

    val islandList = ArrayList<IslandResponse.Detail>()

    val islandLiveData = Transformations.switchMap(islandResponseViewModel) { sort ->
        Repository.getIsLand(sort, 10)
    }

    fun searchIsland(sort: String){
        islandResponseViewModel.value = sort
    }

}