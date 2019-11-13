package com.example.progresee.viewmodels

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import com.example.progresee.R
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.User
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class UserViewModel(private val appRepository: AppRepository, private val classroomId: String) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _isAdmin = appRepository.isAdmin
    val isAdmin
        get() = _isAdmin


    private val adapterList = hashMapOf<String, User>()
    val users = MutableLiveData<List<User>>()

    private val classroom = MutableLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _removeUser = MutableLiveData<Pair<String, String>>()
    val removeUser
        get() = _removeUser

    private val _transfer = MutableLiveData<Pair<String, String>>()
    val transfer
        get() = _transfer

    private val _navigateBackToTaskFragment = MutableLiveData<Boolean?>()
    val navigateBackToTaskFragment
        get() = _navigateBackToTaskFragment

    private val _transferSuccessful = MutableLiveData<Boolean?>()
    val transferSuccessful
        get() = _transferSuccessful

    private val _removedUserSnackBar = MutableLiveData<Boolean?>()
    val removedUserSnackBar
        get() = _removedUserSnackBar

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBarClassroom = MutableLiveData<Boolean?>()
    val showSnackBarClassroom
        get() = _showSnackBarClassroom

    private val _showSnackBarRefresh = MutableLiveData<Boolean?>()
    val showSnackBarRefresh
        get() = _showSnackBarRefresh

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment
        get() = _navigateBackToClassroomFragment


    init {
        setClassroomListener(classroomId)
        loadUsers()
    }

    fun loadUsers() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        Timber.wtf(classroomId)
                        val response = appRepository.getUsersInClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroomId
                        ).await()
                        Timber.wtf("hey")
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.forEach {
                                setUsersListeners(it.key)
                            }
                        } else {
                            Timber.wtf("Something went wrong ${response.code()} ${response.errorBody().toString()}")
                        }
                    } catch (e: Exception) {
                        Timber.wtf("${e.message}${e.printStackTrace()}")
                    } finally {
                        withContext(Dispatchers.Main) {
                            hideProgressBar()
                        }
                    }
                }
            }
        }
    }

    private fun setClassroomListener(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("classrooms")
            .document(uid)

        docRef.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                Timber.wtf("Current data: ${snapshot.data}")

                val classroomFirestore =
                    snapshot.toObject(Classroom::class.java)
                Timber.wtf("classroom -> $classroomFirestore")
                classroomFirestore?.let {
                    if (!it.archived) {
                        Timber.wtf("formatted classroom is -> $it")
                        classroom.value = it
                    } else {
                        if (appRepository.isAdmin.value == false) {
                            showSnackBarClassroomDeleted()
                            onClassroomDeleted()
                        }
                    }
                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }

    private fun setUsersListeners(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("users")
            .document(uid)

        docRef.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                Timber.wtf("Current data: ${snapshot.data}")

                val userFirestore =
                    snapshot.toObject(User::class.java)
                Timber.wtf("user -> $userFirestore")
                userFirestore?.let {
                    adapterList[it.uid] = it
                    users.value = adapterList.values.toList()
                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }


    fun onUserClicked(user: User, context: Context, view: View) {
        val popup = PopupMenu(context, view)
        popup.inflate(R.menu.users_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.remove_user -> {
                    showRemoveUserDialog(user.fullName, user.uid)
                }
                R.id.transfer -> {
                    showTransferDialog(user.fullName, user.uid)
                }
            }
            true
        }
        popup.show()
    }


    fun transferClassroom(userUid: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val request = appRepository.transferClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            userUid
                        ).await()
                        if (request.isSuccessful) {
                            val data = request.body()
                            data?.let{
                                loadUsers()
                            }
                            withContext(Dispatchers.Main) {
                                hideProgressBar()
                                showTransferSuccessful()
                            }
                        } else Timber.wtf("${request.code()}${request.errorBody()}")
                    } catch (e: Exception) {
                        Timber.wtf("${e.printStackTrace()}${e.message}")
                    }
                }
            }

        }
    }

    fun removeUser(userUid: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val request = appRepository.removeUserAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            userUid
                        ).await()
                        if (request.isSuccessful) {
                            val data = request.body()
                           data?.let{
                               withContext(Dispatchers.Main) {
                                   adapterList.remove(userUid)
                                   users.value=adapterList.values.toList()
                               }
                               loadUsers()
                           }
                            withContext(Dispatchers.Main) {
                                showRemovedUser()
                            }
                        } else Timber.wtf("${request.code()}${request.errorBody()}")
                    } catch (e: Exception) {
                        Timber.wtf("${e.printStackTrace()}${e.cause}")
                    } finally {
                        withContext(Dispatchers.Main) {
                            hideProgressBar()
                        }
                    }
                }
            }
        }
    }




    override fun showProgressBar() {
        super.showProgressBar()
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        _showProgressBar.value = null
    }

    private fun showRemoveUserDialog(userName: String, userUid: String) {
        _removeUser.value = userName to userUid
    }

    fun hideRemoveUserDialog() {
        _removeUser.value = null
    }

    private fun showTransferDialog(userName: String, userUid: String) {
        _transfer.value = userName to userUid
    }

    fun hideTransferDialog() {
        _transfer.value = null
    }

    private fun showTransferSuccessful() {
        _transferSuccessful.value = true
    }

    fun hideTransferSuccessful() {
        _transferSuccessful.value = null
    }

    private fun showRemovedUser() {
        _removedUserSnackBar.value = true
    }

    fun hideRemoveUserSnackBar() {
        _removedUserSnackBar.value = null
    }

    fun showSnackBarClassroomDeleted() {
        _showSnackBarClassroom.value = true
    }

    fun hideSnackBarClassroomDeleted() {
        _showSnackBarClassroom.value = null
    }


    fun showSnackBarRefresh() {
        _showSnackBarRefresh.value = true
    }

    fun hideRefreshSnackBar() {
        _showSnackBarRefresh.value = null
    }


    private fun onClassroomDeleted() {
        _navigateBackToClassroomFragment.value = true
    }

    fun doneNavigateToClassroomFragment() {
        _navigateBackToClassroomFragment.value = null
    }

}