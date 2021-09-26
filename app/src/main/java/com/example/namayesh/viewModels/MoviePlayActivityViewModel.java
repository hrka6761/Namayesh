package com.example.namayesh.viewModels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MoviePlayActivityViewModel extends ViewModel {

    MutableLiveData<Integer> currentPosition;

    public MutableLiveData<Integer> getCurrentPosition() {

        if (currentPosition == null) {
            currentPosition = new MutableLiveData<>();
            currentPosition.setValue(0);
        }

        return currentPosition;
    }
}
