package com.muedsa.hatv

import com.muedsa.hatv.repository.DemoHARepositoryImpl
import com.muedsa.hatv.repository.HARepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideHARepository() =
        if (BuildConfig.FLAVOR == Flavors.DEMO) DemoHARepositoryImpl() else HARepositoryImpl()
}