package bmi.demo.application.presentation.di

import bmi.demo.application.domain.repository.BmiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies to the BMI feature.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of BmiRepository
     *
     *  @return BmiRepository
     */
    @Provides
    @Singleton
    fun provideBmiRepository(): BmiRepository {
        return BmiRepository()
    }
}
