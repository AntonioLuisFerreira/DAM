package dam.a47500.whereto.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.data.User
import dam.a47500.whereto.database.FirebaseAuthRepository
import dam.a47500.whereto.database.FirebaseFirestore
import dam.a47500.whereto.database.FirebasePostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private lateinit var _repository: FirebaseAuthRepository

    fun initViewModel(repository: FirebaseAuthRepository){
        _repository = repository
    }

    fun setUser(user: User) {
        viewModelScope.launch(Dispatchers.Default) {
            _repository.setUser(user)
        }
    }



    fun getUserByUID(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedUser = _repository.getUserByUID(uid)
                _user.postValue(fetchedUser)
            } catch (e: Exception) {
                _user.postValue(null)  // handle error appropriately
            }
        }
    }

    fun getUserByName(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedUser = _repository.getUserByName(username)
                _user.postValue(fetchedUser)
            } catch (e: Exception) {
                _user.postValue(null)  // handle error appropriately
            }
        }
    }
}