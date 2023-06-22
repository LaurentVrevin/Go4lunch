package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.LocationInterface;
import repositories.LocationRepository;
import viewmodels.LocationViewModel;

@InstallIn(SingletonComponent.class)
@Module
public abstract class LocationModule {
    @Binds
    public abstract LocationInterface bindLocationInterface(LocationRepository locationRepository);
}
