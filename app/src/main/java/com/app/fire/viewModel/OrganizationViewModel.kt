package com.app.fire.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.fire.model.OrganizationModelFirestore
import com.app.fire.util.FirestoreUtil
import kotlinx.coroutines.launch

class OrganizationViewModel: ViewModel() {
    companion object{
        private const val PATH="organizations"
    }
    private lateinit var organizationViewModel: OrganizationViewModel
    private val _userLiveData = MutableLiveData<List<OrganizationModelFirestore>>()
    val userLiveData: LiveData<List<OrganizationModelFirestore>> get() = _userLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    /**
     * Register user to Firestore.
     */
    fun addOrganizationToFirestore(
        userData: OrganizationModelFirestore,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            FirestoreUtil.addDocument(
                collectionPath = PATH,
                data = userData,
                onSuccess = { onSuccess() },
                onFailure = { e -> onFailure(e) }
            )
        }
    }
    fun fetchOrganizations() {
        FirestoreUtil.getAllDocuments(
            collectionPath = PATH,
            clazz = OrganizationModelFirestore::class.java,
            onSuccess = { user ->
                _userLiveData.value = user
            },
            onFailure = { exception ->
                _errorLiveData.value = exception.message
            }
        )
    }
}