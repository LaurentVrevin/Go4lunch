package viewmodels;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import repositories.MapViewRepositoryImpl;


@HiltViewModel
public class MapViewViewModel extends ViewModel {
    public MapViewRepositoryImpl mMapViewRepository;

    //for data


    @Inject
    public MapViewViewModel(MapViewRepositoryImpl mapViewRepository){
        mMapViewRepository = mapViewRepository;
    }
    public MapViewViewModel(){

    }

}