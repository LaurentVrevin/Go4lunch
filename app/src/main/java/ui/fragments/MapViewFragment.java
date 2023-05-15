package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapview, container, false);

        // Obtenez la référence du MapView dans le layout xml
        mapView = view.findViewById(R.id.mapView);

        // Définissez la clé API Google Maps
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Une fois que le map est prêt, on récupère la GoogleMap
        this.googleMap = googleMap;

        // Personnalisez votre map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        // Activez la possibilité de zoomer sur la carte
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        // Activez la possibilité de faire glisser la carte
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        // Définissez les coordonnées géographiques de l'endroit que vous souhaitez afficher
        LatLng position = new LatLng(49.1818, -0.3698);

        // Déplacez la caméra à la position spécifiée
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15);
        googleMap.moveCamera(cameraUpdate);

        // Récupérez la liste des restaurants à proximité et affichez-les sur la carte
        // Utilisez les données des restaurants pour afficher des markers sur la carte

        // Notez que vous devez également gérer les cas où la localisation n'est pas disponible ou que l'utilisateur a désactivé la localisation.
    }
}