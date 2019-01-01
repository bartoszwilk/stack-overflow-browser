package com.example.stackoverflowbrowser.mvi.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.mvi.MviView
import com.example.stackoverflowbrowser.mvi.search.list.IssueListItem
import com.example.stackoverflowbrowser.mvi.search.list.IssuesAdapter
import com.example.stackoverflowbrowser.util.extension.isScrollDown
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.tomasznajda.ktx.android.gone
import com.tomasznajda.ktx.android.visible
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import pl.wp.videostar.viper.web.WebStarter

const val LOAD_NEXT_PAGE_THRESHOLD = 4

class IssuesSearchActivity : MviView<IssuesSearchIntent, IssuesSearchViewState>, AppCompatActivity() {

    private val viewModel: IssuesSearchViewModel by viewModel()
    private val webStarter: WebStarter by inject()
    private val issuesAdapter = IssuesAdapter()
    private val disposables = CompositeDisposable()
    private val queryChangeIntent by lazy<Observable<IssuesSearchIntent>> {
        RxSearchView.queryTextChanges(searchView)
            .skipInitialValue()
            .map { it.toString() }
            .map(IssuesSearchIntent::Query)
    }
    private val loadMoreIntent by lazy<Observable<IssuesSearchIntent>> {
        RxRecyclerView
            .scrollEvents(issueList)
            .filter { it.isScrollDown }
            .filter { issuesAdapter.isLoading.not() }
            .map { issuesAdapter.items.size }
            .filter { adapterItemsCount ->
                issueList.lastCompletelyVisibleItemPosition == adapterItemsCount - LOAD_NEXT_PAGE_THRESHOLD
            }
            .distinctUntilChanged()
            .map { searchView.query.toString() }
            .map(IssuesSearchIntent::LoadNextPage)
    }
    private val refreshIntent: Observable<IssuesSearchIntent>
        get() = RxSwipeRefreshLayout
            .refreshes(refreshLayout)
            .map { searchView.query.toString() }
            .map(IssuesSearchIntent::Refresh)

    override val intents: Observable<IssuesSearchIntent>
        get() = Observable.merge(queryChangeIntent, loadMoreIntent, refreshIntent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.states.subscribe(::render).addTo(disposables)
        viewModel.processIntents(intents)
        subscribeForIssueClicks()
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    override fun render(state: IssuesSearchViewState) {
        issuesAdapter.isLoading = state.isLoadingNextPage
        with(state) {
            error?.let {
                loadingView.gone()
                refreshLayout.isRefreshing = false
                Toast.makeText(this@IssuesSearchActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            if (isLoadingFirstPage) {
                loadingView.visible()
                issueList.gone()
                emptyResultsInfo.gone()
            }
            if (searchResults?.isNotEmpty() == true) {
                loadingView.gone()
                refreshLayout.isRefreshing = false
                emptyResultsInfo.gone()
                issueList.visible()
                issuesAdapter.items = searchResults.map(::IssueListItem)
            } else if (searchResults?.isEmpty() == true) {
                refreshLayout.isRefreshing = false
                loadingView.gone()
                emptyResultsInfo.visible()
                issueList.gone()
            }
        }
    }

    private fun initRecyclerView() {
        issueList.adapter = issuesAdapter
        issueList.layoutManager = LinearLayoutManager(this)
    }

    //This is not a proper solution for starting new screen. It's going to be improved in the near future.
    private fun subscribeForIssueClicks() =
        issuesAdapter
            .issueClicks
            .subscribeBy(onNext = { webStarter.start(this, it.url) })
            .addTo(disposables)

    private val RecyclerView.lastCompletelyVisibleItemPosition: Int
        get() = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
}
