package dam.a47500.whereto.database



import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

class DBModule(private val context: Context) {


    val firebaseFirestore: FirebaseFirestore
    val firebasePostsRepository: FirebasePostsRepository
    val firebaseAuthRepository: FirebaseAuthRepository

    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance : DBModule ? = null
        fun getInstance (context : Context): DBModule {
            if ( instance != null ) return instance !!
            synchronized ( this ) {
                return DBModule(context)
            }
            return instance!!
        }
    }

    init {

        firebaseFirestore = FirebaseFirestore()
        firebasePostsRepository = FirebasePostsRepository(firebaseFirestore)
        firebaseAuthRepository = FirebaseAuthRepository(firebaseFirestore)
    }
}