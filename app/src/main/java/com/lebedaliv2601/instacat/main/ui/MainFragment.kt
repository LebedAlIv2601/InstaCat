package com.lebedaliv2601.instacat.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.lebedaliv2601.instacat.R
import com.lebedaliv2601.instacat.databinding.FragmentMainBinding
import com.lebedaliv2601.instacat.databinding.ItemCatBinding
import com.lebedaliv2601.instacat.main.ui.model.CatMainModel
import com.lebedaliv2601.instacat.utils.BasePagingAdapter
import com.lebedaliv2601.instacat.utils.HelpFragment
import com.lebedaliv2601.instacat.utils.customViews.BottomInfoDialog
import com.lebedaliv2601.instacat.utils.setPagingAction
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainFragment : HelpFragment(), KoinComponent {

    private var binding: FragmentMainBinding? = null
    private val vm: MainViewModel by viewModel()
    private val catsListAdapter: BasePagingAdapter<CatMainModel> by lazy {
        BasePagingAdapter(
            dataLayoutRes = R.layout.item_cat,
            dataLayoutBind = { item, view ->
                val vb = ItemCatBinding.bind(view)
                val controller = Fresco.newDraweeControllerBuilder()
                    .setUri(item.url)
                    .setAutoPlayAnimations(true)
                    .build()
                vb.sdvCatImage.controller = controller
            },
            defaultErrorAction = {
                vm.getMoreCats()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvMainCatsList?.apply {
            adapter = catsListAdapter
            itemAnimator = null
            setPagingAction(CATS_LIST_ITEMS_BEFORE_END_PAGING) {
                vm.getMoreCats()
            }
        }

        binding?.srlRefresh?.setOnRefreshListener {
            vm.getCatsList(true)
        }

        binding?.cErrorOverlap?.setAction {
            vm.getCatsList(false)
        }

        binding?.tbMainToolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.miHelp -> BottomInfoDialog.createDialog(
                    parentFragmentManager,
                    getString(R.string.main_info_dialog_title),
                    getString(R.string.main_info_text)
                )
            }
            true
        }
        initObservers()
    }

    private fun initObservers() {
        vm.catsListState.observeState {
            when (it) {
                is MainCatsListState.Loading -> {
                    hideErrorOverlap()
                    binding?.apply {
                        pbProgress.visibility = View.VISIBLE
                        rvMainCatsList.visibility = View.GONE
                    }
                }

                is MainCatsListState.Success -> {
                    hideErrorOverlap()
                    catsListAdapter.removeNotDataItem()
                    binding?.apply {
                        pbProgress.visibility = View.GONE
                        rvMainCatsList.visibility = View.VISIBLE
                        catsListAdapter.updateList(it.data)
                    }
                }
                is MainCatsListState.PagingSuccess -> {
                    catsListAdapter.removeNotDataItem()
                    catsListAdapter.updateList(it.data)
                }
                is MainCatsListState.Error -> {
                    binding?.apply {
                        pbProgress.visibility = View.GONE
                        rvMainCatsList.visibility = View.GONE
                        showErrorOverlap()
                    }
                }
                is MainCatsListState.UpdateError -> {
                    Toast.makeText(
                        context,
                        getString(R.string.main_cant_update_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is MainCatsListState.PagingLoading -> {
                    catsListAdapter.addLoadingItem()
                }
                is MainCatsListState.PagingError -> {
                    catsListAdapter.addErrorItem()
                }
            }
        }

        vm.isRefreshing.observeState {
            binding?.srlRefresh?.isRefreshing = it
        }
    }

    private fun showErrorOverlap(message: String = "") {
        binding?.cErrorOverlap?.apply {
            visibility = View.VISIBLE
            if (message.isNotBlank()) {
                setMessageText(message)
            }
        }
    }

    private fun hideErrorOverlap() {
        binding?.cErrorOverlap?.visibility = View.GONE
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val CATS_LIST_ITEMS_BEFORE_END_PAGING = 15
    }
}