package com.example.stackoverflowbrowser.mvi.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.mvi.MviView
import com.example.stackoverflowbrowser.mvi.search.list.IssueListItem
import com.example.stackoverflowbrowser.mvi.search.list.IssuesAdapter
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.tomasznajda.ktx.android.gone
import com.tomasznajda.ktx.android.visible
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class IssuesSearchActivity : MviView<IssuesSearchIntent, IssuesSearchViewState>, AppCompatActivity() {

    private val viewModel: IssuesSearchViewModel by viewModel()
    private val issuesAdapter = IssuesAdapter()
    private val disposables = CompositeDisposable()
    private val queryChangeIntent by lazy<Observable<IssuesSearchIntent>> {
        RxSearchView.queryTextChanges(searchView)
            .skipInitialValue()
            .map { it.toString() }
            .map(IssuesSearchIntent::Query)
    }

    override val intents: Observable<IssuesSearchIntent>
        get() = queryChangeIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.states.subscribe(::render).addTo(disposables)
        viewModel.processIntents(intents)
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    override fun render(state: IssuesSearchViewState) {
        with(state) {
            error?.let {
                loadingView.gone()
                Toast.makeText(this@IssuesSearchActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            if (isLoadingFirstPage) {
                loadingView.visible()
                issueList.gone()
                emptyResultsInfo.gone()
            }
            if (searchResults?.isNotEmpty() == true) {
                loadingView.gone()
                emptyResultsInfo.gone()
                issueList.visible()
                issuesAdapter.items = searchResults.map(::IssueListItem)
            } else if (searchResults?.isEmpty() == true) {
                loadingView.gone()
                emptyResultsInfo.visible()
            }
        }
    }

    private fun initRecyclerView() {
        issueList.adapter = issuesAdapter
        issueList.layoutManager = LinearLayoutManager(this)
    }
}
