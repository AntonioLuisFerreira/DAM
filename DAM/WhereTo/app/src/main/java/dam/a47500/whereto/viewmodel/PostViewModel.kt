package dam.a47500.whereto.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.database.FirebaseFirestore
import dam.a47500.whereto.database.FirebasePostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>?>()


    val posts : LiveData<List<Post>?>
        get() = _posts

    private val _uid = MutableLiveData<String?>()
    val uid: LiveData<String?> get() = _uid

    private lateinit var _repository: FirebasePostsRepository

    fun initViewModel(repository: FirebasePostsRepository){
        _repository = repository
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchPosts(){
        viewModelScope.launch(Dispatchers.Default) {
            val postsList = _repository.getPosts()
            _posts.postValue(postsList.value)
        }
    }

    fun fetchUserPosts(userName: String){
        viewModelScope.launch(Dispatchers.Default) {
            val postsList = _repository.getUserPosts(userName)
            _posts.postValue(postsList.value)
        }
    }

    fun fetchMyPosts(){
        viewModelScope.launch(Dispatchers.Default) {
            val postsList = _repository.getMyPosts()
            _posts.postValue(postsList.value)
        }
    }
}