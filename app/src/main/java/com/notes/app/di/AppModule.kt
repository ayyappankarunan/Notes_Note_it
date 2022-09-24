package com.notes.app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.notes.app.feature_note.data.data_source.NoteDatabase
import com.notes.app.feature_note.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import com.notes.app.feature_note.data.repository.NoteRepositoryImpl
import com.notes.app.feature_note.domain.repository.NoteRepository
import com.notes.app.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): NoteDatabase =
        Room.databaseBuilder(application, NoteDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository = NoteRepositoryImpl(db.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases =
        NoteUseCases(
            getNote = GetNote(repository),
            getNotes = GetNotes(repository),
            insertNote = InsertNote(repository),
            deleteNote = DeleteNote(repository)
        )

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext appContext: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(appContext)
}