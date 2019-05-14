package com.genialabs.dermia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PredictViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<CharSequence> nombre = new MutableLiveData<>();
    public void setName(CharSequence input){
        //setValue efectos en la UI y putValue efectos en background
        nombre.setValue(input);
    }
    public LiveData<CharSequence> getName(){
        return nombre;
    }

}
