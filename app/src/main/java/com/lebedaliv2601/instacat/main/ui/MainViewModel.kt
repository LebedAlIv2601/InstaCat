package com.lebedaliv2601.instacat.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lebedaliv2601.instacat.main.data.repository.MainRepository
import com.lebedaliv2601.instacat.main.ui.model.CatMainModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    app: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(app) {

    private val loadedCatsList = mutableListOf<CatMainModel>()

    private val _catsListState = MutableLiveData<MainCatsListState>()
    val catsListState: LiveData<MainCatsListState>
        get() = _catsListState

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val disposables: MutableList<Disposable> = mutableListOf()

    init {
        _isRefreshing.value = false
        getCatsList(false)
    }

    fun getMoreCats() {
        _catsListState.value = MainCatsListState.PagingLoading
        disposables.add(mainRepository.getCats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (_catsListState.value !is MainCatsListState.Success) {
                        loadedCatsList.addAll(it)
                        _catsListState.value = MainCatsListState.PagingSuccess(
                            loadedCatsList.toList()
                        )
                    }
                },
                {
                    if (_catsListState.value !is MainCatsListState.Success) {
                        _catsListState.value = MainCatsListState.PagingError
                    }
                }
            )
        )
    }

    fun getCatsList(isRefresh: Boolean) {
        if (_catsListState.value !is MainCatsListState.Loading) {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _catsListState.value = MainCatsListState.Loading
            }
            disposables.add(mainRepository.getCats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _catsListState.value = MainCatsListState.Success(it)
                        loadedCatsList.clear()
                        loadedCatsList.addAll(it)
                        _isRefreshing.value = false
                    },
                    {
                        if (isRefresh) {
                            _catsListState.value = MainCatsListState.UpdateError
                            _isRefreshing.value = false
                        } else {
                            _catsListState.value = MainCatsListState.Error
                        }
                    }
                )
            )
        } else if (isRefresh) {
            _isRefreshing.value = false
        }
    }

    override fun onCleared() {
        disposables.forEach {
            it.dispose()
        }
        super.onCleared()
    }

}

sealed class MainCatsListState {
    object Loading : MainCatsListState()
    object Error : MainCatsListState()
    class Success(val data: List<CatMainModel>) : MainCatsListState()
    object PagingLoading : MainCatsListState()
    object PagingError : MainCatsListState()
    class PagingSuccess(val data: List<CatMainModel>) : MainCatsListState()
    object UpdateError : MainCatsListState()
}