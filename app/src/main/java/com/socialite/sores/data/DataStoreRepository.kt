package com.socialite.sores.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.socialite.sores.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.socialite.sores.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.socialite.sores.util.Constants.Companion.PREFERENCE_BACK_ONLINE
import com.socialite.sores.util.Constants.Companion.PREFERENCE_DIET_TYPE
import com.socialite.sores.util.Constants.Companion.PREFERENCE_DIET_TYPE_ID
import com.socialite.sores.util.Constants.Companion.PREFERENCE_MEAL_TYPE
import com.socialite.sores.util.Constants.Companion.PREFERENCE_MEAL_TYPE_ID
import com.socialite.sores.util.Constants.Companion.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(PREFERENCE_NAME)
    }

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCE_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCE_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val isBackOnline = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveIsBackOnline(isBackOnline: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.isBackOnline] = isBackOnline
        }
    }

    val readMelaAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readIsBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isBackOnline = preference[PreferenceKeys.isBackOnline] ?: false
            isBackOnline
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)