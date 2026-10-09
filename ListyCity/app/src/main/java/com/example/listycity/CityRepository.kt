package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            _cities.clear()
            snapshot?.documents?.forEach { doc ->
                val city = doc.toObject(City::class.java)
                if (city != null && city.name.isNotBlank()) {
                    _cities.add(city)
                }
            }
        }
    }


    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        if (oldCity.name != updatedCity.name) {
            deleteCity(oldCity)
        }
        citiesRef.document(updatedCity.name).set(updatedCity)
    }

    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
    }
}